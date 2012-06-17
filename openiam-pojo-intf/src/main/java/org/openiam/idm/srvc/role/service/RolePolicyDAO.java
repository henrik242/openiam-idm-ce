package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.dto.RolePolicy;

import java.util.List;

/**
 * Data access interface for domain model class RolePolicy.
 *
 * @see org.openiam.idm.srvc.role.dto.RolePolicy
 */
public interface RolePolicyDAO {

    public void add(RolePolicy transientInstance);

    public void remove(RolePolicy persistentInstance);

    public RolePolicy findById(java.lang.String id);

    public RolePolicy update(RolePolicy detachedInstance);


    public List<RolePolicy> findRolePolicies(String serviceId, String roleId);

}