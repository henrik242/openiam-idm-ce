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
package org.openiam.provision.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.mule.module.client.MuleClient;
import org.openiam.provision.dto.ProvisionUser;

import javax.jws.WebService;
import java.util.*;

/**
 * @author suneet
 */
@WebService(endpointInterface = "org.openiam.provision.service.AsynchUserProvisionService",
        targetNamespace = "http://www.openiam.org/service/provision",
        portName = "DefaultProvisionControllerServicePort",
        serviceName = "AsynchUserProvisionService")
public class AsynchUserProvisioningServiceImpl implements MuleContextAware, AsynchUserProvisionService {

    protected static final Log log = LogFactory.getLog(AsynchUserProvisioningServiceImpl.class);

    protected ProvisionService provisionService;
    MuleContext muleContext;

    static protected ResourceBundle res = ResourceBundle.getBundle("datasource");
    static String serviceHost = res.getString("openiam.service_base");
	static String serviceContext = res.getString("openiam.idm.ws.path");

    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#addUser(org.openiam.provision.dto.ProvisionUser)
      */
    public void addUser(ProvisionUser user) {
        log.debug("START PROVISIONING - ADD USER CALLED...................");

		try {

			Map<String,String> msgPropMap =  new HashMap<String,String>();
			msgPropMap.put("SERVICE_HOST", serviceHost);
			msgPropMap.put("SERVICE_CONTEXT", serviceContext);


			//Create the client with the context
			MuleClient client = new MuleClient(muleContext);
			client.sendAsync("vm://provisionServiceAddMessage", (ProvisionUser)user, msgPropMap);

		}catch(Exception e) {
			log.debug("EXCEPTION:AsynchIdentitySynchService");
			log.error(e);
			//e.printStackTrace();
		}
		log.debug("END PROVISIONING - ADD USER ---------------------");

    }


    /* (non-Javadoc)
      * @see org.openiam.provision.service.ProvisionService#modifyUser(org.openiam.provision.dto.ProvisionUser)
      */
    public void modifyUser(ProvisionUser user) {
            log.debug("START PROVISIONING - MODIFY USER CALLED...................");

            try {

                Map<String,String> msgPropMap =  new HashMap<String,String>();
                msgPropMap.put("SERVICE_HOST", serviceHost);
                msgPropMap.put("SERVICE_CONTEXT", serviceContext);


                //Create the client with the context
                MuleClient client = new MuleClient(muleContext);
                client.sendAsync("vm://provisionServiceModifyMessage", (ProvisionUser)user, msgPropMap);

            }catch(Exception e) {
                log.debug("EXCEPTION:AsynchIdentitySynchService");
                log.error(e);
                //e.printStackTrace();
            }
            log.debug("END PROVISIONING - MODIFY USER ---------------------");


    }



    public ProvisionService getProvisionService() {
        return provisionService;
    }

    public void setProvisionService(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    public void setMuleContext(MuleContext ctx) {
        log.debug("AsynchUserProvisioningServiceImpl - setMuleContext called.");
        muleContext = ctx;

    }


}
