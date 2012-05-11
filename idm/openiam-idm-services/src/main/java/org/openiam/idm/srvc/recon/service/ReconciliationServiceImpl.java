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
package org.openiam.idm.srvc.recon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.connector.type.LookupRequest;
import org.openiam.connector.type.LookupResponse;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.recon.command.ReconciliationCommandFactory;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.idm.srvc.recon.dto.ReconciliationResponse;
import org.openiam.idm.srvc.recon.dto.ReconciliationSituation;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceRole;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.LookupUserResponse;
import org.openiam.provision.service.ConnectorAdapter;
import  org.openiam.provision.service.ProvisionService;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.provision.service.RemoteConnectorAdapter;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.LookupRequestType;
import org.openiam.spml2.msg.LookupResponseType;
import org.openiam.spml2.msg.PSOIdentifierType;
import org.openiam.spml2.msg.StatusCodeType;

/**
 * @author suneet
 *
 */
public class ReconciliationServiceImpl implements ReconciliationService, MuleContextAware {

	protected ReconciliationSituationDAO reconSituationDao;
	protected ReconciliationResultDAO reconResultDao;
	protected ReconciliationConfigDAO reconConfigDao;
	protected ReconciliationResultDAO reconResultDetailDao;
    protected MuleContext muleContext;
    protected LoginDataService loginManager;
    protected  ProvisionService provisionService;
    protected ResourceDataService resourceDataService;
    protected UserDataService userMgr;
    protected ManagedSystemDataService managedSysService;
    protected ConnectorDataService connectorService;
    protected ConnectorAdapter connectorAdapter;
    protected RemoteConnectorAdapter remoteConnectorAdapter;
    protected RoleDataService roleDataService;


    private static final Log log = LogFactory.getLog(ReconciliationServiceImpl.class);

    public ReconciliationConfig addConfig(ReconciliationConfig config) {
        if (config == null) {
			throw new IllegalArgumentException("config parameter is null");
		}
		return reconConfigDao.add(config);


    }

    public ReconciliationConfig updateConfig(ReconciliationConfig config) {
        if (config == null) {
                    throw new IllegalArgumentException("config parameter is null");
        }
        return reconConfigDao.update(config);


    }

    public void removeConfigByResourceId(String resourceId) {
        if (resourceId == null) {
                    throw new IllegalArgumentException("resourceId parameter is null");
        }
        reconConfigDao.removeByResourceId(resourceId);

    }

    public void removeConfig(String configId) {
        if (configId == null) {
                    throw new IllegalArgumentException("configId parameter is null");
        }
        ReconciliationConfig config =  reconConfigDao.findById(configId);
        reconConfigDao.remove(config);

    }

    public ReconciliationConfig getConfigByResource(String resourceId) {
    if (resourceId == null) {
                    throw new IllegalArgumentException("resourceId parameter is null");
     }
     return reconConfigDao.findByResourceId(resourceId);

    }

    public ReconciliationSituationDAO getReconSituationDao() {
		return reconSituationDao;
	}


	public void setReconSituationDao(ReconciliationSituationDAO reconSituationDao) {
		this.reconSituationDao = reconSituationDao;
	}


	public ReconciliationResultDAO getReconResultDao() {
		return reconResultDao;
	}


	public void setReconResultDao(ReconciliationResultDAO reconResultDao) {
		this.reconResultDao = reconResultDao;
	}


	public ReconciliationConfigDAO getReconConfigDao() {
		return reconConfigDao;
	}


	public void setReconConfigDao(ReconciliationConfigDAO reconConfigDao) {
		this.reconConfigDao = reconConfigDao;
	}


	public ReconciliationResultDAO getReconResultDetailDao() {
		return reconResultDetailDao;
	}


	public void setReconResultDetailDao(ReconciliationResultDAO reconResultDetailDao) {
		this.reconResultDetailDao = reconResultDetailDao;
	}

    public ReconciliationConfig getConfigById(String configId) {
       if (configId == null) {
                    throw new IllegalArgumentException("configId parameter is null");
        }
        return reconConfigDao.findById(configId);

    }

    public void setMuleContext(MuleContext ctx) {

       muleContext = ctx;
    }

