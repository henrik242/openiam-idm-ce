package org.openiam.provision.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.openiam.base.SysConfiguration;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.connector.type.ResumeRequest;
import org.openiam.connector.type.SuspendRequest;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.StatusCodeType;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;


/**
 * Class to handle the Disable User operation
 * @author suneet shah
 *
 */
public class DisableUserDelegate {


    private UserDataService userMgr;
    private AuditHelper auditHelper;
    private SysConfiguration sysConfiguration;
    private LoginDataService loginManager;
    private ManagedSystemDataService managedSysService;
    private ConnectorAdapter connectorAdapter;
    private RemoteConnectorAdapter remoteConnectorAdapter;

    private ResourceDataService resourceDataService;
    private RoleDataService roleDataService;
    private ConnectorDataService connectorService;

	protected static final Log log = LogFactory.getLog(DisableUserDelegate.class);



	public Response disableUser(String userId, boolean operation, String requestorId, MuleContext muleContext) {
		log.debug("----disableUser called.------");
		log.debug("operation code=" + operation);
		
		Response response = new Response(ResponseStatus.SUCCESS);
		
		String requestId = "R" + UUIDGen.getUUID();
		String strOperation = null;
	
		if (userId == null) {
			response.setStatus(ResponseStatus.FAILURE);
			response.setErrorCode(ResponseCode.USER_NOT_FOUND);	
			return response;
		}
		User usr = this.userMgr.getUserWithDependent(userId, false);

		if (usr == null) {
            auditHelper.addLog((operation) ? "DISABLE" : "ENABLE", sysConfiguration.getDefaultSecurityDomain(), null,
				"IDM SERVICE", requestorId, "IDM", "USER",
				userId, null,  "FAILURE", null,  null,
				null, requestId, null, null, null);

			response.setStatus(ResponseStatus.FAILURE);
			response.setErrorCode(ResponseCode.USER_NOT_FOUND);	
			return response;			
		}
		// disable the user in OpenIAM		
		if (operation) {
			usr.setSecondaryStatus(UserStatusEnum.DISABLED);
			strOperation = "DISABLE";
		}else {
			// enable an account that was previously disabled.
			usr.setSecondaryStatus(null);
			strOperation = "ENABLE";
		}
		userMgr.updateUserWithDependent(usr,false);

        Login lRequestor = loginManager.getPrimaryIdentity(requestorId);
        Login lTargetUser = loginManager.getPrimaryIdentity(userId);

        if (lRequestor != null && lTargetUser != null) {
			
            auditHelper.addLog(strOperation, lRequestor.getId().getDomainId(), lRequestor.getId().getLogin(),
                    "IDM SERVICE", requestorId, "IDM", "USER",
                    usr.getUserId(), null,  "SUCCESS", null,  null,
                    null, requestId, null, null, null,
                    null, lTargetUser.getId().getLogin(), lTargetUser.getId().getDomainId() );
        }else {
            if(log.isDebugEnabled()) {
                log.debug(String.format("Unable to log disable operation.  Requestor: %s, Target: %s", lRequestor, lTargetUser));
            }

            response.setStatus(ResponseStatus.FAILURE);
            response.setErrorCode(ResponseCode.OBJECT_NOT_FOUND);
            response.setErrorText(String.format("Requestor: '%s' or User: '%s' not found", requestorId, userId));
            return response;
        }
		// disable the user in the managed systems
		

		// typical sync
		List<Login> principalList = loginManager.getLoginByUser(usr.getUserId());
		if (principalList != null) {
			log.debug("PrincipalList size =" + principalList.size());
			for ( Login lg : principalList) {
				// get the managed system for the identity - ignore the managed system id that is linked to openiam's repository
				log.debug("-diabling managed system=" + lg.getId().getLogin() + " - " + lg.getId().getManagedSysId() ) ;
				
				if (!lg.getId().getManagedSysId().equalsIgnoreCase(sysConfiguration.getDefaultManagedSysId())) {
					String managedSysId = lg.getId().getManagedSysId();
					// update the target system
					ManagedSys mSys = managedSysService.getManagedSys(managedSysId);
					
					if (operation) {
						// suspend		
						log.debug("preparing suspendRequest object");
						
		            	SuspendRequestType suspendReq = new SuspendRequestType();
		                PSOIdentifierType idType = new PSOIdentifierType(lg.getId().getLogin(),null, managedSysId );
		                suspendReq.setPsoID(idType);
		                suspendReq.setRequestID(requestId);
		                connectorAdapter.suspendRequest(mSys, suspendReq,muleContext);
		                
		                
					} else {
						// resume - re-enable
						log.debug("preparing resumeRequest object");

                        // reset flags that go with this identiy
                        lg.setAuthFailCount(0);
                        lg.setIsLocked(0);
                        lg.setPasswordChangeCount(0);
                        loginManager.updateLogin(lg);



						
		            	ResumeRequestType resumeReq = new ResumeRequestType();
		                PSOIdentifierType idType = new PSOIdentifierType(lg.getId().getLogin(),null, managedSysId );
		                resumeReq.setPsoID(idType);
		                resumeReq.setRequestID(requestId);
		                connectorAdapter.resumeRequest(mSys, resumeReq,muleContext);
					}
                    
                    String domainId = null;
                    String loginId = null;
                    if (lRequestor.getId() != null) {
                       domainId = lRequestor.getId().getDomainId();
                       loginId = lRequestor.getId().getLogin();
                    }
                    
	        		auditHelper.addLog(strOperation + " IDENTITY", domainId, loginId,
	        				"IDM SERVICE", requestorId, "IDM", "USER",
	        				null, null,  "SUCCESS", requestId,  null, 
	        				null, requestId, null, null, null,
                            null, lg.getId().getLogin(), lg.getId().getDomainId());
				}else {
                    lg.setAuthFailCount(0);
                    lg.setIsLocked(0);
                    lg.setPasswordChangeCount(0);
                    loginManager.updateLogin(lg);
                }
			}
		}
        // Synchronize provision resources (e.g. connectors)
        final List<Role> roleList = roleDataService.getUserRoles(userId);
        Login lg = loginManager.getPrimaryIdentity(userId);
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (final Role role : roleList) {
                final RoleId roleId = role.getId();
                final List<Resource> resourceList = resourceDataService.getResourcesForRole(roleId.getServiceId(), roleId.getRoleId());
                if (CollectionUtils.isNotEmpty(resourceList)) {
                    for (final Resource resource : resourceList) {
                        final ManagedSys mSys = managedSysService.getManagedSys(resource.getManagedSysId());
                        if (mSys != null) {

                            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());
                            boolean connectorSuccess = false;
                            if (connector.getConnectorInterface() != null &&
                                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {
                                org.openiam.connector.type.ResponseType resp;

                                if (operation) {
                                    SuspendRequest suspendRequest = new SuspendRequest();
                                    suspendRequest.setScriptHandler(mSys.getSuspendHandler());
                                    suspendRequest.setUserIdentity(lg.getId().getLogin());
                                    suspendRequest.setTargetID(lg.getId().getManagedSysId());
                                    suspendRequest.setHostLoginId(mSys.getUserId());
                                    suspendRequest.setHostLoginPassword(mSys.getDecryptPassword());
                                    suspendRequest.setHostUrl(mSys.getHostUrl());
                                    suspendRequest.setRequestID("R" + System.currentTimeMillis());
                                    resp = remoteConnectorAdapter.suspend(mSys, suspendRequest, connector, muleContext);
                                } else {
                                    ResumeRequest resumeRequestType = new ResumeRequest();
                                    resumeRequestType.setScriptHandler(mSys.getSuspendHandler());
                                    resumeRequestType.setUserIdentity(lg.getId().getLogin());
                                    resumeRequestType.setTargetID(lg.getId().getManagedSysId());
                                    resumeRequestType.setHostLoginId(mSys.getUserId());
                                    resumeRequestType.setHostLoginPassword(mSys.getDecryptPassword());
                                    resumeRequestType.setHostUrl(mSys.getHostUrl());
                                    resumeRequestType.setRequestID("R" + System.currentTimeMillis());
                                    resp = remoteConnectorAdapter.resumeRequest(mSys, resumeRequestType, connector, muleContext);
                                }

                                if (resp.getStatus() == StatusCodeType.SUCCESS) {
                                    connectorSuccess = true;
                                } else {

                                }
                            }
                        }
                    }
                }
            }
        }
		response.setStatus(ResponseStatus.SUCCESS);
		return response;

		
	}

	

	public UserDataService getUserMgr() {
		return userMgr;
	}


	public void setUserMgr(UserDataService userMgr) {
		this.userMgr = userMgr;
	}


	public AuditHelper getAuditHelper() {
		return auditHelper;
	}


	public void setAuditHelper(AuditHelper auditHelper) {
		this.auditHelper = auditHelper;
	}



	public SysConfiguration getSysConfiguration() {
		return sysConfiguration;
	}



	public void setSysConfiguration(SysConfiguration sysConfiguration) {
		this.sysConfiguration = sysConfiguration;
	}


	public LoginDataService getLoginManager() {
		return loginManager;
	}


	public void setLoginManager(LoginDataService loginManager) {
		this.loginManager = loginManager;
	}


	public ManagedSystemDataService getManagedSysService() {
		return managedSysService;
	}


	public void setManagedSysService(ManagedSystemDataService managedSysService) {
		this.managedSysService = managedSysService;
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

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public RoleDataService getRoleDataService() {
        return roleDataService;
    }

    public void setRoleDataService(RoleDataService roleDataService) {
        this.roleDataService = roleDataService;
    }

    public ConnectorDataService getConnectorService() {
        return connectorService;
    }

    public void setConnectorService(ConnectorDataService connectorService) {
        this.connectorService = connectorService;
    }
}
