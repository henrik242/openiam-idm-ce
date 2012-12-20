package org.openiam.idm.srvc.res.service;

import java.util.*;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
//import org.openiam.idm.srvc.mngsys.service.AttributeMapDAO;
import org.openiam.dozer.converter.ResourceDozerConverter;

import org.openiam.dozer.converter.ResourcePrivilegeDozerConverter;
import org.openiam.dozer.converter.ResourcePropDozerConverter;
import org.openiam.dozer.converter.ResourceRoleDozerConverter;
import org.openiam.dozer.converter.ResourceTypeDozerConverter;

import org.openiam.dozer.converter.ResourceUserDozerConverter;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.domain.ResourceRoleEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.res.domain.ResourceTypeEntity;
import org.openiam.idm.srvc.res.domain.ResourceUserEntity;
import org.openiam.idm.srvc.res.dto.*;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.role.dto.Role;
import org.springframework.beans.factory.annotation.Autowired;

@WebService(endpointInterface = "org.openiam.idm.srvc.res.service.ResourceDataService", targetNamespace = "urn:idm.openiam.org/srvc/res/service", portName = "ResourceDataWebServicePort", serviceName = "ResourceDataWebService")
public class ResourceDataServiceImpl implements ResourceDataService {

    protected ResourceDAO resourceDao;
    protected ResourceTypeDAO resourceTypeDao;
    protected ResourcePropDAO resourcePropDao;
    protected ResourceRoleDAO resourceRoleDao;
    protected ResourceUserDAO resourceUserDao;
    protected ResourcePrivilegeDAO resourcePrivilegeDao;

    protected LoginDataService loginManager;
    protected UserDataService userManager;
    protected RoleDataService roleDataService;
    protected OrganizationDataService orgManager;

	@Autowired
	private ResourceDozerConverter resourceConverter;

	@Autowired
	private ResourceRoleDozerConverter resourceRoleConverter;

	@Autowired
	private ResourcePropDozerConverter resourcePropConverter;

	@Autowired
	private ResourceUserDozerConverter resourceUserConverter;

	@Autowired
	private ResourcePrivilegeDozerConverter resourcePrivilegeDozerConverter;

    @Autowired
	private ResourceTypeDozerConverter resourceTypeConverter;

    private static final Log log = LogFactory
            .getLog(ResourceDataServiceImpl.class);

    public ResourceDAO getResourceDao() {
        return resourceDao;
    }

    public void setResourceDao(ResourceDAO resourceDao) {
        this.resourceDao = resourceDao;
    }

    public ResourceTypeDAO getResourceTypeDao() {
        return resourceTypeDao;
    }

    public void setResourceTypeDao(ResourceTypeDAO resourceTypeDao) {
        this.resourceTypeDao = resourceTypeDao;
    }

    public ResourcePropDAO getResourcePropDao() {
        return resourcePropDao;
    }

    public void setResourcePropDao(ResourcePropDAO resourcePropDao) {
        this.resourcePropDao = resourcePropDao;
    }

    /**
     * Gets the resource role dao.
     *
     * @return the resource role dao
     */
    public ResourceRoleDAO getResourceRoleDao() {
        return resourceRoleDao;
    }

    /**
     * Sets the resource role dao.
     *
     * @param resourceRoleDao the new resource role dao
     */
    public void setResourceRoleDao(ResourceRoleDAO resourceRoleDao) {
        this.resourceRoleDao = resourceRoleDao;
    }

    //
    // /**
    // * Gets the resource user dao.
    // *
    // * @return the resource user dao
    // */
    // public ResourceUserDAO getResourceUserDao() {
    // return resourceUserDao;
    // }
    //
    // /**
    // * Sets the resource user dao.
    // *
    // * @param resourceUserDao the new resource user dao
    // */
    // public void setResourceUserDao(ResourceUserDAO resourceUserDao) {
    // this.resourceUserDao = resourceUserDao;
    // }

    // Resource ---------------------------------------

    /**
     * Add a new resource from a transient resource object and sets resourceId
     * in the returned object.
     *
     * @param resource
     * @return
     */
    public Resource addResource(Resource resource) {
        if (resource == null)
            throw new IllegalArgumentException("Resource object is null");
        ResourceEntity resourceEntity = resourceConverter.convertToEntity(resource, true);
        resourceEntity = resourceDao.add(resourceEntity);
        return resourceConverter.convertToDTO(resourceEntity, true);
    }

    /**
     * Add a new resource from a transient resource object. Sets resourceId and
     * associates resource with a resource type. Sets category and branch from
     * parent.
     *
     * @param resource
     * @param resourceTypeId
     * @return
     */
    // public Resource addChildResource(Resource resource, Resource
    // resourceParent) {
    // resource.setResourceParent(resourceParent);
    // resource.setResourceType(resourceParent.getResourceType());
    // resource.setCategoryId(resourceParent.getCategoryId());
    // resource.setBranchId(resourceParent.getBranchId());
    // return addResource(resource);
    // }

    /**
     * Add a new resource from a transient resource object. Sets resourceId and
     * associates resource with a resource type.
     *
     * @param resource
     * @param resourceTypeId
     * @return
     */
    // public Resource addTypedResource(Resource resource, String
    // resourceTypeId) {
    // ResourceType resourceType = this.getResourceType(resourceTypeId);
    // resource.setResourceType(resourceType);
    // return addResource(resource);
    // }

    /**
     * Find a resource.
     *
     * @param resourceId
     * @return resource
     */
    public Resource getResource(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");

        ResourceEntity resourceEntity = resourceDao.findById(resourceId);
        return resourceEntity != null ? resourceConverter.convertToDTO(resourceEntity, true) : null;
    }


