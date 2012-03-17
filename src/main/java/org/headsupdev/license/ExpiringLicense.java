package org.headsupdev.license;

import java.util.Date;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id$
 * @since 1.0
 */
public interface ExpiringLicense {
    String KEY_EXPIRY = "license.expiry";
    String KEY_VALIDITY = "license.valid-duration";

    Date getExpiryDate();

    long getValidDays();

    Date getInstallDate();

    void setInstallDate( Date installDate );
}
