package org.headsupdev.license;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id: AbstractExpiringLicense.java 76 2012-03-17 23:17:03Z handyande $
 * @since 1.0
 */
public class AbstractExpiringLicense
    extends License
    implements ExpiringLicense
{

    private final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Date installDate = new Date();

    public Date getExpiryDate()
    {
        Date expire = null;
        String value = getProperty( KEY_EXPIRY );
        if ( value != null && value.length() > 0 )
        {
            try
            {
                synchronized( format ) {
                    expire = format.parse( value );
                }
            }
            catch ( Exception e )
            {
                System.err.println("Unable to parse expiry date \"" + value + "\"");
            }
        }

        long days = getValidDays();
        if ( days > 0 )
        {
            Date expire2 = new Date( getInstallDate().getTime() + ( days * 24 * 60 * 60 * 1000 ) );

            if ( expire == null || expire2.before( expire ) )
            {
                return expire2;
            }
        }

        // A null expiry date represents an expiring license with no expiry date
        return expire;
    }

    protected void setExpiryDate( Date expiry )
    {
        this.setProperty( KEY_EXPIRY, format.format( expiry ) );
    }

    public long getValidDays()
    {
        String value = getProperty( KEY_VALIDITY );
        if ( value == null )
        {
            return 0;
        }

        try
        {
            return Long.parseLong( value );
        }
        catch ( NumberFormatException e )
        {
            System.err.println("Unable to parse validity period \"" + value + "\"");
            return 30;
        }
    }

    protected void setValidDays( long days )
    {
        this.setProperty( KEY_VALIDITY, String.valueOf( days ) );
    }

    public Date getInstallDate()
    {
        return installDate;
    }

    public void setInstallDate( Date installDate )
    {
        this.installDate = installDate;
    }
}
