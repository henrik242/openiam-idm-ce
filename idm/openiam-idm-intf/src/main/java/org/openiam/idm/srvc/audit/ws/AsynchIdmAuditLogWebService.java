package org.openiam.idm.srvc.audit.ws;

import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.dto.SearchAudit;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Interface for  <code>IdmAuditLogDataService</code>. All audit logging activities 
 * persisted through this service.
 */
@WebService(targetNamespace = "urn:idm.openiam.org/srvc/audit/service", name = "AsynchAuditDataService")
public interface AsynchIdmAuditLogWebService {

	/**
	 * Creates a new audit log entry. The returned object contains the 
	 * @param log
	 * @return
	 */
    @WebMethod
	void createLog(
            @WebParam(name = "log", targetNamespace = "")
            IdmAuditLog log);



    /**
     * Submit a List of log objects that are linked together. the first audit event in the list will be considered the parent message
     * @param logList
     */
     @WebMethod
    void createLinkedLogs(
             @WebParam(name = "logList", targetNamespace = "")
             List<IdmAuditLog> logList);



}