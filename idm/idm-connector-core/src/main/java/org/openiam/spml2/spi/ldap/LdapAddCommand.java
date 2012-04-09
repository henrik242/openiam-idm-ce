package org.openiam.spml2.spi.ldap;

import org.openiam.exception.ConfigurationException;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;
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

/**
 * LdapAddCommand implements the add operation for the LdapConnector
 * User: suneetshah
 */
public class LdapAddCommand extends LdapAbstractCommand {

    public AddResponseType add(AddRequestType reqType) {

        AddResponseType response = new AddResponseType();
        response.setStatus(StatusCodeType.SUCCESS);
        List<String> targetMembershipList = new ArrayList<String>();


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

            ManagedSystemObjectMatch matchObj = null;
            List<ManagedSystemObjectMatch> matchObjList = managedSysObjectMatchDao.findBySystemId(targetID, "USER");
            if (matchObjList != null && matchObjList.size() > 0) {
                matchObj = matchObjList.get(0);
            }

            log.debug("matchObj = " + matchObj);

            if (matchObj == null) {
                throw new ConfigurationException("LDAP configuration is missing configuration information");
            }

            log.debug("baseDN=" + matchObj.getBaseDn());
            log.debug("ID field=" + matchObj.getKeyField());

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


            BasicAttributes basicAttr = getBasicAttributes(reqType.getData().getAny(), matchObj.getKeyField(), targetMembershipList);


            log.debug("Creating users in ldap.." + ldapName);

            Context result = ldapctx.createSubcontext(ldapName, basicAttr);



            ModificationItem passwordMod[] = getLdapPassword(reqType.getData().getAny(), ldapName);
            if (passwordMod != null) {

                log.debug("Assigning password to user ");
                ldapctx.modifyAttributes(ldapName, passwordMod);
            }

            log.debug("Associating user to objects for membership");

            // check if we already have any assocattions for this user - could be left over from an earlier time
           // List<String> existingAccountMembership = getAccountMembership(ldapName, matchObj, ldapctx);

            List<String> existingAccountMembership = userMembershipList(ldapName, matchObj, ldapctx);


            // get objects that have a dataType of "memberOf"
            for (ExtensibleObject obj : reqType.getData().getAny()) {
                List<ExtensibleAttribute> attrList = obj.getAttributes();
                for (ExtensibleAttribute att : attrList) {



                    if ( att.getDataType() != null &&  "memberOf".equalsIgnoreCase(att.getDataType()) ) {
                        List<String> membershipList = att.getValueList();
                        for (String s : membershipList) {
                            if (s != null && s.length() > 0) {

                                // check if the user already has membership in this object.
                                // ldapName and S
                                if ( !isMemberOf(existingAccountMembership, s) ) {

                                    ModificationItem mods[] = new ModificationItem[1];
                                    mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(att.getName(), ldapName));
                                    ldapctx.modifyAttributes(s, mods);
                                }
                            }

                        }
                    }
                }
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
