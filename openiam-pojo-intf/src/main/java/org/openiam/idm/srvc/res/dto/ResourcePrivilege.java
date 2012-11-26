package org.openiam.idm.srvc.res.dto;

import org.openiam.base.BaseObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;

/**
 * Object to capture the entitlements that are linked to a resource.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourcePrivilege", propOrder = {
        "resourcePrivilegeId",
        "resourceId",
        "name",
        "description",
        "privilegeType"
})
@DozerDTOCorrespondence(ResourcePrivilegeEntity.class)
public class ResourcePrivilege extends BaseObject {

    private static final long serialVersionUID = -5086914281577711436L;
    private String resourcePrivilegeId;
    private String resourceId;
    private String name;
    private String description;
    private String privilegeType;

    public ResourcePrivilege() {

    }

    public ResourcePrivilege(String resourcePrivilegeId) {

        this.resourcePrivilegeId = resourcePrivilegeId;
    }

    public String getResourcePrivilegeId() {
        return resourcePrivilegeId;
    }

    public void setResourcePrivilegeId(String resourcePrivilegeId) {
        this.resourcePrivilegeId = resourcePrivilegeId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(String privilegeType) {
        this.privilegeType = privilegeType;
    }

    @Override
    public String toString() {
        return "ResourcePrivilege{" +
                "resourcePrivilegeId='" + resourcePrivilegeId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", privilegeType='" + privilegeType + '\'' +
                '}';
    }
}

