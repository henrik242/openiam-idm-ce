package org.openiam.idm.srvc.synch.ws;


import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Interface for  <code>GenericObjectSynchWebService</code>. All synchronization activities which are not related to users
 * will use this service.
 */
@WebService(targetNamespace = "http://www.openiam.org/service/synch", name = "GenericObjectSynchWebService")
public interface GenericObjectSynchWebService {

	@WebMethod
	SyncResponse startSynchronization(
            @WebParam(name = "config", targetNamespace = "")
            SynchConfig config);

}