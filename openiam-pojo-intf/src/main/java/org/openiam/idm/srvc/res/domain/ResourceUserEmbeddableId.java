package org.openiam.idm.srvc.res.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.res.dto.ResourceUserId;

@Embeddable
@DozerDTOCorrespondence(ResourceUserId.class)
public class ResourceUserEmbeddableId implements Serializable {
    @Column(name = "RESOURCE_ID", length = 32, nullable = false)
    private String resourceId;
    @Column(name = "USER_ID", length = 32, nullable = false)
    private String userId;
    @Column(name = "PRIVILEGE_ID", length = 32, nullable = false)
    private String privilegeId;

    public ResourceUserEmbeddableId() {
    }

    public ResourceUserEmbeddableId(String resourceId, String userId, String privilegeId) {
        this.resourceId = resourceId;
        this.userId = userId;
        this.privilegeId = privilegeId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceUserEmbeddableId that = (ResourceUserEmbeddableId) o;

        if (privilegeId != null ? !privilegeId.equals(that.privilegeId) : that.privilegeId != null) return false;
        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resourceId != null ? resourceId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (privilegeId != null ? privilegeId.hashCode() : 0);
        return result;
    }
}
