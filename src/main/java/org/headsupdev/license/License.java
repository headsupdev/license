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

import java.util.Properties;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id: License.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class License
{
    protected static final String LICENSED_TO = "license.name";

    private Properties properties = new Properties();

    public License()
    {
    }

    public String getLicenseTitle()
    {
        return "Basic License File";
    }

    public String getLicensedTo()
    {
        return getProperty( LICENSED_TO );
    }

    public void setLicensedTo( String licensedTo )
    {
        setProperty( LICENSED_TO, licensedTo );
    }

    protected String getProperty( String key )
    {
        return getProperties().getProperty( key );
    }

    protected String getProperty( String key, String deflt )
    {
        return getProperties().getProperty( key, deflt );
    }

    protected void setProperty( String key, String value )
    {
        getProperties().setProperty( key, value );
    }

    protected Properties getProperties()
    {
        return properties;
    }
}
