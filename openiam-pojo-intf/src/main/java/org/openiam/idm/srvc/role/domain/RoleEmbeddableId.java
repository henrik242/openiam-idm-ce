package org.openiam.idm.srvc.role.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.openiam.idm.srvc.role.dto.RoleId;

@Embeddable
public class RoleEmbeddableId implements Serializable {
    @Column(name = "ROLE_ID", length = 32, nullable = false)
    private String roleId;

    @Column(name = "SERVICE_ID", length = 32, nullable = false)
    private String serviceId;

    public RoleEmbeddableId() {
    }

    public RoleEmbeddableId(String serviceId, String roleId) {
        this.roleId = roleId;
        this.serviceId = serviceId;
    }

    public RoleEmbeddableId(RoleId roleId) {
        this.roleId = roleId.getRoleId();
        this.serviceId = roleId.getServiceId();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleEmbeddableId that = (RoleEmbeddableId) o;

        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoleId{" +
                "roleId='" + roleId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}
