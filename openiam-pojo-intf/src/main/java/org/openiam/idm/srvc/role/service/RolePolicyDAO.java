package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.domain.RolePolicyEntity;

import java.util.List;

/**
 * Data access interface for domain model class RolePolicy.
 *
 * @see org.openiam.idm.srvc.role.dto.RolePolicy
 */
public interface RolePolicyDAO {

    public void add(RolePolicyEntity transientInstance);

    public void remove(RolePolicyEntity persistentInstance);

    public RolePolicyEntity findById(java.lang.String id);

    public RolePolicyEntity update(RolePolicyEntity detachedInstance);

    public List<RolePolicyEntity> findRolePolicies(String serviceId, String roleId);

}