package org.openiam.provision.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.openiam.base.SysConfiguration;
import org.openiam.connector.type.UserRequest;
import org.openiam.connector.type.UserResponse;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.audit.service.IdmAuditLogDataService;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.login.LoginDAO;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.grp.service.GroupDataService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.pswd.service.PasswordHistoryDAO;
import org.openiam.idm.srvc.pswd.service.PasswordService;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.openiam.spml2.msg.DeleteRequestType;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.ResponseType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Base class that will be extended by all the helper classes that will be used by the DefaultProvisioningService
 */
public class BaseProvisioningHelper implements ApplicationContextAware {

    public static ApplicationContext ac;
    protected UserDataService userMgr;
    protected LoginDataService loginManager;
    protected LoginDAO loginDao;

    protected IdmAuditLogDataService auditDataService;
    protected ManagedSystemDataService managedSysService;
    protected RoleDataService roleDataService;
    protected GroupDataService groupManager;
    protected String connectorWsdl;
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

    protected  MuleContext muleContext;


    final static protected ResourceBundle res = ResourceBundle.getBundle("datasource");
    final static protected String serviceHost = res.getString("openiam.service_base");
    final static protected String serviceContext = res.getString("openiam.idm.ws.path");

    protected static final Log log = LogFactory.getLog(BaseProvisioningHelper.class);


    protected String getResProperty(Set<ResourceProp> resPropSet, String propertyName) {
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

    /* Helper methods for Pre and Post processing scripts */
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



    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    public static ApplicationContext getAc() {
        return ac;
    }

    public static void setAc(ApplicationContext ac) {
        BaseProvisioningHelper.ac = ac;
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

    public String getConnectorWsdl() {
        return connectorWsdl;
    }

    public void setConnectorWsdl(String connectorWsdl) {
        this.connectorWsdl = connectorWsdl;
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

    public void setRemoteConnectorAdapter(RemoteConnectorAdapter remoteConnectorAdapter) {
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

    public void setMuleContext(MuleContext ctx) {

        muleContext = ctx;

    }
}
