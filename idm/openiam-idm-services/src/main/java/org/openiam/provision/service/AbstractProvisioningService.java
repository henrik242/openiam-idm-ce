package org.openiam.provision.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.openiam.base.SysConfiguration;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.audit.service.IdmAuditLogDataService;
import org.openiam.idm.srvc.auth.login.LoginDAO;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.grp.service.GroupDataService;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.pswd.service.PasswordHistoryDAO;
import org.openiam.idm.srvc.pswd.service.PasswordService;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ResourceBundle;

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
