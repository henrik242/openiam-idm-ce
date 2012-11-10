package org.openiam.idm.srvc.res.service;

import org.hibernate.SessionFactory;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.res.domain.ResourceTypeEntity;
import org.openiam.idm.srvc.res.dto.ResourceProp;

import java.util.List;

public interface ResourceDAO {

    /**
     * Gets the resource type dao.
     *
     * @return the resource type dao
     */
    ResourceTypeDAO getResourceTypeDao();

    /**
     * Sets the resource type dao.
     *
     * @param resourceTypeDao the new resource type dao
     */
    void setResourceTypeDao(ResourceTypeDAO resourceTypeDao);

    /**
     * Gets the resource prop dao.
     *
     * @return the resource prop dao
     */
    ResourcePropDAO getResourcePropDao();

    /**
     * Sets the resource prop dao.
     *
     * @param resourcePropDao the new resource prop dao
     */
    void setResourcePropDao(ResourcePropDAO resourcePropDao);

    /**
     * Gets the resource role dao.
     *
     * @return the resource role dao
     */
    ResourceRoleDAO getResourceRoleDao();

    /**
     * Sets the resource role dao.
     *
     * @param resourceRoleDao the new resource role dao
     */
    void setResourceRoleDao(ResourceRoleDAO resourceRoleDao);

    /**
     * Sets the session factory.
     *
     * @param session the new session factory
     */
    void setSessionFactory(SessionFactory session);

    /**
     * Persist.
     *
     * @param transientInstance the transient instance
     */
    void persist(ResourceEntity transientInstance);

    /**
     * Removes the.
     *
     * @param persistentInstance the persistent instance
     */
    void remove(ResourceEntity persistentInstance);

    /**
     * Update.
     *
     * @param detachedInstance the detached instance
     * @return the resource
     */
    ResourceEntity update(ResourceEntity detachedInstance);

    /**
     * Find by id.
     *
     * @param id the id
     * @return the resource
     */
    ResourceEntity findById(java.lang.String id);

    /**
     * Find by example.
     *
     * @param instance the instance
     * @return the list
     */
    List<ResourceEntity> findByExample(ResourceEntity instance);

    /**
     * Adds the.
     *
     * @param instance the instance
     * @return the resource
     */
    ResourceEntity add(ResourceEntity instance);

    /**
     * Find all resources.
     *
     * @return the list
     */
    List<ResourceEntity> findAllResources();

    /**
     * Find resources by Name.
     *
     * @return the list
     */
    List<ResourceEntity> findResourcesByName(String resourceName);

    /**
     * Find resources by name and then pick the first one
     *
     * @param resourceName
     * @return
     */
    ResourceEntity findResourceByName(String resourceName);

    /**
     * Removes the all resources.
     *
     * @return the int
     */
    int removeAllResources();

    /**
     * Find type of resource.
     *
     * @param resourceId the resource id
     * @return the resource type
     */
    ResourceTypeEntity findTypeOfResource(String resourceId);

    /**
     * Removes the properties by resource.
     *
     * @param resourceId the resource id
     * @return the int
     */
    int removePropertiesByResource(String resourceId);

    /**
     * Find resource properties.
     *
     * @param resourceId the resource id
     * @return the list
     */
    List<ResourcePropEntity> findResourceProperties(String resourceId);

    /**
     * Find resources which have a specified attribute
     *
     * @param propName
     * @param propValue
     * @return list of resources
     */
    List<ResourceEntity> findResourcesByProperty(String propName, String propValue);


    /**
     * Find a resource based on a list of unique properties
     *
     * @param propList
     * @return resource
     */
    ResourceEntity findResourceByProperties(List<ResourceProp> propList);


    /**
     * Gets the resources by type.
     *
     * @param resourceTypeId the resource type id
     * @return the resources by type
     */
    List<ResourceEntity> getResourcesByType(String resourceTypeId);


    /**
     * Gets the resources by category.
     *
     * @param categoryId the category id
     * @return the resources by category
     */
    List<ResourceEntity> getResourcesByCategory(String categoryId);

    /**
     * Gets the resources by branch.
     *
     * @param branchId the branch id
     * @return the resources by branch
     */
    List<ResourceEntity> getResourcesByBranch(String branchId);

    /**
     * Gets the child resources.
     *
     * @param resourceId the resource id
     * @return the child resources
     */
    List<ResourceEntity> getChildResources(String resourceId);

    /**
     * Gets the root resources.
     *
     * @return the root resources
     */
    List<ResourceEntity> getRootResources();

    /**
     * Removes the resources by type.
     *
     * @param resourceTypeId the resource type id
     * @return the int
     */
    int removeResourcesByType(String resourceTypeId);

    /**
     * Removes the resources by category.
     *
     * @param categoryId the category id
     * @return the int
     */
    int removeResourcesByCategory(String categoryId);

    /**
     * Removes the resources by branch.
     *
     * @param branchId the branch id
     * @return the int
     */
    int removeResourcesByBranch(String branchId);

    /**
     * Find resource roles by resource.
     *
     * @param resourceId the resource id
     * @return the list
     */
    List<ResourceRoleEntity> findResourceRolesByResource(String resourceId);

    /**
     * Find resources for role.
     *
     * @param domainId the domain id
     * @param roleId   the role id
     * @return the list
     */
    List<ResourceEntity> findResourcesForRole(String domainId, String roleId);

    /**
     * Find resources for roles.
     *
     * @param domainId   the domain id
     * @param roleIdList the role id list
     * @return the list
     */
    List<ResourceEntity> findResourcesForRoles(String domainId,
                                         List<String> roleIdList);

    /**
     * Adds the resource role privilege.
     *
     * @param resourceId  the resource id
     * @param roleId      the role id
     * @param privilegeId the privilege id
     */
    void addResourceRolePrivilege(String resourceId, String roleId,
                                  String privilegeId);

    /**
     * Removes the resource role privilege.
     *
     * @param resourceId  the resource id
     * @param roleId      the role id
     * @param privilegeId the privilege id
     */
    void removeResourceRolePrivilege(String resourceId, String roleId,
                                     String privilegeId);

    /**
     * Removes the resource role privileges.
     *
     * @param resourceId the resource id
     * @return the int
     */
    int removeResourceRolePrivileges(String resourceId);

    List<ResourceEntity> findResourcesForUserRole(String userId);

    List<ResourceEntity> getUserResourcesByType(String userId, String resourceTypeId);



}