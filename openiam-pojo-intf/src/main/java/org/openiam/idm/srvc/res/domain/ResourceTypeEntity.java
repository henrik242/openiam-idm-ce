package org.openiam.idm.srvc.res.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.res.dto.ResourceType;

@Entity
@Table(name = "RESOURCE_TYPE")
@DozerDTOCorrespondence(ResourceType.class)
public class ResourceTypeEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "RESOURCE_TYPE_ID", length = 32)
    private String resourceTypeId;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "METADATA_TYPE_ID", length = 20)
    private String metadataTypeId;

    @Column(name = "PROVISION_RESOURCE")
    private Integer provisionResource;

    @Column(name = "PROCESS_NAME", length = 80)
    private String processName;

    public ResourceTypeEntity() {
    }

    public ResourceTypeEntity(ResourceType resourceType) {
        this.resourceTypeId = resourceType.getResourceTypeId();
        this.description = resourceType.getDescription();
        this.metadataTypeId = resourceType.getMetadataTypeId();
        this.provisionResource = resourceType.getProvisionResource();
        this.processName = resourceType.getProcessName();
    }

    public String getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(String resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    public Integer getProvisionResource() {
        return provisionResource;
    }

    public void setProvisionResource(Integer provisionResource) {
        this.provisionResource = provisionResource;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceTypeEntity that = (ResourceTypeEntity) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (metadataTypeId != null ? !metadataTypeId.equals(that.metadataTypeId) : that.metadataTypeId != null)
            return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null) return false;
        if (provisionResource != null ? !provisionResource.equals(that.provisionResource) : that.provisionResource != null)
            return false;
        if (resourceTypeId != null ? !resourceTypeId.equals(that.resourceTypeId) : that.resourceTypeId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resourceTypeId != null ? resourceTypeId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (metadataTypeId != null ? metadataTypeId.hashCode() : 0);
        result = 31 * result + (provisionResource != null ? provisionResource.hashCode() : 0);
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        return result;
    }
}
