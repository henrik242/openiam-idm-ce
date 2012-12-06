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
package org.openiam.idm.srvc.synch.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.module.client.MuleClient;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.SysConfiguration;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;
import org.openiam.idm.srvc.synch.srcadapter.AdapterFactory;
import org.openiam.idm.srvc.synch.util.UserSearchUtils;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.idm.srvc.user.dto.UserSearch;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.dto.UserResourceAssociation;
import org.openiam.provision.service.ProvisionService;

/**
 * @author suneet
 *
 */
public class IdentitySynchServiceImpl implements IdentitySynchService {
    private SynchConfigDAO synchConfigDao;
    private SynchConfigDataMappingDAO synchConfigMappingDao;
    private AdapterFactory adaptorFactory;
    private MuleContext muleContext;
    private UserDataService userMgr;
    private ProvisionService provisionService;
    protected LoginDataService loginManager;
    protected SysConfiguration sysConfiguration;

    static protected ResourceBundle res = ResourceBundle.getBundle("datasource");

    static String serviceHost = res.getString("openiam.service_base");
    static String serviceContext = res.getString("openiam.idm.ws.path");

	
	private static final Log log = LogFactory.getLog(IdentitySynchServiceImpl.class);


    /*
    * The flags for the running tasks are handled by this Thread-Safe Set.
    * It stores the taskIds of the currently executing tasks.
    * This is faster and as reliable as storing the flags in the database,
    * if the tasks are only launched from ONE host in a clustered environment.
    * It is unique for each class-loader, which means unique per war-deployment.
    */
    private static Set<String> runningTask = Collections.newSetFromMap(new ConcurrentHashMap());
	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.synch.service.IdentitySynchService#getAllConfig()
	 */
	public List<SynchConfig> getAllConfig() {
		List<SynchConfig> configList = synchConfigDao.findAllConfig();
		if ( configList != null && !configList.isEmpty()) {
			return configList;
		}
		return null;
	}

	
	public SynchConfig findById(java.lang.String id)  {
		if (id == null) {
			throw new IllegalArgumentException("id parameter is null");
		}
		
		return synchConfigDao.findById(id);
	}

	public SynchConfig addConfig(SynchConfig synchConfig) {
		if (synchConfig == null) {
			throw new IllegalArgumentException("synchConfig parameter is null");
		}
		return synchConfigDao.add(synchConfig);
		
	}

	public SynchConfig updateConfig(SynchConfig synchConfig) {
		if (synchConfig == null) {
			throw new IllegalArgumentException("synchConfig parameter is null");
		}
		return synchConfigDao.update(synchConfig);
				
	}

	public void removeConfig(String configId ) {
		if (configId == null) {
			throw new IllegalArgumentException("id parameter is null");
		}
		SynchConfig config = synchConfigDao.findById(configId);
		synchConfigDao.remove(config);
		
	}

	public SyncResponse startSynchronization(SynchConfig config) {

        SyncResponse syncResponse = new SyncResponse(ResponseStatus.SUCCESS);

        log.debug("-SYNCHRONIZATION SERVICE: startSynchronization CALLED.^^^^^^^^");

        SyncResponse processCheckResponse = addTask(config.getSynchConfigId());
        if ( processCheckResponse.getStatus() == ResponseStatus.FAILURE ) {
            return processCheckResponse;

        }
        try {

			SourceAdapter adapt = adaptorFactory.create(config);
            adapt.setMuleContext(muleContext);

			long newLastExecTime = System.currentTimeMillis();

            syncResponse = adapt.startSynch(config);
			

			if (syncResponse.getLastRecordTime() == null) {
			
				synchConfigDao.updateExecTime(config.getSynchConfigId(), new Timestamp( newLastExecTime ));
			}else {
				synchConfigDao.updateExecTime(config.getSynchConfigId(), new Timestamp( syncResponse.getLastRecordTime().getTime() ));
			}

            if (syncResponse.getLastRecProcessed() != null) {

				synchConfigDao.updateLastRecProcessed(config.getSynchConfigId(),syncResponse.getLastRecProcessed() );
			}


		    log.debug("-SYNCHRONIZATION SERVICE: startSynchronization COMPLETE.^^^^^^^^");

		}catch( ClassNotFoundException cnfe) {

            cnfe.printStackTrace();

			log.error(cnfe);
            syncResponse = new SyncResponse(ResponseStatus.FAILURE);
            syncResponse.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            syncResponse.setErrorText(cnfe.getMessage());

		}catch(Exception e) {


			log.error(e);
            syncResponse = new SyncResponse(ResponseStatus.FAILURE);
            syncResponse.setErrorText(e.getMessage());

		}finally {
            endTask(config.getSynchConfigId());

            return syncResponse;

        }

	}

