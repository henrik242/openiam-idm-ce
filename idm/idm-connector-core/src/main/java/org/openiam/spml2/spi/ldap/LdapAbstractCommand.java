package org.openiam.spml2.spi.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import java.io.UnsupportedEncodingException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for commands that are usec by the LdapConnector
 * User: suneetshah
 * Date: 7/30/11
 * Time: 1:31 PM
 */
public abstract class LdapAbstractCommand {




    protected static final Log log = LogFactory.getLog(LdapAbstractCommand.class);

    protected ManagedSystemDataService managedSysService;
    protected ResourceDataService resourceDataService;
    protected ManagedSystemObjectMatchDAO managedSysObjectMatchDao;


    protected boolean identityExists(String ldapName, LdapContext ctx) {

        try {
            LdapContext lCtx = (LdapContext) ctx.lookup(ldapName);
        } catch (NamingException ne) {
            return false;
        }
        return true;

    }

    protected boolean isInDirectory(String ldapName, ManagedSystemObjectMatch matchObj,
                                    LdapContext ldapctx) {
        int indx = ldapName.indexOf(",");
        String rdn = null;
        String objectBaseDN = null;
        if (indx > 0) {
            rdn = ldapName.substring(0, ldapName.indexOf(","));
            objectBaseDN = ldapName.substring(indx + 1);
        } else {
            rdn = ldapName;
        }
        log.debug("Lookup rdn = " + rdn);
        log.debug("Search in: " + objectBaseDN);

        String[] attrAry = {"uid", "cn", "fn"};
        NamingEnumeration results = null;
        try {
            //results = search(matchObj, ldapctx, rdn, attrAry);
            results = lookupSearch(matchObj, ldapctx, rdn, attrAry, objectBaseDN);
            if (results != null && results.hasMoreElements()) {
                return true;
            }
            return false;
        } catch (NamingException ne) {
            log.error(ne);
            return false;
        }
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





    protected BasicAttributes getBasicAttributes(List<ExtensibleObject> requestAttribute, String idField, List<String> targetMembershipList) {
        BasicAttributes attrs = new BasicAttributes();

        // add the object class
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");

        // add the ou for this record
        Attribute ouSet = new BasicAttribute("ou");
        String ou = getOU(requestAttribute);
        log.debug("GetAttributes() - ou=" + ou);
        if (ou != null && ou.length() > 0) {
            ouSet.add(ou);
            attrs.put(ouSet);
        }

        // add the structural classes
        attrs.put(oc);

        // add the identifier

        // add the attributes
        for (ExtensibleObject obj : requestAttribute) {
            List<ExtensibleAttribute> attrList = obj.getAttributes();
            for (ExtensibleAttribute att : attrList) {

                log.debug("Attr Name=" + att.getName() + " " + att.getDataType() + " " + att.getValue());


                if (!att.getName().equals(idField)  &&
                   ( att.getDataType() == null || !att.getDataType().equalsIgnoreCase("memberOf") ) ) {


                    Attribute a = null;
                    if (att.isMultivalued()) {
                        List<String> valList = att.getValueList();
                        if (valList != null && valList.size() > 0) {
                            int ctr = 0;
                            for (String s : valList) {
                                if (ctr == 0) {
                                    a = new BasicAttribute(att.getName(), valList.get(ctr));
                                } else {
                                    a.add(valList.get(ctr));
                                }
                                ctr++;
                            }

                        }
                    } else if ("unicodePwd".equalsIgnoreCase(att.getName())) {
                        a = generateActiveDirectoryPassword(att.getValue());
                    } else {
                        a = new BasicAttribute(att.getName(), att.getValue());

                    }
                    attrs.put(a);


                    //new BasicAttribute();

                    //attrs.put(att.getName(), att.getValue());
                }else {
                    if ( "memberOf".equalsIgnoreCase(att.getDataType())) {
                        buildMembershipList(att,targetMembershipList);
                    }
                }
            }
        }

        return attrs;
    }

    protected void buildMembershipList( ExtensibleAttribute att ,List<String>targetMembershipList) {

        log.debug("buildMembershipList:" + att);

        if (att == null)
            return;

        if (att.getValueList() == null || att.getValueList().isEmpty())  {
            return;
        }

        List<String> valList = att.getValueList();
        for (String s : valList) {
            targetMembershipList.add(s);

        }

    }


    protected String getOU(List<ExtensibleObject> requestAttribute) {
        for (ExtensibleObject obj : requestAttribute) {
            List<ExtensibleAttribute> attrList = obj.getAttributes();
            for (ExtensibleAttribute att : attrList) {
                if (att.getName().equalsIgnoreCase("ou")) {
                    return att.getValue();
                }
            }
        }
        return null;
    }

    protected List<String> getAttributeNameList(List<AttributeMap> attrMap) {
        List<String> strList = new ArrayList<String>();

        if (attrMap == null || attrMap.size() == 0) {
            return null;
        }
        for (AttributeMap a : attrMap) {
            strList.add(a.getAttributeName());
        }

        return strList;
    }

    protected NamingEnumeration search(ManagedSystemObjectMatch matchObj,
                                       LdapContext ctx,
                                       String searchValue, String[] attrAry) throws NamingException {

        String attrIds[] = {"1.1", "+", "*", "accountUnlockTime", "aci", "aclRights", "aclRightsInfo", "altServer", "attributeTypes", "changeHasReplFixupOp", "changeIsReplFixupOp", "copiedFrom", "copyingFrom", "createTimestamp", "creatorsName", "deletedEntryAttrs", "dITContentRules", "dITStructureRules", "dncomp", "ds-pluginDigest", "ds-pluginSignature", "ds6ruv", "dsKeyedPassword", "entrydn", "entryid", "hasSubordinates", "idmpasswd", "isMemberOf", "ldapSchemas", "ldapSyntaxes", "matchingRules", "matchingRuleUse", "modDNEnabledSuffixes", "modifiersName", "modifyTimestamp", "nameForms", "namingContexts", "nsAccountLock", "nsBackendSuffix", "nscpEntryDN", "nsds5ReplConflict", "nsIdleTimeout", "nsLookThroughLimit", "nsRole", "nsRoleDN", "nsSchemaCSN", "nsSizeLimit", "nsTimeLimit", "nsUniqueId", "numSubordinates", "objectClasses", "parentid", "passwordAllowChangeTime", "passwordExpirationTime", "passwordExpWarned", "passwordHistory", "passwordPolicySubentry", "passwordRetryCount", "pwdAccountLockedTime", "pwdChangedTime", "pwdFailureTime", "pwdGraceUseTime", "pwdHistory", "pwdLastAuthTime", "pwdPolicySubentry", "pwdReset", "replicaIdentifier", "replicationCSN", "retryCountResetTime", "subschemaSubentry", "supportedControl", "supportedExtension", "supportedLDAPVersion", "supportedSASLMechanisms", "supportedSSLCiphers", "targetUniqueId", "vendorName", "vendorVersion"};

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(attrIds);


        String searchFilter = matchObj.getSearchFilter();
        // replace the place holder in the search filter
        searchFilter = searchFilter.replace("?", searchValue);


        log.debug("Search Filter=" + searchFilter);
        log.debug("Searching BaseDN=" + matchObj.getSearchBaseDn());

        return ctx.search(matchObj.getSearchBaseDn(), searchFilter, searchCtls);


    }

    protected NamingEnumeration lookupSearch(ManagedSystemObjectMatch matchObj,
                                             LdapContext ctx,
                                             String searchValue, String[] attrAry, String objectBaseDN) throws NamingException {

        String attrIds[] = {"1.1", "+", "*", "accountUnlockTime", "aci", "aclRights", "aclRightsInfo", "altServer", "attributeTypes", "changeHasReplFixupOp", "changeIsReplFixupOp", "copiedFrom", "copyingFrom", "createTimestamp", "creatorsName", "deletedEntryAttrs", "dITContentRules", "dITStructureRules", "dncomp", "ds-pluginDigest", "ds-pluginSignature", "ds6ruv", "dsKeyedPassword", "entrydn", "entryid", "hasSubordinates", "idmpasswd", "isMemberOf", "ldapSchemas", "ldapSyntaxes", "matchingRules", "matchingRuleUse", "modDNEnabledSuffixes", "modifiersName", "modifyTimestamp", "nameForms", "namingContexts", "nsAccountLock", "nsBackendSuffix", "nscpEntryDN", "nsds5ReplConflict", "nsIdleTimeout", "nsLookThroughLimit", "nsRole", "nsRoleDN", "nsSchemaCSN", "nsSizeLimit", "nsTimeLimit", "nsUniqueId", "numSubordinates", "objectClasses", "parentid", "passwordAllowChangeTime", "passwordExpirationTime", "passwordExpWarned", "passwordHistory", "passwordPolicySubentry", "passwordRetryCount", "pwdAccountLockedTime", "pwdChangedTime", "pwdFailureTime", "pwdGraceUseTime", "pwdHistory", "pwdLastAuthTime", "pwdPolicySubentry", "pwdReset", "replicaIdentifier", "replicationCSN", "retryCountResetTime", "subschemaSubentry", "supportedControl", "supportedExtension", "supportedLDAPVersion", "supportedSASLMechanisms", "supportedSSLCiphers", "targetUniqueId", "vendorName", "vendorVersion"};

        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(attrIds);


        String searchFilter = matchObj.getSearchFilter();
        // replace the place holder in the search filter
        searchFilter = searchFilter.replace("?", searchValue);

        if (objectBaseDN == null) {
            objectBaseDN = matchObj.getSearchBaseDn();
        }


        log.debug("Search Filter=" + searchFilter);
        log.debug("Searching BaseDN=" + objectBaseDN);

        return ctx.search(objectBaseDN, searchFilter, searchCtls);


    }

    protected Attribute generateActiveDirectoryPassword(String cleartextPassword) {
        try {
            byte[] password = ("\"" + cleartextPassword + "\"").getBytes("UTF-16LE");
            return new BasicAttribute("unicodePwd", password);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return null;
        }
    }


    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }


    public ManagedSystemObjectMatchDAO getManagedSysObjectMatchDao() {
        return managedSysObjectMatchDao;
    }

    public void setManagedSysObjectMatchDao(ManagedSystemObjectMatchDAO managedSysObjectMatchDao) {
        this.managedSysObjectMatchDao = managedSysObjectMatchDao;
    }
}

