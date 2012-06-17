package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.dto.RoleAttribute;

import java.util.List;

/**
 * Data access interface for domain model class RoleAttribute.
 *
 * @see org.openiam.idm.srvc.role.dto.RoleAttribute
 */
public interface RoleAttributeDAO {

    public void add(RoleAttribute transientInstance);

    public void remove(RoleAttribute persistentInstance);

    public RoleAttribute findById(java.lang.String id);

    public RoleAttribute update(RoleAttribute detachedInstance);

    public List<RoleAttribute> findByExample(RoleAttribute instance);

    public void deleteRoleAttributes(String serviceId, String roleId);

}