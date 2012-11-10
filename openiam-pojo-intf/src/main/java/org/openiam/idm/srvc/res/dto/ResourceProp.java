package org.openiam.idm.srvc.res.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openiam.idm.srvc.res.domain.ResourcePropEntity;

// Generated Mar 8, 2009 12:54:32 PM by Hibernate Tools 3.2.2.GA

/**
 * ResourceProp enables the extension of a resource by associated properties (name value pairs) to them.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceProp", propOrder = {
        "resourcePropId",
        "resourceId",
        "metadataId",
        "propValue",
        "name"
})

public class ResourceProp implements java.io.Serializable, Comparable<ResourceProp> {

    private String resourcePropId;
    private String resourceId;
    private String metadataId;
    private String propValue;
    private String name;

    public ResourceProp() {
    }

    public ResourceProp(ResourcePropEntity propEntity) {
        this.resourcePropId = propEntity.getResourcePropId();
        this.resourceId = propEntity.getResourceId();
        this.metadataId = propEntity.getMetadataId();
        this.propValue = propEntity.getPropValue();
        this.name = propEntity.getName();
    }

    public ResourceProp(String metadataId, String resourceId,
                        String resourcePropId, String propValue) {
        super();
        this.metadataId = metadataId;
        this.resourceId = resourceId;
        this.resourcePropId = resourcePropId;
        this.propValue = propValue;
    }


    public ResourceProp(String resourcePropId) {
        this.resourcePropId = resourcePropId;
    }

    public String getResourcePropId() {
        return this.resourcePropId;
    }

    public void setResourcePropId(String resourcePropId) {
        this.resourcePropId = resourcePropId;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getMetadataId() {
        return this.metadataId;
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
    public String toString() {
        return "ResourceProp{" +
                "resourcePropId='" + resourcePropId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", metadataId='" + metadataId + '\'' +
                ", propValue='" + propValue + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public int compareTo(ResourceProp o) {
        if (getName() == null || o == null) {
            // Not recommended, but compareTo() is only used for display purposes in this case
            return Integer.MIN_VALUE;
        }
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceProp)) return false;

        ResourceProp that = (ResourceProp) o;

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
        return resourcePropId != null ? resourcePropId.hashCode() : 0;
    }
}
