/*
 * Copyright 2009-2011, OpenIAM LLC
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the Lesser GNU General Public License 
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
package org.openiam.spml2.spi.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.IdmAuditLogDataService;
import org.openiam.idm.srvc.auth.context.AuthenticationContext;
import org.openiam.idm.srvc.auth.context.PasswordCredential;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.auth.dto.Subject;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.auth.service.AuthenticationConstants;
import org.openiam.idm.srvc.auth.ws.AuthenticationResponse;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.idm.srvc.policy.service.PolicyDAO;
import org.openiam.idm.srvc.recon.command.ReconciliationCommandFactory;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;
import org.openiam.idm.srvc.recon.service.ReconciliationCommand;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.secdomain.dto.SecurityDomain;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.password.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.openiam.spml2.spi.ldap.dirtype.Directory;
import org.openiam.spml2.spi.ldap.dirtype.DirectorySpecificImplFactory;
import org.openiam.spml2.util.connect.ConnectionFactory;
import org.openiam.spml2.util.connect.ConnectionManagerConstant;
import org.openiam.spml2.util.connect.ConnectionMgr;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.*;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.*;

/**
 * Updates the OpenIAM repository with data received from external client.
 *
 * @author suneet
 */

@WebService(endpointInterface = "org.openiam.spml2.interf.ConnectorService",
        targetNamespace = "http://www.openiam.org/service/connector",
        portName = "LDAPConnectorServicePort",
        serviceName = "LDAPConnectorService")
public class LdapConnectorImpl extends AbstractSpml2Complete implements ConnectorService, ApplicationContextAware {

    private static final Log log = LogFactory.getLog(LdapConnectorImpl.class);
    protected ManagedSystemDataService managedSysService;
    protected ManagedSystemObjectMatchDAO managedSysObjectMatchDao;
    protected ResourceDataService resourceDataService;
    protected IdmAuditLogDataService auditDataService;
    protected LoginDataService loginManager;
    protected PolicyDAO policyDao;
    protected SecurityDomainDataService secDomainService;
    protected UserDataService userManager;

    protected LdapSuspend ldapSuspend;
    protected LdapPassword ldapPassword;
    protected LdapAddCommand addCommand;
    protected LdapModifyCommand modifyCommand;
    protected LdapLookupCommand lookupCommand;
    protected LdapDeleteCommand deleteCommand;

    public static ApplicationContext ac;


    static String keystore;

