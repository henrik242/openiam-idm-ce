package org.openiam.spml2.spi.ldap.dirtype;

import org.openiam.spml2.util.connect.ConnectionManagerConstant;
import org.openiam.spml2.util.connect.ConnectionMgr;
import org.openiam.spml2.util.connect.LdapConnectionMgr;

/**
 * Instantiate objects that directory/brand specific implementations.
 */
public class DirectorySpecificImplFactory {

public static Directory create( String dirType) {

    if (dirType == null) {
        // default
        return (new LdapV3());
    }

    if (dirType.equalsIgnoreCase(Directory.ACTIVE_DIRECTORY)) {
        return (new ActiveDirectoryImpl());
    }
    // default to the generic ldap adapter
    return (new LdapV3());

	}


}
