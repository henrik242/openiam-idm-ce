package org.openiam.idm.srvc.res.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.openiam.idm.srvc.res.dto.ResourceUser;

@Entity
@Table(name = "RESOURCE_USER")
public class ResourceUserEntity {

    @EmbeddedId
    private ResourceUserEmbeddableId id;

    @ManyToOne
    @JoinColumn(name="RESOURCE_ID",insertable = false, updatable = false)
    private ResourceEntity resource;

    public ResourceUserEntity() {
    }

    public ResourceUserEntity(ResourceUser resourceUser, ResourceEntity resource) {
        this.id = new ResourceUserEmbeddableId(resourceUser.getId().getResourceId(), resourceUser.getId().getUserId(), resourceUser.getId().getPrivilegeId());
        this.resource = resource;
    }

    public ResourceUserEmbeddableId getId() {
        return id;
    }

    public void setId(ResourceUserEmbeddableId id) {
        this.id = id;
    }

    public ResourceEntity getResource() {
        return resource;
    }

    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceUserEntity that = (ResourceUserEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