    public Resource getResourceByName(String resourceName) {
        if (resourceName == null)
            throw new IllegalArgumentException("resourceName is null");

        ResourceEntity resourceEntity = resourceDao.findResourceByName(resourceName);
        return resourceConverter.convertToDTO(resourceEntity, true);
    }

    /**
     * Find resources by name
     *
     * @return list of resources
     */
    public List<Resource> getResourcesByName(String resourceName) {
        if (resourceName == null)
            throw new IllegalArgumentException("resourceName is null");

        List<ResourceEntity> resourceList = resourceDao.findResourcesByName(resourceName);

        return resourceConverter.convertToDTOList(resourceList, false);
    }


    /**
     * Find resources by example
     *
     * @return list of resources
     */
    public List<Resource> getResourcesByExample(Resource resource) {

        List<ResourceEntity> entities = resourceDao.findByExample(resourceConverter.convertToEntity(resource,true));

        return resourceConverter.convertToDTOList(entities, false);
    }


    /**
     * Find resources which have a specified property
     *
     * @param propName
     * @param propValue
     * @return
     */
    public List<Resource> getResourcesByProperty(String propName, String propValue) {
        if (propName == null)
            throw new IllegalArgumentException("propName is null");
        if (propValue == null)
            throw new IllegalArgumentException("propValue is null");

        List<ResourceEntity> entityList = resourceDao.findResourcesByProperty(propName, propValue);
        return resourceConverter.convertToDTOList(entityList, false);
    }

    /**
     * Find resource which has a specified set of unique properties
     *
     * @param propList
     * @return
     */
    public Resource getResourceByProperties(List<ResourceProp> propList) {
        if (propList == null) {
            throw new IllegalArgumentException("propList is null");
        }

        ResourceEntity resourceEntity = resourceDao.findResourceByProperties(propList);
        return resourceConverter.convertToDTO(resourceEntity, true);
    }


    /**
     * Update a resource.
     *
     * @param resource
     * @return
     */
    public Resource updateResource(Resource resource) {
        if (resource == null)
            throw new IllegalArgumentException("resource object is null");
        ResourceEntity entity = resourceDao.update(resourceConverter.convertToEntity(resource, true));
        return resourceConverter.convertToDTO(entity, true);
    }

    /**
     * Find all resources
     *
     * @return list of resources
     */
    public List<Resource> getAllResources() {
        List<ResourceEntity> resourceEntities = resourceDao.findAllResources();

        return resourceConverter.convertToDTOList(resourceEntities, false);
    }

    /**
     * Remove a resource
     *
     * @param resourceId
     */
    public void removeResource(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");
        ResourceEntity obj = this.resourceDao.findById(resourceId);
        this.resourceDao.remove(obj);
    }

    /**
     * Remove all resources
     */
    public int removeAllResources() {
        return this.resourceDao.removeAllResources();
    }

    // ResourceType -----------------------------------

    /**
     * Add a new resource type
     *
     * @param val
     * @return
     */
    public ResourceType addResourceType(ResourceType val) {
        if (val == null)
            throw new IllegalArgumentException("ResourcType is null");

        ResourceTypeEntity resourceType = resourceTypeDao.add(resourceTypeConverter.convertToEntity(val, true));
        return resourceTypeConverter.convertToDTO(resourceType, true);
    }

    /**
     * Find a resource type
     *
     * @param resourceTypeId
     * @return
     */
    public ResourceType getResourceType(String resourceTypeId) {
        if (resourceTypeId == null)
            throw new IllegalArgumentException("resourceTypeId is null");

        ResourceTypeEntity resourceTypeEntity = resourceTypeDao.findById(resourceTypeId);
        return resourceTypeConverter.convertToDTO(resourceTypeEntity, true);
    }

    /**
     * Update a resource type
     *
     * @param resourceType
     * @return
     */
    public ResourceType updateResourceType(ResourceType resourceType) {
        if (resourceType == null)
            throw new IllegalArgumentException("resourceType object is null");

        ResourceTypeEntity typeEntity = resourceTypeDao.update(resourceTypeConverter.convertToEntity(resourceType, true));
        return resourceTypeConverter.convertToDTO(typeEntity, true);
    }

    /**
     * Find all resource types
     *
     * @return
     */
    public List<ResourceType> getAllResourceTypes() {
        List<ResourceTypeEntity> resourceTypeList = resourceTypeDao
                .findAllResourceTypes();

        return resourceTypeConverter.convertToDTOList(resourceTypeList, false);
    }

    /**
     * Remove a resource type
     *
     * @param resourceTypeId
     */
    public void removeResourceType(String resourceTypeId) {
        if (resourceTypeId == null)
            throw new IllegalArgumentException("resourceTypeId is null");
        ResourceTypeEntity obj = this.resourceTypeDao.findById(resourceTypeId);
        this.resourceTypeDao.remove(obj);
    }

    /**
     * Remove all resource types
     *
     * @return
     */
    public int removeAllResourceTypes() {
        return this.resourceTypeDao.removeAllResourceTypes();
    }

    /**
     * Find type of a resource
     *
     * @param resourceId
     * @return
     */
    public ResourceType findTypeOfResource(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");

        ResourceTypeEntity entity = resourceDao.findTypeOfResource(resourceId);
        return resourceTypeConverter.convertToDTO(entity, true);
    }

