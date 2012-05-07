package org.openiam.spml2.spi.ldap.dirtype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.pswd.service.PasswordGenerator;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.ldap.LdapContext;

/**
 * Provides Active Directory specific functionality
 */
public class ActiveDirectoryImpl implements Directory {

    public static final int UF_ACCOUNTDISABLE     = 2;//0x0002
    public static final int UF_LOCKOUT            = 16;//0x0010
    public static final int UF_PASSWORD_EXPIRED   = 8388608;//0x800000
    public static final int UF_DONT_EXPIRE_PASSWD = 65536;//0x00010000
    public static final int UF_NORMAL_ACCOUNT     = 512;//0x0200
    public static final int UF_PASSWD_NOTREQD = 0x0020;
    public static final int UF_PASSWD_CANT_CHANGE = 0x0040;

    protected static final Log log = LogFactory.getLog(ActiveDirectoryImpl.class);


    public ModificationItem[] setPassword(SetPasswordRequestType reqType) throws UnsupportedEncodingException {


        byte[] password = ("\"" + reqType.getPassword() + "\"").getBytes("UTF-16LE");

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", password));
        return mods;


    }

    public ModificationItem[] suspend(SuspendRequestType request) {

        log.debug("suspending AD user.");

        ModificationItem[] mods = new ModificationItem[1];

        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
                Integer.toString(UF_NORMAL_ACCOUNT +  UF_ACCOUNTDISABLE)));
        return mods;

    }

    public ModificationItem[] resume(ResumeRequestType request) {

        log.debug("Enabling AD user.");

        ModificationItem[] mods = new ModificationItem[1];

        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
                Integer.toString(UF_NORMAL_ACCOUNT )));
        return mods;

    }

    public void delete(DeleteRequestType reqType, LdapContext ldapctx, String ldapName, String onDelete) throws NamingException {

        if ("DELETE".equalsIgnoreCase(onDelete)) {

            ldapctx.destroySubcontext(ldapName);

        }else if ( "DISABLE".equalsIgnoreCase(onDelete)) {

            ModificationItem[] mods = new ModificationItem[1];

            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",
                    Integer.toString(UF_NORMAL_ACCOUNT +  UF_ACCOUNTDISABLE)));

            ldapctx.modifyAttributes(ldapName, mods);

        }

    }

    public void removeAccountMemberships( String ldapName, ManagedSystemObjectMatch matchObj,  LdapContext ldapctx ) {

        List<String> currentMembershipList =  userMembershipList(ldapName, matchObj,   ldapctx);

        log.debug("Current ldap role membership:" + currentMembershipList);


        // remove membership
        if (currentMembershipList != null) {
            for (String s : currentMembershipList) {

                try {
                    ModificationItem mods[] = new ModificationItem[1];
                    mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", ldapName));
                    ldapctx.modifyAttributes(s, mods);
                }catch (NamingException ne ) {
                    log.error(ne);
                }
            }
        }

    }

    public void updateAccountMembership(List<String> targetMembershipList, String ldapName,
                                        ManagedSystemObjectMatch matchObj,  LdapContext ldapctx,
                                        List<ExtensibleObject> requestAttribute) {


        String samAccountName = getSamAccountName(requestAttribute);

        List<String> currentMembershipList = userMembershipList(samAccountName, matchObj,   ldapctx);

        log.debug("- samAccountName = " + samAccountName);
        log.debug("- Current ldap role membership:" + currentMembershipList);
        log.debug("- Target ldap role membership:"  + targetMembershipList);

        if (targetMembershipList == null && currentMembershipList != null) {
            // remove all associations
            for (String s : currentMembershipList) {
                try {
                    log.debug("Removing membership from: " + s + " - " + ldapName);
                    ModificationItem mods[] = new ModificationItem[1];
                    mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", ldapName));
                    ldapctx.modifyAttributes(s, mods);
                }catch (NamingException ne ) {
                    log.error(ne);
                }

            }
        }


        // see if we need to add additional membership entries

        if (targetMembershipList != null) {
            for (String s : targetMembershipList) {
                boolean found = false;
                if (currentMembershipList != null) {
                    for (String cur : currentMembershipList) {
                        if (s.equalsIgnoreCase(cur))  {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found ) {
                    if ( !isMemberOf(currentMembershipList, s) ) {
                        try {
                            ModificationItem mods[] = new ModificationItem[1];
                            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", ldapName));
                            ldapctx.modifyAttributes(s, mods);
                        }catch (NamingException ne ) {
                            log.error(ne);
                        }
                    }
                }
            }
        }

        // remove membership
        if (currentMembershipList != null) {
            for (String s : currentMembershipList) {
                boolean found = false;
                if (targetMembershipList != null) {
                    for (String cur : targetMembershipList) {
                        if (s.equalsIgnoreCase(cur))  {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found ) {
                    try {
                        log.debug("Removing current membership from: " + s + " - " + ldapName);

                        ModificationItem mods[] = new ModificationItem[1];
                        mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("member", ldapName));
                        ldapctx.modifyAttributes(s, mods);
                    }catch (NamingException ne ) {
                        log.error(ne);
                    }
                }
            }
        }


    }

    private String getSamAccountName(List<ExtensibleObject> requestAttribute) {

        for (ExtensibleObject obj : requestAttribute) {
            List<ExtensibleAttribute> attrList = obj.getAttributes();
            for (ExtensibleAttribute att : attrList) {
                if ("sAMAccountName".equalsIgnoreCase(att.getName())) {
                    return att.getValue();
                }

            }
        }
        return null;

    }



    public void setAttributes(String name, Object obj) {

    }

    protected boolean isMemberOf(List<String> membershipList, String objectName)  {

        if (membershipList == null || membershipList.isEmpty()) {
            return false;
        }
        for (String member : membershipList) {
            if (member.equalsIgnoreCase(objectName)) {
                return true;
            }
        }
        return false;

    }


    protected List<String> userMembershipList(String samAccountName,  ManagedSystemObjectMatch matchObj, LdapContext ldapctx) {

        List<String> currentMembershipList = new ArrayList<String>();

        log.debug("AD - userMembershipList() for ..." + samAccountName);
        log.debug(" - MembershipObjectDN= " + matchObj.getSearchBaseDn());

        String userSearchFilter = "(&(objectclass=*)(sAMAccountName=" + samAccountName + "))";
        String searchBase = matchObj.getSearchBaseDn();

        log.debug("Search -->" + userSearchFilter + " in " + searchBase);


        try {

            int totalResults = 0;

            SearchControls ctls = new SearchControls();

            String userReturnedAtts[]={"memberOf"};
            ctls.setReturningAttributes(userReturnedAtts);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search object only

            NamingEnumeration answer = ldapctx.search(searchBase, userSearchFilter, ctls);

            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.next();

                Attributes attrs = sr.getAttributes();
                if (attrs != null) {

                    try {
                        for (NamingEnumeration ae = attrs.getAll();ae.hasMore();) {
                            Attribute attr = (Attribute)ae.next();

                            for (NamingEnumeration e = attr.getAll();e.hasMore();totalResults++) {
                                currentMembershipList.add ((String)e.next());
                            }

                        }

                    }
                    catch (NamingException e)	{
                        log.error("Problem listing membership: " + e.toString());
                    }

                }


            }

        }catch (Exception e) {
            log.error(e.toString());
        }

        if (currentMembershipList.isEmpty()) {
            return null;
        }
        return currentMembershipList;


    }

}
