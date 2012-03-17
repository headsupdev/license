package org.headsupdev.license.exception;

/**
 * A generic exception which all license exceptions extend
 *
 * @author Andrew Williams
 * @version $Id: LicenseException.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class LicenseException
    extends Exception
{
    public LicenseException()
    {
    }

    public LicenseException( Exception cause )
    {
        super( cause );
    }
}
