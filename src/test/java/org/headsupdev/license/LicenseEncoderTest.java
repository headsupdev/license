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

package org.headsupdev.license;

import javax.crypto.KeyGenerator;
import java.security.*;
import java.util.Date;

import junit.framework.TestCase;
import org.headsupdev.license.exception.LicenseExpiredException;
import org.headsupdev.license.exception.UnlicensedVersionException;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id: LicenseEncoderTest.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class LicenseEncoderTest
    extends TestCase
{
    private Key prv, pub, shr;

    public void setUp()
        throws Exception
    {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
        keyGen.initialize( 1024 );
        KeyPair keyPair = keyGen.generateKeyPair();
        prv = keyPair.getPrivate();
        pub = keyPair.getPublic();

        KeyGenerator keyGen2 = KeyGenerator.getInstance( "DES" );
        keyGen2.init( 56 );
        shr = keyGen2.generateKey();
    }

    public void testEncodeDecode()
        throws Exception
    {
        License in = new License();
        LicenseEncoder encoder = new LicenseEncoder();
        in.setProperty( "testing", "ALongStringUsedForTesting" );

        encoder.setPrivateKey( prv );
        encoder.setSharedKey( shr );

        String license = encoder.encodeLicense( in );
        TestCase.assertNotNull( license );

        LicenseDecoder decoder = new LicenseDecoder();
        decoder.setPublicKey( pub );
        decoder.setSharedKey( shr );

        License out = new License();
        decoder.decodeLicense( license, out );
    }

    public void testExpiring()
        throws Exception
    {
        AbstractExpiringLicense in = new AbstractExpiringLicense();
        LicenseEncoder encoder = new LicenseEncoder();
        in.setExpiryDate( new Date( System.currentTimeMillis() - 1000 ) );

        encoder.setPrivateKey( prv );
        encoder.setSharedKey( shr );

        String license = encoder.encodeLicense( in );
        TestCase.assertNotNull( license );

        LicenseDecoder decoder = new LicenseDecoder();
        decoder.setPublicKey( pub );
        decoder.setSharedKey( shr );
        try
        {
            AbstractExpiringLicense out = new AbstractExpiringLicense();
            decoder.decodeLicense( license, out );

            fail( "Decoding should fail due to an expired license" );
        }
        catch ( LicenseExpiredException e )
        {
        }
    }

    public void testExpiringDays()
        throws Exception
    {
        AbstractExpiringLicense in = new AbstractExpiringLicense();
        LicenseEncoder encoder = new LicenseEncoder();
        in.setValidDays( 7 );

        encoder.setPrivateKey( prv );
        encoder.setSharedKey( shr );

        String license = encoder.encodeLicense( in );
        TestCase.assertNotNull( license );

        LicenseDecoder decoder = new LicenseDecoder();
        decoder.setPublicKey( pub );
        decoder.setSharedKey( shr );

        try
        {
            AbstractExpiringLicense out = new AbstractExpiringLicense();
            out.setInstallDate( new Date( System.currentTimeMillis() - ( 10 * 24 * 60 * 60 * 1000 ) ) );
            decoder.decodeLicense( license, out );

            fail( "Decoding should fail due to an expired license" );
        }
        catch ( LicenseExpiredException e )
        {
        }
    }

    public void testValidVersion()
        throws Exception
    {
        AbstractVersionedLicense in = new AbstractVersionedLicense();
        LicenseEncoder encoder = new LicenseEncoder();
        in.setMinVersion( 1.0 );
        in.setMaxVersion( 2.0 );

        encoder.setPrivateKey( prv );
        encoder.setSharedKey( shr );

        String license = encoder.encodeLicense( in );
        TestCase.assertNotNull( license );

        LicenseDecoder decoder = new LicenseDecoder();
        decoder.setPublicKey( pub );
        decoder.setSharedKey( shr );

        AbstractVersionedLicense out = new AbstractVersionedLicense();
        out.setInstallVersion( 1.5 );
        decoder.decodeLicense( license, out );
    }

    public void testInvalidVersion()
        throws Exception
    {
        AbstractVersionedLicense in = new AbstractVersionedLicense();
        LicenseEncoder encoder = new LicenseEncoder();
        in.setMinVersion( 1.0 );
        in.setMaxVersion( 2.0 );

        encoder.setPrivateKey( prv );
        encoder.setSharedKey( shr );

        String license = encoder.encodeLicense( in );
        TestCase.assertNotNull( license );

        LicenseDecoder decoder = new LicenseDecoder();
        decoder.setPublicKey( pub );
        decoder.setSharedKey( shr );

        try
        {
            AbstractVersionedLicense out = new AbstractVersionedLicense();
            out.setInstallVersion( 2.5 );
            decoder.decodeLicense( license, out );

            fail( "Decoding should fail due to an invalid version" );
        }
        catch ( UnlicensedVersionException e )
        {
        }
    }
}
