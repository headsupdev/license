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

import org.headsupdev.license.*;
import org.headsupdev.license.exception.LicenseException;

import java.security.Key;
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

/**
 * A main class that handles creation of keys and licenses etc. 
 *
 * @author Andrew Williams
 * @version $Id: Main.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class Main {
    private ManagerConfiguration config;

    public static void main( String args[] )
        throws Exception
    {
        Main main = new Main();
        if ( args.length < 1 )
        {
            ManagerUI ui = new ManagerUI();
            ui.setVisible( true );
            return;
        }

        ManagerConfiguration config = main.getConfig();
        for ( int i = 0; i < args.length - 1; i++ )
        {
            String option = args[ i ];
            if ( option.equals( "-private" ) )
            {
                config.setPrivateKeyFile( new File( args[ ++i ] ) );
            }
            else if ( option.equals( "-public" ) )
            {
                config.setPublicKeyFile( new File( args[ ++i ] ) );
            }
            else if ( option.equals( "-shared" ) )
            {
                config.setSharedKeyFile( new File( args[ ++i ] ) );
            }
            else if ( option.equals( "-configdir" ) )
            {
                config.setLicenseDirectory( new File( args[ ++i ] ) );
            }
        }

        LicenseManager manager = new LicenseManager( config );
        String command = args[ args.length - 1 ];
        if ( command.equals( "gen-keys" ) )
        {
            manager.generateKeys();
        }
        else if ( command.equals( "verify-keys" ) )
        {
            try
            {
                if ( manager.verifyKeys() )
                {
                    System.out.println( "All keys present and correct" );
                }
                else
                {
                    System.out.println( "Public and private keys not a pair" );
                }
            }
            catch ( Exception e )
            {
                System.out.println( "Public and private keys not a pair" );
                System.out.println( "  cause: " + e.getMessage() );
            }
        }
        else if ( command.equals( "create" ) )
        {
            main.create();
        }
        else if ( command.equals( "test" ) )
        {
            String in = readInput( "Please enter the license to test" );
            main.test( in );
        }
        else if ( command.equals( "base64" ) )
        {
            main.base64Encode();
        }
        else if ( command.equals( "help" ) )
        {
            main.printHelp();
        }
        else
        {
            ManagerUI ui = new ManagerUI();
            ui.setVisible( true );
        }
    }

    public Main()
    {
        config = new ManagerConfiguration();
    }

    public ManagerConfiguration getConfig()
    {
        return config;
    }

    private void printHelp()
    {
        String name = Main.class.getName();
        System.out.println( "Headsup License - create licenses in a secure format with simple commands" );
        System.out.println( "Usage:" );
        System.out.println( "    java " + name + " [options] command" );
        System.out.println();
        System.out.println( "Where options can be: ");
        System.out.println( "    -private filename    Specify the file to use for the private key" );
        System.out.println( "    -public filename     Specify the file to use for the public key" );
        System.out.println( "    -shared filename     Specify the file to use for the shared key" );
        System.out.println( "    -configdir directory Specify the directory for licenses and default keys");
        System.out.println();
        System.out.println( "And command is one of:" );
        System.out.println( "    gen-keys     Write a new set of keys to the current directory" );
        System.out.println( "    verify-keys  Check that the keys needed all exist and work correctly" );
        System.out.println( "    base64       Encode the keys to base64 data, written to files ending .b64" );
        System.out.println( "    manager      Load the main manager screen (default)" );
        System.out.println( "    help         Show this help page" );
    }

    private void create()
        throws Exception
    {
        String who = readInput( "Who is this to be licensed to?" );
        Key prv = LicenseUtils.deserialiseKey( config.getPrivateKeyFile() );
        Key shr = LicenseUtils.deserialiseKey( config.getSharedKeyFile() );

        License in = new License();
        in.setLicensedTo( who );
        LicenseEncoder encoder = new LicenseEncoder();

        encoder.setPrivateKey( prv );
        encoder.setSharedKey( shr );

        String license = encoder.encodeLicense( in );
        System.out.println( "Created license: " + license );
    }

    private void test( String license )
        throws Exception
    {
        Key pub = LicenseUtils.deserialiseKey( config.getPublicKeyFile() );
        Key shr = LicenseUtils.deserialiseKey( config.getSharedKeyFile() );

        LicenseDecoder decoder = new LicenseDecoder();
        decoder.setPublicKey( pub );
        decoder.setSharedKey( shr );

        try
        {
            VerboseLicense out = new VerboseLicense();
            decoder.decodeLicense( license, out );

            Enumeration props = out.getProperties().keys();
            while ( props.hasMoreElements() )
            {
                String key = (String) props.nextElement();
                System.out.println( "  " + key + " => " + out.getProperties().get( key ) );
            }
            System.out.println( "License verified" );
        }
        catch ( Exception e )
        {
            System.out.println( "License check failed - " + e.getMessage() );
        }
    }

    private static String readInput( String prompt )
    {
        System.out.println( prompt + ": " );
        StringBuilder in = new StringBuilder();

        int chr;
        try
        {
            while ( ( chr = System.in.read() ) != '\n' )
            {
                in.append( (char) chr );
            }
        }
        catch ( IOException e )
        {
            // just stop reading...
        }

        return in.toString();
    }

    private void base64Encode()
        throws Exception
    {
        base64Encode( config.getPrivateKeyFile(), new File( config.getPrivateKeyFile().getAbsolutePath() + ".b64" ) );
        base64Encode( config.getPublicKeyFile(), new File( config.getPublicKeyFile().getAbsolutePath() + ".b64" ) );
        base64Encode( config.getSharedKeyFile(), new File( config.getSharedKeyFile().getAbsolutePath() + ".b64" ) );
    }

    private void base64Encode( File in, File out )
        throws Exception
    {
        int length = (int) in.length();
        byte[] buffer = new byte[length];

        DataInputStream stream = new DataInputStream( new FileInputStream( in ) );
        stream.readFully( buffer );
        stream.close();

        String encoded = DatatypeConverter.printBase64Binary( buffer );

        DataOutputStream outStream = new DataOutputStream( new FileOutputStream( out ) );
        outStream.write( encoded.getBytes() );
        outStream.close();
    }

    private class VerboseLicense extends License
    {
        // simply exposing the protected method for our console output
        @Override
        public Properties getProperties()
        {
            return super.getProperties();
        }
    }
}
