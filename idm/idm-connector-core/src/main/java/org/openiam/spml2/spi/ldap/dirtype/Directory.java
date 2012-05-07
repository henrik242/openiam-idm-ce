package org.openiam.spml2.spi.ldap.dirtype;

import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;

import javax.naming.NamingException;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

    ModificationItem[] suspend(SuspendRequestType request);

    ModificationItem[] resume(ResumeRequestType request);

    /**
     * setAttributes allows you to set attributes on the implementation object which may be need for the specific
     * implementation
     * @param name
     * @param obj
     */
    
    void setAttributes(String name, Object obj);

    void delete(DeleteRequestType reqType, LdapContext ldapctx, String ldapName, String onDelete ) throws NamingException;

    void removeAccountMemberships( String ldapName, ManagedSystemObjectMatch matchObj,  LdapContext ldapctx );

    void updateAccountMembership(List<String> targetMembershipList, String ldapName, ManagedSystemObjectMatch matchObj,  LdapContext ldapctx, List<ExtensibleObject> requestAttribute );

    
}
