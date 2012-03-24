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
package org.openiam.idm.srvc.synch.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.msg.ws.SysMessageResponse;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.service.generic.GenericObjectSynchService;

import javax.jws.WebService;
import java.util.List;


/**
 * @author suneet
 *
 */
@WebService(endpointInterface = "org.openiam.idm.srvc.synch.ws.GenericObjectSynchWebService",
		targetNamespace = "http://www.openiam.org/service/synch", 
		portName = "GenericObjectSynchWebServicePort",
		serviceName = "GenericObjectSynchWebService")
public class GenericObjectSynchWebServiceImpl implements GenericObjectSynchWebService, MuleContextAware {

	protected GenericObjectSynchService synchService;
	protected static final Log log = LogFactory.getLog(GenericObjectSynchWebServiceImpl.class);
    protected MuleContext muleContext;


    public SyncResponse startSynchronization(SynchConfig config) {
        synchService.setMuleContext(muleContext);
        return synchService.startSynchronization(config);
    }


     public void setMuleContext(MuleContext ctx) {

		muleContext = ctx;

	}


    public GenericObjectSynchService getSynchService() {
        return synchService;
    }

    public void setSynchService(GenericObjectSynchService synchService) {
        this.synchService = synchService;
    }
}
