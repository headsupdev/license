package org.headsupdev.license;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id: AbstractVersionedLicense.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class AbstractVersionedLicense
    extends License
    implements VersionedLicense
{

    private double installVersion = 1.0;

    public double getInstallVersion()
    {
        return installVersion;
    }

    public void setInstallVersion( double installVersion )
    {
        this.installVersion = installVersion;
    }

    public double getMinVersion()
    {
        String value = this.getProperty( KEY_MIN_VERSION );
        try
        {
            return Double.parseDouble( value );
        }
        catch ( Exception e )
        {
            System.err.println("Unable to parse version number \"" + value + "\"");
            return 1.0;
        }
    }

    protected void setMinVersion( double min )
    {
        setProperty( KEY_MIN_VERSION, String.valueOf( min ) );
    }

    public double getMaxVersion()
    {
        String value = this.getProperty( KEY_MAX_VERSION );
        try
        {
            return Double.parseDouble( value );
        }
        catch ( Exception e )
        {
            System.err.println("Unable to parse version number \"" + value + "\"");
            return 2.0;
        }
    }

    protected void setMaxVersion( double max )
    {
        setProperty( KEY_MAX_VERSION, String.valueOf( max ) );
    }
}