    public AuthenticationResponse login(AuthenticationContext authContext) {

        AuthenticationResponse resp = new AuthenticationResponse();

        Subject sub = new Subject();

        log.debug("login() in LdapConnectorImpl called");

        // current date
        Date curDate = new Date(System.currentTimeMillis());
        PasswordCredential cred = (PasswordCredential) authContext.getCredential();

        String principal = cred.getPrincipal();
        String domainId = cred.getDomainId();
        String password = cred.getPassword();

        User user = authContext.getUser();
        Login lg = authContext.getLogin();
        String managedSysId = authContext.getManagedSysId();
        SecurityDomain securityDomain = this.secDomainService.getSecurityDomain(domainId);


        if (user != null && user.getStatus() != null) {
            log.debug("User Status=" + user.getStatus());
            if (user.getStatus().equals(UserStatusEnum.PENDING_START_DATE)) {
                if (!pendingInitialStartDateCheck(user, curDate)) {
                    log("AUTHENTICATION", "AUTHENTICATION", "FAIL", "INVALID USER STATUS", domainId, null, principal, null, null);
                    resp.setAuthErrorCode(AuthenticationConstants.RESULT_INVALID_USER_STATUS);
                    return resp;
                }
            }
            if (!user.getStatus().equals(UserStatusEnum.ACTIVE) && !user.getStatus().equals(UserStatusEnum.PENDING_INITIAL_LOGIN)) {
                // invalid status
                log("AUTHENTICATION", "AUTHENTICATION", "FAIL", "INVALID USER STATUS", domainId, null, principal, null, null);
                resp.setAuthErrorCode(AuthenticationConstants.RESULT_INVALID_USER_STATUS);
                return resp;
            }
            // check the secondary status
            log.debug("Secondary status=" + user.getSecondaryStatus());
            int ret = checkSecondaryStatus(user);
            if (ret != 1) {
                resp.setAuthErrorCode(ret);
                return resp;
            }

        }
        // get the id of the user from the openiam repository
        List<Login> principalList = loginManager.getLoginByUser(user.getUserId());
        if (principalList == null) {
            log("AUTHENTICATION", "AUTHENTICATION", "FAIL", "INVALID LOGIN", domainId, null, principal, null, null);
            resp.setAuthErrorCode(AuthenticationConstants.RESULT_INVALID_LOGIN);
            return resp;
        }
        Login ldapLogin = null;
        for (Login l : principalList) {
            if (l.getId().getManagedSysId().equalsIgnoreCase(managedSysId)) {
                ldapLogin = l;
            }
        }
        if (ldapLogin == null) {
            log("AUTHENTICATION", "AUTHENTICATION", "FAIL", "INVALID LOGIN", domainId, null, principal, null, null);
            resp.setAuthErrorCode(AuthenticationConstants.RESULT_INVALID_LOGIN);
            return resp;

        }
        if (!ldapLogin.getId().getLogin().contains(principal)) {
            log("AUTHENTICATION", "AUTHENTICATION", "FAIL", "INVALID LOGIN", domainId, null, principal, null, null);
            resp.setAuthErrorCode(AuthenticationConstants.RESULT_INVALID_LOGIN);
            return resp;

        }


        // try to login to AD with this user
        LdapContext tempCtx = connect(ldapLogin.getId().getLogin(), password);
        if (tempCtx == null) {
            log("AUTHENTICATION", "AUTHENTICATION", "FAIL", "RESULT_INVALID_PASSWORD", domainId, null, principal, null, null);
            resp.setAuthErrorCode(AuthenticationConstants.RESULT_INVALID_PASSWORD);
            return resp;
        }


        log.debug("Authentication policyid=" + securityDomain.getAuthnPolicyId());
        // get the authentication lock out policy
        Policy plcy = policyDao.findById(securityDomain.getAuthnPolicyId());
        String attrValue = getPolicyAttribute(plcy.getPolicyAttributes(), "FAILED_AUTH_COUNT");

        String tokenType = getPolicyAttribute(plcy.getPolicyAttributes(), "TOKEN_TYPE");
        String tokenLife = getPolicyAttribute(plcy.getPolicyAttributes(), "TOKEN_LIFE");
        String tokenIssuer = getPolicyAttribute(plcy.getPolicyAttributes(), "TOKEN_ISSUER");


        Map tokenParam = new HashMap();
        tokenParam.put("TOKEN_TYPE", tokenType);
        tokenParam.put("TOKEN_LIFE", tokenLife);
        tokenParam.put("TOKEN_ISSUER", tokenIssuer);
        tokenParam.put("PRINCIPAL", principal);

        // update the login and user records to show this authentication
        lg.setLastAuthAttempt(new Date(System.currentTimeMillis()));
        lg.setLastLogin(new Date(System.currentTimeMillis()));
        lg.setAuthFailCount(0);
        lg.setFirstTimeLogin(0);
        log.debug("Good Authn: Login object updated.");
        loginManager.updateLogin(lg);

        // check the user status
        if (user.getStatus() != null) {
            if (user.getStatus().equals(UserStatusEnum.PENDING_INITIAL_LOGIN) ||
                    // after the start date
                    user.getStatus().equals(UserStatusEnum.PENDING_START_DATE)) {

                user.setStatus(UserStatusEnum.ACTIVE);
                userManager.updateUser(user);
            }
        }

        // Successful login
        sub.setUserId(lg.getUserId());
        sub.setPrincipal(principal);
        sub.setSsoToken(token(lg.getUserId(), tokenParam));
        sub.setDomainId(domainId);
        setResultCode(lg, sub, curDate);

        // send message into to audit log

        log("AUTHENTICATION", "AUTHENTICATION", "SUCCESS", null, domainId, user.getUserId(), principal, null, null);

        resp.setSubject(sub);
        return resp;


    }

