package org.openiam.spml2.spi.ldap;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.spi.ldap.dirtype.Directory;
import org.openiam.spml2.spi.ldap.dirtype.DirectorySpecificImplFactory;
import org.openiam.spml2.util.connect.ConnectionFactory;
import org.openiam.spml2.util.connect.ConnectionManagerConstant;
import org.openiam.spml2.util.connect.ConnectionMgr;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.openiam.idm.srvc.res.dto.ResourceProp;

import java.util.Set;


public class LdapDeleteCommand extends LdapAbstractCommand {

    public ResponseType delete(DeleteRequestType reqType) {

        log.debug("delete request called..");
        ConnectionMgr conMgr = null;
        boolean groupMembershipEnabled = true;
        String delete = "DELETE";
        ResponseType respType = new ResponseType();

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


            Directory dirSpecificImp  = DirectorySpecificImplFactory.create(managedSys.getHandler5());

            conMgr = ConnectionFactory.create(ConnectionManagerConstant.LDAP_CONNECTION);
            conMgr.setApplicationContext(ac);
            LdapContext ldapctx = conMgr.connect(managedSys);

            if (ldapctx == null) {
                respType.setStatus(StatusCodeType.FAILURE);
                respType.setError(ErrorCode.DIRECTORY_ERROR);
                respType.addErrorMessage("Unable to connect to directory.");
                return respType;
            }

            String ldapName = psoID.getID();


            if (groupMembershipEnabled) {
                dirSpecificImp.removeAccountMemberships(ldapName, matchObj[0], ldapctx);
            }

            dirSpecificImp.delete(reqType, ldapctx, ldapName, delete);



        } catch (NamingException ne) {

            log.error(ne);

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

        respType.setStatus(StatusCodeType.SUCCESS);
        return respType;


    }


}
