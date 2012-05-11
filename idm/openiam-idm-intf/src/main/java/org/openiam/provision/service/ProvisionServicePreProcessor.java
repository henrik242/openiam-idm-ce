package org.openiam.provision.service;

import org.openiam.provision.dto.PasswordSync;
import org.openiam.provision.dto.ProvisionUser;

import java.util.Map;

/**
 * Interface which all pre-processor scripts used in the provisioning process should implement
 *
 * Add User:
 * The following objects are passed to addUser: ProvisionUser and BindingMap. The binding map contains the following keys:
 * matchParam
 * operation : ADD, UPDATE, DELTE
 * sysId : Managed SystemID
 * targetSystemAttributes : Attributes that will be sent to the target system
 * targetSystemIdentity : TargetSystemIdentity
 * lg : Identity
 * context: Spring - WebApplicationContext
 * userRole: Roles that a user belongs to
 * org: Organization
 * targetSystemIdentityStatus : Operation in the resource
 * password
 * user
 *
 * Delete User:
 * The following objects are passed to deleteUser: ProvisionUser and BindingMap. The binding map contains the following keys:
 * IDENTITY: Identity that is being deleted
 * RESOURCE: Resource for which you are deleting a user

 *
 */
public interface ProvisionServicePreProcessor {

    /**
     * Provides pre-processing capabilities for each resource that a user is being provisioned into
     *
     * @param user
     * @param bindingMap
     * @return
     */
    int addUser(ProvisionUser user, Map<String, Object> bindingMap);
    int modifyUser(ProvisionUser user, Map<String, Object> bindingMap);
    int deleteUser(ProvisionUser user, Map<String, Object> bindingMap);
    int setPassword(PasswordSync passwordSync,Map<String, Object> bindingMap);

}
