package org.openiam.idm.srvc.res.dto;

// Generated Mar 8, 2009 12:54:32 PM by Hibernate Tools 3.2.2.GA

import org.openiam.base.BaseObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourceGroupEntity;
import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;

/**
 * Resources are items that need to be managed or protected. These can be both logic and physical in nature.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resource", propOrder = {
        "resourceType",
        "resourceId",
        "name",
        "description",
        "resourceParent",
        "branchId",
        "categoryId",
        "displayOrder",
        "nodeLevel",
        "sensitiveApp",
        "managedSysId",
        "URL",
        "resourceRoles",
        "resourceProps",
        "childResources",
        "resourceGroups",
        "entitlements",
        "resOwnerUserId",
        "resOwnerGroupId"
})
public class Resource extends BaseObject {

    private String resourceId;
    private ResourceType resourceType;
    private String name;
    private String description;
    private String resourceParent;
    private String branchId;
    private String categoryId;
    private Integer displayOrder;
    private Integer nodeLevel;
    private Integer sensitiveApp;
    private String managedSysId;
    private String URL;

    private String resOwnerUserId;
    private String resOwnerGroupId;


    private Set<ResourceRole> resourceRoles = new HashSet<ResourceRole>(0);

    private Set<ResourceProp> resourceProps = new HashSet<ResourceProp>(0); // defined as a Set in Hibernate map
    private Set<Resource> childResources = new HashSet<Resource>(0);


    private Set<ResourceGroup> resourceGroups = new HashSet<ResourceGroup>(0);

    private Set<ResourcePrivilege> entitlements = new HashSet<ResourcePrivilege>(0);


    public Resource() {
    }

    public Resource(String resourceId) {
        this.resourceId = resourceId;
    }

    public Resource(String resourceId, String managedSysId) {
        this.resourceId = resourceId;
        this.managedSysId = managedSysId;
    }

    public Resource(ResourceEntity entity) {
               this.resourceId = entity.getResourceId();
        this.resourceType = new ResourceType(entity.getResourceType());
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.branchId = entity.getBranchId();
        this.categoryId = entity.getCategoryId();
        this.displayOrder = entity.getDisplayOrder();
        this.nodeLevel = entity.getNodeLevel();
        this.sensitiveApp = entity.getSensitiveApp();
        this.managedSysId = entity.getManagedSysId();
        this.URL = entity.getURL();
        this.resOwnerUserId = entity.getResOwnerUserId();
        this.resOwnerGroupId = entity.getResOwnerGroupId();
        for (ResourceRoleEntity resourceRole : entity.getResourceRoles()) {
            this.resourceRoles.add(new ResourceRole(resourceRole));
        }
        for (ResourcePropEntity prop : entity.getResourceProps()) {
            this.resourceProps.add(new ResourceProp(prop));
        }
        for (ResourceGroupEntity group : entity.getResourceGroups()) {
            this.resourceGroups.add(new ResourceGroup(group));
        }
        for (ResourcePrivilegeEntity privilege : entity.getEntitlements()) {
            this.entitlements.add(new ResourcePrivilege(privilege));
        }
 /*       for (ResourceEntity child : entity.getChildResources()) {
            this.childResources.add(new Resource(child));
        }*/
    }

    public Resource(String resourceId, String name,
                    String resourceType) {
        super();
        this.resourceId = resourceId;
        this.name = name;
        this.resourceType = new ResourceType(resourceType);
    }

    public Resource(String resourceId, ResourceType resourceType, String name,
                    String description, String resourceParent, String branchId,
                    String categoryId, Integer displayOrder, Integer nodeLevel,
                    Integer sensitiveApp,
                    Set<ResourceRole> resourceRoles,
                    Set<ResourceUser> resourceUsers,
                    //Set<ResourcePolicy> resourcePolicies,
                    Set<ResourceProp> resourceProps) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.description = description;
        this.name = name;
        this.resourceParent = resourceParent;
        this.branchId = branchId;
        this.categoryId = categoryId;
        this.displayOrder = displayOrder;
        this.nodeLevel = nodeLevel;
        this.sensitiveApp = sensitiveApp;
        this.resourceRoles = resourceRoles;
        //this.resourceUsers = resourceUsers;
        //this.resourcePolicies = resourcePolicies;
        this.resourceProps = resourceProps;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceType getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceParent() {
        return this.resourceParent;
    }

    public void setResourceParent(String resourceParent) {
        this.resourceParent = resourceParent;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getNodeLevel() {
        return this.nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public Integer getSensitiveApp() {
        return this.sensitiveApp;
    }

    public void setSensitiveApp(Integer sensitiveApp) {
        this.sensitiveApp = sensitiveApp;
    }


    public Set<ResourceRole> getResourceRoles() {
        return this.resourceRoles;
    }

    public void setResourceRoles(Set<ResourceRole> resourceRoles) {
        this.resourceRoles = resourceRoles;
    }


    public Set<ResourceProp> getResourceProps() {
        return resourceProps;
    }

    public void setResourceProps(Set<ResourceProp> resourceProps) {
        this.resourceProps = resourceProps;
    }

    public ResourceProp getResourceProperty(String propName) {
        if (resourceProps == null) {
            return null;
        }
        for (ResourceProp prop : resourceProps) {
            if (prop.getName().equalsIgnoreCase(propName)) {
                return prop;
            }
        }
        return null;
    }

    public Set<Resource> getChildResources() {
        return childResources;
    }

    public void setChildResources(Set<Resource> childResources) {
        this.childResources = childResources;

    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceType=" + resourceType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resourceParent='" + resourceParent + '\'' +
                ", branchId='" + branchId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", displayOrder=" + displayOrder +
                ", nodeLevel=" + nodeLevel +
                ", sensitiveApp=" + sensitiveApp +
                ", managedSysId='" + managedSysId + '\'' +
                ", URL='" + URL + '\'' +
                ", resOwnerUserId='" + resOwnerUserId + '\'' +
                ", resOwnerGroupId='" + resOwnerGroupId + '\'' +
                ", resourceRoles=" + resourceRoles +
                ", resourceProps=" + resourceProps +
                ", childResources=" + childResources +
                ", resourceGroups=" + resourceGroups +
                ", entitlements=" + entitlements +
                '}';
    }

    public String getManagedSysId() {
        return managedSysId;
    }

    public void setManagedSysId(String managedSysId) {
        this.managedSysId = managedSysId;
    }


    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param uRL the uRL to set
     */
    public void setURL(String uRL) {
        URL = uRL;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Set<ResourceGroup> getResourceGroups() {
        return resourceGroups;
    }

    public void setResourceGroups(Set<ResourceGroup> resourceGroups) {
        this.resourceGroups = resourceGroups;
    }



    public String getResOwnerUserId() {
        return resOwnerUserId;
    }

    public void setResOwnerUserId(String resOwnerUserId) {
        this.resOwnerUserId = resOwnerUserId;
    }

    public String getResOwnerGroupId() {
        return resOwnerGroupId;
    }

    public void setResOwnerGroupId(String resOwnerGroupId) {
        this.resOwnerGroupId = resOwnerGroupId;
    }

    public Set<ResourcePrivilege> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(Set<ResourcePrivilege> entitlements) {
        this.entitlements = entitlements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;

        Resource resource = (Resource) o;

        if (URL != null ? !URL.equals(resource.URL) : resource.URL != null) return false;
        if (branchId != null ? !branchId.equals(resource.branchId) : resource.branchId != null) return false;
        if (categoryId != null ? !categoryId.equals(resource.categoryId) : resource.categoryId != null) return false;
        if (childResources != null ? !childResources.equals(resource.childResources) : resource.childResources != null)
            return false;
        if (description != null ? !description.equals(resource.description) : resource.description != null)
            return false;
        if (displayOrder != null ? !displayOrder.equals(resource.displayOrder) : resource.displayOrder != null)
            return false;
        if (entitlements != null ? !entitlements.equals(resource.entitlements) : resource.entitlements != null)
            return false;
        if (managedSysId != null ? !managedSysId.equals(resource.managedSysId) : resource.managedSysId != null)
            return false;
        if (name != null ? !name.equals(resource.name) : resource.name != null) return false;
        if (nodeLevel != null ? !nodeLevel.equals(resource.nodeLevel) : resource.nodeLevel != null) return false;
        if (resOwnerGroupId != null ? !resOwnerGroupId.equals(resource.resOwnerGroupId) : resource.resOwnerGroupId != null)
            return false;
        if (resOwnerUserId != null ? !resOwnerUserId.equals(resource.resOwnerUserId) : resource.resOwnerUserId != null)
            return false;
        if (resourceGroups != null ? !resourceGroups.equals(resource.resourceGroups) : resource.resourceGroups != null)
            return false;
        if (resourceId != null ? !resourceId.equals(resource.resourceId) : resource.resourceId != null) return false;
        if (resourceParent != null ? !resourceParent.equals(resource.resourceParent) : resource.resourceParent != null)
            return false;
        if (resourceProps != null ? !resourceProps.equals(resource.resourceProps) : resource.resourceProps != null)
            return false;
        if (resourceRoles != null ? !resourceRoles.equals(resource.resourceRoles) : resource.resourceRoles != null)
            return false;
        if (resourceType != null ? !resourceType.equals(resource.resourceType) : resource.resourceType != null)
            return false;
        if (sensitiveApp != null ? !sensitiveApp.equals(resource.sensitiveApp) : resource.sensitiveApp != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return resourceId != null ? resourceId.hashCode() : 0;
    }
}