    public ReconciliationResponse startReconciliation(ReconciliationConfig config) {
        try {
            log.debug("Reconciliation started for configId=" + config.getReconConfigId() + " - resource=" + config.getResourceId() );

            Resource res = resourceDataService.getResource(config.getResourceId());
            String managedSysId =  res.getManagedSysId();
            ManagedSys mSys = managedSysService.getManagedSys(managedSysId);

            log.debug("ManagedSysId = " + managedSysId);
            log.debug("Getting identities for managedSys");

            Map<String, ReconciliationCommand> situations = new HashMap<String, ReconciliationCommand>();
            for(ReconciliationSituation situation : config.getSituationSet()){
                situations.put(situation.getSituation().trim(), ReconciliationCommandFactory.createCommand(situation.getSituationResp(), situation, managedSysId));
                log.debug("Created Command for: " + situation.getSituation());
            }

            List<User> users = new ArrayList<User>();
            for(ResourceRole rRole: res.getResourceRoles()) {
                User[] usrAry = roleDataService.getUsersInRole(mSys.getDomainId(), rRole.getId().getRoleId());
                if(usrAry != null) {
                    for(User user: usrAry){
                        users.add(user);
                    }
                }
            }

            List<Login> principalList =  loginManager.getAllLoginByManagedSys(managedSysId);
            if (principalList == null || principalList.isEmpty()) {
                log.debug("No identities found for managedSysId in IDM repository");
                ReconciliationResponse resp = new ReconciliationResponse(ResponseStatus.SUCCESS);
                return resp;
            }
            for ( User u  : users ) {
                Login l = null;
                User user = userMgr.getUserWithDependent(u.getUserId(), true);
                List<Login> logins = user.getPrincipalList();
                if(logins != null) {
                    for(Login login: logins){
                        if(login.getId().getDomainId().equalsIgnoreCase(mSys.getDomainId()) && login.getId().getManagedSysId().equalsIgnoreCase(managedSysId)){
                            l = login;
                            break;
                        }
                    }
                }
                if(l == null){
                    if(user.getStatus().equals(UserStatusEnum.DELETED)){
                        // User is deleted and has no Identity for this managed system -> goto next user
                        continue;
                    }
                    // There was never a resource account for this user.
                    // Possibility: User was created before the managed Sys was associated.
                    // Situation: Login Not Found
                    ReconciliationCommand command = situations.get("Login Not Found");
                    if(command != null){
                        log.debug("Call command for IDM Login Not Found");
                        command.execute(l, user, null);
                    }
                    ReconciliationResponse resp = new ReconciliationResponse(ResponseStatus.SUCCESS);
                    return resp;
                }

                String principal = l.getId().getLogin();
                log.debug("looking up identity in resource: " + principal);

                LookupUserResponse lookupResp =  provisionService.getTargetSystemUser(principal, managedSysId);

                log.debug("Lookup status for " + principal + " =" +  lookupResp.getStatus());

                //User user = userMgr.getUserByPrincipal(l.getId().getDomainId(), l.getId().getLogin(), l.getId().getManagedSysId(), true);

                if (lookupResp.getStatus() == ResponseStatus.FAILURE && !l.getStatus().equalsIgnoreCase("INACTIVE")) {
                    // Situation: Resource Delete
                    ReconciliationCommand command = situations.get("Resource Delete");
                    if(command != null){
                        log.debug("Call command for Resource Delete");
                        command.execute(l, user, null);
                    }
                } else if (lookupResp.getStatus() == ResponseStatus.SUCCESS) {
                    // found entry in managed sys
                    if(l.getStatus().equalsIgnoreCase("INACTIVE") || user.getStatus().equals(UserStatusEnum.DELETED)) {
                        // Situation: IDM Delete
                        ReconciliationCommand command = situations.get("IDM Delete");
                        if(command != null){
                            log.debug("Call command for IDM Delete");
                            command.execute(l, user, null);
                        }
                    } else {
                        // Situation: IDM Changed/Resource Changed/Match Found
                        ReconciliationCommand command = situations.get("Match Found");
                        if(command != null){
                            log.debug("Call command for Match Found");
                            command.execute(l, user, null);
                        }
                    }

                }
            }


            ProvisionConnector connector = connectorService.getConnector(mSys.getConnectorId());

            if (connector.getConnectorInterface() != null &&
                    connector.getConnectorInterface().equalsIgnoreCase("REMOTE")) {

                log.debug("Calling reconcileResource with Remote connector");

                remoteConnectorAdapter.reconcileResource(config, connector, muleContext);
            } else {

                log.debug("Calling reconcileResource local connector");
                connectorAdapter.reconcileResource(mSys, config, muleContext);
            }
		}catch(Exception e) {
			log.error(e);
            e.printStackTrace();
			ReconciliationResponse resp = new ReconciliationResponse(ResponseStatus.FAILURE);
			resp.setErrorText(e.getMessage());
			return resp;
		}
        ReconciliationResponse resp = new ReconciliationResponse(ResponseStatus.SUCCESS);
        return resp;

    }

    public LoginDataService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public ProvisionService getProvisionService() {
        return provisionService;
    }

    public void setProvisionService(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public UserDataService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataService userMgr) {
        this.userMgr = userMgr;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public void setConnectorService(ConnectorDataService connectorService) {
        this.connectorService = connectorService;
    }

    public void setConnectorAdapter(ConnectorAdapter connectorAdapter) {
        this.connectorAdapter = connectorAdapter;
    }

    public void setRemoteConnectorAdapter(RemoteConnectorAdapter remoteConnectorAdapter) {
        this.remoteConnectorAdapter = remoteConnectorAdapter;
    }

    public void setRoleDataService(RoleDataService roleDataService) {
        this.roleDataService = roleDataService;
    }
}
