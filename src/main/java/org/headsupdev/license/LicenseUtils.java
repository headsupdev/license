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

import java.io.*;
import java.security.Key;
import java.net.URL;

/**
 * Util methods for working with licenses and keys.
 *
 * @author Andrew Williams
 * @version $Id: LicenseUtils.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class LicenseUtils
{
    public static void serialiseKey( File out, Key key )
        throws Exception
    {
        OutputStream stream = new FileOutputStream( out );
        try
        {
            serialiseKey( stream, key );
        }
        finally
        {
            stream.close();
        }
    }

    public static void serialiseKey( OutputStream out, Key key )
        throws Exception
    {
        ObjectOutputStream oos = new ObjectOutputStream( out );
        oos.writeObject( key );
    }

    public static Key deserialiseKey( File in )
        throws Exception
    {
        FileInputStream stream = new FileInputStream( in );
        try
        {
            return deserialiseKey( stream );
        }
        finally
        {
            stream.close();
        }
    }

    public static Key deserialiseKey( URL in )
        throws Exception
    {
        InputStream stream = in.openStream();
        try
        {
            return deserialiseKey( stream );
        }
        finally
        {
            stream.close();
        }
    }

    public static Key deserialiseKey( InputStream in )
        throws Exception
    {
        ObjectInputStream ois = new ObjectInputStream( in );
        return (Key) ois.readObject();
    }

    public static String readLicenseFile( File file )
        throws IOException
    {
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader( new FileReader( file ) );
            return reader.readLine();
        }
        finally
        {
            if ( reader != null )
            {
                try
                {
                    reader.close();
                }
                catch ( IOException e )
                {
                    // ignore
                }
            }
        }
    }

    public static void writeLicenseFile( String license, File file )
        throws IOException
    {
        BufferedWriter writer = null;
        
        try
        {
            writer = new BufferedWriter( new FileWriter( file ) );
            writer.write( license );
        }
        finally
        {
            if ( writer != null )
            {
                try
                {
                    writer.close();
                }
                catch ( IOException e )
                {
                    //ignore
                }
            }
        }
    }
}