    // /**
    // * Link metadata type to a resource
    // *
    // * @param resourceTypeId
    // * @param resourceId
    // */
    // public void linkTypeToResource(String resourceId, String resourceTypeId)
    // {
    // if (resourceTypeId == null)
    // throw new IllegalArgumentException("resourceTypeId is null");
    // if (resourceId == null)
    // throw new IllegalArgumentException("resourceId is null");
    // this.resourceDao.linkTypeToResource(resourceId, resourceTypeId);
    // }
    //
    // /**
    // * Unlink type from resource
    // *
    // * @param resourceId
    // */
    // public void unlinkTypeFromResource(String resourceId) {
    // if (resourceId == null)
    // throw new IllegalArgumentException("resourceId is null");
    //
    // this.resourceDao.unlinkTypeFromResource(resourceId);
    // }

    // ResourceProp ---------------------------------------

    /**
     * Add a resource property.
     *
     * @param resourceProp
     * @return
     */
    public ResourceProp addResourceProp(ResourceProp resourceProp) {
        if (resourceProp == null)
            throw new IllegalArgumentException("ResourceProp object is null");

        ResourcePropEntity resourcePropEntity = resourcePropDao.add(resourcePropConverter.convertToEntity(resourceProp, true));
        return resourcePropConverter.convertToDTO(resourcePropEntity, true);
    }

    // /**
    // * Add a new resource Property from a transient resource Property object.
    // * Sets resourcePropId and associates resourceProp with a resource.
    // *
    // * @param resourceProp
    // * @param resourceId
    // * @return
    // */
    // public ResourceProp addLinkedResourceProp(ResourceProp resourceProp,
    // String resourceId) {
    // if (resourceId == null)
    // throw new IllegalArgumentException("resourceId is null");
    // Resource resource = resourceDao.findById(resourceId);
    //
    // if (resource == null) {
    // log.error("Resource not found for resourceId =" + resourceId);
    // throw new ObjectNotFoundException();
    // }
    //
    // resourceProp.setResource(resource);
    // Set<ResourceProp> props = resource.getResourceProps();
    // if (props == null)
    // props = new HashSet<ResourceProp>();
    // props.add(resourceProp);
    // return resourcePropDao.add(resourceProp);
    // }

    /**
     * Find a resource property.
     *
     * @param resourcePropId
     * @return
     */
    public ResourceProp getResourceProp(String resourcePropId) {
        if (resourcePropId == null)
            throw new IllegalArgumentException("resourcePropId is null");

        return resourcePropConverter.convertToDTO(resourcePropDao.findById(resourcePropId), true);
    }

    /**
     * Update a resource property
     *
     * @param resourceProp
     */
    public ResourceProp updateResourceProp(ResourceProp resourceProp) {
        if (resourceProp == null)
            throw new IllegalArgumentException("resourceProp object is null");

        ResourcePropEntity propEntity = resourcePropDao.update(resourcePropConverter.convertToEntity(resourceProp, true));
        return resourcePropConverter.convertToDTO(propEntity, true);
    }

    /**
     * Find all resource properties
     *
     * @return
     */
    public List<ResourceProp> getAllResourceProps() {
        List<ResourcePropEntity> resourcePropList = resourcePropDao
                .findAllResourceProps();
        return resourcePropConverter.convertToDTOList(resourcePropList, false);
    }

    /**
     * Remove a resource property
     *
     * @param resourcePropId
     */
    public void removeResourceProp(String resourcePropId) {
        if (resourcePropId == null)
            throw new IllegalArgumentException("resourcePropId is null");
        ResourcePropEntity obj = this.resourcePropDao.findById(resourcePropId);
        this.resourcePropDao.remove(obj);
    }

    /**
     * Remove all resource properties
     *
     * @param
     */
    public int removeAllResourceProps() {
        return this.resourcePropDao.removeAllResourceProps();
    }

    /**
     * Remove properties with a specified resourceId
     *
     * @param resourceId
     * @return
     */
    public int removePropertiesByResource(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");

        return this.resourceDao.removePropertiesByResource(resourceId);
    }

    /**
     * Find resource properties
     *
     * @param resourceId
     * @return
     */
    public List<ResourceProp> findResourceProperties(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");

        List<ResourcePropEntity> entities = this.resourceDao.findResourceProperties(resourceId);

        return resourcePropConverter.convertToDTOList(entities, false);
    }

    // /**
    // * Add a property to a resource
    // *
    // * @param resourcePropId
    // * @param resourceId
    // */
    // public void linkPropertyToResource(String resourcePropId, String
    // resourceId) {
    // if (resourcePropId == null)
    // throw new IllegalArgumentException("resourcePropId is null");
    // if (resourceId == null)
    // throw new IllegalArgumentException("resourceId is null");
    //
    // this.resourceDao.linkPropertyToResource(resourcePropId, resourceId);
    // }
    //
    // /**
    // * Remove a property from a resource
    // *
    // * @param resourcePropId
    // * @param resourceId
    // */
    // public void unlinkPropertyFromResource(String resourcePropId,
    // String resourceId) {
    // if (resourcePropId == null)
    // throw new IllegalArgumentException("resourcePropId is null");
    // if (resourceId == null)
    // throw new IllegalArgumentException("resourceId is null");
    //
    // this.resourceDao.unlinkPropertyFromResource(resourcePropId, resourceId);
    // }
    //
    // /**
    // * Remove all properties from a resource
    // *
    // * @param resourceId
    // */
    // public void unlinkAllPropertiesFromResource(String resourceId) {
    // if (resourceId == null)
    // throw new IllegalArgumentException("resourceId is null");
    //
    // this.resourceDao.unlinkAllPropertiesFromResource(resourceId);
    //
    // }

    // ResourceParent ------------------------------------------------

    /**
     * Find resource children
     *
     * @param resourceId
     * @return
     */
    public List<Resource> getChildResources(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");
        List<ResourceEntity> resourceEntities = this.resourceDao.getChildResources(resourceId);

       return resourceConverter.convertToDTOList(resourceEntities, false);
    }

