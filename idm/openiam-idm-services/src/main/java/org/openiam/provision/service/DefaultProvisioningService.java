/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 *
 */
package org.openiam.provision.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.context.MuleContextAware;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.BaseObject;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.connector.type.LookupRequest;
import org.openiam.connector.type.LookupResponse;
import org.openiam.connector.type.UserRequest;
import org.openiam.connector.type.UserResponse;
import org.openiam.exception.EncryptionException;
import org.openiam.exception.ObjectNotFoundException;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.dto.PolicyAttribute;
import org.openiam.idm.srvc.pswd.dto.Password;
import org.openiam.idm.srvc.pswd.dto.PasswordHistory;
import org.openiam.idm.srvc.pswd.dto.PasswordValidationCode;
import org.openiam.idm.srvc.pswd.service.PasswordGenerator;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.AccountLockEnum;
import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.dto.UserResourceAssociation;
import org.openiam.provision.resp.LookupUserResponse;
import org.openiam.provision.resp.PasswordResponse;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.provision.type.ExtensibleUser;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.springframework.context.ApplicationContextAware;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.*;

/**
 * DefaultProvisioningService is responsible for receiving and processing requests that are to be sent to the target
 * system connectors.
 *
 * @author suneet
 */
@WebService(endpointInterface = "org.openiam.provision.service.ProvisionService",
        targetNamespace = "http://www.openiam.org/service/provision",
        portName = "DefaultProvisionControllerServicePort",
        serviceName = "ProvisioningService")
public class DefaultProvisioningService extends AbstractProvisioningService implements MuleContextAware, ProvisionService, ApplicationContextAware {

    private static final Log log = LogFactory.getLog(DefaultProvisioningService.class);


    public Response testConnectionConfig(String managedSysId) {
        return validateConnection.testConnection(managedSysId, muleContext);
    }

    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#addUser(org.openiam.provision.dto.ProvisionUser)
      */
    public ProvisionUserResponse addUser(ProvisionUser user) {
        ProvisionUserResponse resp = new ProvisionUserResponse();

        ScriptIntegration se = null;
        Map<String, Object> bindingMap = new HashMap<String, Object>();
        Organization org = null;
        IdmAuditLog auditLog = null;
        boolean connectorSuccess = true;
        List<IdmAuditLog> pendingLogItems = new ArrayList<IdmAuditLog>();
        String requestId = "R" + UUIDGen.getUUID();

        // flag to determine if we should provision this user in target systems
        boolean provInTargetSystemNow = true;


        // determine if we provision now or in the future
        // if its in the future then we wont put the user in the target systems
        provInTargetSystemNow = provisionUserNow(user);
        if (!provInTargetSystemNow) {
            // start date is in the future
            // flag says that we should prov after the startdate
            user.setStatus(UserStatusEnum.PENDING_START_DATE);
        }

        try {
            se = ScriptFactory.createModule(this.scriptEngine);
        } catch (Exception e) {
            log.error(e);
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_OTHER);
            return resp;
        }


        if (user.getUser().getCompanyId() != null) {
            org = orgManager.getOrganization(user.getUser().getCompanyId());
        }
        // bind the objects to the scripting engine

        bindingMap.put("sysId", sysConfiguration.getDefaultManagedSysId());
        bindingMap.put("user", user);
        bindingMap.put("org", org);
        bindingMap.put("context", ac);
        bindingMap.put("operation", "ADD");


