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
package org.openiam.idm.srvc.synch.service.generic;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.service.MatchObjectRule;
import org.openiam.idm.srvc.synch.service.SourceAdapter;
import org.openiam.idm.srvc.synch.service.TransformScript;
import org.openiam.idm.srvc.synch.service.ValidationScript;
import org.openiam.idm.srvc.synch.srcadapter.MatchRuleFactory;
import org.openiam.idm.srvc.synch.srcadapter.SynchScriptFactory;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Reads a CSV file for use during the synchronization process
 * @author suneet
 *
 */
public class CSVAdapterForGenericObject implements SourceAdapter {

    ObjectAdapterMap adapterMap;

	public static ApplicationContext ac;
	protected LoginDataService loginManager;
	protected RoleDataService roleDataService;
	protected AuditHelper auditHelper;
	
	protected UserDataService userMgr;

	MatchRuleFactory matchRuleFactory;
    protected String scriptEngine;
	
	private static final Log log = LogFactory.getLog(CSVAdapterForGenericObject.class);
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ac = applicationContext;
	}
	
	public SyncResponse startSynch(SynchConfig config) {

        log.debug("CSVAdapterForGenericObject: Starting CSV File Sync");

        LineObject rowHeader = null;
		Reader reader = null;
        String requestId = UUIDGen.getUUID();

        // reference to the script based handler
        ObjectHandler handler = null;

        // START THE AUDIT PROCESS
        IdmAuditLog synchStartLog = new IdmAuditLog();
        synchStartLog.setSynchAttributes("SYNCH_GENERIC_OBJECT", config.getSynchConfigId(), "START", "SYSTEM", requestId);
        synchStartLog = auditHelper.logEvent(synchStartLog);


        // Get the handler for this object type
        // change this fieldname from ProcessRule to something more appropriate

        String objectHandlerName = adapterMap.getHandlerName(config.getProcessRule());

        log.debug("Handler Name:" +  objectHandlerName);

        if (objectHandlerName == null) {
            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.FILE_EXCEPTION.toString(),"No handler defined to object." );
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FILE_EXCEPTION);
            return resp;
        }
        try {

             handler = createHandler(objectHandlerName);

            log.debug("Handler ref:" +  handler);

          }catch (Exception e) {

            e.printStackTrace();

            synchStartLog.updateSynchAttributes("FAIL",ResponseCode.CLASS_NOT_FOUND.toString() , e.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            return resp;
        }

		
		File file = new File(config.getFileName());
		try {
			reader = new FileReader(file);
		}catch(FileNotFoundException fe) {
            fe.printStackTrace();

			log.error(fe);

            synchStartLog.updateSynchAttributes("FAIL",ResponseCode.FILE_EXCEPTION.toString() , fe.toString());
            auditHelper.logEvent(synchStartLog);

			SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
			resp.setErrorCode(ResponseCode.FILE_EXCEPTION);
			return resp;
			
		}

		CSVParser parser = new CSVParser(reader, CSVStrategy.EXCEL_STRATEGY);
		try {
			int ctr = 0;
			String[][] fileContentAry =   parser.getAllValues();
			int size = fileContentAry.length;
			
			
			for (String[] lineAry : fileContentAry) {

				if (ctr == 0) {
                    rowHeader = populateTemplate(lineAry);
					ctr++;
				}else {
					//populate the data object

                    // Build a generic object that contains the data from the source that we can pass to any handler
					LineObject rowObj = rowHeader.copy();
					populateRowObject(rowObj, lineAry);


                    Object obj = handler.populateObject(rowObj);

                    log.debug("Populated object=" + obj.toString());

                    Object existingObj = handler.existingObject(obj);

                    if (existingObj != null) {
                        // object exists
                        log.debug("Object exists");
                    }else {
                        // new object
                        log.debug("New Object");

                    }

                    log.debug(obj.toString());

					
				/*	try {



						
					}catch(ClassNotFoundException cnfe) {
						log.error(cnfe);

                        synchStartLog.updateSynchAttributes("FAIL",ResponseCode.CLASS_NOT_FOUND.toString() , cnfe.toString());
                        auditHelper.logEvent(synchStartLog);

						SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
						resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
						return resp;
					}
					*/
						
				}
			
			}
			
		}catch(IOException io) {

            io.printStackTrace();

            synchStartLog.updateSynchAttributes("FAIL",ResponseCode.IO_EXCEPTION.toString() , io.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.IO_EXCEPTION);
            return resp;


		}

        log.debug("CSV SYNCHRONIZATION COMPLETE^^^^^^^^");


		SyncResponse resp = new SyncResponse(ResponseStatus.SUCCESS);
		return resp;
		
	}


	
	private LineObject populateTemplate(String[] lineAry) {
        LineObject rowHeader = new LineObject();
		Map<String,Attribute> columnMap = new HashMap<String, Attribute>();
		
		int ctr =0;
		for (String s  :lineAry) {
			Attribute a = new Attribute(s, null);
			a.setType("STRING");
			a.setColumnNbr(ctr);
			columnMap.put(a.getName(),a);
			ctr++;
		}
		rowHeader.setColumnMap(columnMap);
        return rowHeader;
	}
	
	

	private void populateRowObject(LineObject rowObj ,String[] lineAry) {
		DateFormat df =  new SimpleDateFormat("MM-dd-yyyy"); 
		Map<String, Attribute> attrMap =  rowObj.getColumnMap();
		Set<String> keySet = attrMap.keySet();
		Iterator<String> it  = keySet.iterator();
		
		while ( it.hasNext()) {
			String key  = it.next();
			Attribute attr =  rowObj.get(key);
			int colNbr = attr.getColumnNbr();
			String colValue = lineAry[colNbr];
			
			
			attr.setValue(colValue);
		}

	}

    private ObjectHandler createHandler(String scriptName) throws ClassNotFoundException, IOException{
        ScriptIntegration se = null;

        se = ScriptFactory.createModule(scriptEngine);

        return (ObjectHandler)se.instantiateClass(null, scriptName);
    }



	public static ApplicationContext getAc() {
		return ac;
	}

	public static void setAc(ApplicationContext ac) {
		CSVAdapterForGenericObject.ac = ac;
	}

	public LoginDataService getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(LoginDataService loginManager) {
		this.loginManager = loginManager;
	}

	public RoleDataService getRoleDataService() {
		return roleDataService;
	}

	public void setRoleDataService(RoleDataService roleDataService) {
		this.roleDataService = roleDataService;
	}

	public UserDataService getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserDataService userMgr) {
		this.userMgr = userMgr;
	}



	public MatchRuleFactory getMatchRuleFactory() {
		return matchRuleFactory;
	}

	public void setMatchRuleFactory(MatchRuleFactory matchRuleFactory) {
		this.matchRuleFactory = matchRuleFactory;
	}

	public AuditHelper getAuditHelper() {
		return auditHelper;
	}

	public void setAuditHelper(AuditHelper auditHelper) {
		this.auditHelper = auditHelper;
	}

    public void setMuleContext(MuleContext ctx) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public ObjectAdapterMap getAdapterMap() {
        return adapterMap;
    }

    public void setAdapterMap(ObjectAdapterMap adapterMap) {
        this.adapterMap = adapterMap;
    }

    public String getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(String scriptEngine) {
        this.scriptEngine = scriptEngine;
    }
}
