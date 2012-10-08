package org.openiam.provision.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.module.client.MuleClient;
import org.mule.util.StringUtils;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.BaseAttributeContainer;
import org.openiam.base.SysConfiguration;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.connector.type.*;
import org.openiam.exception.EncryptionException;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.audit.service.IdmAuditLogDataService;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.auth.login.LoginDAO;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.continfo.dto.ContactConstants;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.service.GroupDataService;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.msg.dto.NotificationParam;
import org.openiam.idm.srvc.msg.dto.NotificationRequest;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.pswd.service.PasswordHistoryDAO;
import org.openiam.idm.srvc.pswd.service.PasswordService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.dto.UserRole;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.provision.type.ExtensibleUser;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.ResponseType;
import org.openiam.spml2.msg.password.SetPasswordRequestType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Base class for the provisioning service
 * User: suneetshah
 */
public abstract class AbstractProvisioningService  implements MuleContextAware, ProvisionService, ApplicationContextAware {

    private static final Log log = LogFactory.getLog(DefaultProvisioningService.class);
    // used to inject the application context into the groovy scripts
    protected static ApplicationContext ac;

    protected UserDataService userMgr;
    protected LoginDataService loginManager;
    protected LoginDAO loginDao;

    protected IdmAuditLogDataService auditDataService;
    protected ManagedSystemDataService managedSysService;
    protected RoleDataService roleDataService;
    protected GroupDataService groupManager;
    protected String defaultProvisioningModel;
    protected SysConfiguration sysConfiguration;
    protected ResourceDataService resourceDataService;
    protected String scriptEngine;
    protected OrganizationDataService orgManager;
    protected PasswordService passwordDS;
    protected AuditHelper auditHelper;
    protected ConnectorAdapter connectorAdapter;
    protected RemoteConnectorAdapter remoteConnectorAdapter;
    protected ConnectorDataService connectorService;
    protected ValidateConnectionConfig validateConnection;
    protected PasswordHistoryDAO passwordHistoryDao;
    protected String preProcessor;
    protected String postProcessor;
    protected DeprovisionSelectedResourceHelper deprovisionSelectedResource;

    MuleContext muleContext;

    protected static final String MATCH_PARAM = "matchParam";
    protected static final String TARGET_SYSTEM_IDENTITY_STATUS = "targetSystemIdentityStatus";
    protected static final String TARGET_SYSTEM_IDENTITY = "targetSystemIdentity";
    protected static final String TARGET_SYSTEM_ATTRIBUTES = "targetSystemAttributes";

    protected static final String TARGET_SYS_RES_ID = "resourceId";
    protected static final String TARGET_SYS_MANAGED_SYS_ID = "managedSysId";
    protected static final String TARGET_SYS_SECURITY_DOMAIN = "securityDomain";

    protected static final String IDENTITY_NEW = "NEW";
    protected static final String IDENTITY_EXIST = "EXIST";

    final static protected ResourceBundle res = ResourceBundle.getBundle("datasource");
    final static protected String serviceHost = res.getString("openiam.service_base");
    final static protected String serviceContext = res.getString("openiam.idm.ws.path");

    public void setMuleContext(MuleContext ctx) {
        log.debug("Provisioning - setMuleContext called.");
        muleContext = ctx;

    }

    protected void checkAuditingAttributes(ProvisionUser pUser) {
        if ( pUser.getRequestClientIP() == null || pUser.getRequestClientIP().isEmpty() ) {
            pUser.setRequestClientIP("NA");
        }
        if ( pUser.getRequestorLogin() == null || pUser.getRequestorLogin().isEmpty() ) {
            pUser.setRequestorLogin("NA");;
        }
        if ( pUser.getRequestorDomain() == null || pUser.getRequestorDomain().isEmpty() ) {
            pUser.setRequestorDomain("NA");
        }
        if ( pUser.getCreatedBy() == null || pUser.getCreatedBy().isEmpty() ) {
            pUser.setCreatedBy("NA");
        }
    }

    protected boolean callConnector(Login mLg, String requestId, ManagedSys mSys,
                                 ManagedSystemObjectMatch matchObj, ExtensibleUser extUser,
                                 ProvisionConnector connector,
                                 ProvisionUser user, IdmAuditLog idmAuditLog) {

        if (connector.getConnectorInterface() != null &&
                connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

           return remoteAdd(mLg, requestId, mSys, matchObj, extUser, connector, user, idmAuditLog);

        }

        return localAdd(mLg, requestId, mSys, matchObj, extUser, user, idmAuditLog);


    }

    protected Map<String, String> getCurrentObjectAtTargetSystem(Login mLg, ManagedSys mSys, ProvisionConnector connector, ManagedSystemObjectMatch matchObj) {

        String identity = mLg.getId().getLogin();

        log.debug("Getting the current attributes in the target system for =" + identity);

        log.debug("- IsRename: " + mLg.getOrigPrincipalName());

        if (mLg.getOrigPrincipalName() != null && !mLg.getOrigPrincipalName().isEmpty()) {
            identity = mLg.getOrigPrincipalName();
        }


        Map<String, String> curValueMap = new HashMap<String, String>();

        if (connector.getConnectorInterface() != null &&
                connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

            LookupRequest reqType = new LookupRequest();
            reqType.setSearchValue(identity);

            reqType.setTargetID(mLg.getId().getManagedSysId());
            reqType.setHostLoginId(mSys.getUserId());
            reqType.setHostLoginPassword(mSys.getDecryptPassword());
            reqType.setHostUrl(mSys.getHostUrl());
            reqType.setBaseDN(matchObj.getBaseDn());

            LookupResponse lookupRespType = null;

            lookupRespType = remoteConnectorAdapter.lookupRequest(mSys, reqType, connector, muleContext);

            if (lookupRespType == null || lookupRespType.getStatus() == StatusCodeType.FAILURE) {
                log.debug("Attribute lookup did not find a match.");
                return null;
            }

        } else {

            List<ExtensibleAttribute> extAttrList = getTargetSystemUser(identity, mSys.getManagedSysId()).getAttrList();
            if (extAttrList != null) {
                for (ExtensibleAttribute obj : extAttrList) {
                    String name = obj.getName();
                    String value = obj.getValue();
                    curValueMap.put(name, value);
                }
            } else {
                log.debug(" - NO attributes found in target system lookup ");
            }


        }


        if (curValueMap.size() == 0) {
            return null;
        }
        return curValueMap;

    }

    protected void sendCredentialsToUser(User user, String identity, String password) {

        try {

            NotificationRequest request = new NotificationRequest();
            request.setUserId(user.getUserId());
            request.setNotificationType("NEW_USER_EMAIL");

            request.getParamList().add(new NotificationParam("IDENTITY", identity));
            request.getParamList().add(new NotificationParam("PSWD", password));

            MuleClient client = new MuleClient(muleContext);

            Map<String, String> msgPropMap = new HashMap<String, String>();
            msgPropMap.put("SERVICE_HOST", serviceHost);
            msgPropMap.put("SERVICE_CONTEXT", serviceContext);

            client.sendAsync("vm://notifyUserByEmailMessage", request, msgPropMap);

        } catch (MuleException me) {
            log.error(me.toString());
        }

    }

    protected void sendCredentialsToSupervisor(User user, String identity, String password, String name) {

        try {

            NotificationRequest request = new NotificationRequest();
            request.setUserId(user.getUserId());
            request.setNotificationType("NEW_USER_EMAIL_SUPERVISOR");

            request.getParamList().add(new NotificationParam("IDENTITY", identity));
            request.getParamList().add(new NotificationParam("PSWD", password));
            request.getParamList().add(new NotificationParam("NAME", name));

            MuleClient client = new MuleClient(muleContext);

            Map<String, String> msgPropMap = new HashMap<String, String>();
            msgPropMap.put("SERVICE_HOST", serviceHost);
            msgPropMap.put("SERVICE_CONTEXT", serviceContext);

            client.sendAsync("vm://notifyUserByEmailMessage", request, msgPropMap);

        } catch (MuleException me) {
            log.error(me.toString());
        }

    }


    // -------- Methods used by the Default Provisioning Service ------

    // - methods to build attributes

    public String buildPrincipalName(List<AttributeMap> attrMap, ScriptIntegration se,
                                     Map<String, Object> bindingMap) {
        for (AttributeMap attr : attrMap) {
            Policy policy = attr.getAttributePolicy();
            String url = policy.getRuleSrcUrl();
            String objectType = attr.getMapForObjectType();
            if (objectType != null) {
                if (objectType.equalsIgnoreCase("PRINCIPAL")) {
                    if (url != null) {
                        return (String) se.execute(bindingMap, url);
                    }
                }
            }


        }
        return null;
    }

    public ExtensibleUser buildFromRules(ProvisionUser pUser,
                                         List<AttributeMap> attrMap, ScriptIntegration se,
                                         String managedSysId, String domainId,
                                         Map<String, Object> bindingMap,
                                         String createdBy) {

        final ExtensibleUser extUser = new ExtensibleUser();


        if (attrMap != null) {

            if (log.isDebugEnabled()) {
                log.debug("buildFromRules: attrMap IS NOT null");
            }

            final Login identity = new Login();
            final LoginId loginId = new LoginId();

            // init values
            loginId.setDomainId(domainId);
            loginId.setManagedSysId(managedSysId);

            for (final AttributeMap attr : attrMap) {

                if (StringUtils.equalsIgnoreCase(attr.getStatus(), "IN-ACTIVE")) {
                    continue;
                }

                final Policy policy = attr.getAttributePolicy();
                final String url = policy.getRuleSrcUrl();
                if (url != null) {
                    Object output = se.execute(bindingMap, url);
                    if (output != null) {
                        final String objectType = attr.getMapForObjectType();
                        if (objectType != null) {
                            if (StringUtils.equalsIgnoreCase("PRINCIPAL", objectType)) {
                                if (log.isDebugEnabled()) {
                                    log.debug(String.format("buildFromRules: ManagedSysId=%s, login=%s", managedSysId, output));
                                }

                                loginId.setLogin((String) output);
                                extUser.setPrincipalFieldName(attr.getAttributeName());
                                extUser.setPrincipalFieldDataType(attr.getDataType());

                            }


                            if (StringUtils.equalsIgnoreCase(objectType, "USER") || StringUtils.equalsIgnoreCase(objectType, "PASSWORD")) {

                                if (log.isDebugEnabled()) {
                                    log.debug(String.format("buildFromRules: attribute: %s->%s", attr.getAttributeName(), output));
                                }

                                if (output instanceof String) {

                                    output = (StringUtils.isBlank((String) output)) ? attr.getDefaultValue() : output;
                                    extUser.getAttributes().add(new ExtensibleAttribute(attr.getAttributeName(), (String) output, 1, attr.getDataType()));

                                } else if (output instanceof Date) {
                                    final Date d = (Date) output;
                                    final String DATE_FORMAT = "MM/dd/yyyy";
                                    final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                                    extUser.getAttributes().add(new ExtensibleAttribute(attr.getAttributeName(), sdf.format(d), 1, attr.getDataType()));

                                } else if (output instanceof BaseAttributeContainer) {

                                    // process a complex object which can be passed to the connector

                                    ExtensibleAttribute newAttr = new ExtensibleAttribute(attr.getAttributeName(), (BaseAttributeContainer) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);
                                    extUser.getAttributes().add(newAttr);

                                } else {
                                    extUser.getAttributes().add(new ExtensibleAttribute(attr.getAttributeName(), (List) output, 1, attr.getDataType()));
                                }
                            }

                        }
                    }
                }
            }
            identity.setId(loginId);
            identity.setAuthFailCount(0);
            identity.setCreateDate(new Date(System.currentTimeMillis()));
            identity.setCreatedBy(createdBy);
            identity.setIsLocked(0);
            identity.setFirstTimeLogin(1);
            identity.setStatus("ACTIVE");
            if (pUser.getPrincipalList() == null) {
                List<Login> idList = new ArrayList<Login>();
                idList.add(identity);
                pUser.setPrincipalList(idList);
            } else {
                pUser.getPrincipalList().add(identity);
            }

        } else {
            log.debug("- attMap IS null");
        }

        // show the identities in the pUser object


        return extUser;


    }