    // /**
    // * Set a parent resource
    // *
    // * @param parentResourceId
    // * @param childResourceId
    // */
    // public void linkResourceToParent(String childResourceId,
    // String parentResourceId) {
    // if (parentResourceId == null)
    // throw new IllegalArgumentException("parentResourceId is null");
    // if (childResourceId == null)
    // throw new IllegalArgumentException("childResourceId is null");
    // this.resourceDao
    // .linkResourceToParent(childResourceId, parentResourceId);
    // }
    //
    // /**
    // * Remove a resource parent relationship
    // *
    // * @param childResourceId
    // */
    // public void unlinkResourceFromParent(String childResourceId) {
    // if (childResourceId == null)
    // throw new IllegalArgumentException("childResourceId is null");
    // this.resourceDao.unlinkResourceFromParent(childResourceId);
    // }
    //
    // resource hierarchy methods
    // -------------------------------------------------

    /**
     * Find a resource and its descendants and return as nested List.
     *
     * @param resourceId the resource id
     * @return list of nested lists of resource objects
     */

    public List<Resource> getResourceTree(String resourceId) {

        List<ResourceEntity> resourceTree = new LinkedList<ResourceEntity>();
        resourceTree.add(resourceDao.findById(resourceId));

        List<ResourceEntity> resourceChildren = resourceDao.getChildResources(resourceId);
        resourceTree.addAll(resourceChildren);

        for (Iterator<ResourceEntity> it = resourceChildren.iterator(); it.hasNext(); ) {
            ResourceEntity r = it.next();
            List<ResourceEntity> descendents = getResourceTreeHelper(r.getResourceId());
            if (!((descendents == null) || (descendents.isEmpty()))) {
                r.setChildResources(new HashSet<ResourceEntity>(descendents));
            }
        }

        return resourceConverter.convertToDTOList(resourceTree, false);
    }

    /**
     * Recursive helper method to get nested resource descendants.
     *
     * @param resourceId the resource id
     * @return the resource tree helper
     */
    private List<ResourceEntity> getResourceTreeHelper(String resourceId) {

        List<ResourceEntity> descendents = resourceDao.getChildResources(resourceId);

        for (Iterator<ResourceEntity> it = descendents.iterator(); it.hasNext(); ) {
            ResourceEntity r = it.next();
            List<ResourceEntity> nextChildren = resourceDao.getChildResources(r
                    .getResourceId());
            if (!((nextChildren == null) || (nextChildren.isEmpty()))) {
                r.setChildResources(new HashSet<ResourceEntity>(nextChildren));
                getResourceTreeHelper(r.getResourceId());
            }
        }
        return descendents;
    }

    private String getResourceType(ResourceEntity r) {
        String rt = "";
        ResourceTypeEntity resType = r.getResourceType();
        if (resType != null)
            rt = resType.getResourceTypeId() + ":";
        return rt;
    }

    /**
     * Find a resource and its descendants and return as an xml tree.
     *
     * @param resourceId the resource id
     * @return xml string
     */
    public String getResourceTreeXML(String resourceId) {
        StringBuffer xml = new StringBuffer();

        ResourceEntity mainResource = resourceDao.findById(resourceId);

        xml.append("<Resource label='" + getResourceType(mainResource)
                + mainResource.getName() + "' resourceId='"
                + mainResource.getResourceId() + "'>");
        // xml.append("<ResourceId label='id'>" + mainResource.getResourceId() +
        // "</ResourceId>");
        // xml.append("<ResourceDescription label='desc'>" +
        // mainResource.getDescription()+ "</ResourceDescription>");
        // xml.append("<Resources label='child'>");

        // descendants:
        List<ResourceEntity> resourceTree = resourceDao.getChildResources(resourceId);
        for (Iterator<ResourceEntity> it = resourceTree.iterator(); it.hasNext(); ) {
            ResourceEntity r = (ResourceEntity) it.next();

            xml.append("<Resource label='" + getResourceType(r) + r.getName()
                    + "' resourceId='" + r.getResourceId() + "'>");
            // xml.append("<ResourceId label='id'>" + r.getResourceId() +
            // "</ResourceId>");
            // xml.append("<ResourceDescription label='desc'>" +
            // r.getDescription()+ "</ResourceDescription>");

            // xml.append(getResourceTreeXmlHelper(r.getResourceId(), xml));
            getResourceTreeXmlHelper(r.getResourceId(), xml);

            xml.append("</Resource>");
        }

        // xml.append("</Resources>");
        xml.append("</Resource>");

        return xml.toString();
    }

    /**
     * Recursive helper method to get nested resource descendants as xml.
     *
     * @param resourceId the resource id
     * @param xml        the xml
     * @return the resource tree xml helper
     */
    private StringBuffer getResourceTreeXmlHelper(String resourceId,
                                                  StringBuffer xml) {

        List<ResourceEntity> descendents = resourceDao.getChildResources(resourceId);

        // xml.append("<Resources label='child'>");
        for (Iterator<ResourceEntity> it = descendents.iterator(); it.hasNext(); ) {
            ResourceEntity r = (ResourceEntity) it.next();

            xml.append("<Resource label='" + getResourceType(r) + r.getName()
                    + "' resourceId='" + r.getResourceId() + "'>");
            // xml.append("<ResourceId label='id'>" + r.getResourceId() +
            // "</ResourceId>");
            // xml.append("<ResourceDescription label='desc'>" +
            // r.getDescription()+ "</ResourceDescription>");
            getResourceTreeXmlHelper(r.getResourceId(), xml);
            xml.append("</Resource>");
        }
        // xml.append("</Resources>");

        return xml;

    }

