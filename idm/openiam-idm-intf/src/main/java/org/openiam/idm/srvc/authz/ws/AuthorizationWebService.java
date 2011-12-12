package org.openiam.idm.srvc.authz.ws;

import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.authz.dto.AuthzRequest;
import org.openiam.idm.srvc.authz.dto.AuthzResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Interface for  <code>IdmAuditLogDataService</code>. All audit logging activities 
 * persisted through this service.
 */
@WebService(targetNamespace = "urn:idm.openiam.org/srvc/authz/service", name = "AuthorizationWebService")
public interface AuthorizationWebService {



    @WebMethod
    AuthzResponse isAuthorized(
            @WebParam(name = "request", targetNamespace = "")
            AuthzRequest request);




}