    public ProvisionUserResponse createUser(ProvisionUser user, List<IdmAuditLog> logList) {

        ProvisionUserResponse resp = new ProvisionUserResponse();
        resp.setStatus(ResponseStatus.SUCCESS);
        ResponseCode code;

        // check that phone and email are added to the collections.
        associateEmail(user);
        associatePhone(user);

        User newUser = userMgr.addUser(user.getUser());
        if (newUser == null || newUser.getUserId() == null) {
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;
        }
        user.setUserId(newUser.getUserId());
        log.debug("User id=" + newUser.getUserId() + " created in openiam repository");

        addSupervisor(user);
        try {
            addPrincipals(user);
        }catch(EncryptionException e) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FAIL_ENCRYPTION);
            return resp;
        }
        code = addGroups(user, newUser.getUserId(), logList);
        if (code != ResponseCode.SUCCESS) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(code);
            return resp;
        }
        code = addRoles(user, newUser.getUserId(), logList);
        if (code != ResponseCode.SUCCESS) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(code);
            return resp;
        }
        code = addAffiliations(user, newUser.getUserId(), logList);
        if (code != ResponseCode.SUCCESS) {
            resp.setStatus(ResponseStatus.FAILURE);
            resp.setErrorCode(code);
            return resp;
        }


        return resp;
    }



    /**
     * User object supports N Email addresses. make sure that there is a value
     * @param user
     */
    private void associateEmail(ProvisionUser user) {

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return;

        }
        Set<EmailAddress> emailSet = user.getEmailAddress();

        if (!containsEmail("EMAIL1", emailSet)) {

            EmailAddress e = new EmailAddress(user.getEmail(), "EMAIL1", null, ContactConstants.PARENT_TYPE_USER, 1);
            user.getEmailAddress().add(e);

        }



    }

    private boolean containsEmail(String name, Set<EmailAddress> emailAddressSet) {

        if (emailAddressSet == null || emailAddressSet.isEmpty()) {
            return false;
        }

        for (EmailAddress e : emailAddressSet) {
            if (e.getName() != null && e.getName().equalsIgnoreCase(name)) {
                return true;
            }

        }
        return false;


    }

    private void associatePhone(ProvisionUser user) {
        if (user.getPhoneNbr() == null || user.getPhoneNbr().isEmpty()) {
            return;

        }
        Set<Phone> phoneSet = user.getPhone();

        if (!containsPhone("DESK PHONE", phoneSet)) {
            Phone p = new Phone("DESK PHONE", user.getPhoneNbr(), user.getPhoneExt(), user.getAreaCd(), ContactConstants.PARENT_TYPE_USER, null, user.getCountryCd());
            user.getPhone().add(p);
        }
    }

    private boolean containsPhone(String name, Set<Phone> phoneSet) {

        if (phoneSet == null || phoneSet.isEmpty()) {
            return false;
        }

        for (Phone p : phoneSet) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(name)) {
                return true;
            }

        }
        return false;


    }

    private void addSupervisor(ProvisionUser u) {
        Supervisor supervisor = u.getSupervisor();
        if (supervisor != null && supervisor.getSupervisor() != null) {
            supervisor.setEmployee(u.getUser());
            userMgr.addSupervisor(supervisor);
        }
    }

    private void addPrincipals(ProvisionUser u) throws EncryptionException {
        List<Login> principalList = u.getPrincipalList();
        if (principalList != null && !principalList.isEmpty()) {
            for (Login lg: principalList) {
                lg.setFirstTimeLogin(1);
                lg.setIsLocked(0);
                lg.setCreateDate(new Date(System.currentTimeMillis()));
                lg.setUserId(u.getUserId());
                lg.setStatus("ACTIVE");
                // encrypt the password
                if (lg.getPassword() != null) {
                    String pswd = lg.getPassword();
                    lg.setPassword(loginManager.encryptPassword(pswd));
                }
                loginManager.addLogin(lg);
            }
        }

    }

    private ResponseCode addGroups(ProvisionUser user, String newUserId, List<IdmAuditLog> logList) {
        List<Group> groupList = user.getMemberOfGroups();

        if (groupList != null) {
            for ( Group g : groupList) {
                // check if the group id is valid
                if (g.getGrpId() == null) {
                    return ResponseCode.GROUP_ID_NULL;
                }
                if ( groupManager.getGroup(g.getGrpId()) == null)  {
                    if (g.getGrpId() == null) {
                        return ResponseCode.GROUP_ID_NULL;
                    }
                }
                groupManager.addUserToGroup(g.getGrpId(), newUserId);
                // add to audit log
                logList.add( auditHelper.createLogObject("ADD GROUP", user.getRequestorDomain(), user.getRequestorLogin(),
                        "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                        null, "SUCCESS", null, "USER_STATUS",
                        user.getUser().getStatus().toString(),
                        null, null, user.getSessionId(), null, g.getGrpName(),
                        user.getRequestClientIP(), null, null) );

            }
        }
        return ResponseCode.SUCCESS;
    }

    private ResponseCode addRoles(ProvisionUser user, String newUserId, List<IdmAuditLog> logList) {
        List<Role> roleList = user.getMemberOfRoles();
        log.debug("Role list = " + roleList);
        if (roleList != null && roleList.size() > 0) {
            for (Role r: roleList) {
                // check if the roleId is valid
                if (r.getId().getServiceId() == null || r.getId().getRoleId() == null) {
                    return ResponseCode.ROLE_ID_NULL;
                }
                if (roleDataService.getRole(r.getId().getServiceId(), r.getId().getRoleId()) == null ) {
                    return ResponseCode.ROLE_ID_INVALID;
                }

                UserRole ur = new UserRole(newUserId, r.getId().getServiceId(), r.getId().getRoleId());

                if ( r.getStartDate() != null) {
                    ur.setStartDate(r.getStartDate());
                }
                if ( r.getEndDate() != null ) {
                    ur.setEndDate(r.getEndDate());
                }
                roleDataService.assocUserToRole(ur);


                logList.add( auditHelper.createLogObject("ADD ROLE", user.getRequestorDomain(), user.getRequestorLogin(),
                        "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                        null, "SUCCESS", null, "USER_STATUS",
                        user.getUser().getStatus().toString(),
                        "NA", null, user.getSessionId(), null, ur.getRoleId(),
                        user.getRequestClientIP(), null, null) );


            }
        }
        return ResponseCode.SUCCESS;
    }


    private ResponseCode addAffiliations(ProvisionUser user, String newUserId, List<IdmAuditLog> logList) {
        List<Organization> affiliationList = user.getUserAffiliations();
        log.debug("addAffiliations:Affiliation List list = " + affiliationList);
        if (affiliationList != null && affiliationList.size() > 0) {
            for (Organization org: affiliationList) {
                // check if the roleId is valid
                if (org.getOrgId() == null) {
                    return ResponseCode.OBJECT_ID_INVALID;
                }
                orgManager.addUserToOrg(org.getOrgId(), user.getUserId());

                logList.add( auditHelper.createLogObject("ADD AFFILIATION", user.getRequestorDomain(), user.getRequestorLogin(),
                        "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                        null, "SUCCESS", null, "USER_STATUS",
                        user.getUser().getStatus().toString(),
                        "NA", null, user.getSessionId(), null, org.getOrganizationName(),
                        user.getRequestClientIP(), null, null) );


            }
        }
        return ResponseCode.SUCCESS;
    }



    /**
     * Builds the list of principals from the policies that we have defined in the groovy scripts.
     * @param user
     * @param bindingMap
     * @param se
     */
    protected void buildPrimaryPrincipal( ProvisionUser user,
                                       Map<String, Object> bindingMap,
                                       ScriptIntegration se) {

        List<Login> principalList = new ArrayList<Login>();
        List<AttributeMap> policyAttrMap = this.managedSysService.getResourceAttributeMaps(sysConfiguration.getDefaultManagedSysId());

        log.debug("Building primary identity. ");

        if (policyAttrMap != null) {

            log.debug("- policyAttrMap IS NOT null");

            Login primaryIdentity = new Login();
            LoginId primaryID = new LoginId();
            EmailAddress primaryEmail = new EmailAddress();

            // init values
            primaryID.setDomainId(sysConfiguration.getDefaultSecurityDomain());
            primaryID.setManagedSysId(sysConfiguration.getDefaultManagedSysId());

            try {
                for (  AttributeMap attr : policyAttrMap ) {
                    Policy policy = attr.getAttributePolicy();
                    String url = policy.getRuleSrcUrl();
                    if (url != null) {
                        String output = (String)se.execute(bindingMap, url);
                        String objectType = attr.getMapForObjectType();
                        if (objectType != null) {
                            if (objectType.equalsIgnoreCase("PRINCIPAL")) {
                                if (attr.getAttributeName().equalsIgnoreCase("PRINCIPAL")) {
                                    primaryID.setLogin(output);
                                }
                                if (attr.getAttributeName().equalsIgnoreCase("PASSWORD")) {
                                    primaryIdentity.setPassword(output);
                                }
                                if (attr.getAttributeName().equalsIgnoreCase("DOMAIN")) {
                                    primaryID.setDomainId(output);
                                }
                            }
                            if (objectType.equals("EMAIL")) {
                                primaryEmail.setEmailAddress(output);
                                primaryEmail.setIsDefault(1);
                            }
                        }
                    }
                }
            }catch(Exception e) {
                log.error(e);
            }
            primaryIdentity.setId(primaryID);
            principalList.add(primaryIdentity);
            user.setPrincipalList(principalList);

           // user.getEmailAddress().add(primaryEmail);

        }else {
            log.debug("- policyAttrMap IS null");
        }


    }

    /**
     * when a request already contains an identity and password has not been setup, this method generates a password
     * based on our rules.
     * @param user
     * @param bindingMap
     * @param se
     */

    protected void setPrimaryIDPassword( ProvisionUser user,
                                      Map<String, Object> bindingMap,
                                      ScriptIntegration se) {


        // this method should only be the called if the request already contains 1 or more identities

        List<Login> principalList = user.getPrincipalList();
        List<AttributeMap> policyAttrMap = this.managedSysService.getResourceAttributeMaps(sysConfiguration.getDefaultManagedSysId());
        //List<AttributeMap> policyAttrMap = resourceDataService.getResourceAttributeMaps(sysConfiguration.getDefaultManagedSysId());

        log.debug("setPrimaryIDPassword() ");

        if (policyAttrMap != null) {

            log.debug("- policyAttrMap IS NOT null");

            Login primaryIdentity =  user.getPrimaryPrincipal(sysConfiguration.getDefaultManagedSysId());


            try {
                for (  AttributeMap attr : policyAttrMap ) {
                    Policy policy = attr.getAttributePolicy();
                    String url = policy.getRuleSrcUrl();
                    if (url != null) {
                        String output = (String)se.execute(bindingMap, url);
                        String objectType = attr.getMapForObjectType();
                        if (objectType != null) {
                            if (objectType.equalsIgnoreCase("PRINCIPAL")) {

                                if (attr.getAttributeName().equalsIgnoreCase("PASSWORD")) {
                                    primaryIdentity.setPassword(output);
                                }

                            }

                        }
                    }
                }
            }catch(Exception e) {
                log.error(e);
            }
            //primaryIdentity.setId(primaryID);
            //principalList.add(primaryIdentity);
            user.setPrincipalList(principalList);
            //user.getEmailAddress().add(primaryEmail);

        }else {
            log.debug("- policyAttrMap IS null");
        }


    }



    private boolean identityInDomain(String secDomain, List<Login> identityList) {
        for (Login l : identityList) {
            if ( l.getId().getDomainId().equalsIgnoreCase(secDomain)) {
                return true;
            }

        }
        return false;

    }

    private void addIdentity(String secDomain, Login primaryIdentity) {
        if ( loginManager.getLoginByManagedSys(secDomain,primaryIdentity.getId().getLogin(), primaryIdentity.getId().getManagedSysId()) == null ){

            LoginId id = new LoginId(secDomain,primaryIdentity.getId().getLogin(), primaryIdentity.getId().getManagedSysId());

            Login newLg = new Login();

            newLg.setId(id);
            newLg.setAuthFailCount(0);
            newLg.setFirstTimeLogin(primaryIdentity.getFirstTimeLogin());
            newLg.setIsLocked(primaryIdentity.getIsLocked());
            newLg.setLastAuthAttempt(primaryIdentity.getLastAuthAttempt());
            newLg.setGracePeriod(primaryIdentity.getGracePeriod());
            newLg.setManagedSysName(primaryIdentity.getManagedSysName());
            newLg.setPassword(primaryIdentity.getPassword());
            newLg.setPasswordChangeCount(primaryIdentity.getPasswordChangeCount());
            newLg.setStatus(primaryIdentity.getStatus());
            newLg.setIsLocked(primaryIdentity.getIsLocked());
            newLg.setOrigPrincipalName(primaryIdentity.getOrigPrincipalName());
            newLg.setUserId(primaryIdentity.getUserId());
            newLg.setResetPassword(primaryIdentity.getResetPassword());


            log.debug("Adding identity = " + newLg);

            loginManager.addLogin(newLg);
        }


    }

    protected int callPreProcessor(String operation, ProvisionUser pUser, Map<String, Object> bindingMap ) {

        ProvisionServicePreProcessor addPreProcessScript = createProvPreProcessScript(preProcessor);
        if (addPreProcessScript != null && !pUser.isSkipPreprocessor()) {
            addPreProcessScript.setMuleContext(muleContext);
            return executeProvisionPreProcess(addPreProcessScript, bindingMap, pUser, null, operation);

        }
        // pre-processor was skipped
        return ProvisioningConstants.SUCCESS;
    }


    protected int callPostProcessor(String operation, ProvisionUser pUser, Map<String, Object> bindingMap ) {

        ProvisionServicePostProcessor addPostProcessScript = createProvPostProcessScript(postProcessor);

        if (addPostProcessScript != null && !pUser.isSkipPostProcessor()) {
            addPostProcessScript.setMuleContext(muleContext);
            return executeProvisionPostProcess(addPostProcessScript, bindingMap, pUser, null, "ADD");

        }
        // pre-processor was skipped
        return ProvisioningConstants.SUCCESS;
    }



    protected PreProcessor createPreProcessScript(String scriptName) {
        try {
            ScriptIntegration se = null;
            se = ScriptFactory.createModule(scriptEngine);
            return (PreProcessor) se.instantiateClass(null, scriptName);
        } catch (Exception ce) {
            log.error(ce);
            return null;

        }

    }

    protected PostProcessor createPostProcessScript(String scriptName) {
        try {
            ScriptIntegration se = null;
            se = ScriptFactory.createModule(scriptEngine);
            return (PostProcessor) se.instantiateClass(null, scriptName);
        } catch (Exception ce) {
            log.error(ce);
            return null;

        }

    }

    protected ProvisionServicePreProcessor createProvPreProcessScript(String scriptName) {
        try {
            ScriptIntegration se = null;
            se = ScriptFactory.createModule(scriptEngine);
            return (ProvisionServicePreProcessor) se.instantiateClass(null, scriptName);
        } catch (Exception ce) {
            log.error(ce);
            return null;

        }

    }

    protected ProvisionServicePostProcessor createProvPostProcessScript(String scriptName) {
        try {
            ScriptIntegration se = null;
            se = ScriptFactory.createModule(scriptEngine);
            return (ProvisionServicePostProcessor) se.instantiateClass(null, scriptName);
        } catch (Exception ce) {
            log.error(ce);
            return null;

        }

    }

    protected int executeProvisionPreProcess(ProvisionServicePreProcessor ppScript, Map<String, Object> bindingMap, ProvisionUser user, PasswordSync passwordSync, String operation) {
        if ("ADD".equalsIgnoreCase(operation)) {
            return ppScript.addUser(user, bindingMap);
        }
        if ("MODIFY".equalsIgnoreCase(operation)) {
            return ppScript.modifyUser(user, bindingMap);
        }
        if ("DELETE".equalsIgnoreCase(operation)) {
            return ppScript.deleteUser(user, bindingMap);
        }
        if ("SET_PASSWORD".equalsIgnoreCase(operation)) {
            return ppScript.setPassword(passwordSync, bindingMap);
        }

        return 0;


    }

    protected int executeProvisionPostProcess(ProvisionServicePostProcessor ppScript, Map<String, Object> bindingMap, ProvisionUser user, PasswordSync passwordSync, String operation) {
        if ("ADD".equalsIgnoreCase(operation)) {
            return ppScript.addUser(user, bindingMap);
        }
        if ("MODIFY".equalsIgnoreCase(operation)) {
            return ppScript.modifyUser(user, bindingMap);
        }
        if ("DELETE".equalsIgnoreCase(operation)) {
            return ppScript.deleteUser(user, bindingMap);
        }
        if ("SET_PASSWORD".equalsIgnoreCase(operation)) {
            return ppScript.setPassword(passwordSync, bindingMap);
        }

        return 0;


    }


    protected int executePreProcess(PreProcessor ppScript, Map<String, Object> bindingMap, ProvisionUser user, String operation) {
        if ("ADD".equalsIgnoreCase(operation)) {
            return ppScript.addUser(user, bindingMap);
        }
        if ("MODIFY".equalsIgnoreCase(operation)) {
            return ppScript.modifyUser(user, bindingMap);
        }
        if ("DELETE".equalsIgnoreCase(operation)) {
            return ppScript.deleteUser(user, bindingMap);
        }
        if ("SET_PASSWORD".equalsIgnoreCase(operation)) {
            return ppScript.setPassword(bindingMap);
        }

        return 0;


    }

    protected int executePostProcess(PostProcessor ppScript, Map<String, Object> bindingMap, ProvisionUser user, String operation, boolean success) {
        if ("ADD".equalsIgnoreCase(operation)) {
            return ppScript.addUser(user, bindingMap, success);
        }
        if ("MODIFY".equalsIgnoreCase(operation)) {
            return ppScript.modifyUser(user, bindingMap, success);

        }
        if ("DELETE".equalsIgnoreCase(operation)) {
            return ppScript.deleteUser(user, bindingMap, success);

        }

        if ("SET_PASSWORD".equalsIgnoreCase(operation)) {
            return ppScript.setPassword(bindingMap, success);
        }
        return 0;


    }

    protected String updateUser(ProvisionUser user, User origUser) {



        String requestId = UUIDGen.getUUID();

        log.debug("ModifyUser: updateUser called." );



        User newUser = user.getUser();
        updateUserObject(origUser, newUser);

        log.debug("User object pending update:" + origUser);

        userMgr.updateUserWithDependent(origUser, true);

        return requestId;
    }

    public void updateUserObject(User origUser, User newUser) {

        origUser.updateUser(newUser);

        updateUserEmail(origUser, newUser);
        updatePhone(origUser, newUser);
        updateAddress(origUser, newUser);
    }

    private void updateUserEmail(User origUser, User newUser) {
        Set<EmailAddress> origEmailSet = origUser.getEmailAddress();
        Set<EmailAddress> newEmailSet = newUser.getEmailAddress();

        if (origEmailSet == null && newEmailSet != null) {
            log.debug("New email list is not null");
            origEmailSet = new HashSet<EmailAddress>();
            origEmailSet.addAll(newEmailSet);
            // update the instance variable so that it can passed to the connector with the right operation code
            for (EmailAddress em  : newEmailSet) {
                em.setOperation(AttributeOperationEnum.ADD);
            }
            return;
        }

        if ( (origEmailSet != null && origEmailSet.size() > 0 ) && (newEmailSet == null || newEmailSet.size() == 0 )) {
            log.debug("orig email list is not null and nothing was passed in for the newEmailSet - ie no change");
            for (EmailAddress em  : origEmailSet) {
                em.setOperation(AttributeOperationEnum.NO_CHANGE);
            }
            return;
        }

        // if in new address, but not in old, then add it with operation 1
        // else add with operation 2
        if (newEmailSet != null) {
            for (EmailAddress em : newEmailSet) {
                if (em.getOperation() == AttributeOperationEnum.DELETE) {
                    // get the email object from the original set of emails so that we can remove it
                    EmailAddress e = getEmailAddress(em.getEmailId(), origEmailSet);
                    if (e != null) {
                        origEmailSet.remove(e);
                    }

                }else {
                    // check if this address is in the current list
                    // if it is - see if it has changed
                    // if it is not - add it.
                    EmailAddress origEmail =  getEmailAddress(em.getEmailId(), origEmailSet);
                    if (origEmail == null) {
                        em.setOperation(AttributeOperationEnum.ADD);
                        origEmailSet.add(em);

                        log.debug("EMAIL ADDRESS -> ADD NEW ADDRESS = " + em.getEmailAddress() );

                    }else {
                        if (em.equals(origEmail)) {
                            // not changed
                            em.setOperation(AttributeOperationEnum.NO_CHANGE);
                            log.debug("EMAIL ADDRESS -> NO CHANGE = " + em.getEmailAddress() );
                        }else {
                            // object changed
                            origEmail.updateEmailAddress(em);
                            origEmailSet.add(origEmail);
                            origEmail.setOperation(AttributeOperationEnum.REPLACE);
                            log.debug("EMAIL ADDRESS -> REPLACE = " + em.getEmailAddress() );
                        }
                    }
                }
            }
        }
        // if a value is in original list and not in the new list - then add it on
        for (EmailAddress e : origEmailSet) {
            EmailAddress newEmail =  getEmailAddress(e.getEmailId(), newEmailSet);
            if (newEmail == null) {
                e.setOperation(AttributeOperationEnum.NO_CHANGE);
            }
        }

    }

    private void updatePhone(User origUser, User newUser) {
        Set<Phone> origPhoneSet = origUser.getPhone();
        Set<Phone> newPhoneSet = newUser.getPhone();

        if (origPhoneSet == null && newPhoneSet != null) {
            log.debug("New email list is not null");
            origPhoneSet = new HashSet<Phone>();
            origPhoneSet.addAll(newPhoneSet);
            // update the instance variable so that it can passed to the connector with the right operation code
            for (Phone ph : newPhoneSet) {
                ph.setOperation(AttributeOperationEnum.ADD);
            }
            return;
        }

        if ( (origPhoneSet != null && origPhoneSet.size() > 0 ) && (newPhoneSet == null || newPhoneSet.size() == 0 )) {
            log.debug("orig phone list is not null and nothing was passed in for the newPhoneSet - ie no change");
            for (Phone ph  : origPhoneSet) {
                ph.setOperation(AttributeOperationEnum.NO_CHANGE);
            }
            return;
        }

        // if in new address, but not in old, then add it with operation 1
        // else add with operation 2
        if ( newPhoneSet != null) {
            for (Phone ph : newPhoneSet) {
                if (ph.getOperation() == AttributeOperationEnum.DELETE) {

                    // get the email object from the original set of emails so that we can remove it
                    Phone e = getPhone(ph.getPhoneId(), origPhoneSet);
                    if (e != null) {
                        origPhoneSet.remove(e);
                    }
                }else {
                    // check if this address is in the current list
                    // if it is - see if it has changed
                    // if it is not - add it.

                    Phone origPhone =  getPhone(ph.getPhoneId(), origPhoneSet);
                    if (origPhone == null) {
                        ph.setOperation(AttributeOperationEnum.ADD);
                        origPhoneSet.add(ph);
                    }else {
                        if (ph.equals(origPhone)) {
                            // not changed
                            ph.setOperation(AttributeOperationEnum.NO_CHANGE);

                        }else {
                            // object changed
                            origPhone.updatePhone(ph);
                            origPhoneSet.add(origPhone);
                            origPhone.setOperation(AttributeOperationEnum.REPLACE);

                        }
                    }
                }
            }
        }
        // if a value is in original list and not in the new list - then add it on
        for (Phone ph : origPhoneSet) {
            Phone newPhone =  getPhone(ph.getPhoneId(), newPhoneSet);
            if (newPhone == null) {
                ph.setOperation(AttributeOperationEnum.NO_CHANGE);

            }
        }

    }

    private void updateAddress(User origUser, User newUser) {
        Set<Address> origAddressSet = origUser.getAddresses();
        Set<Address> newAddressSet = newUser.getAddresses();

        if (origAddressSet == null && newAddressSet != null) {
            log.debug("New email list is not null");
            origAddressSet = new HashSet<Address>();
            origAddressSet.addAll(newAddressSet);
            // update the instance variable so that it can passed to the connector with the right operation code
            for (Address ph : newAddressSet) {
                ph.setOperation(AttributeOperationEnum.ADD);

            }
            return;
        }

        if ( (origAddressSet != null && origAddressSet.size() > 0 ) && (newAddressSet == null || newAddressSet.size() == 0 )) {
            log.debug("orig Address list is not null and nothing was passed in for the newAddressSet - ie no change");
            for (Address ph  : origAddressSet) {
                ph.setOperation(AttributeOperationEnum.NO_CHANGE);

            }
            return;
        }

        // if in new address, but not in old, then add it with operation 1
        // else add with operation 2
        for (Address ph : newAddressSet) {
            if (ph.getOperation() == AttributeOperationEnum.DELETE) {

                // get the email object from the original set of emails so that we can remove it
                Address e = getAddress(ph.getAddressId(), origAddressSet);
                if (e != null) {
                    origAddressSet.remove(e);
                }

            }else {
                // check if this address is in the current list
                // if it is - see if it has changed
                // if it is not - add it.
                log.debug("evaluate Address");
                Address origAddress =  getAddress(ph.getAddressId(), origAddressSet);
                if (origAddress == null) {
                    ph.setOperation(AttributeOperationEnum.ADD);
                    origAddressSet.add(ph);

                }else {
                    if (ph.equals(origAddress)) {
                        // not changed
                        ph.setOperation(AttributeOperationEnum.NO_CHANGE);

                    }else {
                        // object changed
                        origAddress.updateAddress(ph);
                        origAddressSet.add(origAddress);
                        origAddress.setOperation(AttributeOperationEnum.REPLACE);

                    }
                }
            }
        }
        // if a value is in original list and not in the new list - then add it on
        for (Address ph : origAddressSet) {
            Address newAddress =  getAddress(ph.getAddressId(), newAddressSet);
            if (newAddress == null) {
                ph.setOperation(AttributeOperationEnum.NO_CHANGE);

            }
        }

    }


    private EmailAddress getEmailAddress(String id, Set<EmailAddress> emailSet) {
        Iterator<EmailAddress> emailIt = emailSet.iterator();
        while (emailIt.hasNext()) {
            EmailAddress email = emailIt.next();
            if (email.getEmailId() != null) {
                if (email.getEmailId().equals(id) && (id != null && id.length() > 0)) {
                    return email;
                }
            }
        }
        return null;

    }

    private Phone getPhone(String id, Set<Phone> phoneSet) {
        Iterator<Phone> phoneIt = phoneSet.iterator();
        while (phoneIt.hasNext()) {
            Phone phone = phoneIt.next();
            if (phone.getPhoneId() != null) {
                if (phone.getPhoneId().equals(id) && (id != null && id.length() > 0)) {
                    return phone;
                }
            }
        }
        return null;

    }

    private Address getAddress(String id, Set<Address> addressSet) {
        Iterator<Address> addressIt = addressSet.iterator();
        while (addressIt.hasNext()) {
            Address adr = addressIt.next();
            if (adr.getAddressId() != null  ) {
                if (adr.getAddressId().equals(id) && (id != null && id.length() > 0)) {
                    return adr;
                }
            }
        }
        return null;

    }

    protected void addMissingUserComponents(ProvisionUser user) {

        log.debug("addMissingUserComponents() called.");

        // check addresses
        Set<Address> addressSet = user.getAddresses();

        if (addressSet == null || addressSet.isEmpty()) {

            log.debug("- Adding original addressSet to the user object");

            List<Address> addressList = userMgr.getAddressList(user.getUserId());
            if (addressList != null && !addressList.isEmpty()) {

                user.setAddresses(new HashSet<Address>(addressList));

            }
        }

        // check email addresses

        Set<EmailAddress> emailAddressSet =  user.getEmailAddress();
        if (emailAddressSet == null || emailAddressSet.isEmpty()) {

            log.debug("- Adding original emailSet to the user object");

            List<EmailAddress> emailList =  userMgr.getEmailAddressList(user.getUserId());
            if ( emailList != null && !emailList.isEmpty() ) {

                user.setEmailAddress( new HashSet<EmailAddress>(emailList) );

            }

        }

        // check the phone objects
        Set<Phone> phoneSet = user.getPhone();
        if (phoneSet == null || phoneSet.isEmpty()) {

            log.debug("- Adding original phoneSet to the user object");

            List<Phone> phoneList  = userMgr.getPhoneList(user.getUserId());
            if ( phoneList != null && !phoneList.isEmpty()) {

                user.setPhone(new HashSet<Phone>(phoneList));

            }

        }


        // check the user attributes
        Map<String, UserAttribute> userAttrSet = user.getUserAttributes();
        if (userAttrSet == null || userAttrSet.isEmpty() ) {

            log.debug("- Adding original user attributes to the user object");

            User u =  userMgr.getUserWithDependent(user.getUserId(), true);
            if (  u.getUserAttributes() != null) {
                user.setUserAttributes(u.getUserAttributes());
            }

        }


        // the affiliations
        List<Organization> affiliationList =  user.getUserAffiliations();
        if (affiliationList == null || affiliationList.isEmpty()){

            log.debug("- Adding original affiliationList to the user object");

            List<Organization> userAffiliations = orgManager.getOrganizationsForUser(user.getUserId());
            if (userAffiliations != null && !userAffiliations.isEmpty())  {

                user.setUserAffiliations(userAffiliations);
            }

        }

        // add roles if not part of the request
        List<Role> userRoleList = user.getMemberOfRoles();
        if ( userRoleList == null || !userRoleList.isEmpty()) {
             List<Role> curRoles = roleDataService.getUserRoles(user.getUserId());
            user.setMemberOfRoles(curRoles);

        }


    }

    public void updateSupervisor(User user, Supervisor supervisor) {

        if (supervisor == null) {
            return;
        }
        // check the current supervisor - if different - remove it and add the new one.
        List<Supervisor> supervisorList = userMgr.getSupervisors(user.getUserId());
        for (Supervisor s : supervisorList) {
            log.debug("looking to match supervisor ids = " + s.getSupervisor().getUserId() + " " + supervisor.getSupervisor().getUserId());
            if (s.getSupervisor().getUserId().equalsIgnoreCase(supervisor.getSupervisor().getUserId())) {
                return;
            }
            userMgr.removeSupervisor(s);
        }
        log.debug("adding supervisor: " + supervisor.getSupervisor().getUserId());
        supervisor.setEmployee(user);
        userMgr.addSupervisor(supervisor);

    }

    public void updateGroupAssociation(String userId, List<Group> origGroupList,  List<Group> newGroupList) {

        log.debug("updating group associations..");
        log.debug("origGroupList =" + origGroupList);
        log.debug("newGroupList=" + newGroupList);

        if ( (origGroupList == null || origGroupList.size() == 0 )  &&
                (newGroupList == null || newGroupList.size() == 0 )) {
            return;
        }

        if ( (origGroupList == null || origGroupList.size() == 0 )  &&
                (newGroupList != null || newGroupList.size() > 0 )) {

            log.debug("New group list is not null");
            origGroupList = new ArrayList<Group>();
            origGroupList.addAll(newGroupList);
            // update the instance variable so that it can passed to the connector with the right operation code
            for (Group g : newGroupList) {
                g.setOperation(AttributeOperationEnum.ADD);
                    this.groupManager.addUserToGroup(g.getGrpId(), userId);
            }
            return;
        }

        if ( (origGroupList != null && origGroupList.size() > 0 ) && (newGroupList == null || newGroupList.size() == 0 )) {
            log.debug("orig group list is not null and nothing was passed in for the newGroupList - ie no change");
            for (Group g  : origGroupList) {
                g.setOperation(AttributeOperationEnum.NO_CHANGE);
            }
            return;
        }

        // if in new address, but not in old, then add it with operation 1
        // else add with operation 2
        for (Group g : newGroupList) {
            if (g.getOperation() == AttributeOperationEnum.DELETE) {
                log.debug("removing Group :" + g.getGrpId() );
                // get the email object from the original set of emails so that we can remove it
                Group grp = getGroup(g.getGrpId(), origGroupList);
                if (grp != null) {
                    this.groupManager.removeUserFromGroup(grp.getGrpId(), userId);
                }
            }else {
                // check if this address is in the current list
                // if it is - see if it has changed
                // if it is not - add it.
                log.debug("evaluate Group");
                Group origGroup =  getGroup(g.getGrpId(), origGroupList);
                if (origGroup == null) {
                    g.setOperation(AttributeOperationEnum.ADD);
                    groupManager.addUserToGroup(g.getGrpId(), userId);
                }else {
                    if (g.getGrpId().equals(origGroup.getGrpId())) {
                        // not changed
                        g.setOperation(AttributeOperationEnum.NO_CHANGE);
                    }
                }
            }
        }
        // if a value is in original list and not in the new list - then add it on
        for (Group g : origGroupList) {
            Group newGroup =  getGroup(g.getGrpId(), newGroupList);
            if (newGroup == null) {
                g.setOperation(AttributeOperationEnum.NO_CHANGE);
            }
        }

    }

    public void updateRoleAssociation(String userId, List<Role> origRoleList,  List<Role> newRoleList, List<IdmAuditLog> logList,
                                      ProvisionUser pUser, Login primaryIdentity,
                                      List<Role> activeRoleList, List<Role> deleteRoleList) {

        log.debug("updateRoleAssociation():");
        log.debug("-origRoleList =" + origRoleList);
        log.debug("-newRoleList=" + newRoleList);



        List<UserRole> currentUserRole = roleDataService.getUserRolesForUser(userId);
        User user = userMgr.getUserWithDependent(userId,false);


        if ( (origRoleList == null || origRoleList.size() == 0 )  &&
                (newRoleList == null || newRoleList.size() == 0 )) {
            return;
        }

        // scneario where the original role list is empty but new roles are passed in on the request
        if ( (origRoleList == null || origRoleList.size() == 0 )  &&
                (newRoleList != null || newRoleList.size() > 0 )) {

            log.debug("New Role list is not null");
            origRoleList = new ArrayList<Role>();
            origRoleList.addAll(newRoleList);
            // update the instance variable so that it can passed to the connector with the right operation code
            for (Role rl : newRoleList) {
                rl.setOperation(AttributeOperationEnum.ADD);
                activeRoleList.add(rl);

                UserRole ur = new UserRole(userId, rl.getId().getServiceId(),
                        rl.getId().getRoleId());

                if ( rl.getStartDate() != null) {
                    ur.setStartDate(rl.getStartDate());
                }
                if ( rl.getEndDate() != null ) {
                    ur.setEndDate(rl.getEndDate());
                }
                roleDataService.assocUserToRole(ur);

                logList.add( auditHelper.createLogObject("ADD ROLE", pUser.getRequestorDomain(),  pUser.getRequestorLogin(),
                        "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                        null, "SUCCESS", null, "USER_STATUS",
                        user.getStatus().toString(),
                        "NA", null, null, null, ur.getRoleId(),
                        pUser.getRequestClientIP(), primaryIdentity.getId().getLogin(), primaryIdentity.getId().getDomainId()));

                //roleDataService.addUserToRole(rl.getId().getServiceId(), rl.getId().getRoleId(), userId);
            }
            return;
        }

        // roles were originally assigned to this user, but this request does not have any roles.
        // need to ensure that old roles are marked with the no-change operation code.
        if ( (origRoleList != null && origRoleList.size() > 0 ) && (newRoleList == null || newRoleList.size() == 0 )) {
            log.debug("orig Role list is not null and nothing was passed in for the newRoleList - ie no change");
            for (Role r  : origRoleList) {
                r.setOperation(AttributeOperationEnum.NO_CHANGE);
                activeRoleList.add(r);
            }
            return;
        }

        // if in new roleList, but not in old, then add it with operation 1
        // else add with operation 2
        for (Role r : newRoleList) {
            if (r.getOperation() == AttributeOperationEnum.DELETE) {

                log.debug("removing Role :" + r.getId() );

                // get the email object from the original set of emails so that we can remove it
                Role rl = getRole(r.getId(), origRoleList);
                if (rl != null) {
                    roleDataService.removeUserFromRole(rl.getId().getServiceId(), rl.getId().getRoleId(), userId);

                    logList.add( auditHelper.createLogObject("REMOVE ROLE", pUser.getRequestorDomain(),  pUser.getRequestorLogin(),
                            "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                            null, "SUCCESS", null, "USER_STATUS",
                            user.getStatus().toString(),
                            "NA", null, null, null, rl.getId().getRoleId(),
                            pUser.getRequestClientIP(), primaryIdentity.getId().getLogin(), primaryIdentity.getId().getDomainId()));

                }
                log.debug("Adding role to deleteRoleList =" + rl);
                deleteRoleList.add(rl);

                // need to pass on to connector that a role has been removed so that
                // the connector can also take action on this event.

                activeRoleList.add(r);
            }else {
                // check if this address is in the current list
                // if it is - see if it has changed
                // if it is not - add it.
                log.debug("Evaluate Role: " + r.getId());

                Role origRole =  getRole(r.getId(), origRoleList);

                log.debug("OrigRole found=" + origRole);

                if (origRole == null) {
                    r.setOperation(AttributeOperationEnum.ADD);
                    activeRoleList.add(r);

                    UserRole ur = new UserRole(userId, r.getId().getServiceId(),
                            r.getId().getRoleId());

                    if ( r.getStartDate() != null) {
                        ur.setStartDate(r.getStartDate());
                    }
                    if ( r.getEndDate() != null ) {
                        ur.setEndDate(r.getEndDate());
                    }
                    roleDataService.assocUserToRole(ur);

                    logList.add( auditHelper.createLogObject("ADD ROLE", pUser.getRequestorDomain(),  pUser.getRequestorLogin(),
                            "IDM SERVICE", user.getCreatedBy(), "0", "USER", user.getUserId(),
                            null, "SUCCESS", null, "USER_STATUS",
                            user.getStatus().toString(),
                            "NA", null, null, null, ur.getRoleId(),
                            pUser.getRequestClientIP(),primaryIdentity.getId().getLogin(), primaryIdentity.getId().getDomainId()));

                    //roleDataService.addUserToRole(r.getId().getServiceId(), r.getId().getRoleId(), userId);
                }else {
                    // get the user role object
                    log.debug("checking if no_change or replace");
                    //if (r.equals(origRole)) {
                    //UserRole uRole = userRoleAttrEq(r, currentUserRole);
                    if (r.getId().equals(origRole.getId()) && userRoleAttrEq(r, currentUserRole)  ) {
                        // not changed
                        log.debug("- no_change ");
                        r.setOperation(AttributeOperationEnum.NO_CHANGE);
                        activeRoleList.add(r);
                    }else {
                        log.debug("- Attr not eq - replace");
                        r.setOperation(AttributeOperationEnum.REPLACE);
                        activeRoleList.add(r);

                        // object changed
                        //UserRole ur = new UserRole(userId, r.getId().getServiceId(),
                        //		r.getId().getRoleId());
                        UserRole ur = getUserRole(r, currentUserRole);
                        if ( ur != null) {
                            if ( r.getStartDate() != null) {
                                ur.setStartDate(r.getStartDate());
                            }
                            if ( r.getEndDate() != null ) {
                                ur.setEndDate(r.getEndDate());
                            }
                            if ( r.getStatus() != null ) {
                                ur.setStatus(r.getStatus());
                            }
                            roleDataService.updateUserRoleAssoc(ur);
                        }else {
                            UserRole usrRl = new UserRole(user.getUserId(), r.getId().getServiceId(), r.getId().getRoleId());
                            roleDataService.assocUserToRole(usrRl);

                        }
                    }
                }
            }
        }
        // if a value is in original list and not in the new list - then add it on
        for (Role rl : origRoleList) {
            Role newRole =  getRole(rl.getId(), newRoleList);
            if (newRole == null) {
                rl.setOperation(AttributeOperationEnum.NO_CHANGE);
                activeRoleList.add(rl);
            }
        }

    }

    public List<Role> getActiveRoleList(List<Role> activeRoleList, List<Role> deleteRoleList ) {


        List<Role> rList = new ArrayList<Role>();
        // create a list of roles that are not in the deleted list
        for ( Role r : activeRoleList) {

            boolean found =false;

            log.debug("- Evaluating Role=" + r);

            if (deleteRoleList != null && !deleteRoleList.isEmpty()) {
                for ( Role delRl : deleteRoleList) {

                    log.debug("- Evaluating deleted Role = " + delRl);
                    if ( delRl != null) {

                        if (!found && r.getId().getRoleId().equalsIgnoreCase(delRl.getId().getRoleId())) {
                            found = true;

                            log.debug("- - Deleted Role found = " + delRl);
                        }

                    }
                }
            }
            if (!found) {
                log.debug("- Adding Role to Active Role List=" + r);
                rList.add(r);
            }
        }
        return rList;
    }


    /* User Org Affiliation */

    public void updateUserOrgAffiliation(String userId, List<Organization> newOrgList) {
        List<Organization>  currentOrgList = orgManager.getOrganizationsForUser(userId);


        if (newOrgList == null) {
            return;
        }

        for ( Organization o : newOrgList ) {

            boolean inCurList = isCurrentOrgInNewList(o,currentOrgList);

            if (o.getOperation() == null ||
                    o.getOperation() == AttributeOperationEnum.ADD ||
                    o.getOperation() == AttributeOperationEnum.NO_CHANGE) {

                if (!inCurList) {
                    orgManager.addUserToOrg(o.getOrgId(),userId);
                }

            }else if ( o.getOperation() == AttributeOperationEnum.DELETE ) {
                if (inCurList) {
                    orgManager.removeUserFromOrg(o.getOrgId(),userId);
                }
            }

        }

    }


    private boolean isCurrentOrgInNewList(Organization newOrg, List<Organization> curOrgList) {
        if (curOrgList != null) {
            for ( Organization o : curOrgList) {
                if (o.getOrgId().equals(newOrg.getOrgId())) {

                    return true;
                }
            }
        }

        return false;
    }


    private Group getGroup(String grpId, List<Group> origGroupList) {
        for (Group g : origGroupList ) {
            if (g.getGrpId().equalsIgnoreCase(grpId)) {
                return g;
            }
        }
        return null;
    }

    private Role getRole(RoleId roleId, List<Role> roleList) {
        for (Role rl : roleList ) {
            if (rl.getId().equals(roleId)) {
                return rl;
            }
        }
        return null;
    }

    private Login getPrincipal(LoginId loginId, List<Login> loginList) {
        for (Login lg : loginList ) {
            if (lg.getId().getManagedSysId().equals(loginId.getManagedSysId())) {
                return lg;
            }
        }
        return null;
    }

    private boolean notInDeleteResourceList(Login l, List<Resource> deleteResourceList) {
        if (deleteResourceList == null) {
            return true;
        }
        for ( Resource r : deleteResourceList) {
            if (l.getId().getManagedSysId().equalsIgnoreCase(r.getManagedSysId())) {
                return false;
            }
        }
        return true;
    }


    private UserRole getUserRole(Role r, List<UserRole> currentUserRole) {
        //boolean retval = true;

        if (currentUserRole == null) {
            return null;
        }

        // get the user role object
        for (UserRole u : currentUserRole) {
            if (r.getId().getRoleId().equalsIgnoreCase(u.getRoleId()) &&
                    r.getId().getServiceId().equalsIgnoreCase(u.getServiceId())) {
                return u;
            }
        }
        return null;

    }

    private boolean userRoleAttrEq(Role r, List<UserRole> currentUserRole) {
        //boolean retval = true;

        if (currentUserRole == null) {
            return false;
        }

        UserRole ur = null;
        // get the user role object
        for (UserRole u : currentUserRole) {
            if (r.getId().getRoleId().equalsIgnoreCase(u.getRoleId()) &&
                    r.getId().getServiceId().equalsIgnoreCase(u.getServiceId())) {
                ur = u;
            }
        }
        if (ur == null) {
            return false;
        }
        if (r.getStatus() != null) {
            if ( !r.getStatus().equalsIgnoreCase(ur.getStatus()) ) {
                return false;
            }
        }
        if (r.getStartDate() != null) {
            if ( !r.getStartDate().equals(ur.getStartDate()) ){
                return false;
            }
        }
        if (r.getEndDate() != null) {
            if ( !r.getEndDate().equals(ur.getEndDate()) ){
                return false;
            }
        }
        return true;
    }


    /* Update Principal List */

    public void updatePrincipalList(String userId, List<Login> origLoginList,
                                    List<Login> newLoginList,
                                    List<Resource> deleteResourceList,
                                    List<Login> principalList) {

        log.debug("** updating Principals in modify User.");
        log.debug("- origPrincpalList =" + origLoginList);
        log.debug("- newPrincipalList=" + newLoginList);

        if ( (origLoginList == null || origLoginList.size() == 0 )  &&
                (newLoginList == null || newLoginList.size() == 0 )) {
            return;
        }

        if ( (origLoginList == null || origLoginList.size() == 0 )  &&
                (newLoginList != null || newLoginList.size() > 0 )) {

            log.debug("New Principal list is not null, but Original Principal List is null");
            origLoginList = new ArrayList<Login>();
            origLoginList.addAll(newLoginList);
            // update the instance variable so that it can passed to the connector with the right operation code
            for (Login lg : newLoginList) {
                lg.setOperation(AttributeOperationEnum.ADD);
                lg.setUserId(userId);
                principalList.add(lg);
                loginManager.addLogin(lg);
            }
            return;
        }

        if ( (origLoginList != null && origLoginList.size() > 0 ) && (newLoginList == null || newLoginList.size() == 0 )) {
            log.debug("orig Principal list is not null and nothing was passed in for the newPrincipal list - ie no change");
            for (Login l  : origLoginList) {
                l.setOperation(AttributeOperationEnum.NO_CHANGE);
                if (notInDeleteResourceList(l,deleteResourceList)) {
                    l.setStatus("ACTIVE");
                    l.setAuthFailCount(0);
                    l.setIsLocked(0);
                    l.setPasswordChangeCount(0);
                    // reset the password from the primary identity
                    // get the primary identity for this user
                    Login primaryIdentity = loginManager.getPrimaryIdentity(l.getUserId());
                    if (primaryIdentity != null) {
                        log.debug("Identity password reset to: " + primaryIdentity.getPassword());
                        l.setPassword( primaryIdentity.getPassword() );
                    }

                    loginManager.updateLogin(l);
                }
                principalList.add(l);
            }
            return;
        }

        // if in new login, but not in old, then add it with operation 1
        // else add with operation 2
        log.debug("New Principal List is not null and OriginalList is not null - Compare the list of identities.");

        for (Login l : newLoginList) {

            if (l.getOperation() == AttributeOperationEnum.DELETE) {

                log.debug("removing Login :" + l.getId() );
                // get the email object from the original set of emails so that we can remove it
                Login lg = getPrincipal(l.getId(), origLoginList);

                if (lg != null) {
                    lg.setStatus("INACTIVE");
                    loginManager.updateLogin(lg);

                    log.debug("Login updated with status of INACTIVE in IdM database.  ");
                }
                principalList.add(l);

            }else {

                // check if this login is in the current list
                // if it is - see if it has changed
                // if it is not - add it.
                log.debug("evaluate Login");
                Login origLogin =  getPrincipal(l.getId(), origLoginList);
                log.debug("OrigLogin found=" + origLogin);
                if (origLogin == null) {
                    l.setOperation(AttributeOperationEnum.ADD);
                    l.setUserId(userId);
                    principalList.add(l);
                    loginManager.addLogin(l);

                }else {
                    if (l.getId().equals(origLogin.getId())) {
                        // not changed
                        log.debug("Identities are equal - No Change");
                        log.debug("OrigLogin status=" + origLogin.getStatus());

                        // if the request contains a password, then set the password
                        // as part of the modify request

                        if (l.getPassword() != null && !l.getPassword().equals(origLogin.getPassword())) {
                            // update the password

                            log.debug("Password change detected during synch process");

                            Login newLg = (Login)origLogin.clone();
                            try {
                                newLg.setPassword(loginManager.encryptPassword(l.getPassword()));
                            }catch(EncryptionException e) {
                                log.error(e);
                                e.printStackTrace();
                            }
                            loginManager.changeIdentityName(newLg.getId().getLogin(), newLg.getPassword(),
                                    newLg.getUserId(), newLg.getId().getManagedSysId(), newLg.getId().getDomainId());
                            principalList.add(newLg);
                        } else {
                            log.debug("Updating Identity in IDM repository");
                            if (l.getOperation() == AttributeOperationEnum.REPLACE) {
                                // user set the replace flag
                                loginManager.updateLogin(l);
                                principalList.add(l);
                            } else {

                                log.debug("Flagged identity with NO_CHANGE attribute");

                                l.setOperation(AttributeOperationEnum.NO_CHANGE);
                                principalList.add(l);
                            }

                        }


                    }else {
                        log.debug("Identity changed - RENAME");


                        // clone the object
                        Login newLg = (Login)origLogin.clone();
                        // add it back with the changed identity
                        newLg.setOperation(AttributeOperationEnum.REPLACE);
                        newLg.getId().setLogin(l.getId().getLogin());

                        //encrypt the password and save it
                        String newPassword = l.getPassword();
                        if (newPassword == null) {
                            newLg.setPassword(null);
                        }else {
                            try {
                                newLg.setPassword(loginManager.encryptPassword(newPassword));
                            }catch(EncryptionException e) {
                                log.error(e);
                                e.printStackTrace();
                            }
                        }
                        loginManager.changeIdentityName(newLg.getId().getLogin(), newLg.getPassword(),
                                newLg.getUserId(), newLg.getId().getManagedSysId(), newLg.getId().getDomainId());
                        //loginManager.addLogin(newLg);


                        // we cannot send the encrypted password to the connector
                        // set the password back
                        newLg.setPassword(newPassword);
                        // used the match up the
                        newLg.setOrigPrincipalName(origLogin.getId().getLogin());
                        principalList.add(newLg);
                    }
                }
            }
        }
        // if a value is in original list and not in the new list - then add it on
        log.debug("Check if a value is in the original principal list but not in the new Principal List");
        for (Login lg : origLoginList) {
            Login newLogin =  getPrincipal(lg.getId(), newLoginList);
            if (newLogin == null) {
                lg.setOperation(AttributeOperationEnum.NO_CHANGE);
                principalList.add(lg);
            }
        }

    }

    public Login getPrimaryIdentity(String managedSysId, List<Login> principalList) {

        log.debug("Getting identity for ManagedSysId");

        if (	principalList == null ||
                principalList.size() == 0) {
            return null;
        }

        log.debug(" - principals ->" + principalList);

        for (Login l  : principalList) {
            if (l.getId().getManagedSysId().equalsIgnoreCase(managedSysId)) {

                log.debug("getPrimaryIdentity() return ->" + l);

                return l;
            }
        }
        log.debug("getPrimaryIdentity() not found. returning null" );
        return null;
    }


    /**
     * If the user has selected roles that are in multiple domains, we need to make sure that they identities for
     * each of these domains
     * @param primaryIdentity
     * @param roleList
     */

    public void validateIdentitiesExistforSecurityDomain(Login primaryIdentity, List<Role> roleList) {

        log.debug("validateIdentitiesExistforSecurityDomain");

        List<Login> identityList = loginManager.getLoginByUser(primaryIdentity.getUserId());
        String managedSysId = primaryIdentity.getId().getManagedSysId();

        log.debug("Identitylist =" + identityList);

        if (roleList != null) {
            for (Role r : roleList) {
                String secDomain = r.getId().getServiceId();
                if (!identityInDomain(secDomain, managedSysId ,identityList)) {

                    log.debug("Adding identity to :" + secDomain);

                    addIdentity(secDomain, primaryIdentity);
                }
            }
            }

        // determine if we should remove an identity
        if (identityList != null) {
            for (Login l : identityList) {
                if (l.getId().getManagedSysId().equalsIgnoreCase(managedSysId)) {
                    boolean found = false;
                    for ( Role r : roleList) {
                        if (r.getId().getServiceId().equalsIgnoreCase(l.getId().getDomainId())) {
                            found = true ;
                        }

                    }
                    if (!found) {
                        if ( l.getId().getManagedSysId().equalsIgnoreCase( "0" )) {
                            // primary identity - do not delete. Just disable its status
                            log.debug("Primary identity - chagne its status");
                            l.setStatus("INACTIVE");
                            loginManager.updateLogin(l);

                        }else {

                            log.debug("Removing identity for  :" + l.getId() );
                            loginManager.removeLogin(l.getId().getDomainId(), l.getId().getLogin(), l.getId().getManagedSysId());
                        }
                    }
                }

            }
        }

    }

    private boolean identityInDomain(String secDomain, String managedSysId,  List<Login> identityList) {

        log.debug("IdentityinDomain =" + secDomain + "-" + managedSysId);

        for (Login l : identityList) {
            if ( l.getId().getDomainId().equalsIgnoreCase(secDomain) &&
                    l.getId().getManagedSysId().equalsIgnoreCase(managedSysId)) {
                return true;
            }

        }
        return false;

    }


    /**
     * Update the list of attributes with the correct operation values so that they can be
     * passed to the connector
     */
    public ExtensibleUser updateAttributeList(org.openiam.provision.type.ExtensibleUser extUser, Map<String,String> currentValueMap ) {
        if (extUser == null) {
            return null;
        }
        log.debug("updateAttributeList: Updating operations on attributes being passed to connectors");
        log.debug("updateAttributeList: Current attributeMap = " + currentValueMap);


        List<ExtensibleAttribute> extAttrList = extUser.getAttributes();
        if (extAttrList == null) {

            log.debug("Extended user attributes is null");

            return null;
        }

        log.debug("updateAttributeList: New Attribute List = " + extAttrList);
        if ( extAttrList != null && currentValueMap == null) {
            for (ExtensibleAttribute attr  : extAttrList) {
                attr.setOperation(1);
            }
        }else {

            for (ExtensibleAttribute attr  : extAttrList) {
                String nm = attr.getName();
                if (currentValueMap == null) {
                    attr.setOperation(1);
                }else {
                    String curVal = currentValueMap.get(nm);
                    if (curVal == null) {
                        // temp hack
                        if (nm.equalsIgnoreCase("objectclass")) {
                            attr.setOperation(2);
                        }else {
                            log.debug("- Op = 1 - AttrName = " +nm );

                            attr.setOperation(1);
                        }
                    }else {
                        if (curVal.equalsIgnoreCase(attr.getValue())) {
                            log.debug("- Op = 0 - AttrName = " +nm );

                            attr.setOperation(0);
                        }else {

                            log.debug("- Op = 2 - AttrName = " +nm );

                            attr.setOperation(2);
                        }
                    }
                }
            }
        }
        return extUser;

    }

    public ExtensibleUser buildModifyFromRules(ProvisionUser pUser,
                                               Login currentIdentity,
                                               List<AttributeMap> attrMap, ScriptIntegration se,
                                               String managedSysId, String domainId,
                                               Map<String, Object> bindingMap,
                                               String createdBy) {

        ExtensibleUser extUser = new ExtensibleUser();


        if (attrMap != null) {

            log.debug("buildModifyFromRules: attrMap IS NOT null");


            for (AttributeMap attr : attrMap) {

                if ("IN-ACTIVE".equalsIgnoreCase(attr.getStatus())) {
                    continue;
                }

                Policy policy = attr.getAttributePolicy();
                String url = policy.getRuleSrcUrl();
                if (url != null) {
                    Object output = se.execute(bindingMap, url);
                    if (output != null) {
                        String objectType = attr.getMapForObjectType();
                        if (objectType != null) {

                            log.debug("buildModifyFromRules: OBJECTTYPE=" + objectType + " SCRIPT OUTPUT=" + output + " attribute name=" + attr.getAttributeName());

                            if (objectType.equalsIgnoreCase("USER") || objectType.equalsIgnoreCase("PASSWORD")) {

                                ExtensibleAttribute newAttr;
                                if (output instanceof String) {

                                    // if its memberOf object than dont add it to the list
                                    // the connectors can detect a delete if an attribute is not in the list

                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), (String) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);
                                    extUser.getAttributes().add(newAttr);


                                } else if (output instanceof Date) {
                                    // date
                                    Date d = (Date) output;
                                    String DATE_FORMAT = "MM/dd/yyyy";
                                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), sdf.format(d), 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);

                                    extUser.getAttributes().add(newAttr);
                                } else if (output instanceof BaseAttributeContainer) {
                                    // process a complex object which can be passed to the connector
                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), (BaseAttributeContainer) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);
                                    extUser.getAttributes().add(newAttr);

                                } else {
                                    // process a list - multi-valued object
                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), (List) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);

                                    extUser.getAttributes().add(newAttr);

                                    log.debug("buildModifyFromRules: added attribute to extUser:" + attr.getAttributeName());
                                }

                            } else if (objectType.equalsIgnoreCase("PRINCIPAL")) {

                                extUser.setPrincipalFieldName(attr.getAttributeName());
                                extUser.setPrincipalFieldDataType(attr.getDataType());

                            }
                        }
                    }
                }
            }

            if (pUser.getPrincipalList() == null) {
                List<Login> principalList = new ArrayList<Login>();
                principalList.add(currentIdentity);
                pUser.setPrincipalList(principalList);
            } else {
                pUser.getPrincipalList().add(currentIdentity);
            }

        }


        return extUser;


    }




    /* REMOTE VS LOCAL CONNECTORS */

    protected boolean localAdd(Login mLg, String requestId, ManagedSys mSys,
                             ManagedSystemObjectMatch matchObj, ExtensibleUser extUser,
                             ProvisionUser user, IdmAuditLog idmAuditLog) {

        AddRequestType addReqType = new AddRequestType();

        PSOIdentifierType idType = new PSOIdentifierType(mLg.getId().getLogin(), null, "target");

        addReqType.setPsoID(idType);
        addReqType.setRequestID(requestId);
        addReqType.setTargetID(mLg.getId().getManagedSysId());
        addReqType.getData().getAny().add(extUser);

        log.debug("Local connector - Creating identity in target system:" + mLg.getId());
        AddResponseType resp = connectorAdapter.addRequest(mSys, addReqType, muleContext);

        auditHelper.addLog("ADD IDENTITY", user.getRequestorDomain(), user.getRequestorLogin(),
                "IDM SERVICE", user.getCreatedBy(), mLg.getId().getManagedSysId(),
                "USER", user.getUserId(),
                idmAuditLog.getLogId(), resp.getStatus().toString(), idmAuditLog.getLogId(), "IDENTITY_STATUS",
                mLg.getStatus().toString(),
                requestId, resp.getErrorCodeAsStr(), user.getSessionId(), resp.getErrorMessage(),
                user.getRequestorLogin(), mLg.getId().getLogin(), mLg.getId().getDomainId());
        return resp.getStatus() != StatusCodeType.FAILURE;


    }


    protected boolean remoteAdd(Login mLg, String requestId, ManagedSys mSys,
                              ManagedSystemObjectMatch matchObj, ExtensibleUser extUser,
                              ProvisionConnector connector,
                              ProvisionUser user, IdmAuditLog idmAuditLog) {

        log.debug("Calling remote connector " + connector.getName());

        UserRequest userReq = new UserRequest();
        userReq.setUserIdentity(mLg.getId().getLogin());
        userReq.setRequestID(requestId);
        userReq.setTargetID(mLg.getId().getManagedSysId());
        userReq.setHostLoginId(mSys.getUserId());
        userReq.setHostLoginPassword(mSys.getDecryptPassword());
        userReq.setHostUrl(mSys.getHostUrl());
        if (matchObj != null) {
            userReq.setBaseDN(matchObj.getBaseDn());
        }
        userReq.setOperation("ADD");
        userReq.setUser(extUser);

        UserResponse resp = remoteConnectorAdapter.addRequest(mSys, userReq, connector, muleContext);

        auditHelper.addLog("ADD IDENTITY", user.getRequestorDomain(), user.getRequestorLogin(),
                "IDM SERVICE", user.getCreatedBy(), mLg.getId().getManagedSysId(),
                "USER", user.getUserId(),
                idmAuditLog.getLogId(), resp.getStatus().toString(), idmAuditLog.getLogId(), "IDENTITY_STATUS",
                user.getUser().getStatus().toString(),
                requestId, resp.getErrorCodeAsStr(), user.getSessionId(), resp.getErrorMsgAsStr(),
                user.getRequestClientIP(), mLg.getId().getLogin(), mLg.getId().getDomainId());

        if (resp.getStatus() == StatusCodeType.FAILURE) {
            return false;
        }
        return true;


    }


    protected UserResponse remoteDelete(
            Login mLg,
            String requestId,
            ManagedSys mSys,
            ProvisionConnector connector,
            ManagedSystemObjectMatch matchObj,
            ProvisionUser user,
            IdmAuditLog auditLog
    ) {

        UserRequest request = new UserRequest();

        request.setUserIdentity(mLg.getId().getLogin());
        request.setRequestID(requestId);
        request.setTargetID(mLg.getId().getManagedSysId());
        request.setHostLoginId(mSys.getUserId());
        request.setHostLoginPassword(mSys.getDecryptPassword());
        request.setHostUrl(mSys.getHostUrl());
        if (matchObj != null) {
            request.setBaseDN(matchObj.getBaseDn());
        }
        request.setOperation("DELETE");

        UserResponse resp = remoteConnectorAdapter.deleteRequest(mSys, request, connector, muleContext);

        auditHelper.addLog("DELETE IDENTITY", auditLog.getDomainId(), auditLog.getPrincipal(),
                "IDM SERVICE", user.getCreatedBy(), mLg.getId().getManagedSysId(),
                "IDENTITY", user.getUserId(),
                auditLog.getLogId(), resp.getStatus().toString(), auditLog.getLogId(), "IDENTITY_STATUS",
                "DELETED",
                requestId, resp.getErrorCodeAsStr(), user.getSessionId(), resp.getErrorMsgAsStr(),
                user.getRequestClientIP(), mLg.getId().getLogin(), mLg.getId().getDomainId());

        return resp;


    }

    protected ResponseType localDelete(Login l, String requestId,
                                     PSOIdentifierType idType,
                                     ManagedSys mSys,
                                     ProvisionUser user,
                                     IdmAuditLog auditLog) {

        log.debug("Local delete for=" + l);

        DeleteRequestType reqType = new DeleteRequestType();
        reqType.setRequestID(requestId);
        reqType.setPsoID(idType);

        ResponseType resp = connectorAdapter.deleteRequest(mSys, reqType, muleContext);


        String logid = null;
        String status = null;

        if (resp.getStatus() != null) {
            status = resp.getStatus().toString();
        }

        if (auditLog != null) {
            logid = auditLog.getLogId();
        }

        auditHelper.addLog("DELETE IDENTITY", user.getRequestorDomain(), user.getRequestorLogin(),
                "IDM SERVICE", user.getCreatedBy(), l.getId().getManagedSysId(),
                "IDENTITY", user.getUserId(),
                logid, status, logid,
                "IDENTITY_STATUS", "DELETED",
                requestId, resp.getErrorCodeAsStr(), user.getSessionId(), resp.getErrorMessage(),
                user.getRequestClientIP(), l.getId().getLogin(), l.getId().getDomainId());

        return resp;


    }

    protected void localResetPassword(String requestId, Login login,
                                    String password,
                                    ManagedSys mSys,
                                    PasswordSync passwordSync) {

        SetPasswordRequestType pswdReqType = new SetPasswordRequestType();
        PSOIdentifierType idType = new PSOIdentifierType(login.getId().getLogin(), null,
                mSys.getManagedSysId());
        pswdReqType.setPsoID(idType);
        pswdReqType.setRequestID(requestId);
        pswdReqType.setPassword(password);
        ResponseType respType = connectorAdapter.setPasswordRequest(mSys, pswdReqType, muleContext);

        auditHelper.addLog("RESET PASSWORD IDENTITY", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                "IDM SERVICE", null, mSys.getManagedSysId(), "PASSWORD", null, null, respType.getStatus().toString(), "NA", null,
                null,
                requestId, respType.getErrorCodeAsStr(), null, respType.getErrorMessage(),
                null, login.getId().getLogin(), login.getId().getDomainId());


    }

    protected void remoteResetPassword(String requestId, Login login,
                                     String password,
                                     ManagedSys mSys,
                                     ManagedSystemObjectMatch matchObj,
                                     ProvisionConnector connector,
                                     PasswordSync passwordSync) {

        PasswordRequest req = new PasswordRequest();
        req.setUserIdentity(login.getId().getLogin());
        req.setRequestID(requestId);
        req.setTargetID(login.getId().getManagedSysId());
        req.setHostLoginId(mSys.getUserId());
        req.setHostLoginPassword(mSys.getPswd());
        req.setHostUrl(mSys.getHostUrl());
        req.setBaseDN(matchObj.getBaseDn());
        req.setOperation("RESET_PASSWORD");
        req.setPassword(password);

        org.openiam.connector.type.ResponseType respType = remoteConnectorAdapter.resetPasswordRequest(mSys, req, connector, muleContext);

        auditHelper.addLog("RESET PASSWORD IDENTITY", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                "IDM SERVICE", null, mSys.getManagedSysId(), "PASSWORD", null, null, respType.getStatus().toString(), "NA", null,
                null,
                requestId, respType.getErrorCodeAsStr(), null, respType.getErrorMsgAsStr(),
                passwordSync.getRequestClientIP(), login.getId().getLogin(), login.getId().getDomainId());


    }


    protected org.openiam.connector.type.ResponseType remoteSetPassword(String requestId, Login login,
                                                                      PasswordSync passwordSync,
                                                                      ManagedSys mSys,
                                                                      ManagedSystemObjectMatch matchObj,
                                                                      ProvisionConnector connector) {

        PasswordRequest req = new PasswordRequest();
        req.setUserIdentity(login.getId().getLogin());
        req.setRequestID(requestId);
        req.setTargetID(login.getId().getManagedSysId());
        req.setHostLoginId(mSys.getUserId());
        req.setHostLoginPassword(mSys.getPswd());
        req.setHostUrl(mSys.getHostUrl());
        req.setBaseDN(matchObj.getBaseDn());
        req.setOperation("SET_PASSWORD");
        req.setPassword(passwordSync.getPassword());

        org.openiam.connector.type.ResponseType respType = remoteConnectorAdapter.setPasswordRequest(mSys, req, connector, muleContext);

        auditHelper.addLog("SET PASSWORD IDENTITY", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                "IDM SERVICE", null, "PASSWORD", "PASSWORD", null, null, respType.getStatus().toString(), "NA", null,
                null,
                requestId, respType.getErrorCodeAsStr(), null, respType.getErrorMsgAsStr(),
                passwordSync.getRequestClientIP(), login.getId().getLogin(), login.getId().getDomainId());

        return respType;


    }

    protected ResponseType localSetPassword(String requestId, Login login,
                                          PasswordSync passwordSync,
                                          ManagedSys mSys) {

        SetPasswordRequestType pswdReqType = new SetPasswordRequestType();
        PSOIdentifierType idType = new PSOIdentifierType(login.getId().getLogin(), null,
                mSys.getManagedSysId());
        pswdReqType.setPsoID(idType);
        pswdReqType.setRequestID(requestId);
        pswdReqType.setPassword(passwordSync.getPassword());

        // add the extensible attributes is they exist

        if (passwordSync.isPassThruAttributes()) {
            List<ExtensibleAttribute> attrList = passwordSync.getAttributeList();
            if (attrList != null) {
                ExtensibleObject extObj = new ExtensibleObject();
                extObj.setName("ATTRIBUTES");
                extObj.setAttributes(attrList);
                pswdReqType.getAny().add(extObj);
            }

        }


        ResponseType respType = connectorAdapter.setPasswordRequest(mSys, pswdReqType, muleContext);

        auditHelper.addLog("SET PASSWORD IDENTITY", passwordSync.getRequestorDomain(), passwordSync.getRequestorLogin(),
                "IDM SERVICE", null, "PASSWORD", "PASSWORD", null, null, respType.getStatus().toString(), "NA", null,
                null,
                requestId, respType.getErrorCodeAsStr(), null, respType.getErrorMessage(),
                passwordSync.getRequestClientIP(), login.getId().getLogin(), login.getId().getDomainId());
        return respType;

    }


    public PasswordHistoryDAO getPasswordHistoryDao() {
        return passwordHistoryDao;
    }

    public void setPasswordHistoryDao(PasswordHistoryDAO passwordHistoryDao) {
        this.passwordHistoryDao = passwordHistoryDao;
    }

    public String getPreProcessor() {
        return preProcessor;
    }

    public void setPreProcessor(String preProcessor) {
        this.preProcessor = preProcessor;
    }

    public String getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(String postProcessor) {
        this.postProcessor = postProcessor;
    }

    public DeprovisionSelectedResourceHelper getDeprovisionSelectedResource() {
        return deprovisionSelectedResource;
    }

    public void setDeprovisionSelectedResource(DeprovisionSelectedResourceHelper deprovisionSelectedResource) {
        this.deprovisionSelectedResource = deprovisionSelectedResource;
    }

    /**
     * ***** Spring methods ************
     */

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    public UserDataService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataService userMgr) {
        this.userMgr = userMgr;
    }

    public LoginDataService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public LoginDAO getLoginDao() {
        return loginDao;
    }

    public void setLoginDao(LoginDAO loginDao) {
        this.loginDao = loginDao;
    }

    public IdmAuditLogDataService getAuditDataService() {
        return auditDataService;
    }

    public void setAuditDataService(IdmAuditLogDataService auditDataService) {
        this.auditDataService = auditDataService;
    }


    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public RoleDataService getRoleDataService() {
        return roleDataService;
    }

    public void setRoleDataService(RoleDataService roleDataService) {
        this.roleDataService = roleDataService;
    }

    public GroupDataService getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupDataService groupManager) {
        this.groupManager = groupManager;
    }


    public String getDefaultProvisioningModel() {
        return defaultProvisioningModel;
    }

    public void setDefaultProvisioningModel(String defaultProvisioningModel) {
        this.defaultProvisioningModel = defaultProvisioningModel;
    }

    public SysConfiguration getSysConfiguration() {
        return sysConfiguration;
    }

    public void setSysConfiguration(SysConfiguration sysConfiguration) {
        this.sysConfiguration = sysConfiguration;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public String getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(String scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public OrganizationDataService getOrgManager() {
        return orgManager;
    }

    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }

    public PasswordService getPasswordDS() {
        return passwordDS;
    }

    public void setPasswordDS(PasswordService passwordDS) {
        this.passwordDS = passwordDS;
    }


    public AuditHelper getAuditHelper() {
        return auditHelper;
    }

    public void setAuditHelper(AuditHelper auditHelper) {
        this.auditHelper = auditHelper;
    }


    public ConnectorAdapter getConnectorAdapter() {
        return connectorAdapter;
    }

    public void setConnectorAdapter(ConnectorAdapter connectorAdapter) {
        this.connectorAdapter = connectorAdapter;
    }


    public RemoteConnectorAdapter getRemoteConnectorAdapter() {
        return remoteConnectorAdapter;
    }

    public void setRemoteConnectorAdapter(
            RemoteConnectorAdapter remoteConnectorAdapter) {
        this.remoteConnectorAdapter = remoteConnectorAdapter;
    }

    public ConnectorDataService getConnectorService() {
        return connectorService;
    }

    public void setConnectorService(ConnectorDataService connectorService) {
        this.connectorService = connectorService;
    }

    public ValidateConnectionConfig getValidateConnection() {
        return validateConnection;
    }

    public void setValidateConnection(ValidateConnectionConfig validateConnection) {
        this.validateConnection = validateConnection;
    }

}
