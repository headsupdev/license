/*
 * Copyright 2010-2011 Heads Up Development Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.headsupdev.license.manager;

import org.headsupdev.license.License;
import org.headsupdev.license.LicenseUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FilenameFilter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * TODO: Document me
 *
 * User: andy
 * Date: 16/03/2012
 * Time: 22:57
 *
 * @author Andrew Williams
 * @since 1.1
 */
public class LicenseManager
{
    private ManagerConfiguration config;

    public LicenseManager( ManagerConfiguration config )
    {
        this.config = config;
    }

    public File[] getLicenseFiles()
    {
        return config.getLicenseDirectory().listFiles( new FilenameFilter()
        {
            public boolean accept( File file, String s )
            {
                return s.endsWith( ".license" );
            }
        } );
    }

    public ManagerConfiguration getConfig()
    {
        return config;
    }

    public License createLicense()
    {
        return new License();
    }

    public void generateKeys()
        throws Exception
    {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
        keyGen.initialize( 1024 );
        KeyPair keyPair = keyGen.generateKeyPair();
        LicenseUtils.serialiseKey( config.getPrivateKeyFile(), keyPair.getPrivate() );
        LicenseUtils.serialiseKey( config.getPublicKeyFile(), keyPair.getPublic() );

        KeyGenerator keyGen2 = KeyGenerator.getInstance( "DES" );
        keyGen2.init( 56 );
        Key shared = keyGen2.generateKey();
        LicenseUtils.serialiseKey( config.getSharedKeyFile(), shared );
    }

    public boolean verifyKeys()
        throws Exception
    {
        Key prv = LicenseUtils.deserialiseKey( config.getPrivateKeyFile() );
        Key pub = LicenseUtils.deserialiseKey( config.getPublicKeyFile() );
        Key shr = LicenseUtils.deserialiseKey( config.getSharedKeyFile() );

        String checking = "This is a test";

        try
        {
            Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
            cipher.init( Cipher.ENCRYPT_MODE, prv );
            byte[] encoded = cipher.doFinal( checking.getBytes() );

            Cipher cipher2 = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
            cipher2.init( Cipher.DECRYPT_MODE, pub );
            return checking.equals( new String( cipher2.doFinal( encoded ) ) );
        }
        catch ( Exception e )
        {
            // fall back to the null result so it will fail...
            throw e;
        }
    }
}