    /**
     * Find a resource and all its descendants and put them in a list.
     *
     * @param resourceId the resource id
     * @return resource list
     */

    public List<Resource> getResourceFamily(String resourceId) {

        List<ResourceEntity> resourceTree = new LinkedList<ResourceEntity>();
        resourceTree.add(resourceDao.findById(resourceId));

        List<ResourceEntity> resourceChildren = resourceDao
                .getChildResources(resourceId);
        resourceTree.addAll(resourceChildren);

        for (Iterator<ResourceEntity> it = resourceChildren.iterator(); it.hasNext(); ) {
            ResourceEntity r = it.next();
            List<ResourceEntity> descendents = new LinkedList<ResourceEntity>();
            resourceTree.addAll(getResourceFamilyHelper(r.getResourceId(),
                    descendents));
        }

        return resourceConverter.convertToDTOList(resourceTree, false);
    }

    /**
     * Recursive method to get resource descendants in a single non nested list.
     *
     * @param resourceId  the resource id
     * @param descendents the descendents
     * @return the resource family helper
     */
    private List<ResourceEntity> getResourceFamilyHelper(String resourceId,
                                                         List<ResourceEntity> descendents) {

        List<ResourceEntity> children = resourceDao.getChildResources(resourceId);
        for (ResourceEntity child : children) {
            descendents.add(child);
        }
        for (Iterator<ResourceEntity> it = children.iterator(); it.hasNext(); ) {
            ResourceEntity r = it.next();
            getResourceFamilyHelper(r.getResourceId(), descendents);
        }
        return descendents;
    }

    // Resource get methods
    // =====================================================

    /**
     * Find resources having a specified metadata type
     *
     * @param resourceTypeId
     * @return
     */
    public List<Resource> getResourcesByType(String resourceTypeId) {
        if (resourceTypeId == null)
            throw new IllegalArgumentException("resourceTypeId is null");
        List<ResourceEntity> entities = this.resourceDao.getResourcesByType(resourceTypeId);

        return resourceConverter.convertToDTOList(entities, false);
    }

    /**
     * Find root resources i.e. resources with null or blank value for parent
     *
     * @return
     */
    public List<Resource> getRootResources() {
        List<ResourceEntity> resourcesEntities = this.resourceDao.getRootResources();

        return resourceConverter.convertToDTOList(resourcesEntities, false);
    }

    /**
     * Find all resources for a specified category.
     *
     * @param categoryId
     * @return
     */
    public List<Resource> getResourcesByCategory(String categoryId) {
        if (categoryId == null)
            throw new IllegalArgumentException("categoryId is null");
        List<ResourceEntity> resourceEntity = this.resourceDao.getResourcesByCategory(categoryId);

        return resourceConverter.convertToDTOList(resourceEntity, false);
    }

    /**
     * Find all resources with a specified branch
     *
     * @param branchId
     * @return
     */
    public List<Resource> getResourcesByBranch(String branchId) {
        if (branchId == null)
            throw new IllegalArgumentException("branchId is null");
        List<ResourceEntity> entityList = this.resourceDao.getResourcesByBranch(branchId);

        return resourceConverter.convertToDTOList(entityList, false);
    }

    /**
     * Remove resources having a specified metadata type
     *
     * @param resourceTypeId
     * @return rows affected
     */
    public int removeResourcesByType(String resourceTypeId) {
        if (resourceTypeId == null)
            throw new IllegalArgumentException("resourceTypeId is null");
        return this.resourceDao.removeResourcesByType(resourceTypeId);
    }

    /**
     * Remove all resources for a specified category.
     *
     * @param categoryId
     * @return rows affected
     */
    public int removeResourcesByCategory(String categoryId) {
        if (categoryId == null)
            throw new IllegalArgumentException("categoryId is null");
        return this.resourceDao.removeResourcesByCategory(categoryId);
    }

    /**
     * Remove all resources with a specified branch
     *
     * @param branchId
     * @return rows affected
     */
    public int removeResourcesByBranch(String branchId) {
        if (branchId == null)
            throw new IllegalArgumentException("branchId is null");
        return this.resourceDao.removeResourcesByBranch(branchId);

    }

    // public List<Resource> getResourceNodeTree (String resourceId) {
    //
    // List<Resource> resourceList = new ArrayList();
    //
    // List<Resource> rootChildren =
    // resourceDao.getResourcesByParent(resourceId);
    //
    // if ( (rootChildren == null)||(rootChildren.isEmpty()) ) {
    // return resourceList;
    // }
    //
    // for (Iterator<Resource> it = rootChildren.iterator(); it.hasNext(); ) {
    // Resource r = (Resource) it.next();
    // resourceList.add(r);
    // getChildResourcesNodeLevel(r.getResourceId(), resourceList);
    // }
    //
    // return resourceList;
    //
    // }

    // public List<Resource> getChildResourcesNodeLevel(String resourceId,
    // List<Resource> resourceList) {
    //
    // Resource res = this.resourceDao.findById(resourceId);
    // int nodeLevel = res.getNodeLevel();
    // ++nodeLevel;
    //
    // List<Resource> childResources =
    // resourceDao.getResourcesByParent(resourceId);
    // if ( (childResources == null)||(childResources.isEmpty()) ) {
    // return resourceList;
    // }
    //
    // for (Iterator<Resource> it = childResources.iterator(); it.hasNext(); ) {
    // Resource r = (Resource)it.next();
    // r.setNodeLevel(nodeLevel);
    // resourceList.add(r);
    // getChildResourcesNodeLevel(r.getResourceId(), resourceList);
    // }
    //
    // return resourceList;
    //
    // }

    // ResourceRole ---------------------------------------

