package org.openiam.spml2.spi.ldap;

import org.openiam.base.BaseAttribute;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.spi.ldap.dirtype.Directory;
import org.openiam.spml2.spi.ldap.dirtype.DirectorySpecificImplFactory;
import org.openiam.spml2.util.connect.ConnectionFactory;
import org.openiam.spml2.util.connect.ConnectionManagerConstant;
import org.openiam.spml2.util.connect.ConnectionMgr;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//import org.openiam.spml2.msg.ExtensibleAttribute;

/**
 * Implements modify capability for the LdapConnector
 * User: suneetshah
 * Date: 7/30/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LdapModifyCommand extends LdapAbstractCommand {

    public ModifyResponseType modify(ModifyRequestType reqType) {
        /* FOR LDAP, need to be able to move object's OU - incase of re-org, person changes roles, etc */
        /* Need to be able add and remove users from groups */

        log.debug("LDAP Modify request called..");
        ConnectionMgr conMgr = null;
        LdapContext ldapctx = null;
        List<ExtensibleObject> extobjectList = null;
        boolean groupMembershipEnabled = true;


        List<BaseAttribute> targetMembershipList = new ArrayList<BaseAttribute>();

        ModifyResponseType respType = new ModifyResponseType();
        respType.setStatus(StatusCodeType.SUCCESS);


        String requestID = reqType.getRequestID();
        /* PSO - Provisioning Service Object -
           *     -  ID must uniquely specify an object on the target or in the target's namespace
           *     -  Try to make the PSO ID immutable so that there is consistency across changes. */
        PSOIdentifierType psoID = reqType.getPsoID();
        /* targetID -  */
        String targetID = psoID.getTargetID();
        /* ContainerID - May specify the container in which this object should be created
           *      ie. ou=Development, org=Example */
        PSOIdentifierType containerID = psoID.getContainerID();


        // modificationType contains a collection of objects for each type of operation
        List<ModificationType> modificationList = reqType.getModification();

        log.debug("ModificationList = " + modificationList);
        log.debug("Modificationlist size= " + modificationList.size());


        /* A) Use the targetID to look up the connection information under managed systems */
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        log.debug("managedSys found for targetID=" + targetID + " " + " Name=" + managedSys.getName());
        try {

            conMgr = ConnectionFactory.create(ConnectionManagerConstant.LDAP_CONNECTION);
            conMgr.setApplicationContext(ac);
            ldapctx = conMgr.connect(managedSys);
        } catch (NamingException ne) {
            respType.setStatus(StatusCodeType.FAILURE);
            respType.setError(ErrorCode.DIRECTORY_ERROR);
            respType.addErrorMessage(ne.toString());
            return respType;
        }

        log.debug("Ldapcontext = " + ldapctx);

        if (ldapctx == null) {
            respType.setStatus(StatusCodeType.FAILURE);
            respType.setError(ErrorCode.DIRECTORY_ERROR);
            respType.addErrorMessage("Unable to connect to directory.");
            return respType;
        }

        String ldapName = psoID.getID();

        ExtensibleAttribute origIdentity = isRename(modificationList);
        if (origIdentity != null) {
            log.debug("Renaming identity: " + origIdentity.getValue());

            try {
                ldapctx.rename(origIdentity.getValue(), ldapName);
                log.debug("Renaming : " + origIdentity.getValue());

            } catch (NamingException ne) {
                respType.setStatus(StatusCodeType.FAILURE);
                respType.setError(ErrorCode.DIRECTORY_ERROR);
                respType.addErrorMessage(ne.toString());
                return respType;
            }
        }


        // determine if this record already exists in ldap
        // move to separate method
        ManagedSystemObjectMatch[] matchObj = managedSysService.managedSysObjectParam(targetID, "USER");

        Set<ResourceProp> rpSet = getResourceAttributes(managedSys.getResourceId());
        ResourceProp rpGroupMembership = getResourceAttr(rpSet, "GROUP_MEMBERSHIP_ENABLED");

        // BY DEFAULT - we want to enable group membership
        if (rpGroupMembership == null || rpGroupMembership.getPropValue() == null || "Y".equalsIgnoreCase(rpGroupMembership.getPropValue())) {
            groupMembershipEnabled = true;
        } else if (rpGroupMembership.getPropValue() != null) {

            if ("N".equalsIgnoreCase(rpGroupMembership.getPropValue())) {
                groupMembershipEnabled = false;
            }
        }

        Directory dirSpecificImp = DirectorySpecificImplFactory.create(managedSys.getHandler5());


        if (isInDirectory(ldapName, matchObj[0], ldapctx)) {

            log.debug("ldapName found in directory. Update the record..");

            try {
                List<ModificationType> modTypeList = reqType.getModification();
                for (ModificationType mod : modTypeList) {
                    ExtensibleType extType = mod.getData();
                    extobjectList = extType.getAny();
                    for (ExtensibleObject obj : extobjectList) {


                        List<ExtensibleAttribute> attrList = obj.getAttributes();
                        List<ModificationItem> modItemList = new ArrayList<ModificationItem>();
                        for (ExtensibleAttribute att : attrList) {

                            log.debug("Extensible Attribute: " + att.getName() + " " + att.getDataType());

                            if (att.getDataType() == null) {
                                continue;
                            }

                            if (att.getDataType().equalsIgnoreCase("memberOf")) {
                                if (groupMembershipEnabled) {
                                    buildMembershipList(att, targetMembershipList);
                                }
                            }

                            if (att.getOperation() != 0 && att.getName() != null && !att.getDataType().equalsIgnoreCase("memberOf")) {

                                // set an attribute to null
                                if ((att.getValue() == null || att.getValue().contains("null")) && (att.getValueList() == null || att.getValueList().size() == 0)) {


                                    modItemList.add(new ModificationItem(att.getOperation(), new BasicAttribute(att.getName(), null)));
                                } else {
                                    // valid value

                                    //  if (!att.getDataType().equalsIgnoreCase("memberOf")) {
                                    if ("unicodePwd".equalsIgnoreCase(att.getName())) {
                                        Attribute a = generateActiveDirectoryPassword(att.getValue());
                                        modItemList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, a));
                                    } else if (!"userPassword".equalsIgnoreCase(att.getName()) &&
                                            !"ORIG_IDENTITY".equalsIgnoreCase(att.getName())) {


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

                                        } else {
                                            a = new BasicAttribute(att.getName(), att.getValue());

                                        }
                                        modItemList.add(new ModificationItem(att.getOperation(), a));
                                        //modItemList.add( new ModificationItem(att.getOperation(), new BasicAttribute(att.getName(), att.getValue())));
                                    }
                                }
                            }
                        }
                        ModificationItem[] mods = new ModificationItem[modItemList.size()];
                        modItemList.toArray(mods);

                        log.debug("ModifyAttribute array=" + mods);
                        log.debug("ldapName=" + ldapName);
                        ldapctx.modifyAttributes(ldapName, mods);


                    }
                }
            } catch (NamingException ne) {
                log.error(ne.getMessage(), ne);

                respType.setStatus(StatusCodeType.FAILURE);
                respType.setError(ErrorCode.DIRECTORY_ERROR);
                respType.addErrorMessage(ne.toString());
                return respType;


            } finally {
                /* close the connection to the directory */
                try {
                    if (conMgr != null) {
                        conMgr.close();
                    }

                } catch (NamingException n) {
                    log.error(n);
                }

            }
        } else {
            // create the record in ldap
            log.debug("ldapName NOT FOUND in directory. Adding new record to directory..");

            List<ModificationType> modTypeList = reqType.getModification();
            for (ModificationType mod : modTypeList) {

                ExtensibleType extType = mod.getData();
                extobjectList = extType.getAny();

                log.debug("Modify: Extensible Object List =" + extobjectList);

                BasicAttributes basicAttr = getBasicAttributes(extobjectList, matchObj[0].getKeyField(),
                        targetMembershipList, groupMembershipEnabled);

                try {
                    Context result = ldapctx.createSubcontext(ldapName, basicAttr);
                } catch (NamingException ne) {
                    respType.setStatus(StatusCodeType.FAILURE);
                    respType.setError(ErrorCode.DIRECTORY_ERROR);
                    respType.addErrorMessage(ne.toString());
                    return respType;

                } finally {
                    /* close the connection to the directory */
                    try {
                        if (conMgr != null) {
                            conMgr.close();
                        }

                    } catch (NamingException n) {
                        log.error(n);
                    }

                }
            }

        }

        if (groupMembershipEnabled) {
            dirSpecificImp.updateAccountMembership(targetMembershipList, ldapName, matchObj[0], ldapctx, extobjectList);
        }

        return respType;
    }


    private ExtensibleAttribute isRename(List<ModificationType> modTypeList) {
        for (ModificationType mod : modTypeList) {
            ExtensibleType extType = mod.getData();
            List<ExtensibleObject> extobjectList = extType.getAny();
            for (ExtensibleObject obj : extobjectList) {

                log.debug("ReName Object:" + obj.getName() + " - operation=" + obj.getOperation());

                List<ExtensibleAttribute> attrList = obj.getAttributes();
                List<ModificationItem> modItemList = new ArrayList<ModificationItem>();
                for (ExtensibleAttribute att : attrList) {
                    if (att.getOperation() != 0 && att.getName() != null) {
                        if (att.getName().equalsIgnoreCase("ORIG_IDENTITY")) {
                            return att;
                        }
                    }
                }
            }
        }
        return null;
    }


}


