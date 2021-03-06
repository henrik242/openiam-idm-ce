package org.openiam.webadmin.conn.mngsys;

/*
 * Copyright 2009, OpenIAM LLC 
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.ResourceBundle;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.dto.ManagedSystemObjectMatch;
import org.openiam.idm.srvc.mngsys.dto.ProvisionConnector;
import org.openiam.idm.srvc.mngsys.service.ConnectorDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.secdomain.dto.SecurityDomain;
import org.openiam.spml2.interf.ConnectorService;

/**
 * Controller to manage connectivity information for a managed system
 * @author suneet
 *
 */
public class ManagedSysConnectionController extends SimpleFormController {


	private static final Log log = LogFactory.getLog(ManagedSysConnectionController.class);
	static protected ResourceBundle res = ResourceBundle.getBundle("connector");



	private ManagedSystemDataService managedSysService; 
	private SecurityDomainDataService secDomainService;
	private ConnectorDataService connectorService;
    protected ProvisionService provService;
	
	


	public ManagedSysConnectionController() {
		super();
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"),true) );
				
	}
	

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		

		String sysId = request.getParameter("sysId");
		String menuGroup = request.getParameter("menuGroup");
		String domainId = request.getParameter("domainId");
		
		String connectorId = request.getParameter("connectorId");
		
		log.debug("formBackingObject: domainId=" + domainId);
		
		// look up the panel for the connectorId
		String connectorPanel = null;
		try {
			connectorPanel = res.getString(connectorId);
		}catch(MissingResourceException me) {
			connectorPanel = "/managedsys/sysconnection.jsp";
		}catch(Exception me) {
			connectorPanel = "/managedsys/sysconnection.jsp";
		}
		request.setAttribute("connectorPanel", connectorPanel);
		
		// used by the UI for to show the side menu
		request.setAttribute("menuGroup", menuGroup);
		request.setAttribute("sysId", sysId);
			
		log.info("formBackingObject: sysId=" + sysId);
		
