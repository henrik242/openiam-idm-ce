package org.openiam.idm.srvc.res.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.openiam.idm.srvc.res.dto.ResourceRole;

@Entity
@Table(name="RESOURCE_ROLE")
public class ResourceRoleEntity {

    @EmbeddedId
    private ResourceRoleEmbeddableId id;

	@Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    public ResourceRoleEntity() {
    }

    public ResourceRoleEntity(ResourceRole resourceRole) {
        this.id = new ResourceRoleEmbeddableId(resourceRole.getId());
        this.startDate = resourceRole.getStartDate();
        this.endDate = resourceRole.getEndDate();
    }

    public ResourceRoleEmbeddableId getId() {
        return id;
    }

    public void setId(ResourceRoleEmbeddableId id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
