package org.openiam.idm.srvc.res.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.res.dto.ResourceGroup;

@Entity
@Table(name = "RESOURCE_GROUP")
public class ResourceGroupEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "RES_GROUP_ID", length = 32)
    private String resGroupId;

    @Column(name = "RESOURCE_ID", length = 32)
    private String resourceId;

    @Column(name = "GRP_ID", length = 32)
    private String groupId;

    @Column(name = "START_DATE", length = 19)
    private Date startDate;

    @Column(name = "END_DATE", length = 19)
    private Date endDate;

    public ResourceGroupEntity() {
    }

    public ResourceGroupEntity(ResourceGroup resourceGroup) {
        this.resGroupId = resourceGroup.getGroupId();
        this.resourceId = resourceGroup.getResourceId();
        this.groupId = resourceGroup.getGroupId();
        this.startDate = resourceGroup.getStartDate();
        this.endDate = resourceGroup.getEndDate();
    }

    public String getResGroupId() {
        return resGroupId;
    }

    public void setResGroupId(String resGroupId) {
        this.resGroupId = resGroupId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

        ResourceGroupEntity that = (ResourceGroupEntity) o;

        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (resGroupId != null ? !resGroupId.equals(that.resGroupId) : that.resGroupId != null) return false;
        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resGroupId != null ? resGroupId.hashCode() : 0;
        result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