		if (sysId != null && sysId.length() > 0) {
			
			ManagedSystemObjectMatch[] matchObjAry=  managedSysService.managedSysObjectParam(sysId, "USER");
			ManagedSystemObjectMatch matchObj = null;
			if (matchObjAry != null && matchObjAry.length > 0) {
				matchObj = matchObjAry[0];
			}
		
			ManagedSys sys = managedSysService.getManagedSys(sysId);
			ManagedSysConnectionCommand cmd = new ManagedSysConnectionCommand(
				sys.getCommProtocol(), sys.getConnectorId(),
				sys.getDescription(), sys.getDomainId(), sys.getEndDate(), sys.getHostUrl(),
				sys.getManagedSysId(), sys.getName(), sys.getPort(), sys.getDecryptPassword(),
				sys.getStartDate(), sys.getStatus(), sys.getUserId(), sys.getPrimaryRepository(),
				sys.getSecondaryRepositoryId(), sys.getUpdateSecondary(),
                sys.getDriverUrl(),
             sys.getConnectionString(),
             sys.getAddHandler(),
             sys.getModifyHandler(),
             sys.getDeleteHandler(),
             sys.getPasswordHandler(),
             sys.getSuspendHandler(),
             sys.getHandler1(),
             sys.getHandler2(),
             sys.getHandler3(),
             sys.getHandler4(),
             sys.getHandler5()       );
			
			
			if (matchObj != null) {
				cmd.setBaseDn(matchObj.getBaseDn());
				cmd.setSearchBaseDn(matchObj.getSearchBaseDn());
				cmd.setKeyField(matchObj.getKeyField());
				cmd.setSearchFilter(matchObj.getSearchFilter());
				cmd.setObjectSearchId(matchObj.getObjectSearchId());
			}
			return cmd;
		}
		ManagedSysConnectionCommand cmd = new ManagedSysConnectionCommand();
		cmd.setDomainId(domainId);
		return cmd;
	}



	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		
		ProvisionConnector[] connectorAry = (ProvisionConnector[])connectorService.getAllConnectors();
		
		ManagedSys[] sysAry = managedSysService.getAllManagedSys();
		
		Map model = new HashMap();
		model.put("connectors", connectorAry);
		model.put("systems", sysAry);
		
		return model;
	}
	

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		log.debug("onSubmit called");
		
		ManagedSysConnectionCommand sysConCommand = (ManagedSysConnectionCommand)command;
		ManagedSys sys;
		
		String btn = request.getParameter("btn");

		// delete a connection
		if (btn != null && btn.equalsIgnoreCase("Delete")) {
			
			ManagedSystemObjectMatch[] matchObj = managedSysService.managedSysObjectParam(sysConCommand.getManagedSysId(), "USER");
			if (matchObj != null) {
				for ( ManagedSystemObjectMatch o : matchObj) {
					managedSysService.removeManagedSystemObjectMatch(o);
				}
			}
			managedSysService.removeManagedSystem(sysConCommand.getManagedSysId());
			// delete the match object 
			
	        ModelAndView mav = new ModelAndView("/deleteconfirm");
	        mav.addObject("msg", "Managed system has been successfully deleted.");
			request.setAttribute("menuGroup", null);
			request.setAttribute("sysId", null);
	        return mav;

		}



		
		// add or update connection
		if (sysConCommand.getManagedSysId() == null || 
			sysConCommand.getManagedSysId().isEmpty() ) {
			// new 
			log.debug("onSubmit - New");
			sys = new ManagedSys();
			formToSys(sysConCommand, sys);
			ManagedSys newSys = managedSysService.addManagedSystem(sys);
			
			System.out.println("ManagedSysId =" + newSys.getManagedSysId());
			
			sysConCommand.setManagedSysId(newSys.getManagedSysId());
			// create a match object match object
			ManagedSystemObjectMatch matchObj =sysConCommand.getMatchObj();
			matchObj.setObjectSearchId(null);
			matchObj.setManagedSys(newSys.getManagedSysId());
			managedSysService.addManagedSystemObjectMatch(matchObj);
		}else {
			// existing record
			log.debug("onSubmit - Update");
			sys = managedSysService.getManagedSys(sysConCommand.getManagedSysId());
			formToSys(sysConCommand, sys);
			
			System.out.println("connectorid=" + sys.getConnectorId());
			
			managedSysService.updateManagedSystem(sys);
			// update the match object
			ManagedSystemObjectMatch matchObj =sysConCommand.getMatchObj();
			matchObj.setManagedSys(sys.getManagedSysId());
			if (matchObj.getObjectSearchId() != null && matchObj.getObjectSearchId().length() ==0 ) {
				System.out.println("new match object");
				matchObj.setObjectSearchId(null);
				managedSysService.addManagedSystemObjectMatch(matchObj);
			}else {
				System.out.println("Existing match object");
				managedSysService.updateManagedSystemObjectMatch(matchObj);
			}
		}


        if (btn != null && btn.equalsIgnoreCase("Test Connection")) {
            Response resp =  provService.testConnectionConfig(sysConCommand.getManagedSysId());

            ModelAndView mav = new ModelAndView("/managedsys/testconnect");

            if (resp.getStatus() == ResponseStatus.FAILURE) {
                mav.addObject("msg", "Connection Failed.");
                mav.addObject("error", resp.getErrorText());

            } else {
                mav.addObject("msg", "Connection Successful.");
            }
             return mav;

		}


		// populate the form objects - reference data is only called on the first request.
		ProvisionConnector[] connectorAry = (ProvisionConnector[])connectorService.getAllConnectors();
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject("managedSysConnectionCmd", sysConCommand);
		mav.addObject("msg", "Your information has been successfully saved.");
		mav.addObject("connectors", connectorAry);
		return mav;

		
	}
	private void formToSys(ManagedSysConnectionCommand sysConCommand, ManagedSys sys) {
		sys.setCommProtocol(sysConCommand.getCommProtocol());
		sys.setConnectorId(sysConCommand.getConnectId());
		sys.setDescription(sysConCommand.getDescription());
		sys.setDomainId(sysConCommand.getDomainId());
		sys.setEndDate(sysConCommand.getEndDt());
		sys.setHostUrl(sysConCommand.getHostUrl());
		sys.setName(sysConCommand.getName());
		sys.setPort(sysConCommand.getPort());
		sys.setPswd(sysConCommand.getPswd());
		sys.setStartDate(sysConCommand.getStartDt());
		sys.setStatus(sysConCommand.getStatus());
		sys.setUserId(sysConCommand.getUserId());
		sys.setPrimaryRepository(sysConCommand.getPrimaryRepository());
		sys.setUpdateSecondary(sysConCommand.getUpdateSecondary());
		sys.setSecondaryRepositoryId(sysConCommand.getSecondaryRepositoryId());


        sys.setAddHandler(sysConCommand.getAddHandler());
        sys.setConnectionString(sysConCommand.getConnectionString());
        sys.setDeleteHandler(sysConCommand.getDeleteHandler());
        sys.setDriverUrl(sysConCommand.getDriverUrl());
        sys.setHandler1(sysConCommand.getHandler1());
        sys.setHandler2(sysConCommand.getHandler2());
        sys.setHandler3(sysConCommand.getHandler3());
        sys.setHandler4(sysConCommand.getHandler4());
        sys.setHandler5(sysConCommand.getHandler5());
        sys.setModifyHandler(sysConCommand.getModifyHandler());
        sys.setPasswordHandler(sysConCommand.getPasswordHandler());
        sys.setSuspendHandler(sysConCommand.getSuspendHandler());
	}



	public ManagedSystemDataService getManagedSysService() {
		return managedSysService;
	}

	public void setManagedSysService(ManagedSystemDataService managedSysService) {
		this.managedSysService = managedSysService;
	}

	
	public SecurityDomainDataService getSecDomainService() {
		return secDomainService;
	}

	public void setSecDomainService(SecurityDomainDataService secDomainService) {
		this.secDomainService = secDomainService;
	}





	public ConnectorDataService getConnectorService() {
		return connectorService;
	}




	public void setConnectorService(ConnectorDataService connectorService) {
		this.connectorService = connectorService;
	}

    public ProvisionService getProvService() {
        return provService;
    }

    public void setProvService(ProvisionService provService) {
        this.provService = provService;
    }
}
