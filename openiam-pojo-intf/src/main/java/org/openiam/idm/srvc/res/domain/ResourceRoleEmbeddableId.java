package org.openiam.idm.srvc.res.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.res.dto.ResourceRoleId;

@Embeddable
@DozerDTOCorrespondence(ResourceRoleId.class)
public class ResourceRoleEmbeddableId implements Serializable {

    @Column(name = "ROLE_ID", length = 32, nullable = false)
    private String roleId;

    @Column(name = "RESOURCE_ID", length = 32, nullable = false)
    private String resourceId;

    @Column(name = "PRIVILEGE_ID", length = 32, nullable = false)
    private String privilegeId;

    @Column(name = "SERVICE_ID", length = 32, nullable = false)
    private String domainId;

    public ResourceRoleEmbeddableId() {
    }

    public ResourceRoleEmbeddableId(String roleId, String resourceId, String privilegeId, String domainId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.privilegeId = privilegeId;
        this.domainId = domainId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceRoleEmbeddableId that = (ResourceRoleEmbeddableId) o;

        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
        return result;
    }
}
