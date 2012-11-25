package org.openiam.idm.srvc.res.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.res.dto.ResourceRole;

@Entity
@Table(name="RESOURCE_ROLE")
@DozerDTOCorrespondence(ResourceRole.class)
public class ResourceRoleEntity {

    @EmbeddedId
    private ResourceRoleEmbeddableId id;

	@Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    public ResourceRoleEntity() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceRoleEntity that = (ResourceRoleEntity) o;

        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
