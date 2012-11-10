package org.openiam.idm.srvc.res.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.res.dto.ResourcePrivilege;

@Entity
@Table(name = "RESOURCE_PRIVILEGE")
public class ResourcePrivilegeEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "RESOURCE_PRIVLEGE_ID", length = 32)
    private String resourcePrivilegeId;

    @Column(name = "RESOURCE_ID", length = 32)
    private String resourceId;

    @Column(name = "PRIVILEGE_NAME", length = 32)
    private String name;

    @Column(name = "DESCRIPTION", length = 40)
    private String description;

    @Column(name = "PRIVILEGE_TYPE", length = 20)
    private String privilegeType;

    public ResourcePrivilegeEntity() {
    }

    public ResourcePrivilegeEntity(ResourcePrivilege privilege) {
        this.resourcePrivilegeId = privilege.getResourcePrivilegeId();
        this.resourceId = privilege.getResourceId();
        this.name = privilege.getName();
        this.description = privilege.getDescription();
        this.privilegeType = privilege.getPrivilegeType();
    }

    public ResourcePrivilegeEntity(String resourcePrivilegeId) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourcePrivilegeEntity that = (ResourcePrivilegeEntity) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (privilegeType != null ? !privilegeType.equals(that.privilegeType) : that.privilegeType != null)
            return false;
        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) return false;
        if (resourcePrivilegeId != null ? !resourcePrivilegeId.equals(that.resourcePrivilegeId) : that.resourcePrivilegeId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resourcePrivilegeId != null ? resourcePrivilegeId.hashCode() : 0;
        result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (privilegeType != null ? privilegeType.hashCode() : 0);
        return result;
    }
}
