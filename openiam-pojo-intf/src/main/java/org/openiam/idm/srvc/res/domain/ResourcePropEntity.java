package org.openiam.idm.srvc.res.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.res.dto.ResourceProp;

@Entity
@Table(name="RESOURCE_PROP")
public class ResourcePropEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="RESOURCE_PROP_ID", length=32)
    private String resourcePropId;

    @Column(name="RESOURCE_ID",length=32)
    private String resourceId;

    @Column(name="METADATA_ID",length=20)
    private String metadataId;

    @Column(name="PROP_VALUE",length=200)
    private String propValue;

    @Column(name="NAME",length=40)
    private String name;

    public ResourcePropEntity() {
    }

    public ResourcePropEntity(ResourceProp resourceProp) {
        this.resourcePropId = resourceProp.getResourcePropId();
        this.resourceId = resourceProp.getResourceId();
        this.metadataId = resourceProp.getMetadataId();
        this.propValue = resourceProp.getPropValue();
        this.name = resourceProp.getName();
    }

    public String getResourcePropId() {
        return resourcePropId;
    }

    public void setResourcePropId(String resourcePropId) {
        this.resourcePropId = resourcePropId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourcePropEntity that = (ResourcePropEntity) o;

        if (metadataId != null ? !metadataId.equals(that.metadataId) : that.metadataId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (propValue != null ? !propValue.equals(that.propValue) : that.propValue != null) return false;
        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) return false;
        if (resourcePropId != null ? !resourcePropId.equals(that.resourcePropId) : that.resourcePropId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resourcePropId != null ? resourcePropId.hashCode() : 0;
        result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
        result = 31 * result + (metadataId != null ? metadataId.hashCode() : 0);
        result = 31 * result + (propValue != null ? propValue.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