        // run the pre-processor before the body of the add operation
        if (callPreProcessor("ADD", user, bindingMap) != ProvisioningConstants.SUCCESS) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_PREPROCESSOR);
            return resp;
        }

        if (user.getStatus() == null) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.MISSING_REQUIRED_ATTRIBUTE);
            return resp;
        }

        // make sure that our object as the attribute set that will be used for audit logging
        checkAuditingAttributes(user);


        // CREATE THE PRIMARY IDENTITY IF IT HAS NOT BEEN PASSED IN

        boolean customPassword = false;
        Login primaryLogin = null;

        if (user.getPrincipalList() == null || user.getPrincipalList().isEmpty()) {
            // build the list
            buildPrimaryPrincipal(user, bindingMap, se);

        } else {
            primaryLogin = user.getPrimaryPrincipal(sysConfiguration.getDefaultManagedSysId());
            // Check if a custom password is set
            if (primaryLogin.getPassword() != null && !primaryLogin.getPassword().trim().isEmpty()) {
                customPassword = true;
            } else {
                setPrimaryIDPassword(user, bindingMap, se);
            }
        }

        if (primaryLogin == null) {
            primaryLogin = user.getPrimaryPrincipal(sysConfiguration.getDefaultManagedSysId());
        }

        // check if there is a custom password provided in the request
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            customPassword = true;
            primaryLogin.setPassword(user.getPassword());
        }

        Policy passwordPolicy = user.getPasswordPolicy();
        if (passwordPolicy == null) {
            passwordPolicy = passwordDS.getPasswordPolicyByUser(primaryLogin.getId().getDomainId(), user.getUser());
        }

        // if the password of the primaryIdentity is a custom password validate the password
        if (customPassword) {
            Password password = new Password();
            password.setDomainId(primaryLogin.getId().getDomainId());
            password.setManagedSysId(primaryLogin.getId().getManagedSysId());
            password.setPassword(primaryLogin.getPassword());
            password.setPrincipal(primaryLogin.getId().getLogin());

            try {
                PasswordValidationCode valCode = passwordDS.isPasswordValidForUserAndPolicy(password, user.getUser(), primaryLogin, passwordPolicy);
                if (valCode == null || valCode != PasswordValidationCode.SUCCESS) {
                    auditHelper.addLog("CREATE", user.getRequestorDomain(), user.getRequestorLogin(),
                            "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                            null, "FAIL", null, "USER_STATUS",
                            user.getUser().getStatus().toString(),
                            requestId, ResponseCode.FAIL_DECRYPTION.toString(), user.getSessionId(), "Password validation failed",
                            user.getRequestClientIP(), primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId());

                    resp.setStatus(ResponseStatus.FAILURE);
                    resp.setErrorCode(ResponseCode.FAIL_NEQ_PASSWORD);
                    return resp;
                }
            } catch (ObjectNotFoundException e) {
                auditHelper.addLog("CREATE", user.getRequestorDomain(), user.getRequestorLogin(),
                        "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                        null, "FAIL", null, "USER_STATUS",
                        user.getUser().getStatus().toString(),
                        requestId, ResponseCode.FAIL_DECRYPTION.toString(), user.getSessionId(), e.toString(),
                        user.getRequestClientIP(), primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId());

                resp.setStatus(ResponseStatus.FAILURE);
                resp.setErrorCode(ResponseCode.FAIL_NEQ_PASSWORD);
                return resp;
            }
        }

        // validate that this identity does not already exist
        LoginId dupId = primaryLogin.getId();
        Login dupPrincipal = loginManager.getLoginByManagedSys(dupId.getDomainId(), dupId.getLogin(), dupId.getManagedSysId());

        if (dupPrincipal != null) {
            // identity exists

            auditHelper.addLog("CREATE", user.getRequestorDomain(), user.getRequestorLogin(),
                    "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                    null, "FAIL", null, "USER_STATUS",
                    user.getUser().getStatus().toString(),
                    requestId, "DUPLICATE PRINCIPAL", user.getSessionId(), "Identity already exists:" + dupId.getManagedSysId() + " - " + dupId.getLogin(),
                    user.getRequestClientIP(), primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId());

            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.DUPLICATE_PRINCIPAL);
            return resp;

        }
        // identity passed isDuplicate check





        /* Create the new user in the openiam repository */
        resp = createUser(user, pendingLogItems);

        if (resp.getStatus() == ResponseStatus.SUCCESS) {
            auditLog = auditHelper.addLog("CREATE", user.getRequestorDomain(), user.getRequestorLogin(),
                    "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                    null, "SUCCESS", null, "USER_STATUS",
                    user.getUser().getStatus().toString(),
                    requestId, null, user.getSessionId(), null,
                    user.getRequestClientIP(), primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId());
            auditHelper.persistLogList(pendingLogItems, requestId, user.getSessionId());

        } else {
            auditLog = auditHelper.addLog("CREATE", user.getRequestorDomain(), user.getRequestorLogin(),
                    "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                    null, "FAIL", null, "USER_STATUS",
                    user.getUser().getStatus().toString(),
                    requestId, resp.getErrorCode().toString(), user.getSessionId(), resp.getErrorText(),
                    user.getRequestClientIP(), primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId());
        }


        // need decrypted password for use in the connectors:
        String decPassword = null;
        try {
            decPassword = loginManager.decryptPassword(primaryLogin.getPassword());
        } catch (EncryptionException e) {

            auditHelper.addLog("CREATE", user.getRequestorDomain(), user.getRequestorLogin(),
                    "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                    null, "FAIL", null, "USER_STATUS",
                    user.getUser().getStatus().toString(),
                    requestId, ResponseCode.FAIL_DECRYPTION.toString(), user.getSessionId(), e.toString(),
                    user.getRequestClientIP(), primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId());


            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_DECRYPTION);
            return resp;
        }
        bindingMap.put("lg", primaryLogin);
        bindingMap.put("password", decPassword);

        // if the add password to history flag is on, the add this password to the history so that its not used again

        if (user.isAddInitialPasswordToHistory() || customPassword) {
            // add the auto generated password to the history so that the user can not use this password as their first password
            PasswordHistory hist = new PasswordHistory(primaryLogin.getId().getLogin(), primaryLogin.getId().getDomainId(),
                    primaryLogin.getId().getManagedSysId());
            hist.setPassword(primaryLogin.getPassword());
            passwordHistoryDao.add(hist);
        }


        // Update attributes that will be used by the password policy
        passwordPolicy = passwordDS.getPasswordPolicy(primaryLogin.getId().getDomainId(), primaryLogin.getId().getLogin(), primaryLogin.getId().getManagedSysId());
        PolicyAttribute policyAttr = getPolicyAttribute("CHNG_PSWD_ON_RESET", passwordPolicy);
        if (policyAttr != null) {
            // don't force the user to immediately change it's own password
            if (policyAttr.getValue1().equalsIgnoreCase("1") && !customPassword) {
                primaryLogin.setResetPassword(1);
            } else {
                primaryLogin.setResetPassword(0);
            }
            // determin the password expiration and grace period dates
            setPasswordExpValues(passwordPolicy, primaryLogin);

            loginManager.updateLogin(primaryLogin);
        }


        // provision the user into the systems that they should have access to.
        // get the list of resources for each role that user belongs too.

        bindingMap.put("userRole", user.getMemberOfRoles());

        if (provInTargetSystemNow) {
            List<Resource> resourceList = getResourcesForRole(user.getMemberOfRoles());

            // update the resource list to include the resources that have been added directly
            if (resourceList == null) {

                resourceList = new ArrayList<Resource>();
            }

            addDirectResourceAssociation(user, resourceList);

            if (resourceList != null && !resourceList.isEmpty()) {
                for (Resource res : resourceList) {

                    log.debug("Resource->managedSysId =" + res.getManagedSysId());
                    log.debug("Resource->resourceId =" + res.getResourceId());

                    String managedSysId = res.getManagedSysId();

                    if (managedSysId != null && managedSysId.length() > 0) {

                        bindingMap.put(TARGET_SYS_RES_ID, res.getResourceId());
                        bindingMap.put(TARGET_SYS_MANAGED_SYS_ID, res.getManagedSysId());


                        // object that will be sent to the connectors
                        List<AttributeMap> attrMap = managedSysService.getResourceAttributeMaps(res.getResourceId());
                        //List<AttributeMap> attrMap = resourceDataService.getResourceAttributeMaps(res.getResourceId());

                        ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

                        log.debug("Managed sys =" + mSys);

                        ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                        ManagedSystemObjectMatch matchObj = null;
                        ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(managedSysId, "USER");
                        if (matchObjAry != null && matchObjAry.length > 0) {
                            matchObj = matchObjAry[0];
                            bindingMap.put(MATCH_PARAM, matchObj);
                        }


                        log.debug(" - Building principal Name for: " + managedSysId);
                        // build the primary identity
                        String newPrincipalName = buildPrincipalName(attrMap, se, bindingMap);

                        log.debug(" - New principalName = " + newPrincipalName);

                        // get the current object as it stands in the target system
                        LoginId resLoginId = new LoginId(primaryLogin.getId().getDomainId(), newPrincipalName, managedSysId);
                        Login resLogin = new Login();
                        resLogin.setId(resLoginId);


                        Map<String, String> currentValueMap = getCurrentObjectAtTargetSystem(resLogin, mSys, connector, matchObj);
                        log.debug("Values in target system:" + currentValueMap);
                        // if currentValueMap is null - then add the value - it does not exist in the target system

                        if (currentValueMap == null || currentValueMap.size() == 0) {
                            // we may have identity for a user, but it my have been deleted from the target system
                            // we dont need re-generate the identity in this c
                            bindingMap.put(TARGET_SYSTEM_IDENTITY_STATUS, IDENTITY_NEW);
                            bindingMap.put(TARGET_SYSTEM_ATTRIBUTES, null);
                        } else {
                            bindingMap.put(TARGET_SYSTEM_IDENTITY_STATUS, IDENTITY_EXIST);
                            bindingMap.put(TARGET_SYSTEM_ATTRIBUTES, currentValueMap);
                        }

                        bindingMap.put(TARGET_SYSTEM_IDENTITY, newPrincipalName);
                        bindingMap.put(TARGET_SYS_SECURITY_DOMAIN, resLoginId.getDomainId());

                        // pre-processing
                        String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                        if (preProcessScript != null && !preProcessScript.isEmpty()) {
                            PreProcessor ppScript = createPreProcessScript(preProcessScript);
                            if (ppScript != null) {
                                if (executePreProcess(ppScript, bindingMap, user, "ADD") == ProvisioningConstants.FAIL) {
                                    continue;
                                }
                            }
                        }


                        // attributes are built using groovy script rules
                        ExtensibleUser extUser = buildFromRules(user, attrMap, se,
                                managedSysId, sysConfiguration.getDefaultSecurityDomain(),
                                bindingMap, user.getCreatedBy());


                        List<Login> priList = user.getPrincipalList();
                        if (priList != null) {
                            for (Login l : priList) {
                                log.debug("identity after builder=" + l.getId());
                            }
                        } else {
                            log.debug("priList is null");
                        }

                        // get the identity linked to this resource / managedsys
                        Login mLg = getPrincipalForManagedSys(managedSysId, user.getPrincipalList());
                        if (mLg == null) {
                            mLg = new Login();
                        }
                        mLg.setPassword(primaryLogin.getPassword());
                        mLg.setUserId(primaryLogin.getUserId());

                        log.debug("Creating identity in openiam repository:" + mLg.getId());

                        // validate if the identity exists in the system first


                        connectorSuccess = callConnector(mLg, requestId, mSys, matchObj, extUser, connector, user, auditLog);

                        // only put the identity into the openiam repository if we successfully created the identity
                        if (connectorSuccess) {

                            Login tempPrincipal = loginManager.getLoginByManagedSys(mLg.getId().getDomainId(), mLg.getId().getLogin(), mLg.getId().getManagedSysId());

                            if (tempPrincipal == null) {
                                loginManager.addLogin(mLg);

                            } else {
                                log.debug("Skipping the creation of identity in openiam repository. Identity already exists" + mLg.getId());
                            }

                        }

                        // post processing
                        String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
                        if (postProcessScript != null && !postProcessScript.isEmpty()) {
                            PostProcessor ppScript = createPostProcessScript(postProcessScript);
                            if (ppScript != null) {
                                executePostProcess(ppScript, bindingMap, user, "ADD", connectorSuccess);
                            }
                        }


                        bindingMap.remove(MATCH_PARAM);
                    }

                }

            }
        }

        // make sure that we have an identity for each domain that this user belongs to
        validateIdentitiesExistforSecurityDomain(primaryLogin, user.getMemberOfRoles());


        /* Response object */
        if (!connectorSuccess) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_CONNECTOR);
        } else {

            if (user.isEmailCredentialsToNewUsers()) {
                sendCredentialsToUser(user, primaryLogin.getId().getLogin(), decPassword);
            }
            if (user.isEmailCredentialsToSupervisor()) {
                if (user.getSupervisor() != null) {
                    Supervisor sv = user.getSupervisor();
                    if (sv != null && sv.getSupervisor() != null) {
                        sendCredentialsToSupervisor(sv.getSupervisor(), primaryLogin.getId().getLogin(),
                                decPassword, user.getFirstName() + " " + user.getLastName());
                    }
                }
            }

            resp.setStatus(ResponseStatus.SUCCESS);
        }

        bindingMap.put("userAfterAdd", user);

        // call the post processor


        if (callPostProcessor("ADD", user, bindingMap) != ProvisioningConstants.SUCCESS) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_PREPROCESSOR);
            return resp;
        }

        resp.setUser(user);
        return resp;
    }


    /**
     * Determine when we are going to provision a user
     *
     * @param user
     * @return
     */
    private boolean provisionUserNow(ProvisionUser user) {

        // if no start is provided then we can assume that we want to provision the user now. There is no future provisioning date
        // if a date is provided and its in the future, then provision it later

        Date curDate = new Date(System.currentTimeMillis());
        Date startDate = user.getStartDate();

        if (startDate == null) {
            // no startDate specified = assume that we can provision now
            return true;
        }

        if (!user.isProvisionOnStartDate()) {
            return true;
        }


        return !curDate.before(startDate);

    }


    private void setPasswordExpValues(Policy plcy, Login lg) {
        Calendar cal = Calendar.getInstance();
        Calendar expCal = Calendar.getInstance();


        String pswdExpValue = getPolicyAttribute(plcy.getPolicyAttributes(), "PWD_EXPIRATION");
        String gracePeriod = getPolicyAttribute(plcy.getPolicyAttributes(), "PWD_EXP_GRACE");


        // password has been changed - we dont need to force a change password on the next login


        // calculate when the password will expire
        if (pswdExpValue != null && !pswdExpValue.isEmpty()) {
            cal.add(Calendar.DATE, Integer.parseInt(pswdExpValue));
            expCal.add(Calendar.DATE, Integer.parseInt(pswdExpValue));
            lg.setPwdExp(expCal.getTime());

            // calc the grace period if there is a policy for it
            if (gracePeriod != null && !gracePeriod.isEmpty()) {
                cal.add(Calendar.DATE, Integer.parseInt(gracePeriod));
                lg.setGracePeriod(cal.getTime());
            }
        }


    }

    private String getPolicyAttribute(Set<PolicyAttribute> attr, String name) {
        assert name != null : "Name parameter is null";

        log.debug("Attribute Set size=" + attr.size());

        for (PolicyAttribute policyAtr : attr) {
            if (policyAtr.getName().equalsIgnoreCase(name)) {
                return policyAtr.getValue1();
            }
        }
        return null;

    }


    private PolicyAttribute getPolicyAttribute(String attributeName, Policy policy) {
        if (policy == null) {
            return null;
        }
        PolicyAttribute attribute = policy.getAttribute(attributeName);
        if (attribute.getValue1() == null || attribute.getValue1().length() == 0) {
            return null;
        }
        return attribute;
    }

    private Login getPrincipalForManagedSys(String mSys, List<Login> principalList) {
        if (principalList == null) {
            return null;
        }
        for (Login l : principalList) {
            if (mSys != null) {
                if (l.getId().getManagedSysId().equalsIgnoreCase(mSys)) {
                    return l;
                }
            }

        }
        return null;

    }


    @WebMethod
    public ProvisionUserResponse deleteByUserId(
            ProvisionUser user,
            UserStatusEnum status,
            String requestorId) {

        log.debug("----deleteByUserId called.------");

        IdmAuditLog auditLog = null;
        Map<String, Object> bindingMap = new HashMap<String, Object>();

        ProvisionUserResponse response = new ProvisionUserResponse(ResponseStatus.SUCCESS);

        if (status != UserStatusEnum.DELETED &&
                status != UserStatusEnum.LEAVE &
                        status != UserStatusEnum.TERMINATE &&
                status != UserStatusEnum.RETIRED) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_STATUS);
            return response;
        }


        String requestId = "R" + UUIDGen.getUUID();

        User usr = this.userMgr.getUserWithDependent(user.getUserId(), false);
        if (usr == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }
        ProvisionUser pUser = new ProvisionUser(usr);

        ProvisionServicePreProcessor deletePreProcessScript = createProvPreProcessScript(preProcessor);
        if (deletePreProcessScript != null && !user.isSkipPreprocessor()) {
            deletePreProcessScript.setMuleContext(muleContext);
            if (executeProvisionPreProcess(deletePreProcessScript, bindingMap, pUser, null, "DELETE") != ProvisioningConstants.SUCCESS) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.FAIL_PREPROCESSOR);
                return response;
            }
        }

        if (usr.getStatus() == UserStatusEnum.DELETED ||
                usr.getStatus() == UserStatusEnum.TERMINATE) {
            log.debug("User was already deleted. Nothing more to do.");
            return response;
        }

        usr.updateUser(user.getUser());

        // delete user and all its identities.
        usr.setStatus(status);
        usr.setSecondaryStatus(null);
        usr.setLastUpdatedBy(requestorId);
        usr.setLastUpdate(new Date(System.currentTimeMillis()));
        userMgr.updateUserWithDependent(usr, false);

        Login lRequestor = loginManager.getPrimaryIdentity(requestorId);
        Login lTargetUser = loginManager.getPrimaryIdentity(usr.getUserId());

        if (lRequestor != null && lTargetUser != null) {

            auditLog = auditHelper.addLog("DELETE", lRequestor.getId().getDomainId(), lRequestor.getId().getLogin(),
                    "IDM SERVICE", usr.getCreatedBy(), "0", "USER", usr.getUserId(),
                    null, "SUCCESS", null, "USER_STATUS",
                    usr.getStatus().toString(),
                    requestId, null, null, null,
                    null, lTargetUser.getId().getLogin(), lTargetUser.getId().getDomainId());
        } else {
            log.debug("Unable to log disable operation. Of of the following is null:");
            log.debug("Requestor identity=" + lRequestor);
            log.debug("Target identity=" + lTargetUser);
        }


        // update the identities and set them to inactive
        List<Login> principalList = loginManager.getLoginByUser(user.getUserId());
        if (principalList != null) {
            for (Login l : principalList) {
                if (l.getStatus() != null && !l.getStatus().equalsIgnoreCase("INACTIVE")) {
                    l.setStatus("INACTIVE");
                    l.setAuthFailCount(0);
                    l.setPasswordChangeCount(0);
                    l.setIsLocked(0);
                    loginManager.updateLogin(l);

                    // check if we should update the target system
                    if (user.isNotifyTargetSystems()) {

                        // only add the connectors if its a secondary identity.
                        if (!l.getId().getManagedSysId().equalsIgnoreCase(this.sysConfiguration.getDefaultManagedSysId())) {

                            // some connectors, such as the appTables connector, need data
                            // about other attributes during a delete - so generate a list of attributes

                            ManagedSys mSys = managedSysService.getManagedSys(l.getId().getManagedSysId());
                            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                            ManagedSystemObjectMatch matchObj = null;
                            ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");

                            log.debug("Deleting id=" + l.getId().getLogin());
                            log.debug("- delete using managed sys id=" + mSys.getManagedSysId());

                            PSOIdentifierType idType = new PSOIdentifierType(l.getId().getLogin(), null,
                                    l.getId().getManagedSysId());

                            if (connector.getConnectorInterface() != null &&
                                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                                remoteDelete(l, requestId, mSys, connector, matchObj, user, auditLog);

                            } else {

                                localDelete(l, requestId, idType, mSys, user, auditLog);

                            }

                        }
                    }
                }

            }
        }

        ProvisionServicePostProcessor deletePostProcessScript = createProvPostProcessScript(postProcessor);
        if (deletePostProcessScript != null && !user.isSkipPostProcessor()) {
            deletePostProcessScript.setMuleContext(muleContext);
            if (executeProvisionPostProcess(deletePostProcessScript, bindingMap, pUser, null, "DELETE") != ProvisioningConstants.SUCCESS) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.FAIL_POSTPROCESSOR);
                return response;
            }
        }


        response.setStatus(ResponseStatus.SUCCESS);
        return response;


    }


    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#deleteUser(java.lang.String, java.lang.String, java.lang.String)
      */
    public ProvisionUserResponse deleteUser(String securityDomain,
                                            String managedSystemId, String principal, UserStatusEnum status,
                                            String requestorId) {
        log.debug("----deleteUser called.------");


        IdmAuditLog auditLog = null;

        ProvisionUserResponse response = new ProvisionUserResponse(ResponseStatus.SUCCESS);
        Map<String, Object> bindingMap = new HashMap<String, Object>();

        if (status != UserStatusEnum.DELETED &&
                status != UserStatusEnum.LEAVE &&
                status != UserStatusEnum.TERMINATE &&
                status != UserStatusEnum.RETIRED) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_STATUS);
            return response;
        }


        String requestId = "R" + UUIDGen.getUUID();

        // get the user object associated with this principal
        Login login = loginManager.getLoginByManagedSys(securityDomain,
                principal, managedSystemId);
        if (login == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
            return response;
        }
        // check if the user active
        String userId = login.getUserId();
        if (userId == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }


        User usr = this.userMgr.getUserWithDependent(userId, false);
        if (usr == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }
        ProvisionUser pUser = new ProvisionUser(usr);

        ProvisionServicePreProcessor deletePreProcessScript = createProvPreProcessScript(preProcessor);
        if (deletePreProcessScript != null && !pUser.isSkipPreprocessor()) {
            deletePreProcessScript.setMuleContext(muleContext);
            if (executeProvisionPreProcess(deletePreProcessScript, bindingMap, pUser, null, "DELETE") != ProvisioningConstants.SUCCESS) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.FAIL_PREPROCESSOR);
                return response;
            }
        }


        if (usr.getStatus() == UserStatusEnum.DELETED ||
                usr.getStatus() == UserStatusEnum.TERMINATE) {
            log.debug("User was already deleted. Nothing more to do.");
            return response;
        }


        if (!managedSystemId.equalsIgnoreCase(sysConfiguration.getDefaultManagedSysId())) {
            // managedSysId point to one of the seconardary identities- just terminate that identity
            login.setStatus("INACTIVE");
            login.setAuthFailCount(0);
            login.setPasswordChangeCount(0);
            login.setIsLocked(0);
            loginManager.updateLogin(login);
            // call delete on the connector
            ManagedSys mSys = managedSysService.getManagedSys(managedSystemId);

            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

            ManagedSystemObjectMatch matchObj = null;
            ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");


            // pre-processing

            Resource res = null;
            String resourceId = mSys.getResourceId();


            bindingMap.put("IDENTITY", login);
            bindingMap.put("RESOURCE", res);

            if (resourceId != null) {
                res = resourceDataService.getResource(resourceId);
                if (res != null) {
                    String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                    if (preProcessScript != null && !preProcessScript.isEmpty()) {
                        PreProcessor ppScript = createPreProcessScript(preProcessScript);
                        if (ppScript != null) {
                            executePreProcess(ppScript, bindingMap, pUser, "DELETE");
                        }
                    }
                }
            }


            PSOIdentifierType idType = new PSOIdentifierType(principal, null, managedSystemId);

            boolean connectorSuccess = false;

            if (connector.getConnectorInterface() != null &&
                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {
                UserResponse resp = remoteDelete(login, requestId, mSys, connector, matchObj, new ProvisionUser(usr), auditLog);
                if (resp.getStatus() == StatusCodeType.SUCCESS) {
                    connectorSuccess = true;
                }
            } else {
                ResponseType resp = localDelete(login, requestId, idType, mSys, new ProvisionUser(usr), auditLog);
                if (resp.getStatus() == StatusCodeType.SUCCESS) {
                    connectorSuccess = true;
                }
            }

            String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
            if (postProcessScript != null && !postProcessScript.isEmpty()) {
                PostProcessor ppScript = createPostProcessScript(postProcessScript);
                if (ppScript != null) {
                    executePostProcess(ppScript, bindingMap, pUser, "DELETE", connectorSuccess);
                }
            }


        } else {
            // delete user and all its identities.
            usr.setStatus(status);
            usr.setSecondaryStatus(null);
            usr.setLastUpdatedBy(requestorId);
            usr.setLastUpdate(new Date(System.currentTimeMillis()));
            userMgr.updateUserWithDependent(usr, false);

            Login lRequestor = loginManager.getPrimaryIdentity(requestorId);
            Login lTargetUser = loginManager.getPrimaryIdentity(userId);

            if (lRequestor != null && lTargetUser != null) {

                auditLog = auditHelper.addLog("DELETE", lRequestor.getId().getDomainId(), lRequestor.getId().getLogin(),
                        "IDM SERVICE", usr.getCreatedBy(), "0", "USER", usr.getUserId(),
                        null, "SUCCESS", null, "USER_STATUS",
                        usr.getStatus().toString(),
                        requestId, null, null, null,
                        null, lTargetUser.getId().getLogin(), lTargetUser.getId().getDomainId());
            } else {
                log.debug("Unable to log disable operation. Of of the following is null:");
                log.debug("Requestor identity=" + lRequestor);
                log.debug("Target identity=" + lTargetUser);
            }


            // update the identities and set them to inactive
            List<Login> principalList = loginManager.getLoginByUser(userId);
            if (principalList != null) {
                for (Login l : principalList) {
                    if (l.getStatus() != null && !l.getStatus().equalsIgnoreCase("INACTIVE")) {
                        l.setStatus("INACTIVE");
                        l.setAuthFailCount(0);
                        l.setPasswordChangeCount(0);
                        l.setIsLocked(0);
                        loginManager.updateLogin(l);

                        // only add the connectors if its a secondary identity.
                        if (!l.getId().getManagedSysId().equalsIgnoreCase(this.sysConfiguration.getDefaultManagedSysId())) {

                            ManagedSys mSys = managedSysService.getManagedSys(l.getId().getManagedSysId());

                            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                            ManagedSystemObjectMatch matchObj = null;
                            ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");

                            log.debug("Deleting id=" + l.getId().getLogin());
                            log.debug("- delete using managed sys id=" + mSys.getManagedSysId());

                            // pre-processing
                            bindingMap.put("IDENTITY", l);
                            bindingMap.put("RESOURCE", res);

                            Resource res = null;
                            String resourceId = mSys.getResourceId();
                            if (resourceId != null) {
                                res = resourceDataService.getResource(resourceId);
                                if (res != null) {
                                    String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                                    if (preProcessScript != null && !preProcessScript.isEmpty()) {
                                        PreProcessor ppScript = createPreProcessScript(preProcessScript);
                                        if (ppScript != null) {
                                            if (executePreProcess(ppScript, bindingMap, pUser, "DELETE") == ProvisioningConstants.FAIL) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }


                            PSOIdentifierType idType = new PSOIdentifierType(l.getId().getLogin(), null,
                                    l.getId().getManagedSysId());

                            boolean connectorSuccess = false;

                            if (connector.getConnectorInterface() != null &&
                                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {
                                UserResponse resp = remoteDelete(login, requestId, mSys, connector, matchObj, pUser, auditLog);
                                if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                    connectorSuccess = true;
                                }

                            } else {
                                ResponseType resp = localDelete(login, requestId, idType, mSys, pUser, auditLog);

                                if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                    connectorSuccess = true;
                                }
                            }

                            String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
                            if (postProcessScript != null && !postProcessScript.isEmpty()) {
                                PostProcessor ppScript = createPostProcessScript(postProcessScript);
                                if (ppScript != null) {
                                    executePostProcess(ppScript, bindingMap, pUser, "DELETE", connectorSuccess);
                                }
                            }


                        }
                    }

                }
            }

        }

        ProvisionServicePostProcessor deletePostProcessScript = createProvPostProcessScript(postProcessor);
        if (deletePostProcessScript != null && !pUser.isSkipPostProcessor()) {
            deletePostProcessScript.setMuleContext(muleContext);
            if (executeProvisionPostProcess(deletePostProcessScript, bindingMap, pUser, null, "DELETE") != ProvisioningConstants.SUCCESS) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.FAIL_POSTPROCESSOR);
                return response;
            }
        }

        response.setStatus(ResponseStatus.SUCCESS);
        return response;

    }


    @Override
    public ProvisionUserResponse deprovisionSelectedResources(String userId, String requestorUserId, List<String> resourceList) {
        deprovisionSelectedResource.setMuleContext(muleContext);
        return deprovisionSelectedResource.deprovisionSelectedResources(userId, requestorUserId, resourceList);
    }

    /* (non-Javadoc)
    * @see org.openiam.provision.service.ProvisionService#disableUser(java.lang.String, boolean)
    */
    public Response disableUser(String userId, boolean operation, String requestorId) {
        // get the user
        DisableUserDelegate disableUser = (DisableUserDelegate) ac.getBean("disableUser");

        return disableUser.disableUser(userId, operation, requestorId, muleContext);


    }

    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#lockUser(java.lang.String, org.openiam.provision.dto.AccountLockEnum)
      */
    public Response lockUser(String userId, AccountLockEnum operation, String requestorId) {
        final Response response = new Response();
        String auditReason = null;

        if (userId == null) {
            throw new NullPointerException("userId is null");
        }
        if (requestorId == null) {
            throw new NullPointerException("requestorId is null");
        }

        if (operation == null) {
            throw new NullPointerException("Operation parameter is null");
        }

        User user = userMgr.getUserWithDependent(userId, false);
        if (user == null) {
            log.error("UserId " + userId + " not found");
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.OBJECT_NOT_FOUND);
            return response;

        }
        Login lg = loginManager.getPrimaryIdentity(userId);

        if (operation.equals(AccountLockEnum.LOCKED)) {
            user.setSecondaryStatus(UserStatusEnum.LOCKED);
            if (lg != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Identity flag set to locked.");
                }
                lg.setIsLocked(1);
            }
            auditReason = "LOCKED";
        } else if (operation.equals(AccountLockEnum.LOCKED_ADMIN)) {
            user.setSecondaryStatus(UserStatusEnum.LOCKED_ADMIN);
            if (lg != null) {
                lg.setIsLocked(2);
            }
            auditReason = "LOCKED_ADMIN";
        } else {
            user.setSecondaryStatus(null);
            if (lg == null) {
                log.error(String.format("Primary identity for UserId %s not found", userId));
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
                return response;
            }
            lg.setAuthFailCount(0);
            lg.setIsLocked(0);
            auditReason = "UNLOCK";
        }
        loginManager.updateLogin(lg);
        userMgr.updateUserWithDependent(user, false);

        String requestId = "R" + UUIDGen.getUUID();

        Login lRequestor = loginManager.getPrimaryIdentity(requestorId);

        String login = null;
        String domain = null;
        if (lg.getId() != null) {
            login = lg.getId().getLogin();
            domain = lg.getId().getDomainId();
        }

        final String logDomainId = (lRequestor != null && lRequestor.getId() != null) ? lRequestor.getId().getDomainId() : null;
        final String logLoginId = (lRequestor != null && lRequestor.getId() != null) ? lRequestor.getId().getLogin() : null;
        final String logUserId = (user != null && user.getUserId() != null) ? user.getUserId() : null;

        auditHelper.addLog(auditReason, logDomainId, logLoginId,
                "IDM SERVICE", requestorId, "USER", "USER", logUserId, null, "SUCCESS", null, null,
                null,
                requestId, auditReason, null, null,
                null, login, domain);


        final List<Login> loginList = loginManager.getLoginByUser(userId);
        for (final Login userLogin : loginList) {
            if (userLogin != null) {
                final LoginId id = userLogin.getId();
                if (id != null && id.getManagedSysId() != null && !id.getManagedSysId().equals("0")) {
                    ResponseType responsetype = null;
                    final String managedSysId = id.getManagedSysId();
                    final ManagedSys managedSys = managedSysService.getManagedSys(managedSysId);
                    final PSOIdentifierType psoIdentifierType = new PSOIdentifierType(id.getLogin(), null, managedSysId);
                    if (AccountLockEnum.LOCKED.equals(operation) || AccountLockEnum.LOCKED_ADMIN.equals(operation)) {
                        final SuspendRequestType suspendCommand = new SuspendRequestType();
                        suspendCommand.setPsoID(psoIdentifierType);
                        suspendCommand.setRequestID("R" + System.currentTimeMillis());
                        connectorAdapter.suspendRequest(managedSys, suspendCommand, muleContext);
                    } else {
                        final ResumeRequestType resumeRequest = new ResumeRequestType();
                        resumeRequest.setPsoID(psoIdentifierType);
                        resumeRequest.setRequestID("R" + System.currentTimeMillis());
                        //responsetype = client.resume(resumeRequest);
                        connectorAdapter.resumeRequest(managedSys, resumeRequest, muleContext);
                    }

                    if (responsetype == null) {
                        log.info("Response object from set password is null");
                        response.setStatus(ResponseStatus.FAILURE);
                        return response;
                    }

                    if (responsetype.getStatus() == null) {
                        log.info("Response status is null");
                        response.setStatus(ResponseStatus.FAILURE);
                        return response;
                    }
                    log.info(String.format("Response status=%s", response.getStatus()));

                    //TODO:  process the result of the WS call to resume/suspend of teh connector
                }
            }
        }
        final List<Role> roleList = roleDataService.getUserRoles(user.getUserId());
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (final Role role : roleList) {
                final RoleId roleId = role.getId();
                final List<Resource> resourceList = resourceDataService.getResourcesForRole(roleId.getServiceId(), roleId.getRoleId());
                if (CollectionUtils.isNotEmpty(resourceList)) {
                    for (final Resource resource : resourceList) {
                        final ManagedSys managedSys = managedSysService.getManagedSys(resource.getManagedSysId());
                        if (managedSys != null) {
                            ResponseType responsetype = null;
                            final PSOIdentifierType psoIdentifierType = new PSOIdentifierType(lg.getId().getLogin(), null, managedSys.getManagedSysId());
                            if (AccountLockEnum.LOCKED.equals(operation) || AccountLockEnum.LOCKED_ADMIN.equals(operation)) {
                                final SuspendRequestType suspendCommand = new SuspendRequestType();
                                suspendCommand.setPsoID(psoIdentifierType);
                                suspendCommand.setRequestID("R" + System.currentTimeMillis());
                                connectorAdapter.suspendRequest(managedSys, suspendCommand, muleContext);
                            } else {
                                final ResumeRequestType resumeRequest = new ResumeRequestType();
                                resumeRequest.setPsoID(psoIdentifierType);
                                resumeRequest.setRequestID("R" + System.currentTimeMillis());
                                //responsetype = client.resume(resumeRequest);
                                connectorAdapter.resumeRequest(managedSys, resumeRequest, muleContext);
                            }

                            if (responsetype == null) {
                                log.info("Response object from set password is null");
                                response.setStatus(ResponseStatus.FAILURE);
                                return response;
                            }

                            if (responsetype.getStatus() == null) {
                                log.info("Response status is null");
                                response.setStatus(ResponseStatus.FAILURE);
                                return response;
                            }
                            log.info(String.format("Response status=%s", response.getStatus()));

                            //TODO:  process the result of the WS call to resume/suspend of teh connector
                            /*
                            if(StringUtils.isNotBlank(managedSys.getConnectorId())) {
                                final ProvisionConnector connector = connectorService.getConnector(managedSys.getConnectorId());
                                if(connector != null) {
                                    final ClientProxyFactoryBean factory = new JaxWsProxyFactoryBean();
                                    factory.setServiceClass(ConnectorService.class);

                                    log.info("Service endpoint : " + connector.getServiceUrl() );

                                    factory.setAddress(connector.getServiceUrl());
                                    javax.xml.namespace.QName qname = javax.xml.namespace.QName.valueOf(connector.getServiceNameSpace());
                                    factory.setEndpointName(qname);
                                    final ConnectorService client = (ConnectorService) factory.create();

                                    log.info("connector service client " + client);

                                    ResponseType responsetype = null;
                                    final PSOIdentifierType psoIdentifierType = new PSOIdentifierType(lg.getId().getLogin(),null, lg.getId().getManagedSysId());

                                    if(AccountLockEnum.LOCKED.equals(operation) || AccountLockEnum.LOCKED_ADMIN.equals(operation)) {
                                        final SuspendRequestType suspendCommand = new SuspendRequestType();
                                        suspendCommand.setPsoID(psoIdentifierType);
                                        suspendCommand.setRequestID("R" + System.currentTimeMillis());
                                        connectorAdapter.suspendRequest(managedSys, suspendCommand, muleContext);
                                    } else {
                                        final ResumeRequestType resumeRequest = new ResumeRequestType();
                                        resumeRequest.setPsoID(psoIdentifierType);
                                        resumeRequest.setRequestID("R" + System.currentTimeMillis());
                                        //responsetype = client.resume(resumeRequest);
                                        connectorAdapter.resumeRequest(managedSys, resumeRequest, muleContext);
                                    }

                                    if (responsetype == null) {
                                        log.info("Response object from set password is null");
                                        response.setStatus(ResponseStatus.FAILURE);
                                        return response;
                                    }

                                    if (responsetype.getStatus() == null) {
                                        log.info("Response status is null");
                                        response.setStatus(ResponseStatus.FAILURE);
                                        return response;
                                    }
                                    log.info(String.format("Response status=%s", response.getStatus()));

                                    //TODO:  process the result of the WS call to resume/suspend of teh connector
                                }
                            }
                            */
                        }
                    }
                }
            }
        }
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }


    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#modifyUser(org.openiam.provision.dto.ProvisionUser)
      */
    public ProvisionUserResponse modifyUser(ProvisionUser pUser) {
        ProvisionUserResponse resp = new ProvisionUserResponse();
        String requestId = "R" + UUIDGen.getUUID();
        ScriptIntegration se = null;
        Map<String, Object> bindingMap = new HashMap<String, Object>();
        Organization org = null;
        String primaryLogin = null;
        List<IdmAuditLog> pendingLogItems = new ArrayList<IdmAuditLog>();

        List<Role> activeRoleList = new ArrayList<Role>();
        List<Role> deleteRoleList = new ArrayList<Role>();
        List<Login> principalList = new ArrayList<Login>();

       // ModifyUser modifyUser = (ModifyUser) ac.getBean("modifyUser");
       // AttributeListBuilder attrListBuilder = (AttributeListBuilder) ac.getBean("attributeListBuilder");
       // modifyUser.init();


        log.debug("---DEFAULT PROVISIONING SERVICE: modifyUser called --");

        List<Login> newPrincipalList = pUser.getPrincipalList();


        try {
            se = ScriptFactory.createModule(this.scriptEngine);
        } catch (Exception e) {
            log.error(e);
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_OTHER);
            return resp;
        }
        if (pUser.getUser().getCompanyId() != null) {
            org = orgManager.getOrganization(pUser.getUser().getCompanyId());
        }

        User origUser = userMgr.getUserWithDependent(pUser.getUserId(), true);
        if (origUser == null || origUser.getUserId() == null) {
            throw new IllegalArgumentException("UserId is not valid. UserId=" + pUser.getUserId());
        }

        // bind the objects to the scripting engine

        bindingMap.put("sysId", sysConfiguration.getDefaultManagedSysId());
        //bindingMap.put("user", pUser.getUser());
        bindingMap.put("org", org);
        bindingMap.put("context", ac);
        bindingMap.put("operation", "MODIFY");
        // clone the user object so that we have it for comparison in the scripts
        bindingMap.put("userBeforeModify", new ProvisionUser(origUser));

        ProvisionServicePreProcessor modifyPreProcessScript = createProvPreProcessScript(preProcessor);
        if (modifyPreProcessScript != null && !pUser.isSkipPreprocessor()) {
            modifyPreProcessScript.setMuleContext(muleContext);
            if (executeProvisionPreProcess(modifyPreProcessScript, bindingMap, pUser, null, "MODIFY") != ProvisioningConstants.SUCCESS) {
                resp.setStatus(ResponseStatus.FAILURE);
                resp.setErrorCode(ResponseCode.FAIL_PREPROCESSOR);
                return resp;
            }
        }


        // make sure that our object as the attribute set that will be used for audit logging
        checkAuditingAttributes(pUser);


        // get the current values
        List<Role> curRoleList = roleDataService.getUserRolesAsFlatList(pUser.getUserId());
        List<Group> curGroupList = groupManager.getUserInGroupsAsFlatList(pUser.getUserId());
        List<Login> curPrincipalList = loginManager.getLoginByUser(pUser.getUserId());

        // get the current user object - update it with the new values and then save it


        // check that a primary identity exists some where
        Login curPrimaryIdentity = getPrimaryIdentity("0",curPrincipalList);

        //Login curPrimaryIdentity = loginManager.getPrimaryIdentity(pUser.getUserId());
        if (curPrimaryIdentity == null &&  pUser.getPrincipalList() == null) {
            log.debug("Identity not found...");
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
            return resp;
        }

        pUser.setObjectState(BaseObject.UPDATE);

        // check if the user is missing components
        addMissingUserComponents(pUser);




        // make the role and group list before these updates available to the attribute policies
        bindingMap.put("currentRoleList", curRoleList);
        bindingMap.put("currentGroupList", curGroupList);


        // update the openiam repository with the new user information
        updateUser(pUser, origUser);

        // update the supervisor
        updateSupervisor(origUser, pUser.getSupervisor());

        // update the group
        updateGroupAssociation(origUser.getUserId(), curGroupList, pUser.getMemberOfGroups());

        // update the role association
        updateRoleAssociation(origUser.getUserId(), curRoleList, pUser.getMemberOfRoles(), pendingLogItems, pUser,
                curPrimaryIdentity, activeRoleList, deleteRoleList);

        updateUserOrgAffiliation(origUser.getUserId(), pUser.getUserAffiliations());

       // List<Role> activeRoleList = modifyUser.getActiveRoleList();
        bindingMap.put("userRole", activeRoleList);

        // determine the list of active resources
        //log.debug("Active Role List=" + modifyUser.getActiveRoleList());
        List<Resource> resourceList = getResourcesForRole(getActiveRoleList(activeRoleList, deleteRoleList));
        List<Resource> deleteResourceList = getResourcesForRole(deleteRoleList);

        // add or remove resources that are being associated directly

        if (deleteResourceList == null) {
            deleteResourceList = new ArrayList<Resource>();
        }
        if (resourceList == null) {
            resourceList = new ArrayList<Resource>();
        }

        applyResourceExceptions(pUser, resourceList, deleteResourceList);

        // if there were changes in the role definition, the update the resource list
        // SAS - Oct 2  - may be unnecessary
        // updateResourceListByRoleChanges(resourceList, deleteResourceList, curPrincipalList);

        log.debug("Resources to be added ->> " + resourceList);
        log.debug("Delete the following resources ->> " + deleteResourceList);

        if (deleteResourceList != null && !deleteResourceList.isEmpty()) {
            if (resourceList != null && !resourceList.isEmpty()) {
                deleteResourceList.removeAll(resourceList);
            }
        }
        // determine which resources are new and which ones are existing
        updateResourceState(resourceList, curPrincipalList);

        // update the principal list
        updatePrincipalList(origUser.getUserId(), curPrincipalList, newPrincipalList, deleteResourceList, principalList);

        // get primary identity and bind it for the groovy scripts
        String decPassword = null;
        Login primaryIdentity =  getPrimaryIdentity(this.sysConfiguration.getDefaultManagedSysId(), principalList);
        if (primaryIdentity != null) {
            primaryLogin = primaryIdentity.getId().getLogin();
            String password = primaryIdentity.getPassword();
            if (password != null) {
                try {

                    decPassword = loginManager.decryptPassword(password);
                    bindingMap.put("password", decPassword);

                } catch (EncryptionException e) {

                    bindingMap.put("password", password);

                }
            }
            bindingMap.put("lg", primaryIdentity);
        } else {
            log.debug("Primary identity not found for user=" + origUser.getUserId());
        }

        log.debug("Binding active roles to scripting");
        log.debug("- role list -> " + activeRoleList);
        log.debug("- Primary Identity : " + primaryIdentity);

        // SAS - Do not change the list of roles
        //pUser.setMemberOfRoles(activeRoleList);
        //  bindingMap.put("user", origUser);

        bindingMap.put("user", pUser);


        log.debug("**Updated orig user=" + origUser);
        log.debug("-- " + origUser.getUserId() + " " + origUser.getFirstName() + " " + origUser.getLastName());

        String userStatus = null;
        if (pUser.getUser().getStatus() != null) {
            userStatus = pUser.getUser().getStatus().toString();
        }

        IdmAuditLog auditLog = auditHelper.addLog("MODIFY", pUser.getRequestorDomain(), pUser.getRequestorLogin(),
                "IDM SERVICE", origUser.getCreatedBy(), "0", "USER", origUser.getUserId(),
                null, "SUCCESS", null, "USER_STATUS", userStatus,
                requestId, null, pUser.getSessionId(), null,
                pUser.getRequestClientIP(), primaryIdentity.getId().getLogin(), primaryIdentity.getId().getDomainId());

        auditHelper.persistLogList(pendingLogItems, requestId, pUser.getSessionId());


        // deprovision the identities which are no longer needed.
        if (deleteResourceList != null && !deleteResourceList.isEmpty()) {
            // delete these resources which are not needed in the new role assignment

            deProvisionResources(deleteResourceList, origUser.getUserId(), pUser.getLastUpdatedBy(), requestId,
                    pUser,auditLog.getLogId(), userStatus, origUser );
        }



        if (resourceList != null) {
            log.debug("Resource list is not null.. ");
            int ctr = 1;

            for (Resource res : resourceList) {
                String managedSysId = res.getManagedSysId();

                if (pUser.getSrcSystemId() != null) {
                    if (res.getResourceId().equalsIgnoreCase(pUser.getSrcSystemId())) {
                        continue;
                    }
                }


                bindingMap.put(TARGET_SYS_RES_ID, res.getResourceId());
                bindingMap.put(TARGET_SYS_MANAGED_SYS_ID, res.getManagedSysId());


                if (managedSysId != null) {

                    // object that will be sent to the connectors
                    List<AttributeMap> attrMap = this.managedSysService.getResourceAttributeMaps(res.getResourceId());
                    //List<AttributeMap> attrMap = resourceDataService.getResourceAttributeMaps(res.getResourceId());


                    ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

                    if (mSys == null || mSys.getConnectorId() == null) {
                        continue;
                    }


                    ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                    ManagedSystemObjectMatch matchObj = null;
                    ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(managedSysId, "USER");
                    if (matchObjAry != null && matchObjAry.length > 0) {
                        matchObj = matchObjAry[0];
                        bindingMap.put(MATCH_PARAM, matchObj);
                    }
                    // build the request
                    ModifyRequestType modReqType = new ModifyRequestType();

                    // get the identity linked to this resource / managedsys
                    // determin if this identity exists in IDM or not
                    // if not, do an ADD otherwise, do an update

                    Login mLg = getPrincipalForManagedSys(managedSysId, principalList);
                    //Login mLg = getPrincipalForManagedSys(managedSysId, curPrincipalList);


                    if (mLg != null && mLg.getId() != null) {
                        bindingMap.put(TARGET_SYS_SECURITY_DOMAIN, mLg.getId().getDomainId());
                    } else {
                        bindingMap.put(TARGET_SYS_SECURITY_DOMAIN, sysConfiguration.getDefaultSecurityDomain());
                    }

                    log.debug("PROCESSING IDENTITY =" + mLg);

                    if (mLg != null && mLg.getOperation() == AttributeOperationEnum.DELETE) {

                        // delete this identity
                        List<Resource> delRes = new ArrayList<Resource>();
                        delRes.add(res);

                        log.debug("DEPROVISIONING IDENTITY FOR RES =" + res.getName());

                        deProvisionResources(delRes, origUser.getUserId(), pUser.getLastUpdatedBy(), requestId,
                                pUser,auditLog.getLogId(), userStatus, origUser );



                        //deProvisionResources(List<Resource> deleteResourceList, String userId, String requestorId, String requestId)

                    } else {


                        if (res.getObjectState().equalsIgnoreCase(BaseObject.NEW) || mLg == null) {
                            if (mLg == null) {
                                // create the secondary identity for this resource
                                log.debug("Building identity for managedSysId=" + managedSysId);

                                log.debug("-Building attributes for managedSysId =" + managedSysId);

                                log.debug("-Primary Identity=" + primaryIdentity);
                                log.debug("-pUser - user=" + pUser.getUser());

                                bindingMap.put(TARGET_SYSTEM_IDENTITY_STATUS, IDENTITY_NEW);
                                bindingMap.put(TARGET_SYSTEM_IDENTITY, "");
                                bindingMap.put(TARGET_SYSTEM_ATTRIBUTES, null);

                                bindingMap.put(TARGET_SYS_SECURITY_DOMAIN, sysConfiguration.getDefaultSecurityDomain());


                                // pre-processing
                                String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                                if (preProcessScript != null && !preProcessScript.isEmpty()) {
                                    PreProcessor ppScript = createPreProcessScript(preProcessScript);
                                    if (ppScript != null) {
                                        if (executePreProcess(ppScript, bindingMap, pUser, "MODIFY") == ProvisioningConstants.FAIL) {
                                            continue;
                                        }
                                    }
                                }


                                ExtensibleUser extUser = buildFromRules(pUser, attrMap, se,
                                        managedSysId, primaryIdentity.getId().getDomainId(),
                                        bindingMap, pUser.getUser().getLastUpdatedBy());

                                List<Login> priList = pUser.getPrincipalList();
                                if (priList != null) {
                                    for (Login l : priList) {
                                        log.debug("identity after builder=" + l.getId());
                                    }
                                } else {
                                    log.debug("priList is null");
                                }

                                // build the request
                                AddRequestType addReqType = new AddRequestType();
                                // get the identity linked to this resource / managedsys
                                mLg = getPrincipalForManagedSys(managedSysId, priList);
                                if (mLg == null) {
                                    mLg = new Login();
                                }
                                // mLg.setPassword(primaryLogin.getPassword());
                                mLg.setUserId(primaryIdentity.getUserId());

                                bindingMap.put(TARGET_SYS_SECURITY_DOMAIN, mLg.getId().getDomainId());

                                log.debug("Creating identity in openiam repository:" + mLg.getId());
                                if (mLg.getPassword() == null) {
                                    mLg.setPassword(primaryIdentity.getPassword());
                                }

                                Login tempPrincipal = loginManager.getLoginByManagedSys(mLg.getId().getDomainId(), mLg.getId().getLogin(), mLg.getId().getManagedSysId());

                                if (tempPrincipal == null) {
                                    loginManager.addLogin(mLg);
                                } else {
                                    log.debug("Skipping the creation of identity in openiam repository. Identity already exists" + mLg.getId());
                                }

                                //loginManager.addLogin(mLg);

                                boolean connectorSuccess = false;

                                if (connector.getConnectorInterface() != null &&
                                        connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                                    connectorSuccess = remoteAdd(mLg, requestId, mSys, matchObj, extUser, connector, pUser, auditLog);

                                } else {

                                    PSOIdentifierType idType = new PSOIdentifierType(mLg.getId().getLogin(), null, "target");
                                    addReqType.setPsoID(idType);
                                    addReqType.setRequestID(requestId);
                                    addReqType.setTargetID(mLg.getId().getManagedSysId());
                                    addReqType.getData().getAny().add(extUser);

                                    log.debug("Creating identity in target system:" + mLg.getId());
                                    AddResponseType responseType = connectorAdapter.addRequest(mSys, addReqType, muleContext);
                                    if (responseType.getStatus() == StatusCodeType.SUCCESS) {
                                        connectorSuccess = true;
                                    }
                                }

                                // post processing
                                String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
                                if (postProcessScript != null && !postProcessScript.isEmpty()) {
                                    PostProcessor ppScript = createPostProcessScript(postProcessScript);
                                    if (ppScript != null) {
                                        executePostProcess(ppScript, bindingMap, pUser, "MODIFY", connectorSuccess);
                                    }
                                }


                                auditHelper.addLog("ADD IDENTITY", pUser.getRequestorDomain(), pUser.getRequestorLogin(),
                                        "IDM SERVICE", origUser.getCreatedBy(), mLg.getId().getManagedSysId(), "USER", origUser.getUserId(),
                                        null, "SUCCESS", auditLog.getLogId(), "USER_STATUS",
                                        userStatus,
                                        requestId, null, pUser.getSessionId(), null,
                                        pUser.getRequestClientIP(), mLg.getId().getLogin(), mLg.getId().getDomainId());


                                bindingMap.remove(MATCH_PARAM);
                            }

                        } else {

                            log.debug("Building attributes for managedSysId =" + managedSysId);

                            log.debug("identity for managedSys is not null " + mLg.getId().getLogin());

                            // get the current object as it stands in the target system
                            Map<String, String> currentValueMap = getCurrentObjectAtTargetSystem(mLg, mSys, connector, matchObj);
                            // if currentValueMap is null - then add the value - it does not exist in the target system

                            if (currentValueMap == null || currentValueMap.size() == 0) {
                                bindingMap.put(TARGET_SYSTEM_IDENTITY_STATUS, IDENTITY_NEW);

                                // we may have identity for a user, but it my have been deleted from the target system
                                // we dont need re-generate the identity in this c
                                bindingMap.put(TARGET_SYSTEM_IDENTITY, mLg.getId().getLogin());
                                bindingMap.put(TARGET_SYSTEM_ATTRIBUTES, null);
                            } else {
                                bindingMap.put(TARGET_SYSTEM_IDENTITY_STATUS, IDENTITY_EXIST);
                                bindingMap.put(TARGET_SYSTEM_IDENTITY, mLg.getId().getLogin());
                                bindingMap.put(TARGET_SYSTEM_ATTRIBUTES, currentValueMap);
                            }

                            bindingMap.put(TARGET_SYS_SECURITY_DOMAIN, mLg.getId().getDomainId());

                            String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                            if (preProcessScript != null && !preProcessScript.isEmpty()) {
                                PreProcessor ppScript = createPreProcessScript(preProcessScript);
                                if (ppScript != null) {
                                    if (executePreProcess(ppScript, bindingMap, pUser, "MODIFY") == ProvisioningConstants.FAIL) {
                                        continue;
                                    }
                                }
                            }


                            // what the new object will look like
                            ExtensibleUser extUser = buildModifyFromRules(pUser,
                                    mLg, attrMap, se, managedSysId, mLg.getId().getDomainId(), bindingMap,
                                    pUser.getUser().getLastUpdatedBy());

                            // updates the attributes with the correct operation codes
                            extUser = updateAttributeList(extUser, currentValueMap);

                            // test to see if the updates were carried for forward
                            List<ExtensibleAttribute> extAttList = extUser.getAttributes();
                            //

                            boolean connectorSuccess = false;
                            if (connector.getConnectorInterface() != null &&
                                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                                if (mLg.getOperation() == AttributeOperationEnum.REPLACE && mLg.getOrigPrincipalName() != null) {
                                    extAttList.add(new ExtensibleAttribute("ORIG_IDENTITY", mLg.getOrigPrincipalName(), 2, "String"));
                                }

                                UserRequest userReq = new UserRequest();
                                userReq.setUserIdentity(mLg.getId().getLogin());
                                userReq.setRequestID(requestId);
                                userReq.setTargetID(mLg.getId().getManagedSysId());
                                userReq.setHostLoginId(mSys.getUserId());
                                userReq.setHostLoginPassword(mSys.getDecryptPassword());
                                userReq.setHostUrl(mSys.getHostUrl());
                                userReq.setBaseDN(matchObj.getBaseDn());
                                userReq.setOperation("ADD");
                                userReq.setUser(extUser);

                                UserResponse respType = remoteConnectorAdapter.modifyRequest(mSys, userReq, connector, muleContext);
                                if (respType.getStatus() == StatusCodeType.SUCCESS) {
                                    connectorSuccess = true;
                                }

                            } else {
                                PSOIdentifierType idType = new PSOIdentifierType(mLg.getId().getLogin(), null, "target");
                                idType.setTargetID(mLg.getId().getManagedSysId());
                                modReqType.setPsoID(idType);
                                modReqType.setRequestID(requestId);

                                // check if this request calls for the identity being renamed
                                log.debug("Send request to connector - Original Principal Name = " + mLg.getOrigPrincipalName());

                                if (mLg.getOrigPrincipalName() != null) {
                                    extAttList.add(new ExtensibleAttribute("ORIG_IDENTITY", mLg.getOrigPrincipalName(), 2, "String"));

                                    //if (mLg.getOrigPrincipalName().equalsIgnoreCase(mLg.getId().getLogin())) {
                                    //    extAttList.add(new ExtensibleAttribute("ORIG_IDENTITY", mLg.getOrigPrincipalName(), 2, "String"));
                                    //}

                                }

                                ModificationType mod = new ModificationType();
                                mod.getData().getAny().add(extUser);

                                List<ModificationType> modTypeList = modReqType.getModification();
                                modTypeList.add(mod);

                                log.debug("Creating identity in target system:" + mLg.getId());
                                ModifyResponseType respType = connectorAdapter.modifyRequest(mSys, modReqType, muleContext);

                                if (respType.getStatus() == StatusCodeType.SUCCESS) {
                                    connectorSuccess = true;
                                }


                            }
                            // post processing
                            String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
                            if (postProcessScript != null && !postProcessScript.isEmpty()) {
                                PostProcessor ppScript = createPostProcessScript(postProcessScript);
                                if (ppScript != null) {
                                    executePostProcess(ppScript, bindingMap, pUser, "MODIFY", connectorSuccess);
                                }
                            }

                        }
                    }
                    bindingMap.remove(MATCH_PARAM);
                }
            }


        }


        validateIdentitiesExistforSecurityDomain(primaryIdentity, activeRoleList);

        log.debug("DEFAULT PROVISIONING SERVICE: modifyUser complete");

        bindingMap.put("userAfterModify", pUser);


        ProvisionServicePostProcessor modifyPostProcessScript = createProvPostProcessScript(postProcessor);
        if (modifyPostProcessScript != null && !pUser.isSkipPostProcessor()) {
            modifyPostProcessScript.setMuleContext(muleContext);
            if (executeProvisionPostProcess(modifyPostProcessScript, bindingMap, pUser, null, "MODIFY") != ProvisioningConstants.SUCCESS) {
                resp.setStatus(ResponseStatus.FAILURE);
                resp.setErrorCode(ResponseCode.FAIL_POSTPROCESSOR);
                return resp;
            }
        }

        /* Response object */
        resp.setStatus(ResponseStatus.SUCCESS);
        resp.setUser(pUser);
        return resp;

    }

    private void updateResourceState(List<Resource> resourceList, List<Login> curPrincipalList) {
        if (resourceList == null) {
            return;
        }
        for (Login l : curPrincipalList) {
            for (Resource r : resourceList) {
                if (r.getManagedSysId() != null) {
                    if (r.getManagedSysId().equalsIgnoreCase(l.getId().getManagedSysId())) {
                        r.setObjectState(BaseObject.UPDATE);
                    }
                }
            }
        }
    }

    private void deProvisionResources(List<Resource> deleteResourceList,
                                      String userId, String requestorId, String requestId,
                                      ProvisionUser pUser, String auditLogId, String status, User origUser) {
        if (deleteResourceList != null) {

            List<Login> identityList = loginManager.getLoginByUser(userId);

            for (Resource res : deleteResourceList) {
                String managedSysId = res.getManagedSysId();

                log.debug("Deleting identity for managedSys=" + managedSysId);

                // object that will be sent to the connectors
                List<AttributeMap> attrMap = managedSysService.getResourceAttributeMaps(res.getResourceId());
                //List<AttributeMap> attrMap = resourceDataService.getResourceAttributeMaps(res.getResourceId());

                Login mLg = getPrincipalForManagedSys(managedSysId, identityList);


                if (mLg != null) {
                    // make sure the identity exists before we deprovision it.
                    ManagedSys mSys = managedSysService.getManagedSys(managedSysId);
                    ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                    mLg.setStatus("INACTIVE");
                    mLg.setAuthFailCount(0);
                    mLg.setPasswordChangeCount(0);
                    mLg.setIsLocked(0);
                    // change the password to a random scrambled password
                    String scrambledPassword = PasswordGenerator.generatePassword(10);
                    try {
                        mLg.setPassword(loginManager.encryptPassword(scrambledPassword));
                    } catch (EncryptionException ee) {
                        log.error(ee);
                        // put the password in a clean state so that the operation continues
                        mLg.setPassword(null);
                    }

                    loginManager.updateLogin(mLg);

                    // LOG THIS EVENT

                    auditHelper.addLog("REMOVE IDENTITY", pUser.getRequestorDomain(), pUser.getRequestorLogin(),
                            "IDM SERVICE", origUser.getCreatedBy(), mLg.getId().getManagedSysId(), "USER", origUser.getUserId(),
                            null, "SUCCESS", auditLogId, "USER_STATUS",
                            status,
                            requestId, null, pUser.getSessionId(),
                            null,
                            pUser.getRequestClientIP(), mLg.getId().getLogin(), mLg.getId().getDomainId());

                    PSOIdentifierType idType = new PSOIdentifierType(mLg.getId().getLogin(), null, managedSysId);


                    if (connector.getConnectorInterface() != null &&
                            connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                        UserRequest request = new UserRequest();

                        request.setUserIdentity(mLg.getId().getLogin());
                        request.setRequestID(requestId);
                        request.setTargetID(mLg.getId().getManagedSysId());
                        request.setHostLoginId(mSys.getUserId());
                        request.setHostLoginPassword(mSys.getDecryptPassword());
                        request.setHostUrl(mSys.getHostUrl());

                        request.setOperation("DELETE");


                        remoteConnectorAdapter.deleteRequest(mSys, request, connector, muleContext);

                    } else {
                        DeleteRequestType reqType = new DeleteRequestType();
                        reqType.setRequestID(requestId);
                        reqType.setPsoID(idType);


                        ResponseType delRes = connectorAdapter.deleteRequest(mSys, reqType, muleContext);

                    }

                }
            }
        }


    }


    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#resetPassword(org.openiam.provision.dto.PasswordSync)
      */
    public PasswordResponse resetPassword(PasswordSync passwordSync) {
        log.debug("----resetPassword called.------");

        PasswordResponse response = new PasswordResponse(ResponseStatus.SUCCESS);

        String requestId = "R" + UUIDGen.getUUID();

        // get the user object associated with this principal
        Login login = loginManager.getLoginByManagedSys(passwordSync.getSecurityDomain(),
                passwordSync.getPrincipal(), passwordSync.getManagedSystemId());
        if (login == null) {
            auditHelper.addLog("RESET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", null, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.PRINCIPAL_NOT_FOUND.toString(), null, "Principal not found: " + passwordSync.getPrincipal());


            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
            return response;
        }
        // check if the user active
        String userId = login.getUserId();
        if (userId == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }
        User usr = this.userMgr.getUserWithDependent(userId, false);
        if (usr == null) {
            auditHelper.addLog("RESET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", userId, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.PRINCIPAL_NOT_FOUND.toString(), null, "User object not found: " + passwordSync.getPrincipal(),
                    passwordSync.getRequestClientIP(), passwordSync.getSecurityDomain(), passwordSync.getPrincipal());

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }

        String password = passwordSync.getPassword();
        if (password == null || password.length() == 0) {
            // autogenerate the password
            password = String.valueOf(PasswordGenerator.generatePassword(8));
        }
        String encPassword = null;
        try {
            encPassword = loginManager.encryptPassword(password);
        } catch (EncryptionException e) {
            auditHelper.addLog("RESET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), passwordSync.getManagedSystemId(), "PASSWORD", userId, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.FAIL_ENCRYPTION.toString(), null, e.toString());

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.FAIL_ENCRYPTION);
            return response;
        }
        boolean retval = loginManager.resetPassword(passwordSync.getSecurityDomain(), passwordSync.getPrincipal(),
                passwordSync.getManagedSystemId(), encPassword);

        if (retval) {
            log.debug("-Password changed in openiam repository for user:" + passwordSync.getPrincipal());

            auditHelper.addLog("RESET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), passwordSync.getManagedSystemId(), "PASSWORD", userId, null, "SUCCESS", null, null,
                    null,
                    requestId, null, null, null,
                    passwordSync.getRequestClientIP(), passwordSync.getPrincipal(), passwordSync.getSecurityDomain());


        } else {
            auditHelper.addLog("RESET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), passwordSync.getManagedSystemId(), "PASSWORD", null, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.PRINCIPAL_NOT_FOUND.toString(), null, "Principal not found: " + passwordSync.getPrincipal());

            Response resp = new Response();
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
        }


        if (passwordSync.getManagedSystemId().equalsIgnoreCase(this.sysConfiguration.getDefaultManagedSysId())) {
            // typical sync
            List<Login> principalList = loginManager.getLoginByUser(login.getUserId());
            if (principalList != null) {
                log.debug("PrincipalList size =" + principalList.size());
                for (Login lg : principalList) {
                    // get the managed system for the identity - ignore the managed system id that is linked to openiam's repository
                    log.debug("**** Managed System Id in passwordsync object=" + passwordSync.getManagedSystemId());

                    if (!lg.getId().getManagedSysId().equalsIgnoreCase(passwordSync.getManagedSystemId()) &&
                            !lg.getId().getManagedSysId().equalsIgnoreCase(sysConfiguration.getDefaultManagedSysId())) {

                        // determine if you should sync the password or not
                        String managedSysId = lg.getId().getManagedSysId();
                        Resource res = resourceDataService.getResource(managedSysId);

                        log.debug(" - managedsys id = " + managedSysId);
                        log.debug(" - Resource for sysId =" + res);

                        // check the sync flag

                        if (syncAllowed(res)) {

                            log.debug("Sync allowed for sys=" + managedSysId);
                            loginManager.resetPassword(lg.getId().getDomainId(),
                                    lg.getId().getLogin(), lg.getId().getManagedSysId(),
                                    encPassword);

                            ManagedSys mSys = managedSysService.getManagedSys(managedSysId);
                            // check for null..

                            if (mSys != null) {

                                ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                                ManagedSystemObjectMatch matchObj = null;
                                ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(managedSysId, "USER");
                                if (matchObjAry != null && matchObjAry.length > 0) {
                                    matchObj = matchObjAry[0];
                                }


                                if (connector.getConnectorInterface() != null &&
                                        connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                                    remoteResetPassword(requestId, lg, password, mSys, matchObj, connector, passwordSync);


                                } else {
                                    localResetPassword(requestId, lg, password, mSys, passwordSync);

                                }
                            }
                        }
                    }
                }
            }

        } else {
            // update just the system that was specified


            ManagedSys mSys = managedSysService.getManagedSys(passwordSync.getManagedSystemId());
            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());


            ManagedSystemObjectMatch matchObj = null;
            ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");
            if (matchObjAry != null && matchObjAry.length > 0) {
                matchObj = matchObjAry[0];
            }

            if (connector.getConnectorInterface() != null &&
                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                remoteResetPassword(requestId, login, password, mSys, matchObj, connector, passwordSync);


            } else {

                localResetPassword(requestId, login, password, mSys, passwordSync);


            }


        }


        response.setStatus(ResponseStatus.SUCCESS);
        return response;

    }

    public LookupUserResponse getTargetSystemUser(
            String principalName,
            String managedSysId) {

        log.debug("getTargetSystemUser called. for = " + principalName);

        LookupUserResponse response = new LookupUserResponse(ResponseStatus.SUCCESS);
        response.setManagedSysId(managedSysId);
        response.setPrincipalName(principalName);
        // get the connector for the managedSystem

        ManagedSys mSys = managedSysService.getManagedSys(managedSysId);
        ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

        ManagedSystemObjectMatch matchObj = null;
        ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(managedSysId, "USER");


        // do the lookup

        if (connector.getConnectorInterface() != null &&
                connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

            log.debug("Calling lookupRequest with Remote connector");

            LookupRequest reqType = new LookupRequest();
            reqType.setSearchValue(principalName);

            reqType.setTargetID(managedSysId);
            reqType.setHostLoginId(mSys.getUserId());
            reqType.setHostLoginPassword(mSys.getDecryptPassword());
            reqType.setHostUrl(mSys.getHostUrl());

            if (matchObj != null) {

                reqType.setBaseDN(matchObj.getBaseDn());
            }

            LookupResponse responseType = remoteConnectorAdapter.lookupRequest(mSys, reqType, connector, muleContext);
            if (responseType.getStatus() == StatusCodeType.FAILURE) {
                response.setStatus(ResponseStatus.FAILURE);
                return response;
            }
            //   response.setResponseValue( responseType.getValue() );

            return response;


        } else {

            log.debug("Calling lookupRequest local connector");

            LookupRequestType request = new LookupRequestType();
            PSOIdentifierType idType = new PSOIdentifierType(principalName, null, managedSysId);
            request.setPsoID(idType);


            LookupResponseType responseType = connectorAdapter.lookupRequest(mSys, request, muleContext);

            if (responseType.getStatus() == StatusCodeType.FAILURE) {
                response.setStatus(ResponseStatus.FAILURE);
                return response;
            }

            if (responseType.getAny() != null && responseType.getAny().size() > 0) {
                ExtensibleObject extObj = responseType.getAny().get(0);


                response.setAttrList(extObj.getAttributes());

            }

            return response;


        }
    }

    public LookupUserResponse getTargetSystemUserWithUserId(
            String userId,
            String managedSysId) {

        // get the principalName for this managedSysId

        List<Login> principalList = loginManager.getLoginByUser(userId);

        for (Login l : principalList) {

            if (l.getId().getManagedSysId().equalsIgnoreCase(managedSysId)) {
                return getTargetSystemUser(l.getId().getLogin(), managedSysId);
            }

        }

        LookupUserResponse response = new LookupUserResponse(ResponseStatus.FAILURE);
        response.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
        return response;


    }


    /* (non-Javadoc)
    * @see org.openiam.provision.service.ProvisionService#setPassword(org.openiam.provision.dto.PasswordSync)
    */
    public Response setPassword(PasswordSync passwordSync) {
        log.debug("----setPassword called.------");

        Response response = new Response(ResponseStatus.SUCCESS);
        Map<String, Object> bindingMap = new HashMap<String, Object>();


        ProvisionServicePreProcessor passwordPreScript = createProvPreProcessScript(preProcessor);
        if (passwordPreScript != null) {
            passwordPreScript.setMuleContext(muleContext);
            if (executeProvisionPreProcess(passwordPreScript, bindingMap, null, passwordSync, "SET_PASSWORD") != ProvisioningConstants.SUCCESS) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.FAIL_PREPROCESSOR);
                return response;
            }
        }


        String requestId = "R" + UUIDGen.getUUID();

        // get the user object associated with this principal
        Login login = loginManager.getLoginByManagedSys(passwordSync.getSecurityDomain(),
                passwordSync.getPrincipal(), passwordSync.getManagedSystemId());
        if (login == null) {
            auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", null, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.PRINCIPAL_NOT_FOUND.toString(), null, null,
                    passwordSync.getRequestClientIP(), passwordSync.getPrincipal(), passwordSync.getSecurityDomain());

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
            return response;
        }
        // check if the user active
        String userId = login.getUserId();
        if (userId == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }
        User usr = this.userMgr.getUserWithDependent(userId, false);
        if (usr == null) {
            auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", null, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.USER_NOT_FOUND.toString(), null, null,
                    passwordSync.getRequestClientIP(), passwordSync.getPrincipal(), passwordSync.getSecurityDomain());

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }

        // validate the password against password policy
        Password pswd = new Password();
        pswd.setDomainId(passwordSync.getSecurityDomain());
        pswd.setManagedSysId(passwordSync.getManagedSystemId());
        pswd.setPrincipal(passwordSync.getPrincipal());
        pswd.setPassword(passwordSync.getPassword());
        try {
            PasswordValidationCode rtVal = passwordDS.isPasswordValid(pswd);
            if (rtVal != PasswordValidationCode.SUCCESS) {

                auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                        "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", usr.getUserId(), null, "FAILURE", null, null,
                        null, requestId, rtVal.getValue(), null, null,
                        passwordSync.getRequestClientIP(), passwordSync.getPrincipal(), passwordSync.getSecurityDomain());

                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.valueOf(rtVal.getValue()));
                return response;
            }

        } catch (ObjectNotFoundException oe) {
            oe.printStackTrace();
            log.error(oe);
        }


        String encPassword = null;
        try {
            encPassword = loginManager.encryptPassword(passwordSync.getPassword());
        } catch (EncryptionException e) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.FAIL_ENCRYPTION);
            return response;
        }

        // make sure that update all the primary identity records
        List<Login> principalList = loginManager.getLoginByUser(login.getUserId());
        // List<Login> identityList =  loginManager.getLoginByUser(usr.getUserId()) ;
        for (Login l : principalList) {
            if (l.getId().getManagedSysId().equalsIgnoreCase(passwordSync.getManagedSystemId())) {


                boolean retval = loginManager.setPassword(l.getId().getDomainId(), l.getId().getLogin(),
                        passwordSync.getManagedSystemId(), encPassword);
                if (retval) {
                    log.debug("-Password changed in openiam repository for user:" + passwordSync.getPrincipal());

                    auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                            "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", usr.getUserId(), null, "SUCCESS", null, null,
                            null,
                            requestId, null, null, null,
                            passwordSync.getRequestClientIP(), l.getId().getLogin(), l.getId().getDomainId());

                    // update the user object that the password was changed
                    usr.setDatePasswordChanged(new Date(System.currentTimeMillis()));
                    // reset any locks that may be in place
                    if (usr.getSecondaryStatus() == UserStatusEnum.LOCKED) {
                        usr.setSecondaryStatus(null);
                    }
                    // if the user status was inactive, then make it active
                    if (usr.getStatus() == UserStatusEnum.INACTIVE) {
                        usr.setStatus(UserStatusEnum.ACTIVE);

                    }

                    this.userMgr.updateUserWithDependent(usr, false);

                } else {
                    auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                            "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", usr.getUserId(), null, "FAILURE", null, null,
                            null, requestId, null, null, null,
                            passwordSync.getRequestClientIP(), l.getId().getLogin(), l.getId().getDomainId());

                    Response resp = new Response();
                    resp.setStatus(ResponseStatus.FAILURE);
                    resp.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
                }
            }
        }


        boolean connectorSuccess = false;

        if (passwordSync.getManagedSystemId().equalsIgnoreCase(this.sysConfiguration.getDefaultManagedSysId())) {
            // typical sync
            //List<Login> principalList = loginManager.getLoginByUser(login.getUserId());
            // if (principalList != null) {
            log.debug("PrincipalList size =" + principalList.size());
            for (Login lg : principalList) {
                // get the managed system for the identity - ignore the managed system id that is linked to openiam's repository
                log.debug("**** Managed System Id in passwordsync object=" + passwordSync.getManagedSystemId());

                if (!lg.getId().getManagedSysId().equalsIgnoreCase(sysConfiguration.getDefaultManagedSysId())) {

                    // determine if you should sync the password or not
                    String managedSysId = lg.getId().getManagedSysId();
                    Resource res = resourceDataService.getResource(managedSysId);

                    log.debug(" - managedsys id = " + managedSysId);
                    log.debug(" - Resource for sysId =" + res);

                    // check the sync flag

                    if (syncAllowed(res)) {

                        log.debug("Sync allowed for sys=" + managedSysId);


                        // pre-process

                        bindingMap.put("IDENTITY", lg);
                        bindingMap.put("RESOURCE", res);
                        bindingMap.put("PASSWORD_SYNC", passwordSync);

                        if (res != null) {
                            String preProcessScript = getResProperty(res.getResourceProps(), "PRE_PROCESS");
                            if (preProcessScript != null && !preProcessScript.isEmpty()) {
                                PreProcessor ppScript = createPreProcessScript(preProcessScript);
                                if (ppScript != null) {
                                    if (executePreProcess(ppScript, bindingMap, null, "SET_PASSWORD") == ProvisioningConstants.FAIL) {
                                        continue;
                                    }
                                }
                            }
                        }


                        // update the password in openiam
                        loginManager.setPassword(lg.getId().getDomainId(),
                                lg.getId().getLogin(), lg.getId().getManagedSysId(),
                                encPassword);

                        // update the target system
                        ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

                        ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                        ManagedSystemObjectMatch matchObj = null;
                        ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");
                        if (matchObjAry != null && matchObjAry.length > 0) {
                            matchObj = matchObjAry[0];
                        }

                        if (connector.getConnectorInterface() != null &&
                                connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                            org.openiam.connector.type.ResponseType resp = remoteSetPassword(requestId, lg, passwordSync, mSys, matchObj, connector);
                            if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                connectorSuccess = true;
                            }


                        } else {

                            ResponseType resp = localSetPassword(requestId, lg, passwordSync, mSys);
                            if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                connectorSuccess = true;
                            }

                        }

                        // post-process
                        if (res != null) {
                            String postProcessScript = getResProperty(res.getResourceProps(), "POST_PROCESS");
                            if (postProcessScript != null && !postProcessScript.isEmpty()) {
                                PostProcessor ppScript = createPostProcessScript(postProcessScript);
                                if (ppScript != null) {
                                    executePostProcess(ppScript, bindingMap, null, "SET_PASSWORD", connectorSuccess);
                                }
                            }
                        }

                    } else {
                        log.debug("Sync not allowed for sys=" + managedSysId);
                    }
                }
            }
            //}
        } else {
            // just the update the managed system that was specified.
            ManagedSys mSys = managedSysService.getManagedSys(passwordSync.getManagedSystemId());
            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

            ManagedSystemObjectMatch matchObj = null;
            ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");
            if (matchObjAry != null && matchObjAry.length > 0) {
                matchObj = matchObjAry[0];
            }

            // pre-process
            Resource resource = resourceDataService.getResource(mSys.getResourceId());

            bindingMap.put("IDENTITY", login);
            bindingMap.put("PASSWORD_SYNC", passwordSync);


            if (resource != null) {
                bindingMap.put("RESOURCE", resource);

                String preProcessScript = getResProperty(resource.getResourceProps(), "PRE_PROCESS");
                if (preProcessScript != null && !preProcessScript.isEmpty()) {
                    PreProcessor ppScript = createPreProcessScript(preProcessScript);
                    if (ppScript != null) {
                        executePreProcess(ppScript, bindingMap, null, "SET_PASSWORD");
                    }
                }
            }

            if (connector.getConnectorInterface() != null &&
                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                remoteSetPassword(requestId, login, passwordSync, mSys, matchObj, connector);

            } else {

                localSetPassword(requestId, login, passwordSync, mSys);

            }
            // post-process
            if (res != null) {
                String postProcessScript = getResProperty(resource.getResourceProps(), "POST_PROCESS");
                if (postProcessScript != null && !postProcessScript.isEmpty()) {
                    PostProcessor ppScript = createPostProcessScript(postProcessScript);
                    if (ppScript != null) {
                        executePostProcess(ppScript, bindingMap, null, "SET_PASSWORD", connectorSuccess);
                    }
                }
            }


        }


        ProvisionServicePostProcessor passwordPostScript = createProvPostProcessScript(postProcessor);
        if (passwordPreScript != null) {
            passwordPostScript.setMuleContext(muleContext);
            if (executeProvisionPostProcess(passwordPostScript, bindingMap, null, passwordSync, "SET_PASSWORD") != ProvisioningConstants.SUCCESS) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.FAIL_POSTPROCESSOR);
                return response;
            }
        }


        response.setStatus(ResponseStatus.SUCCESS);
        return response;

    }


    public Response syncPasswordFromSrc(PasswordSync passwordSync) {
        // ManagedSystemId where this event originated.
        // Ensure that we dont send the event back to this system

        log.debug("----syncPasswordFromSrc called.------");
        long curTime = System.currentTimeMillis();

        Response response = new Response(ResponseStatus.SUCCESS);

        String requestId = "R" + UUIDGen.getUUID();

        // get the user object associated with this principal
        Login login = loginManager.getLoginByManagedSys(passwordSync.getSecurityDomain(),
                passwordSync.getPrincipal(), passwordSync.getManagedSystemId());
        if (login == null) {
            auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", null, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.PRINCIPAL_NOT_FOUND.toString(), null, null,
                    passwordSync.getRequestClientIP(), passwordSync.getPrincipal(), passwordSync.getSecurityDomain());

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
            return response;
        }
        // check if the user active
        String userId = login.getUserId();
        if (userId == null) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }
        User usr = this.userMgr.getUserWithDependent(userId, false);
        if (usr == null) {
            auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                    "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", null, null, "FAILURE", null, null,
                    null, requestId, ResponseCode.USER_NOT_FOUND.toString(), null, null,
                    passwordSync.getRequestClientIP(), passwordSync.getPrincipal(), passwordSync.getSecurityDomain());

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.USER_NOT_FOUND);
            return response;
        }

        // do not check the password policy
        // assume that the system that accepted the password already checked this.

        String encPassword = null;
        try {
            encPassword = loginManager.encryptPassword(passwordSync.getPassword());
        } catch (EncryptionException e) {
            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.FAIL_ENCRYPTION);
            return response;
        }

        // make sure that update all the primary identity records
        List<Login> principalList = loginManager.getLoginByUser(login.getUserId());
        // List<Login> identityList =  loginManager.getLoginByUser(usr.getUserId()) ;
        for (Login l : principalList) {
            // if the managedsysId is equal to the source or the openiam default ID, then only update the database
            // otherwise do a sync
            if (l.getId().getManagedSysId().equalsIgnoreCase(passwordSync.getManagedSystemId()) ||
                    l.getId().getManagedSysId().equalsIgnoreCase(sysConfiguration.getDefaultManagedSysId())) {

                log.debug("Updating password for " + l.getId());

                boolean retval = loginManager.setPassword(l.getId().getDomainId(), l.getId().getLogin(),
                        l.getId().getManagedSysId(), encPassword);
                if (retval) {
                    log.debug("-Password changed in openiam repository for user:" + passwordSync.getPrincipal());

                    auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                            "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", usr.getUserId(), null, "SUCCESS", null, null,
                            null,
                            requestId, null, null, null,
                            passwordSync.getRequestClientIP(), l.getId().getLogin(), l.getId().getDomainId());

                    // update the user object that the password was changed
                    usr.setDatePasswordChanged(new Date(curTime));
                    // reset any locks that may be in place
                    if (usr.getSecondaryStatus() == UserStatusEnum.LOCKED) {
                        usr.setSecondaryStatus(null);
                    }
                    this.userMgr.updateUserWithDependent(usr, false);

                } else {
                    auditHelper.addLog("SET PASSWORD", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                            "IDM SERVICE", passwordSync.getRequestorId(), "PASSWORD", "PASSWORD", usr.getUserId(), null, "FAILURE", null, null,
                            null, requestId, null, null, null,
                            passwordSync.getRequestClientIP(), l.getId().getLogin(), l.getId().getDomainId());

                    Response resp = new Response();
                    resp.setStatus(ResponseStatus.FAILURE);
                    resp.setErrorCode(ResponseCode.PRINCIPAL_NOT_FOUND);
                }
            } else {

                log.debug("Synchronizing password from: " + l.getId());

                // determine if you should sync the password or not
                String managedSysId = l.getId().getManagedSysId();
                Resource res = resourceDataService.getResource(managedSysId);


                // check the sync flag

                if (syncAllowed(res)) {

                    log.debug("Sync allowed for sys=" + managedSysId);

                    // update the password in openiam
                    loginManager.setPassword(l.getId().getDomainId(),
                            l.getId().getLogin(), l.getId().getManagedSysId(),
                            encPassword);

                    // update the target system
                    ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

                    ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

                    ManagedSystemObjectMatch matchObj = null;
                    ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(mSys.getManagedSysId(), "USER");
                    if (matchObjAry != null && matchObjAry.length > 0) {
                        matchObj = matchObjAry[0];
                    }

                    // exclude the system where this event occured.

                    if (connector.getConnectorInterface() != null &&
                            connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                        remoteSetPassword(requestId, l, passwordSync, mSys, matchObj, connector);


                    } else {

                        localSetPassword(requestId, l, passwordSync, mSys);

                    }


                } else {
                    log.debug("Sync not allowed for sys=" + managedSysId);
                }

            }
        }


        response.setStatus(ResponseStatus.SUCCESS);
        return response;

    }

    /* ********* Helper Methods ---------------  */

    private boolean syncAllowed(Resource res) {
        Set<ResourceProp> resPropSet = null;
        String syncFlag = null;

        if (res != null) {
            resPropSet = res.getResourceProps();
            syncFlag = getResProperty(resPropSet, "INCLUDE_IN_PASSWORD_SYNC");
            log.debug(" - SyncFlag=" + syncFlag);
        }

        if (res == null) {
            return true;
        } else {
            log.debug("Checking if password sync allowed for resource=" + res.getResourceId());
            log.debug("- sync flag =" + syncFlag);
            if (syncFlag == null || !syncFlag.equalsIgnoreCase("N")) {
                log.debug(" - Sync allowed=true");
                return true;
            }
        }
        return false;
    }

    private String getResProperty(Set<ResourceProp> resPropSet, String propertyName) {
        String value = null;

        if (resPropSet == null) {
            return null;
        }
        Iterator<ResourceProp> propIt = resPropSet.iterator();
        while (propIt.hasNext()) {
            ResourceProp prop = propIt.next();
            if (prop.getName().equalsIgnoreCase(propertyName)) {
                return prop.getPropValue();
            }
        }

        return value;
    }

    /**
     * Returns a list of resources that are applicable for all the roles that a user belongs to.
     *
     * @param roleList
     * @return
     */
    private List<Resource> getResourcesForRole(List<Role> roleList) {

        log.debug("GetResourcesForRole().....");
        // get the list of ids
        String domainId = null;
        List<String> roleIdList = new ArrayList<String>();

        if (roleList == null || roleList.isEmpty()) {
            return null;
        }
        for (Role rl : roleList) {

            if (rl != null) {
                // handle the situation where an invalid role is passed in

                if (domainId == null) {
                    domainId = rl.getId().getServiceId();
                }
                log.debug("-Adding role id to list of roles:" + rl.getId().getRoleId());
                roleIdList.add(rl.getId().getRoleId());
            }
        }

        if (domainId != null && roleIdList != null) {

            //getResourceForRoleList(domainId, roleIdList);
            return resourceDataService.getResourcesForRoles(domainId, roleIdList);
        }
        return null;
    }

    // Check if a resource has been removed from a role that this user still has access to
    private void updateResourceListByRoleChanges(List<Resource> resourceList, List<Resource> deleteResourceList, List<Login> curPrincipalList) {

        boolean match;

        for (Login l : curPrincipalList) {

            match = false;
            for (Resource r : resourceList) {

                if (r.getManagedSysId() != null) {
                    if (l.getId().getManagedSysId().equalsIgnoreCase(r.getManagedSysId())) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) {

                log.debug("No resource match found for : " + l.getId().getManagedSysId());

                ManagedSys msys = managedSysService.getManagedSys(l.getId().getManagedSysId());

                if (msys != null) {
                    if (msys.getResourceId() != null) {

                        log.debug("updateResourceListByRoleChanges(): remove resource  " + msys.getResourceId());

                        Resource deletedRes = resourceDataService.getResource(msys.getResourceId());
                        deleteResourceList.add(deletedRes);
                    }
                }


            }


        }


    }

    private void applyResourceExceptions(ProvisionUser user, List<Resource> addResourceList, List<Resource> deleteResourceList) {
        List<UserResourceAssociation> userResAssocList = user.getUserResourceList();

        if (userResAssocList == null || userResAssocList.isEmpty()) {
            return;
        }

        for (UserResourceAssociation ura : userResAssocList) {

            if (ura.getOperation() == AttributeOperationEnum.DELETE) {

                log.debug("Adding resource " + ura.getResourceId() + " to the delete list ");

                // add this resource to the delete list
                if (!resourceExists(ura.getResourceId(), deleteResourceList)) {

                    if (ura.getManagedSystemId() == null) {

                        Resource resObj = resourceDataService.getResource(ura.getResourceId());
                        ura.setManagedSystemId(resObj.getManagedSysId());


                    }

                    log.debug(" - Adding to deleteResourceList " + ura);
                    deleteResourceList.add(new Resource(ura.getResourceId(), ura.getManagedSystemId()));

                }

            } else if (ura.getOperation() == AttributeOperationEnum.ADD) {
                // add this resource to the delete list
                if (!resourceExists(ura.getResourceId(), addResourceList)) {

                    if (ura.getManagedSystemId() == null) {

                        Resource resObj = resourceDataService.getResource(ura.getResourceId());
                        ura.setManagedSystemId(resObj.getManagedSysId());


                    }

                    addResourceList.add(new Resource(ura.getResourceId(), ura.getManagedSystemId()));
                }
            }


        }

    }

    private void addDirectResourceAssociation(ProvisionUser user, List<Resource> resourceList) {

        log.debug("addDirectResourceAssociation: Adding resources to list directly.");

        List<UserResourceAssociation> userResAssocList = user.getUserResourceList();

        if (userResAssocList == null || userResAssocList.isEmpty()) {
            return;
        }


        for (UserResourceAssociation ura : userResAssocList) {

            if (resourceExists(ura.getResourceId(), resourceList)) {

                if (ura.getOperation() == AttributeOperationEnum.DELETE) {

                    for (Resource r : resourceList) {
                        if (ura.getResourceId().equalsIgnoreCase(r.getResourceId())) {
                            resourceList.remove(r);

                            log.debug("Removing resource from resource list - " + ura.getResourceId());
                        }
                    }

                }

            } else {
                // resource is not current list
                if (ura.getOperation() == AttributeOperationEnum.ADD) {

                    if (ura.getManagedSystemId() == null) {

                        log.debug("addDirectResourceAssociation: URA=" + ura);

                        if (ura.getResourceId() != null) {

                            Resource resObj = resourceDataService.getResource(ura.getResourceId());

                            if (resObj != null) {

                                ura.setManagedSystemId(resObj.getManagedSysId());

                            }
                        }


                    }

                    if (ura.getResourceId() != null && ura.getManagedSystemId() != null) {

                        log.debug("addDirectResourceAssociation:: Adding resource to resource list - " + ura.getResourceId());

                        resourceList.add(new Resource(ura.getResourceId(), ura.getManagedSystemId()));
                    }


                }

            }

        }

    }

    private boolean resourceExists(String resId, List<Resource> resourceList) {

        if (resourceList == null) {
            return false;
        }

        for (Resource r : resourceList) {
            if (r.getResourceId().equalsIgnoreCase(resId)) {
                return true;
            }

        }
        return false;


    }


}
