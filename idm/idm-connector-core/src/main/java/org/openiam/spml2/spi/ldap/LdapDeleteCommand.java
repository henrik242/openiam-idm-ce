package org.openiam.spml2.spi.ldap;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.spi.jdbc.AppTableAbstractCommand;
import org.openiam.spml2.spi.ldap.dirtype.Directory;
import org.openiam.spml2.spi.ldap.dirtype.DirectorySpecificImplFactory;
import org.openiam.spml2.util.connect.ConnectionFactory;
import org.openiam.spml2.util.connect.ConnectionManagerConstant;
import org.openiam.spml2.util.connect.ConnectionMgr;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;


public class LdapDeleteCommand extends LdapAbstractCommand {

    public ResponseType delete(DeleteRequestType reqType) {

        log.debug("delete request called..");
        ConnectionMgr conMgr = null;
        boolean groupMembershipEnabled = true;
        String delete = "DELETE";

        //String uid = null;
        String ou = null;

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


        /* A) Use the targetID to look up the connection information under managed systems */
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        ManagedSystemObjectMatch[] matchObj = managedSysService.managedSysObjectParam(targetID, "USER");

        Set<ResourceProp> rpSet = getResourceAttributes(managedSys.getResourceId());

        ResourceProp rpOnDelete = getResourceAttr(rpSet,"ON_DELETE");
        ResourceProp rpGroupMembership = getResourceAttr(rpSet,"GROUP_MEMBERSHIP_ENABLED");

        if (rpOnDelete == null || rpOnDelete.getPropValue() == null || "DELETE".equalsIgnoreCase(rpOnDelete.getPropValue())) {
            delete = "DELETE";
        }else if (rpOnDelete.getPropValue() != null) {

            if ("DISABLE".equalsIgnoreCase(rpOnDelete.getPropValue())) {
                delete = "DISABLE";
            }
        }

        // BY DEFAULT - we want to enable group membership
        if (rpGroupMembership == null || rpGroupMembership.getPropValue() == null || "Y".equalsIgnoreCase(rpGroupMembership.getPropValue())) {
            groupMembershipEnabled = true;
        }else if (rpGroupMembership.getPropValue() != null) {

            if ("N".equalsIgnoreCase(rpGroupMembership.getPropValue())) {
                groupMembershipEnabled = false;
            }
        }




        try {

            log.debug("managedSys found for targetID=" + targetID + " " + " Name=" + managedSys.getName());
            conMgr = ConnectionFactory.create(ConnectionManagerConstant.LDAP_CONNECTION);
            conMgr.setApplicationContext(ac);
            LdapContext ldapctx = conMgr.connect(managedSys);

            String ldapName = psoID.getID();


            if (groupMembershipEnabled) {
                removeAccountMemberships(ldapName, matchObj[0], ldapctx);
            }

            Directory dirSpecificImp  = DirectorySpecificImplFactory.create(managedSys.getHandler1());


            dirSpecificImp.delete(reqType, ldapctx, ldapName, delete);



        } catch (NamingException ne) {

            log.error(ne);

            ResponseType respType = new ResponseType();
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

        ResponseType respType = new ResponseType();
        respType.setStatus(StatusCodeType.SUCCESS);
        return respType;


    }

    private void removeAccountMemberships( String ldapName, ManagedSystemObjectMatch matchObj,  LdapContext ldapctx ) {

        // get the accounts current membership list
        //List<String> currentMembershipList =  getAccountMembership(ldapName, matchObj,   ldapctx);

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




}
