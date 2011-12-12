package org.openiam.idm.srvc.audit.ws;

import org.openiam.idm.srvc.audit.dto.IdmAuditLog;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Interface for  <code>PublishAuditEventWebService</code>. This service allows you to export/publish audit events to
 * an external source
 */
@WebService(targetNamespace = "urn:idm.openiam.org/srvc/audit/service", name = "PublishAuditEventService")
public interface PublishAuditEventWebService {

	/**
	 * Creates a new audit log entry. The returned object contains the 
	 * @param log
	 * @return
	 */
    @WebMethod
	void publishEvent(
             @WebParam(name = "log", targetNamespace = "")
             IdmAuditLog log);


      @WebMethod
    boolean isAlive();


}

