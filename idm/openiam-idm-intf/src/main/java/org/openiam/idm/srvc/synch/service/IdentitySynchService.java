package org.openiam.idm.srvc.synch.service;

import org.mule.api.MuleContext;
import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.*;

/**
 * Interface for <code>IdentitySynchService</code>. All synchronization
 * activities are managed through this service.
 */
public interface IdentitySynchService {

	List<SynchConfig> getAllConfig();

	SynchConfig findById(java.lang.String id);

	SynchConfig addConfig(SynchConfig synchConfig);

	SynchConfig updateConfig(SynchConfig synchConfig);

	void removeConfig(String configId);

    /**
     * Starts the synchronization process from a source.
     * @param config
     * @return
     */
	SyncResponse startSynchronization(SynchConfig config);

    /**
     * Tests if the connectivity information for our source system is correct.
     * @param config
     * @return
     */
    Response testConnection(SynchConfig config);

    /**
     * Moves a set of users from resource or role. Users are selected based on some search criteria defined in the
     * config object.
     * @param config
     * @return
     */
    Response bulkUserMigration(BulkMigrationConfig config);

    /**
     * When resources associated with a role have been modified, the role membership needs to be resynchronized
     * @param roleId
     * @return
     */
    Response resynchRole(RoleId roleId);


    public void setMuleContext(MuleContext ctx);

}