    /**
     * Add a resource role
     *
     * @param resourceRole
     * @return
     */
    public ResourceRole addResourceRole(ResourceRole resourceRole) {

        if (resourceRole == null)
            throw new IllegalArgumentException("ResourceRole object is null");

        ResourceRoleEntity roleEntity = resourceRoleDao.add(resourceRoleConverter.convertToEntity(resourceRole, true));
        return resourceRoleConverter.convertToDTO(roleEntity, true);
    }

    /**
     * Find resource role
     *
     * @param resourceRoleId
     * @return
     */
    public ResourceRole getResourceRole(ResourceRoleId resourceRoleId) {
        if (resourceRoleId == null)
            throw new IllegalArgumentException("resourceRoleId is null");

        ResourceRoleEntity resourceRoleEntity = resourceRoleDao.findById(new ResourceRoleEmbeddableId(resourceRoleId.getRoleId(), resourceRoleId.getResourceId(), resourceRoleId.getPrivilegeId(), resourceRoleId.getDomainId()));

        return resourceRoleConverter.convertToDTO(resourceRoleEntity, true);
    }

    public List<Role> getRolesForResource(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceRoleId is null");

        return resourceRoleDao.findRolesForResource(resourceId);
    }

    /**
     * Update resource role.
     *
     * @param resourceRole
     * @return
     */
    public ResourceRole updateResourceRole(ResourceRole resourceRole) {
        if (resourceRole == null)
            throw new IllegalArgumentException("resourceRole object is null");

        ResourceRoleEntity roleEntity = resourceRoleDao.update(resourceRoleConverter.convertToEntity(resourceRole, true));
        return resourceRoleConverter.convertToDTO(roleEntity, true);
    }

    /**
     * Find all resource roles
     *
     * @return
     */
    public List<ResourceRole> getAllResourceRoles() {
        List<ResourceRoleEntity> resourceRoleList = resourceRoleDao.findAllResourceRoles();
        return resourceRoleConverter.convertToDTOList(resourceRoleList, false);
    }

    /**
     * Remove resource role.
     *
     * @param resourceRoleId
     */
    public void removeResourceRole(ResourceRoleId resourceRoleId) {
        if (resourceRoleId == null)
            throw new IllegalArgumentException("resourceRoleId is null");
        ResourceRoleEntity obj = this.resourceRoleDao.findById(new ResourceRoleEmbeddableId(resourceRoleId.getRoleId(), resourceRoleId.getResourceId(), resourceRoleId.getPrivilegeId(), resourceRoleId.getDomainId()));
        this.resourceRoleDao.remove(obj);
    }

    /**
     * Remove all resource roles
     */
    public void removeAllResourceRoles() {
        this.resourceRoleDao.removeAllResourceRoles();
    }

