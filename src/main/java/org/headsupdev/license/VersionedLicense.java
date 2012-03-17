package org.headsupdev.license;

/**
 * TODO add a description
 *
 * @author Andrew Williams
 * @version $Id$
 * @since 1.0
 */
public interface VersionedLicense {
    String KEY_MIN_VERSION = "license.version.min";
    String KEY_MAX_VERSION = "license.version.max";

    double getInstallVersion();

    void setInstallVersion( double installVersion );

    double getMinVersion();

    double getMaxVersion();
}
