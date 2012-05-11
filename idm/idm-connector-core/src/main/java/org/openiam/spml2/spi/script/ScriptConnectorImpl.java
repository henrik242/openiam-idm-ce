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

package org.openiam.spml2.spi.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemObjectMatchDAO;
import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.openiam.spml2.base.AbstractSpml2Complete;
import org.openiam.spml2.interf.ConnectorService;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.password.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;
import org.openiam.spml2.spi.ldap.LdapConnectorImpl;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Connector shell that can be used to jumpstart the creation of a connector service.
 *
 * @author suneet
 */

@WebService(endpointInterface = "org.openiam.spml2.interf.ConnectorService",
        targetNamespace = "http://www.openiam.org/service/connector",
        portName = "ScriptConnectorServicePort",
        serviceName = "ScriptConnectorService")

public class ScriptConnectorImpl extends AbstractSpml2Complete implements ConnectorService {

    private static final Log log = LogFactory.getLog(LdapConnectorImpl.class);
    protected ManagedSystemDataService managedSysService;
    protected ManagedSystemObjectMatchDAO managedSysObjectMatchDao;
    protected ResourceDataService resourceDataService;
    protected String scriptEngine;

    public AddResponseType add(AddRequestType reqType) {
        String targetID = reqType.getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).add(reqType);
        } catch (Exception e) {
            log.error("Could not add: " + e.toString());

            AddResponseType resp = new AddResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;

        }
    }

    public ResponseType delete(DeleteRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).delete(reqType);
        } catch (Exception e) {
            log.error("Could not delete: " + e.toString());

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public LookupResponseType lookup(LookupRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).lookup(reqType);
        } catch (Exception e) {
            log.error("Lookup problem: " + e.toString());

            LookupResponseType resp = new LookupResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ModifyResponseType modify(ModifyRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).modify(reqType);
        } catch (Exception e) {
            log.error("Could not modify: " + e.toString());

            ModifyResponseType resp = new ModifyResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ResponseType expirePassword(ExpirePasswordRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).expirePassword(reqType);
        } catch (Exception e) {
            log.error("Could not expire password: " + e.toString());

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ResetPasswordResponseType resetPassword(ResetPasswordRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).resetPassword(reqType);
        } catch (Exception e) {
            log.error("Could not reset password: " + e.toString());

            ResetPasswordResponseType resp = new ResetPasswordResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ResponseType setPassword(SetPasswordRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).setPassword(reqType);
        } catch (Exception e) {
            log.error("Could not set password: " + e.toString());

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ResponseType reconcileResource(@WebParam(name = "config", targetNamespace = "") ReconciliationConfig config) {
        ResponseType response = new ResponseType();
        response.setStatus(StatusCodeType.FAILURE);
        response.setError(ErrorCode.UNSUPPORTED_OPERATION);
        return response;
    }

    public ResponseType testConnection(ManagedSys managedSys) {
        try {
            return createConnector(managedSys).testConnection(managedSys);
        } catch (Exception e) {
            log.error("Could not test connection: " + e.toString());

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ResponseType suspend(SuspendRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).suspend(reqType);
        } catch (Exception e) {
            log.error("Error suspending: " + e.toString());

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ResponseType resume(ResumeRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).resume(reqType);
        } catch (Exception e) {
            log.error("Error resuming : " + e.toString());

            ResponseType resp = new ResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    public ValidatePasswordResponseType validatePassword(ValidatePasswordRequestType reqType) {
        String targetID = reqType.getPsoID().getTargetID();
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        try {
            return createConnector(managedSys).validatePassword(reqType);
        } catch (Exception e) {
            log.error("Error validating password: " + e.toString());

            ValidatePasswordResponseType resp = new ValidatePasswordResponseType();
            resp.setStatus(StatusCodeType.FAILURE);
            return resp;
        }
    }

    private ConnectorService createConnector(ManagedSys managedSys) throws ClassNotFoundException, IOException {
        String connectorPath = "/connector/" + managedSys.getName() + ".groovy";

        ScriptIntegration se = ScriptFactory.createModule(this.scriptEngine);
        Map<String, Object> bindingMap = new HashMap<String, Object>();
        bindingMap.put("managedSys", managedSys);
        return (ConnectorService) se.instantiateClass(bindingMap, connectorPath);
    }

    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public ManagedSystemObjectMatchDAO getManagedSysObjectMatchDao() {
        return managedSysObjectMatchDao;
    }

    public void setManagedSysObjectMatchDao(ManagedSystemObjectMatchDAO managedSysObjectMatchDao) {
        this.managedSysObjectMatchDao = managedSysObjectMatchDao;
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
}