    public LdapContext connect(String userName, String password) {

        //LdapContext ctxLdap = null;
        Hashtable<String, String> envDC = new Hashtable();

        //keystore = secres.getString("KEYSTORE");
        System.setProperty("javax.net.ssl.trustStore", keystore);

        log.debug("Connecting to ldap using principal=" + userName);

        //envDC.put(Context.PROVIDER_URL,host);
        envDC.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        envDC.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple
        envDC.put(Context.SECURITY_PRINCIPAL, userName);  //"administrator@diamelle.local"
        envDC.put(Context.SECURITY_CREDENTIALS, password);
        //	if (protocol != null && protocol.equalsIgnoreCase("SSL")) {
        //		envDC.put(Context.SECURITY_PROTOCOL, protocol);
        //	}

        try {
            return (new InitialLdapContext(envDC, null));
        } catch (NamingException ne) {
            log.error(ne.getMessage());

        }
        return null;
    }


    public ResponseType reconcileResource(@WebParam(name = "config", targetNamespace = "") ReconciliationConfig config) {
        log.debug("reconcile resource called in LDAPConnector");

        Resource res = resourceDataService.getResource(config.getResourceId());
        String managedSysId =  res.getManagedSysId();
        ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

        Map<String, ReconciliationCommand> situations = new HashMap<String, ReconciliationCommand>();
        for(ReconciliationSituation situation : config.getSituationSet()){
            situations.put(situation.getSituation().trim(), ReconciliationCommandFactory.createCommand(situation.getSituationResp(), situation, managedSysId));
            log.debug("Created Command for: " + situation.getSituation());
        }

        ResponseType response = new ResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        LookupRequestType request = new LookupRequestType();
        ManagedSystemObjectMatch[] matchObjAry = managedSysService.managedSysObjectParam(managedSysId, "USER");
        if(matchObjAry.length == 0) {
            log.error("No match object found for this managed sys");
            response.setStatus(StatusCodeType.FAILURE);
            return response;
        }
        String keyField = matchObjAry[0].getKeyField();
        String searchString = keyField + "=*," + matchObjAry[0].getBaseDn();
        PSOIdentifierType idType = new PSOIdentifierType(searchString, null, managedSysId);
        request.setPsoID(idType);

        LookupResponseType responseType = lookup(request);

        if (responseType.getStatus() == StatusCodeType.FAILURE) {
            response.setStatus(StatusCodeType.FAILURE);
            return response;
        }

        if(responseType.getAny() != null && responseType.getAny().size() != 0) {
            for(ExtensibleObject obj: responseType.getAny()){
                log.debug("Reconcile Found User");
                String principal = null;
                String searchPrincipal = null;
                for(ExtensibleAttribute attr: obj.getAttributes()) {
                    if(attr.getName().equalsIgnoreCase(keyField)){
                        principal = attr.getValue();
                        searchPrincipal = keyField + "=" + principal + "," + matchObjAry[0].getBaseDn();
                        break;
                    }
                }
                if(principal != null) {
                    log.debug("reconcile principle found");

                    Login login = loginManager.getLoginByManagedSys(mSys.getDomainId(), searchPrincipal, managedSysId);
                    if(login == null) {
                        log.debug("Situation: IDM Not Found");
                        DeleteRequestType delete = new DeleteRequestType();
                        idType = new PSOIdentifierType(searchPrincipal, null, managedSysId);
                        delete.setPsoID(idType);
                        delete(delete);
                        Login l = new Login();
                        LoginId id = new LoginId();
                        id.setDomainId(mSys.getDomainId());
                        id.setLogin(principal);
                        id.setManagedSysId(managedSysId);
                        l.setId(id);
                        ReconciliationCommand command = situations.get("IDM Not Found");
                        if(command != null){
                            log.debug("Call command for IDM Not Found");
                            command.execute(l, null, obj.getAttributes());
                        }
                    }
                }
            }
        }

        return response;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Used to test if the connectivity information to the larget system is valid.
     * @param managedSys
     * @return
     */
    public ResponseType testConnection(ManagedSys managedSys) {
        ResponseType response = new ResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        ConnectionMgr conMgr = ConnectionFactory.create(ConnectionManagerConstant.LDAP_CONNECTION);
        conMgr.setApplicationContext(ac);

        try {

            LdapContext ldapctx = conMgr.connect(managedSys);
        } catch (NamingException ne) {
            log.error(ne);

            // return a response object - even if it fails so that it can be logged.
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.DIRECTORY_ERROR);
            response.addErrorMessage(ne.toString());

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

        log.debug("Test connection: Response object = " + response);

        return response;
    }

    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlCore#add(org.openiam.spml2.msg.AddRequestType)
      */
    public AddResponseType add(AddRequestType reqType) {
        return addCommand.add(reqType);

    }



    /* (non-Javadoc)
    * @see org.openiam.spml2.interf.SpmlCore#delete(org.openiam.spml2.msg.DeleteRequestType)
    */
    public ResponseType delete(DeleteRequestType reqType) {

        return  deleteCommand.delete(reqType);


    }

    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlCore#listTargets(org.openiam.spml2.msg.ListTargetsRequestType)
      */
    public ListTargetsResponseType listTargets(ListTargetsRequestType reqType) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlCore#lookup(org.openiam.spml2.msg.LookupRequestType)
      */
    public LookupResponseType lookup(LookupRequestType reqType) {

        return lookupCommand.lookup(reqType);

    }



    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlCore#modify(org.openiam.spml2.msg.ModifyRequestType)
      */
    public ModifyResponseType modify(ModifyRequestType reqType) {

        return modifyCommand.modify(reqType);


    }




    /* (non-Javadoc)
    * @see org.openiam.spml2.interf.SpmlPassword#expirePassword(org.openiam.spml2.msg.password.ExpirePasswordRequestType)
    */
    public ResponseType expirePassword(ExpirePasswordRequestType request) {

        return null;
    }

    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlPassword#resetPassword(org.openiam.spml2.msg.password.ResetPasswordRequestType)
      */
    public ResetPasswordResponseType resetPassword(
            ResetPasswordRequestType request) {

        return null;
    }

    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlPassword#setPassword(org.openiam.spml2.msg.password.SetPasswordRequestType)
      */
    public ResponseType setPassword(SetPasswordRequestType reqType) {
        log.debug("setPassword request called..");

        ConnectionMgr conMgr = null;

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



        try {
            log.debug("managedSys found for targetID=" + targetID + " " + " Name=" + managedSys.getName());
            conMgr = ConnectionFactory.create(ConnectionManagerConstant.LDAP_CONNECTION);
            conMgr.setApplicationContext(ac);

            LdapContext ldapctx = conMgr.connect(managedSys);


            String ldapName = psoID.getID();

            // check if the identity exists before setting the password

            ManagedSystemObjectMatch matchObj = null;
            List<ManagedSystemObjectMatch> matchObjList = managedSysObjectMatchDao.findBySystemId(targetID, "USER");
            if (matchObjList != null && matchObjList.size() > 0) {
                matchObj = matchObjList.get(0);
            }

            if (matchObj != null) {

                log.debug("setPassword:: Checking if identity exists before changing the password ");

                if ( !isInDirectory(ldapName, matchObj, ldapctx) ) {

                    ResponseType resp = new ResponseType();
                    resp.setStatus(StatusCodeType.FAILURE);
                    resp.setError(ErrorCode.NO_SUCH_OBJECT);
                    return resp;

                }
            }


            Directory dirSpecificImp  = DirectorySpecificImplFactory.create(managedSys.getHandler1());
            ModificationItem[] mods = dirSpecificImp.setPassword(reqType);


            ldapctx.modifyAttributes(ldapName, mods);

            // check if the request contains additional attributes
            List<ExtensibleObject> extObjList = reqType.getAny();
            if (extObjList != null && extObjList.size() > 0) {
                ExtensibleObject obj = extObjList.get(0);
                if (obj != null) {
                    List<ExtensibleAttribute> attrList = obj.getAttributes();
                    if (attrList != null && attrList.size() > 0) {
                        mods = new ModificationItem[attrList.size()];
                        for (ExtensibleAttribute a : attrList) {
                            mods[0] = new ModificationItem(a.getOperation(), new BasicAttribute(a.getName(), a.getValue()));
                        }
                        ldapctx.modifyAttributes(ldapName, mods);
                    }
                }
            }


        } catch (NamingException ne) {
            log.error(ne.toString());

            log.debug("Returning response object from set password with Status of Failure...");

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            if (ne instanceof OperationNotSupportedException) {
                resp.setError(ErrorCode.OPERATION_NOT_SUPPORTED_EXCEPTION);
            }

            return resp;

        } catch (Exception ne) {
            log.error(ne.getMessage(), ne);

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            resp.setError(ErrorCode.OTHER_ERROR);

            return resp;

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

    /* (non-Javadoc)
      * @see org.openiam.spml2.interf.SpmlPassword#validatePassword(org.openiam.spml2.msg.password.ValidatePasswordRequestType)
      */
    public ValidatePasswordResponseType validatePassword(
            ValidatePasswordRequestType request) {
        // TODO Auto-generated method stub
        return null;
    }


    public ResponseType suspend(SuspendRequestType request) {
        return ldapSuspend.suspend(request);
    }

    public ResponseType resume(ResumeRequestType request) {
        return ldapSuspend.resume(request);
    }


    public ManagedSystemObjectMatchDAO getManagedSysObjectMatchDao() {
        return managedSysObjectMatchDao;
    }

    public void setManagedSysObjectMatchDao(
            ManagedSystemObjectMatchDAO managedSysObjectMatchDao) {
        this.managedSysObjectMatchDao = managedSysObjectMatchDao;
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


    /**
     * Logs a message into the audit log.
     *
     * @param objectTypeId
     * @param actionId
     * @param actionStatus
     * @param reason
     * @param domainId
     * @param userId
     * @param principal
     * @param linkedLogId
     * @param clientId
     */
    public void log(String objectTypeId, String actionId, String actionStatus, String reason,
                    String domainId, String userId, String principal,
                    String linkedLogId, String clientId) {
        IdmAuditLog log = new IdmAuditLog(objectTypeId, actionId, actionStatus,
                reason, domainId, userId, principal,
                linkedLogId, clientId);
    }


    public IdmAuditLogDataService getAuditDataService() {
        return auditDataService;
    }


    public void setAuditDataService(IdmAuditLogDataService auditDataService) {
        this.auditDataService = auditDataService;
    }


    public LoginDataService getLoginManager() {
        return loginManager;
    }


    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public PolicyDAO getPolicyDao() {
        return policyDao;
    }

    public void setPolicyDao(PolicyDAO policyDao) {
        this.policyDao = policyDao;
    }

    public SecurityDomainDataService getSecDomainService() {
        return secDomainService;
    }

    public void setSecDomainService(SecurityDomainDataService secDomainService) {
        this.secDomainService = secDomainService;
    }

    public UserDataService getUserManager() {
        return userManager;
    }

    public void setUserManager(UserDataService userManager) {
        this.userManager = userManager;
    }

    public LdapSuspend getLdapSuspend() {
        return ldapSuspend;
    }

    public void setLdapSuspend(LdapSuspend ldapSuspend) {
        this.ldapSuspend = ldapSuspend;
    }

    public LdapPassword getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(LdapPassword ldapPassword) {
        this.ldapPassword = ldapPassword;
    }

    public LdapAddCommand getAddCommand() {
        return addCommand;
    }

    public void setAddCommand(LdapAddCommand addCommand) {
        this.addCommand = addCommand;
    }

    public LdapModifyCommand getModifyCommand() {
        return modifyCommand;
    }

    public void setModifyCommand(LdapModifyCommand modifyCommand) {
        this.modifyCommand = modifyCommand;
    }

    public LdapLookupCommand getLookupCommand() {
        return lookupCommand;
    }

    public void setLookupCommand(LdapLookupCommand lookupCommand) {
        this.lookupCommand = lookupCommand;
    }

    public LdapDeleteCommand getDeleteCommand() {
        return deleteCommand;
    }

    public void setDeleteCommand(LdapDeleteCommand deleteCommand) {
        this.deleteCommand = deleteCommand;
    }

    // move the password operations to a separate object as we have other operations

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
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
}