    public List<ResourceRole> getResourceRolesByResource(String resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("resourceId is null");
        }
        List<ResourceRoleEntity> entities = resourceDao.findResourceRolesByResource(resourceId);
        return resourceRoleConverter.convertToDTOList(entities, false);
    }

    /**
     * Returns a list of Resource objects that are linked to a Role.
     *
     * @param domainId
     * @param roleId
     * @return
     */
    public List<Resource> getResourcesForRole(String domainId, String roleId) {
        if (domainId == null) {
            throw new IllegalArgumentException("domainId is null");
        }
        if (roleId == null) {
            throw new IllegalArgumentException("roleId is null");
        }
        List<ResourceEntity> entities = resourceDao.findResourcesForRole(domainId, roleId);

        return resourceConverter.convertToDTOList(entities, false);
    }

    /**
     * Returns a list of Resource objects that are linked to the list of Roles.
     *
     * @param domainId
     * @param roleIdList
     * @return
     */
    public List<Resource> getResourcesForRoles(String domainId,
                                               List<String> roleIdList) {
        if (domainId == null) {
            throw new IllegalArgumentException("domainId is null");
        }
        if (roleIdList == null) {
            throw new IllegalArgumentException("roleIdList is null");
        }
        List<ResourceEntity> resourceEntities = resourceDao.findResourcesForRoles(domainId, roleIdList);

        return resourceConverter.convertToDTOList(resourceEntities, false);
    }

    /**
     * Add a resource role privilege
     *
     * @param resourceId  the resource id
     * @param roleId      the role id
     * @param privilegeId the privilege id
     */
    public void addResourceRolePrivilege(String resourceId, String roleId,
                                         String privilegeId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (privilegeId == null)
            throw new IllegalArgumentException("privilegeId is null");

        this.resourceDao.addResourceRolePrivilege(resourceId, roleId,
                privilegeId);
    }

    /**
     * Removes the resource role privilege.
     *
     * @param resourceId  the resource id
     * @param roleId      the role id
     * @param privilegeId the privilege id
     */
    void removeResourceRolePrivilege(String resourceId, String roleId,
                                     String privilegeId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (privilegeId == null)
            throw new IllegalArgumentException("privilegeId is null");

        this.resourceDao.removeResourceRolePrivilege(resourceId, roleId,
                privilegeId);
    }

    /**
     * Removes the all role privileges from resource.
     *
     * @param resourceId the resource id
     */
    void removeResourceRolePrivileges(String resourceId) {
        if (resourceId == null)
            throw new IllegalArgumentException("resourceId is null");

        this.resourceDao.removeResourceRolePrivileges(resourceId);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.openiam.idm.srvc.res.service.ResourceDataService#addUserToResource
      * (org.openiam.idm.srvc.res.dto.ResourceUser)
      */
    public ResourceUser addUserToResource(ResourceUser resourceUser) {
        if (resourceUser == null) {
            throw new IllegalArgumentException("ResourceUser object is null");
        }
        ResourceUserEntity entity = resourceUserDao.add(resourceUserConverter.convertToEntity(resourceUser, true));
        return resourceUserConverter.convertToDTO(entity, true);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.openiam.idm.srvc.res.service.ResourceDataService#getUserResources
      * (java.lang.String)
      */
    public List<ResourceUser> getUserResources(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId object is null");
        }
        List<ResourceUserEntity> entities = resourceUserDao.findAllResourceForUsers(userId);

        return resourceUserConverter.convertToDTOList(entities, true);
    }

    public List<Resource> getResourceObjForUser(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId object is null");
        }

        List<ResourceEntity> resourceEntities = resourceDao.findResourcesForUserRole(userId);

        return resourceConverter.convertToDTOList(resourceEntities, true);
    }


    /*
      * (non-Javadoc)
      *
      * @seeorg.openiam.idm.srvc.res.service.ResourceDataService#
      * removeUserFromAllResources(java.lang.String)
      */
    public void removeUserFromAllResources(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId object is null");
        }
        resourceUserDao.removeUserFromAllResources(userId);
    }

    public ResourceUserDAO getResourceUserDao() {
        return resourceUserDao;
    }

    public void setResourceUserDao(ResourceUserDAO resourceUserDao) {
        this.resourceUserDao = resourceUserDao;
    }

    public boolean isUserAuthorized(String userId, String resourceId) {
        log.info("isUserAuthorized called.");
        List<ResourceUser> resList = getUserResources(userId);
        log.info("- resList= " + resList);
        if (resList == null) {
            log.info("resource list for user is null");
            return false;
        }
        for (ResourceUser ru : resList) {
            log.info("resource id = " + ru.getId().getResourceId());
            if (ru.getId().getResourceId().equalsIgnoreCase(resourceId)) {
                return true;
            }
        }
        return false;

    }


    public boolean isUserAuthorizedByProperty(String userId, String propertyName, String propertyValue) {
        log.info("isUserAuthorized called.");

        if (propertyName == null || propertyName.length() == 0) {
            return false;
        }
        if (propertyValue == null || propertyValue.length() == 0) {
            return false;
        }

        List<Resource> resList = getResourceObjForUser(userId);

        log.info("- resList= " + resList);
        if (resList == null) {
            log.info("resource list for user is null");
            return false;
        }

        log.info("Starting to loop through resource to do authorization checks");

        for (Resource res : resList) {

            Resource r =  getResource(res.getResourceId());

            log.info("Resource with properties: " + r.toString());

            ResourceProp prop = r.getResourceProperty(propertyName);

            log.info("Resource property: " + prop);

            if (prop != null) {
                String val = prop.getPropValue();
                if (val != null && val.length() > 0) {
                    val = val.toLowerCase();
                    propertyValue = propertyValue.toLowerCase();

                    if (propertyValue.contains(val)) {
                        return true;
                    }

                }
            }
        }

        return false;

    }

    public boolean isRoleAuthorized(String domainId, String roleId,
                                    String resourceId) {
        log.info("isUserAuthorized called.");

        List<Resource> resList = this.getResourcesForRole(domainId, roleId);
        log.info("- resList= " + resList);
        if (resList == null) {
            log.info("resource list for user is null");
            return false;
        }
        for (Resource r : resList) {
            log.info("resource id = " + r.getResourceId());
            if (r.getResourceId().equalsIgnoreCase(resourceId)) {
                return true;
            }
        }
        return false;
    }


    /* Temp hack ---------------------  -------------------------*/

    public String attributeString(String domainId, String principal) {

        List<String> oidList = new ArrayList<String>();
        String permitOverRide;
        String orgName = null;
        Organization org = null;

        Login principalLg = loginManager.getLoginByManagedSys(domainId, principal, "0");

        if (principalLg == null) {
            return null;
        }

        User usr = userManager.getUserWithDependent(principalLg.getUserId(), true);
        List<Role> roleList = roleDataService.getUserRoles(principalLg.getUserId());


        if (usr.getCompanyId() != null && usr.getCompanyId().length() > 0) {

            org = orgManager.getOrganization(usr.getCompanyId());
            if (org != null && org.getOrganizationName() != null) {
                orgName = org.getOrganizationName();
                // oid = org.getAlias();
                //  addOid(oidList, org.getAlias());
            } else {
                orgName = "NA";
            }
        }

        UserAttribute attrPermitOverride = usr.getAttribute("permit-override");
        if (attrPermitOverride == null) {
            permitOverRide = "N";
        } else {
            if (attrPermitOverride.getValue() == null || !attrPermitOverride.getValue().equalsIgnoreCase("Y")) {
                permitOverRide = "N";
            } else {
                permitOverRide = "Y";
            }

        }
        List<Organization> affiliationList = null;

        if (roleContains("EMERGENCY_ROLE", roleList)) {

            log.info("Emergency role found");


            affiliationList = orgManager.getAllOrganizations();
        } else {

            affiliationList = orgManager.getOrganizationsForUser(principalLg.getUserId());
        }

        if (affiliationList != null && affiliationList.size() > 0) {
            Set<Organization> orgSet = new TreeSet<Organization>(affiliationList);
            orgSet.add(org);

            for (Organization o : orgSet) {
                //for (Organization o : affiliationList) {
                if (o.getAlias() != null && !o.getAlias().isEmpty()) {

                    addOid(oidList, o.getAlias());
                }

            }
        } else {
            if (org != null && org.getAlias() != null) {
                addOid(oidList, org.getAlias());
            }

        }

        // sort objects
        // role
        Set<Role> roleSet = new TreeSet<Role>(roleList);

        // oidList


        String roleStr = null;

        if (roleList != null && !roleList.isEmpty()) {

            for (Role r : roleSet) {
                //for ( Role r : roleList) {
                if (roleStr == null) {
                    roleStr = r.getId().getRoleId();
                } else {
                    roleStr = roleStr + "," + r.getId().getRoleId();
                }

            }
        }


        StringBuffer headerString = new StringBuffer();

        if (usr.getFirstName() != null && usr.getFirstName().length() > 0) {
            headerString.append("firstname=" + usr.getFirstName());
        } else {
            headerString.append("&firstname=NA");
        }
        if (usr.getLastName() != null && usr.getLastName().length() > 0) {
            headerString.append("&secondname=" + usr.getLastName());
        } else {
            headerString.append("&secondname=NA");
        }
        headerString.append("&fullname=" + usr.getFirstName() + " " + usr.getLastName());
        if (roleStr != null && roleStr.length() > 0) {
            headerString.append("&role=" + roleStr);
        } else {
            headerString.append("&role=NO_ROLE");
        }
        headerString.append("&organization=" + orgName);
        headerString.append("&organizationoid=" + getOidString(oidList));

        headerString.append("&permit=" + permitOverRide);

        return headerString.toString();
    }

    public LoginDataService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public UserDataService getUserManager() {
        return userManager;
    }

    public void setUserManager(UserDataService userManager) {
        this.userManager = userManager;
    }

    public RoleDataService getRoleDataService() {
        return roleDataService;
    }

    public void setRoleDataService(RoleDataService roleDataService) {
        this.roleDataService = roleDataService;
    }

    private void addOid(List<String> oidList, String newOid) {

        for (String oid : oidList) {
            if (oid.equalsIgnoreCase(newOid)) {
                // found - its already in the list
                return;
            }

        }
        oidList.add(newOid);
    }

    private String getOidString(List<String> oidList) {
        StringBuffer oid = new StringBuffer();

        int ctr = 0;
        for (String o : oidList) {

            if (ctr == 0) {
                oid.append(o);
            } else {
                if (o != null && !o.isEmpty())
                    oid.append("," + o);
            }
            ctr++;


        }
        if (oidList.isEmpty()) {
            return "NA";
        }
        return oid.toString();

    }


    private boolean roleContains(String roleId, List<Role> roleList) {

        if (roleList == null || roleList.isEmpty()) {
            return false;
        }


        for (Role r : roleList) {
            if (r != null) {
                log.info("Checking Role name " + r);

                if (r.getId().getRoleId().equalsIgnoreCase(roleId)) {
                    return true;
                }
            }

        }
        return false;

    }

    @Override
    public ResourcePrivilege addResourcePrivilege(ResourcePrivilege resourcePrivilege) {
        if (resourcePrivilege == null) {
            throw new IllegalArgumentException("ResourcePrivilege object is null");
        }
        ResourcePrivilegeEntity privilegeEntity = resourcePrivilegeDao.add(resourcePrivilegeDozerConverter.convertToEntity(resourcePrivilege, true));
        return resourcePrivilegeDozerConverter.convertToDTO(privilegeEntity, true);

    }

    @Override
    public void removeResourcePrivilege(String resourcePrivilegeId) {
        if (resourcePrivilegeId == null) {
            throw new IllegalArgumentException("resourcePrivilegeId object is null");
        }
        resourcePrivilegeDao.remove(new ResourcePrivilegeEntity(resourcePrivilegeId));
    }

    @Override
    public ResourcePrivilege updateResourcePrivilege(ResourcePrivilege resourcePrivilege) {
        if (resourcePrivilege == null) {
            throw new IllegalArgumentException("ResourcePrivilege object is null");
        }
        ResourcePrivilegeEntity privilegeEntity = resourcePrivilegeDao.update(resourcePrivilegeDozerConverter.convertToEntity(resourcePrivilege, true));

        return resourcePrivilegeDozerConverter.convertToDTO(privilegeEntity, true);
    }

    @Override
    public List<ResourcePrivilege> getPrivilegesByResourceId(String resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("resourceId object is null");
        }
        List<ResourcePrivilegeEntity> privilegeEntities = resourcePrivilegeDao.findPrivilegesByResourceId(resourceId);

        return resourcePrivilegeDozerConverter.convertToDTOList(privilegeEntities, false);
    }

    @Override
    public List<ResourcePrivilege> getPrivilegesByEntitlementType(String resourceId, String type) {
        if (resourceId == null) {
            throw new IllegalArgumentException("resourceId object is null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type object is null");
        }
        List<ResourcePrivilegeEntity> privilegeEntities = resourcePrivilegeDao.findPrivilegesByEntitlementType(resourceId, type);

        return resourcePrivilegeDozerConverter.convertToDTOList(privilegeEntities, false);
    }

    public OrganizationDataService getOrgManager() {
        return orgManager;
    }

    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }

    public ResourcePrivilegeDAO getResourcePrivilegeDao() {
        return resourcePrivilegeDao;
    }

    public void setResourcePrivilegeDao(ResourcePrivilegeDAO resourcePrivilegeDao) {
        this.resourcePrivilegeDao = resourcePrivilegeDao;
    }

    public List<Resource> getUserResourcesByType(String userId, String resourceTypeId) {

        if (userId == null) {
            throw new IllegalArgumentException("userId  is null");
        }
        if (resourceTypeId == null) {
            throw new IllegalArgumentException("resourceTypeId object is null");
        }

        List<ResourceEntity> resourceEntities = resourceDao.getUserResourcesByType(userId, resourceTypeId);

        return resourceConverter.convertToDTOList(resourceEntities, false);
    }

}




