package org.openiam.spml2.spi.ldap;

import org.openiam.exception.ConfigurationException;
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
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LdapAddCommand implements the add operation for the LdapConnector
 * User: suneetshah
 */
public class LdapAddCommand extends LdapAbstractCommand {

    public AddResponseType add(AddRequestType reqType) {

        AddResponseType response = new AddResponseType();
        response.setStatus(StatusCodeType.SUCCESS);
        List<String> targetMembershipList = new ArrayList<String>();
        boolean groupMembershipEnabled = true;



        log.debug("add request called..");

        String requestID = reqType.getRequestID();
        /* ContainerID - May specify the container in which this object should be created
           *      ie. ou=Development, org=Example */
        PSOIdentifierType containerID = reqType.getContainerID();
        /* PSO - Provisioning Service Object -
           *     -  ID must uniquely specify an object on the target or in the target's namespace
           *     -  Try to make the PSO ID immutable so that there is consistency across changes. */
        PSOIdentifierType psoID = reqType.getPsoID();
        /* targetID -  */
        String targetID = reqType.getTargetID();

        // Data sent with request - Data must be present in the request per the spec
        ExtensibleType data = reqType.getData();
        Map<QName, String> otherData = reqType.getOtherAttributes();

        /* Indicates what type of data we should return from this operations */
        ReturnDataType returnData = reqType.getReturnData();


        /* A) Use the targetID to look up the connection information under managed systems */
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        log.debug("managedSys found for targetID=" + targetID + " " + " Name=" + managedSys.getName());
        ConnectionMgr conMgr = ConnectionFactory.create(ConnectionManagerConstant.LDAP_CONNECTION);
        conMgr.setApplicationContext(ac);
        try {

            LdapContext ldapctx = conMgr.connect(managedSys);

            log.debug("Ldapcontext = " + ldapctx);

            if (ldapctx == null) {
                response.setStatus(StatusCodeType.FAILURE);
                response.setError(ErrorCode.DIRECTORY_ERROR);
                response.addErrorMessage("Unable to connect to directory.");
                return response;
            }


            ManagedSystemObjectMatch matchObj = null;
            List<ManagedSystemObjectMatch> matchObjList = managedSysObjectMatchDao.findBySystemId(targetID, "USER");
            if (matchObjList != null && matchObjList.size() > 0) {
                matchObj = matchObjList.get(0);
            }

            log.debug("matchObj = " + matchObj);

            if (matchObj == null) {
                throw new ConfigurationException("LDAP configuration is missing configuration information");
            }

            Set<ResourceProp> rpSet = getResourceAttributes(managedSys.getResourceId());
            ResourceProp rpGroupMembership = getResourceAttr(rpSet,"GROUP_MEMBERSHIP_ENABLED");

            // BY DEFAULT - we want to enable group membership
            if (rpGroupMembership == null || rpGroupMembership.getPropValue() == null || "Y".equalsIgnoreCase(rpGroupMembership.getPropValue())) {
                groupMembershipEnabled = true;
            }else if (rpGroupMembership.getPropValue() != null) {

                if ("N".equalsIgnoreCase(rpGroupMembership.getPropValue())) {
                    groupMembershipEnabled = false;
                }
            }


            Directory dirSpecificImp  = DirectorySpecificImplFactory.create(managedSys.getHandler1());


            log.debug("baseDN=" + matchObj.getBaseDn());
            log.debug("ID field=" + matchObj.getKeyField());
            log.debug("Group Membership enabled? " + groupMembershipEnabled);

            // get the baseDN
            String baseDN = matchObj.getBaseDn();


            // get the field that is to be used as the UniqueIdentifier
            //String ldapName = matchObj.getKeyField() +"=" + psoID.getID() + "," + baseDN;
            String ldapName = psoID.getID();

            // check if the identity exists in ldap first before creating the identity
            if (identityExists(ldapName, ldapctx)) {
                log.debug(ldapName + "exists. Returning success from the connector");
                return response;
            }
            //


            BasicAttributes basicAttr = getBasicAttributes(reqType.getData().getAny(), matchObj.getKeyField(),
                    targetMembershipList, groupMembershipEnabled);


            log.debug("Creating users in ldap.." + ldapName);

            Context result = ldapctx.createSubcontext(ldapName, basicAttr);

            if (groupMembershipEnabled) {

                log.debug("Updating account membership...");

                dirSpecificImp.updateAccountMembership(targetMembershipList,ldapName,  matchObj, ldapctx, reqType.getData().getAny());
            }


        } catch (NamingException ne) {
            log.error(ne);
            // return a response object - even if it fails so that it can be logged.
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.DIRECTORY_ERROR);
            response.addErrorMessage(ne.toString());
        } catch (Exception e) {
            e.printStackTrace();

            log.error(e);
            // return a response object - even if it fails so that it can be logged.
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.DIRECTORY_ERROR);
            response.addErrorMessage(e.toString());


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


        return response;
    }

    private ModificationItem[] getLdapPassword( List<ExtensibleObject> requestAttribute, String ldapName) {

        for (ExtensibleObject obj : requestAttribute) {
            List<ExtensibleAttribute> attrList = obj.getAttributes();
            for (ExtensibleAttribute att : attrList) {

                if ("userPassword".equalsIgnoreCase(att.getName())) {
                    ModificationItem mods[] = new ModificationItem[1];
                    mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(att.getName(), att.getValue()));
                    return mods;
                }
            }
        }
        return null;

    }


}
