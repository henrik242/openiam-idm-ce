package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.domain.RoleAttributeEntity;

import java.util.List;

/**
 * Data access interface for domain model class RoleAttribute.
 *
 * @see org.openiam.idm.srvc.role.dto.RoleAttribute
 */
public interface RoleAttributeDAO {

    public void add(RoleAttributeEntity transientInstance);

    public void remove(RoleAttributeEntity persistentInstance);

    public RoleAttributeEntity findById(java.lang.String id);

    public RoleAttributeEntity update(RoleAttributeEntity detachedInstance);

    public List<RoleAttributeEntity> findByExample(RoleAttributeEntity instance);

    public void deleteRoleAttributes(String serviceId, String roleId);

}