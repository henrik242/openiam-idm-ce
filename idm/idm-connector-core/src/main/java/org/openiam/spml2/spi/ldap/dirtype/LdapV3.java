package org.openiam.spml2.spi.ldap.dirtype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.SysConfiguration;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.pswd.service.PasswordGenerator;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.openiam.exception.EncryptionException;
import org.openiam.util.encrypt.HashDigest;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.ldap.LdapContext;
import org.openiam.util.encrypt.SHA1Hash;

/**
 * Implements directory specific extensions for standard LDAP v3
 * User: suneetshah
 */
public class LdapV3 implements Directory{
    
    Map<String, Object> objectMap = new HashMap<String, Object>();
    private static final Log log = LogFactory.getLog(LdapV3.class);
    HashDigest hash = new SHA1Hash();
    

    public ModificationItem[] setPassword(SetPasswordRequestType reqType) throws UnsupportedEncodingException {

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", reqType.getPassword()));

        return mods;
    }

    public ModificationItem[] suspend(SuspendRequestType request)  {

        String scrambledPswd =	PasswordGenerator.generatePassword(10);
        
        hash.HexEncodedHash( "{ssha}" + hash.hash(scrambledPswd));

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", scrambledPswd));

        return mods;
    }

    public ModificationItem[] resume(ResumeRequestType request) {

        String ldapName = (String)objectMap.get("LDAP_NAME");
        LoginDataService loginManager = (LoginDataService)objectMap.get("LOGIN_MANAGER");
        SysConfiguration sysConfiguration = (SysConfiguration)objectMap.get("CONFIGURATION");
        String targetID = (String)objectMap.get("TARGET_ID");

        try {

             // get the current password for the user.
            Login login = loginManager.getLoginByManagedSys(
                    sysConfiguration.getDefaultSecurityDomain(),
                    ldapName,
                    targetID);
            String encPassword = login.getPassword();
            String decPassword = loginManager.decryptPassword(encPassword);

            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", decPassword));
            return mods;
        }catch(EncryptionException e) {
            log.error(e.toString());
            return null;

        }

    }

    public void delete(DeleteRequestType reqType, LdapContext ldapctx, String ldapName, String onDelete) throws NamingException{

        if ("DELETE".equalsIgnoreCase(onDelete)) {

            ldapctx.destroySubcontext(ldapName);

        }else if ( "DISABLE".equalsIgnoreCase(onDelete)) {

            String scrambledPswd =	PasswordGenerator.generatePassword(10);

            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", scrambledPswd));
            ldapctx.modifyAttributes(ldapName, mods);

        }
    }

    /* Group membership functions */

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

        List<String> currentMembershipList = userMembershipList(ldapName, matchObj,   ldapctx);

        log.debug("Current ldap role membership:" + currentMembershipList);

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
                            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("uniqueMember", ldapName));
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
                        mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", ldapName));
                        ldapctx.modifyAttributes(s, mods);
                    }catch (NamingException ne ) {
                        log.error(ne);
                    }
                }
            }
        }


    }

    public void setAttributes(String name, Object obj) {
        objectMap.put(name,obj);
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



    protected List<String> userMembershipList(String userDN,  ManagedSystemObjectMatch matchObj, LdapContext ldapctx) {

        List<String> currentMembershipList = new ArrayList<String>();

        log.debug("isMemberOf()...");
        log.debug(" - userDN =" + userDN);
        log.debug(" - MembershipObjectDN=" + matchObj.getSearchBaseDn());

        String userSearchFilter = "(&(objectclass=*)(uniqueMember=" + userDN + "))";
        String searchBase = matchObj.getSearchBaseDn();



        try {

            SearchControls ctls = new SearchControls();

            String userReturnedAtts[]={"uniqueMember"};
            ctls.setReturningAttributes(userReturnedAtts);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search object only

            NamingEnumeration answer = ldapctx.search(searchBase, userSearchFilter, ctls);


            //Loop through the search results
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.next();

                String objectName = sr.getName();
                if (!objectName.contains(matchObj.getBaseDn()))  {
                    objectName = objectName + "," + matchObj.getBaseDn();
                }

                log.debug("Adding to current membership list " + objectName);
                currentMembershipList.add(objectName);

            }

        }catch (Exception e) {
            e.printStackTrace();

        }

        if (currentMembershipList.isEmpty()) {
            return null;
        }
        return currentMembershipList;


    }

}
