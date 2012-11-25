package org.openiam.idm.srvc.org.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.org.dto.OrganizationAttribute;

@Entity
@Table(name = "COMPANY_ATTRIBUTE")
@DozerDTOCorrespondence(OrganizationAttribute.class)
public class OrganizationAttributeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "COMPANY_ATTR_ID", length = 32, nullable = false)
    private String attrId;

    // protected MetadataElement metadataElement;
    @Column(name = "METADATA_ID", length = 20)
    private String metadataElementId;

    @Column(name = "NAME", length = 20)
    private String name;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private OrganizationEntity organization;

    @Column(name = "VALUE")
    private String value;

    public OrganizationAttributeEntity() {
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getMetadataElementId() {
        return metadataElementId;
    }

    public void setMetadataElementId(String metadataElementId) {
        this.metadataElementId = metadataElementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationAttributeEntity that = (OrganizationAttributeEntity) o;

        if (attrId != null ? !attrId.equals(that.attrId) : that.attrId != null) return false;
        if (metadataElementId != null ? !metadataElementId.equals(that.metadataElementId) : that.metadataElementId != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (organization != null ? !organization.equals(that.organization) : that.organization != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attrId != null ? attrId.hashCode() : 0;
        result = 31 * result + (metadataElementId != null ? metadataElementId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
