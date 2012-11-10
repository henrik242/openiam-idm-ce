package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourceRoleEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.role.dto.Role;

import java.util.List;

public interface ResourceRoleDAO {

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#remove(org.openiam.idm.srvc.res.dto.ResourceRole)
      */
    void remove(ResourceRoleEntity persistentInstance);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#update(org.openiam.idm.srvc.res.dto.ResourceRole)
      */
    ResourceRoleEntity update(ResourceRoleEntity detachedInstance);

    ResourceRoleEntity findById(ResourceRoleEmbeddableId id);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findByExample(org.openiam.idm.srvc.res.dto.ResourceRole)
      */
    List<ResourceRoleEntity> findByExample(ResourceRoleEntity instance);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#add(org.openiam.idm.srvc.res.dto.ResourceRole)
      */
    ResourceRoleEntity add(ResourceRoleEntity instance);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findAllResourceRoles()
      */
    List<ResourceRoleEntity> findAllResourceRoles();

    List<ResourceRoleEntity> findResourcesForRole(String domainId, String roleId);

    void removeResourceRole(String domainId, String roleId);

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#removeAllResourceRoles()
      */
    void removeAllResourceRoles();

    public List<Role> findRolesForResource(String resourceId);

}