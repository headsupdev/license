package org.headsupdev.license.manager;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * A configuration class that handles various aspects of how licenses are loaded and created.
 *
 * @author Andrew Williams
 * @version $Id: Main.java 48 2008-12-21 23:17:08Z handyande $
 * @since 1.0
 */
public class ManagerConfiguration
{
    private static final String LICENSE_DIRECTORY = "LicenseDirectory";
    private File licenseDirectory;
    private File privateKeyFile, publicKeyFile, sharedKeyFile;

    public ManagerConfiguration()
    {
        loadPreferences();
        storePreferences();
    }

    public void loadPreferences()
    {
        Preferences prefs = Preferences.userNodeForPackage( getClass() );
        String licensePref = prefs.get( LICENSE_DIRECTORY, null );
        if ( licensePref != null )
        {
            licenseDirectory = new File( licensePref );
        }
        else
        {
            licenseDirectory = new File( System.getProperty( "user.home" ), ".licenses" );
        }

        privateKeyFile = new File( licenseDirectory, "private-key" );
        publicKeyFile = new File( licenseDirectory, "public-key" );
        sharedKeyFile = new File( licenseDirectory, "shared-key" );
    }

    public void storePreferences()
    {
        Preferences prefs = Preferences.userNodeForPackage( getClass() );
        prefs.put( LICENSE_DIRECTORY, licenseDirectory.getAbsolutePath() );

    }

    public File getLicenseDirectory()
    {
        return licenseDirectory;
    }

    public void setLicenseDirectory( File licenseDirectory )
    {
        this.licenseDirectory = licenseDirectory;
        storePreferences();

        // setup all files using latest config
        loadPreferences();
    }

    public File getPrivateKeyFile()
    {
        return privateKeyFile;
    }

    public void setPrivateKeyFile( File privateKeyFile )
    {
        this.privateKeyFile = privateKeyFile;
    }

    public File getPublicKeyFile()
    {
        return publicKeyFile;
    }

    public void setPublicKeyFile( File publicKeyFile )
    {
        this.publicKeyFile = publicKeyFile;
    }

    public File getSharedKeyFile()
    {
        return sharedKeyFile;
    }

    public void setSharedKeyFile( File sharedKeyFile )
    {
        this.sharedKeyFile = sharedKeyFile;
    }
}
