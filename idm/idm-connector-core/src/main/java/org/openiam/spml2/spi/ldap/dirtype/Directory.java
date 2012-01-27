package org.openiam.spml2.spi.ldap.dirtype;

import org.openiam.spml2.msg.password.SetPasswordRequestType;

import javax.naming.directory.ModificationItem;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: suneetshah
 * Date: 1/26/12
 * Time: 11:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Directory {
    
    final static String ACTIVE_DIRECTORY = "ACTIVE_DIRECTORY";
    final static String LDAP_V3 = "LDAP_V3";

    
    ModificationItem[] setPassword(SetPasswordRequestType reqType) throws UnsupportedEncodingException;
}
