package org.headsupdev.license;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.headsupdev.license.exception.*;

import javax.crypto.Cipher;
import java.util.Date;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.security.Security;
import java.security.Key;
import java.security.MessageDigest;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id: LicenseDecoder.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class LicenseDecoder
{
    private Key pub, shared;

    public LicenseDecoder()
    {
        Security.addProvider( new BouncyCastleProvider() );
    }

    public void setPublicKey( Key key )
    {
        this.pub = key;
    }

    public void setSharedKey( Key key )
    {
        this.shared = key;
    }

    public void decodeLicense( String license, License out )
        throws LicenseException
    {
        try
        {
            out.getProperties().load( new ByteArrayInputStream( decode( license ) ) );

            if ( out instanceof ExpiringLicense )
            {
                Date expires = ( (ExpiringLicense) out ).getExpiryDate();
                if ( expires != null && expires.before( new Date() ) )
                {
                    throw new LicenseExpiredException();
                }
            }
            if ( out instanceof VersionedLicense )
            {
                VersionedLicense ver = (VersionedLicense) out;
                if ( ver.getInstallVersion() < ver.getMinVersion() ||
                        ver.getInstallVersion() >= ver.getMaxVersion() )
                {
                    throw new UnlicensedVersionException();
                }
            }
        }
        catch ( IOException e )
        {
            throw new InvalidFormatException( e );
        }
    }

    protected byte[] decode( String licenseStr )
        throws LicenseException
    {
        if ( pub == null || shared == null )
        {
            throw new IllegalStateException( "Keys must be set before decoding the license" );
        }

        byte[] license = Base64.decode( licenseStr );
        try
        {
            Cipher cipher2 = Cipher.getInstance( "DES/ECB/PKCS5Padding" );
            cipher2.init( Cipher.DECRYPT_MODE, shared );

            byte[] dec2 = cipher2.doFinal( license );

            int dataLen = new String( dec2 ).indexOf( "\0\0" );
            int sigLen = dec2.length - dataLen - 2;

            byte[] myData = new byte[dataLen];
            System.arraycopy(dec2, 0, myData, 0, dataLen );

            MessageDigest digester = MessageDigest.getInstance( "MD5" );
            digester.update( myData );
            byte[] myDigest = digester.digest();

            byte[] mySig = new byte[sigLen];
            System.arraycopy( dec2, dataLen + 2, mySig, 0, sigLen );

            Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
            cipher.init( Cipher.DECRYPT_MODE, pub );
            byte[] digest = cipher.doFinal( mySig );

            if ( !new String( myDigest ).equals( new String( digest ) ) )
            {
                throw new InvalidLicenseException();
            }

            return myData;            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return new byte[0];
        }
    }
}