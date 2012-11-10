package org.openiam.idm.srvc.synch.ws;




import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;

/**
 * Interface for  <code>IdmAuditLogDataService</code>. All audit logging activities 
 * persisted through this service.
 */
@WebService(targetNamespace = "http://www.openiam.org/service/synch", name = "IdentitySynchWebService")
public interface IdentitySynchWebService {
	
	@WebMethod
	SynchConfigListResponse getAllConfig();
	
	@WebMethod
	SynchConfigResponse findById(
			@WebParam(name = "id", targetNamespace = "")
			java.lang.String id);
	
	@WebMethod
	SynchConfigResponse addConfig(
			@WebParam(name = "synchConfig", targetNamespace = "")
			SynchConfig synchConfig);

    @WebMethod
    Response testConnection(
            @WebParam(name = "synchConfig", targetNamespace = "")
            SynchConfig synchConfig);

    /**
     * Moves a set of users from resource or role. Users are selected based on some search criteria defined in the
     * config object.
     * @param config
     * @return
     */
    @WebMethod
    Response bulkUserMigration(
            @WebParam(name = "config", targetNamespace = "")
            BulkMigrationConfig config);


    /**
     * Tests the search criteria to determine how many users will be impacted by the change
     * @param config
     * @return
     */
    Response testBulkMigrationImpact(BulkMigrationConfig config) ;

    /**
     * When resources associated with a role have been modified, the role membership needs to be resynchronized
     * @param roleId
     * @return
     */
    @WebMethod
    Response resynchRole(
            @WebParam(name = "roleId", targetNamespace = "")
            RoleId roleId);

	@WebMethod
	SynchConfigResponse updateConfig(
			@WebParam(name = "synchConfig", targetNamespace = "")
			SynchConfig synchConfig);

	@WebMethod
	Response removeConfig(
			@WebParam(name = "config", targetNamespace = "")
			String configId);

	@WebMethod
	SyncResponse startSynchronization(
			@WebParam(name = "config", targetNamespace = "")
			SynchConfig config);

}