    // manage if the task is running

    /**
     * Updates the RunningTask list to show that a process is running
     * @param configId
     * @return
     */
    public SyncResponse addTask(String configId) {

        SyncResponse resp = new SyncResponse(ResponseStatus.SUCCESS);
        synchronized (runningTask) {
            if(runningTask.contains(configId)) {

                resp = new SyncResponse(ResponseStatus.FAILURE);
                resp.setErrorCode(ResponseCode.FAIL_PROCESS_ALREADY_RUNNING);
                return resp;
            }
            runningTask.add(configId);
            return resp;
        }

    }

    public void endTask(String configID) {
        runningTask.remove(configID);

    }

    public Response testConnection(SynchConfig config) {
        try {
            SourceAdapter adapt = adaptorFactory.create(config);
            adapt.setMuleContext(muleContext);

            return adapt.testConnection(config);
        } catch (ClassNotFoundException e) {
            Response resp = new Response(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            resp.setErrorText(e.getMessage());
            return resp;
        } catch (IOException e) {
            Response resp = new Response(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.IO_EXCEPTION);
            resp.setErrorText(e.getMessage());
            return resp;
        }
    }

    /**
     * Tests the search criteria to determine how many users will be impacted by the change
     * @param config
     * @return
     */
    public Response testBulkMigrationImpact(BulkMigrationConfig config) {

        Response resp = new Response(ResponseStatus.SUCCESS);

        // select the user that we need to move
        UserSearch search = UserSearchUtils.buildSearch(config);
        if (search.isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;
        }

        List<User> searchResult =  userMgr.search(search);

        if (searchResult == null) {
            resp.setResponseValue(new Integer(0));
        }else {
            resp.setResponseValue( new Integer( searchResult.size()));
        }
        return resp;


    }


    public Response bulkUserMigration(BulkMigrationConfig config) {

        // fix the error handling so that errors are reported in the response object.

        Response resp = new Response(ResponseStatus.SUCCESS);

        // select the user that we need to move
        UserSearch search = UserSearchUtils.buildSearch(config);
        if (search.isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;
        }

        List<User> searchResult =  userMgr.search(search);

        if (BulkMigrationConfig.ACTION_RESET_PASSWORD.equalsIgnoreCase( config.getActionType()) ) {

            bulkResetPassword(searchResult, config);

            return resp;

        }

        // all the provisioning service
        for ( User user :  searchResult) {

            log.debug("Migrating user: " + user.getUserId() + " " + user.getLastName());

            ProvisionUser pUser = new ProvisionUser(user);

            if (config.getTargetRole() != null && !config.getTargetRole().isEmpty() ) {

                Role r = parseRole(config.getTargetRole());
                if ( pUser.getMemberOfRoles() == null ) {
                    List<Role> roleList = new ArrayList<Role>();
                    pUser.setMemberOfRoles(roleList);
                }

                if ("ADD".equalsIgnoreCase(config.getOperation())) {
                    // add to role
                    r.setOperation(AttributeOperationEnum.ADD);


                    pUser.getMemberOfRoles().add(r);

                } else {
                    // remove from role
                    r.setOperation(AttributeOperationEnum.DELETE);
                    pUser.getMemberOfRoles().add(r);
                }

            }else if (config.getTargetResource() != null && !config.getTargetResource().isEmpty()) {

                List<UserResourceAssociation> uraList = new ArrayList<UserResourceAssociation>();

                UserResourceAssociation ura = new UserResourceAssociation();
                ura.setResourceId(config.getTargetResource());

                if ("ADD".equalsIgnoreCase(config.getOperation())) {
                    // add to resourceList
                    ura.setOperation(AttributeOperationEnum.ADD);
                    uraList.add(ura);
                    pUser.setUserResourceList(uraList);

                } else {
                    // remove from resource List

                    ura.setOperation(AttributeOperationEnum.DELETE);
                    uraList.add(ura);
                    pUser.setUserResourceList(uraList);

                }

            }
            // send message to provisioning service asynchronously
            //invokeOperation(pUser);
            provisionService.modifyUser(pUser);

        }


        return null;
    }


    private void invokeOperation(ProvisionUser pUser) {
        try {


            Map<String, String> msgPropMap = new HashMap<String, String>();
            msgPropMap.put("SERVICE_HOST", serviceHost);
            msgPropMap.put("SERVICE_CONTEXT", serviceContext);


            //Create the client with the context
            MuleClient client = new MuleClient(muleContext);
            client.sendAsync("vm://provisionServiceModifyMessage", pUser, msgPropMap);


        } catch (Exception e) {
            log.debug("EXCEPTION:bulkUserMigration");
            log.error(e);
        }
    }

    private UserSearch buildSearchByRole(RoleId roleId) {
        UserSearch search = new UserSearch();


        List<String> roleList = new ArrayList<String>();
        roleList.add(roleId.getRoleId() );
        search.setRoleIdList(roleList);
        search.setDomainId(roleId.getServiceId());

        return search;
    }


    private Role parseRole(String roleStr) {
        String domainId = null;
        String roleId = null;

        StringTokenizer st = new StringTokenizer(roleStr, "*");
        if (st.hasMoreTokens()) {
            domainId = st.nextToken();
        }
        if (st.hasMoreElements()) {
            roleId = st.nextToken();
        }
        RoleId id = new RoleId(domainId , roleId);
        Role r = new Role();
        r.setId(id);

        return r;
    }

    private void bulkResetPassword(List<User> searchResult, BulkMigrationConfig config) {

        String managedSysId = sysConfiguration.getDefaultManagedSysId();
        String password = config.getNewPassword();

        for ( User user :  searchResult) {

            List<Login> principalList = loginManager.getLoginByUser(user.getUserId());
            Map<String,String> domainMap = new HashMap<String,String>();
            String primaryPrincipal = null;

            for ( Login l : principalList) {
                domainMap.put( l.getId().getDomainId(), null );

                if (managedSysId.equalsIgnoreCase(l.getId().getManagedSysId())) {
                    primaryPrincipal = l.getId().getLogin();
                }

            }

            Set<String> domainKeys = domainMap.keySet();
            for ( String domain  : domainKeys) {

                PasswordSync pswdSync = new PasswordSync();
                pswdSync.setAction("BULK RESET PASSWORD");
                pswdSync.setManagedSystemId(managedSysId);
                pswdSync.setPassword(password);
                pswdSync.setPrincipal(primaryPrincipal);
                pswdSync.setSecurityDomain(domain);


                pswdSync.setRequestorLogin(config.getRequestorLogin());
                pswdSync.setRequestorDomain(domain);

                provisionService.resetPassword(pswdSync);
            }

        }

    }


    @Override
    public Response resynchRole(RoleId roleId) {

        Response resp = new Response(ResponseStatus.SUCCESS);

        log.debug("Resynch Role: " + roleId );

        // select the user that we need to move
        UserSearch search = buildSearchByRole(roleId);
        if (search.isEmpty()) {
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;
        }

        List<User> searchResult =  userMgr.search(search);


        if (searchResult == null) {
            resp.setStatus(ResponseStatus.FAILURE);
            return resp;
        }

        // create role object to show role membership
        Role rl = new Role();
        rl.setId(roleId);

        // all the provisioning service
        for ( User user :  searchResult) {

            log.debug("Updating the user since this role's configuration has changed.: " + user.getUserId() + " " + user.getLastName());

            ProvisionUser pUser = new ProvisionUser(user);

            if (pUser.getMemberOfRoles() == null ) {
                List<Role> rList = new ArrayList<Role>();
                rList.add(rl);
                pUser.setMemberOfRoles(rList);

            }  else {

                pUser.getMemberOfRoles().add(rl);

            }

            provisionService.modifyUser(pUser);

        }


        return resp;
    }

    public void setMuleContext(MuleContext ctx) {

        muleContext = ctx;
     }

	public SynchConfigDAO getSynchConfigDao() {
	    return synchConfigDao;
	}


	public void setSynchConfigDao(SynchConfigDAO synchConfigDao) {
		this.synchConfigDao = synchConfigDao;
	}


	public SynchConfigDataMappingDAO getSynchConfigMappingDao() {
		return synchConfigMappingDao;
	}


	public void setSynchConfigMappingDao(
			SynchConfigDataMappingDAO synchConfigMappingDao) {
		this.synchConfigMappingDao = synchConfigMappingDao;
	}


	public AdapterFactory getAdaptorFactory() {
		return adaptorFactory;
	}


	public void setAdaptorFactory(AdapterFactory adaptorFactory) {
		this.adaptorFactory = adaptorFactory;
	}

    public UserDataService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataService userMgr) {
        this.userMgr = userMgr;
    }

    public ProvisionService getProvisionService() {
        return provisionService;
    }

    public void setProvisionService(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    public LoginDataService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public SysConfiguration getSysConfiguration() {
        return sysConfiguration;
    }

    public void setSysConfiguration(SysConfiguration sysConfiguration) {
        this.sysConfiguration = sysConfiguration;
    }
}
