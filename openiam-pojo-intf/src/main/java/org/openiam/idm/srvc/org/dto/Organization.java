package org.openiam.idm.srvc.org.dto;

import java.util.HashMap;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.base.AttributeOperationEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Map;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;
import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.user.dto.UserAttributeMapAdapter;

/**
 * <p/>
 * Java class for organization complex type.
 * <p/>
 * <p/>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p/>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organization", propOrder = {
        "alias",
        "attributes",
        "createDate",
        "createdBy",
        "description",
        "domainName",
        "ldapStr",
        "lstUpdate",
        "lstUpdatedBy",
        "metadataTypeId",
        "orgId",
        "organizationName",
        "parentId",
        "classification",
        "internalOrgId",
        "status",
        "abbreviation",
        "symbol",
        "selected",
        "operation"}
)

public class Organization implements java.io.Serializable, Comparable<Organization> {

    /**
     *
     */
    private static final long serialVersionUID = -6297113958697455428L;

    protected String orgId;

    protected String alias;

    @XmlJavaTypeAdapter(OrganizationAttributeMapAdapter.class)
    protected Map<String, OrganizationAttribute> attributes = new HashMap<String, OrganizationAttribute>(0);

    @XmlSchemaType(name = "dateTime")
    protected Date createDate;

    protected String createdBy;

    protected String description;

    protected String domainName;

    protected String ldapStr;

    @XmlSchemaType(name = "dateTime")
    protected Date lstUpdate;

    protected String lstUpdatedBy;

    protected String metadataTypeId;

    protected String organizationName;

    protected String internalOrgId;

    protected String parentId;

    protected String status;

    protected OrgClassificationEnum classification;

    protected String abbreviation;

    protected String symbol;

    protected Boolean selected = Boolean.FALSE;

    protected AttributeOperationEnum operation;

    // Constructors

    /**
     * default constructor
     */
    public Organization() {
    }

    /**
     * minimal constructor
     */
    public Organization(String companyId) {
        this.orgId = companyId;
    }

    public Organization(OrganizationEntity organizationEntity) {
       this.orgId = organizationEntity.getOrgId();
        this.alias = organizationEntity.getAlias();
        this.createDate = organizationEntity.getCreateDate();
        this.createdBy = organizationEntity.getCreatedBy();
        this.description = organizationEntity.getDescription();
        this.domainName = organizationEntity.getDomainName();
        this.ldapStr = organizationEntity.getLdapStr();
        this.lstUpdate = organizationEntity.getLstUpdate();
        this.lstUpdatedBy = organizationEntity.getLstUpdatedBy();
        this.metadataTypeId = organizationEntity.getMetadataTypeId();
        this.organizationName = organizationEntity.getOrganizationName();
        this.internalOrgId = organizationEntity.getInternalOrgId();
        this.parentId = organizationEntity.getParentId();
        this.status = organizationEntity.getStatus();
        this.classification = organizationEntity.getClassification();
        this.abbreviation = organizationEntity.getAbbreviation();
        this.symbol = organizationEntity.getSymbol();
        for(Map.Entry<String, OrganizationAttributeEntity> attributeEntity :  organizationEntity.getAttributes().entrySet()) {
            this.attributes.put(attributeEntity.getKey(), new OrganizationAttribute(attributeEntity.getValue()));
        }

    }

    /**
     * full constructor
     */
    public Organization(String companyId, String metadataTypeId,
                        String companyName, Date lstUpdate, String lstUpdatedBy,
                        String parentId, String status, Date createDate, String createdBy,
                        String alias, String description, String domainName,
                        String ldapStr, HashMap<String, OrganizationAttribute> companyAttributes) {
        this.orgId = companyId;
        this.metadataTypeId = metadataTypeId;
        this.organizationName = companyName;
        this.lstUpdate = lstUpdate;
        this.lstUpdatedBy = lstUpdatedBy;
        this.parentId = parentId;
        this.status = status;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.alias = alias;
        this.description = description;
        this.domainName = domainName;
        this.ldapStr = ldapStr;
        this.attributes = companyAttributes;
    }

    // Property accessors

    /**
     * Gets the value of the alias property.
     *
     * @return possible object is {@link String }
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the value of the alias property.
     *
     * @param value allowed object is {@link String }
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Gets the value of the attributes property.
     *
     * @return possible object is {@link org.openiam.idm.srvc.org.dto.OrganizationAttribute }
     */
    public Map<String, org.openiam.idm.srvc.org.dto.OrganizationAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Sets the value of the attributes property.
     *
     * @param attributes allowed object is {@link org.openiam.idm.srvc.org.dto.OrganizationAttribute }
     */
    public void setAttributes(Map<String, org.openiam.idm.srvc.org.dto.OrganizationAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Gets the value of the createDate property.
     *
     * @return possible object is {@link String }
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     *
     * @param value allowed object is {@link String }
     */
    public void setCreateDate(Date value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the createdBy property.
     *
     * @return possible object is {@link String }
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     *
     * @param value allowed object is {@link String }
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the domainName property.
     *
     * @return possible object is {@link String }
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Sets the value of the domainName property.
     *
     * @param value allowed object is {@link String }
     */
    public void setDomainName(String value) {
        this.domainName = value;
    }

    /**
     * Gets the value of the ldapStr property.
     *
     * @return possible object is {@link String }
     */
    public String getLdapStr() {
        return ldapStr;
    }

    /**
     * Sets the value of the ldapStr property.
     *
     * @param value allowed object is {@link String }
     */
    public void setLdapStr(String value) {
        this.ldapStr = value;
    }

    /**
     * Gets the value of the lstUpdate property.
     *
     * @return possible object is {@link String }
     */
    public Date getLstUpdate() {
        return lstUpdate;
    }

    /**
     * Sets the value of the lstUpdate property.
     *
     * @param value allowed object is {@link String }
     */
    public void setLstUpdate(Date value) {
        this.lstUpdate = value;
    }

    /**
     * Gets the value of the lstUpdatedBy property.
     *
     * @return possible object is {@link String }
     */
    public String getLstUpdatedBy() {
        return lstUpdatedBy;
    }

    /**
     * Sets the value of the lstUpdatedBy property.
     *
     * @param value allowed object is {@link String }
     */
    public void setLstUpdatedBy(String value) {
        this.lstUpdatedBy = value;
    }

    /**
     * Gets the value of the metadataType property.
     *
     * @return possible object is {@link String }
     */
    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    /**
     * Sets the value of the metadataType property.
     *
     * @param value allowed object is {@link String }
     */
    public void setMetadataTypeId(String value) {
        this.metadataTypeId = value;
    }

    /**
     * Gets the value of the orgId property.
     *
     * @return possible object is {@link String }
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * Sets the value of the orgId property.
     *
     * @param value allowed object is {@link String }
     */
    public void setOrgId(String value) {
        this.orgId = value;
    }

    /**
     * Gets the value of the organizationName property.
     *
     * @return possible object is {@link String }
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the value of the organizationName property.
     *
     * @param value allowed object is {@link String }
     */
    public void setOrganizationName(String value) {
        this.organizationName = value;
    }

    /**
     * Gets the value of the parentId property.
     *
     * @return possible object is {@link String }
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     *
     * @param value allowed object is {@link String }
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    public OrgClassificationEnum getClassification() {
        return classification;
    }

    public void setClassification(OrgClassificationEnum classification) {
        this.classification = classification;
    }

    public String getInternalOrgId() {
        return internalOrgId;
    }

    public void setInternalOrgId(String internalOrgId) {
        this.internalOrgId = internalOrgId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    public int compareTo(Organization o) {
        if (getOrganizationName() == null || o == null) {
            return Integer.MIN_VALUE;
        }
        return getOrganizationName().compareTo(o.getOrganizationName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;

        Organization that = (Organization) o;

        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null) return false;
        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        if (classification != that.classification) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (domainName != null ? !domainName.equals(that.domainName) : that.domainName != null) return false;
        if (internalOrgId != null ? !internalOrgId.equals(that.internalOrgId) : that.internalOrgId != null)
            return false;
        if (ldapStr != null ? !ldapStr.equals(that.ldapStr) : that.ldapStr != null) return false;
        if (lstUpdate != null ? !lstUpdate.equals(that.lstUpdate) : that.lstUpdate != null) return false;
        if (lstUpdatedBy != null ? !lstUpdatedBy.equals(that.lstUpdatedBy) : that.lstUpdatedBy != null) return false;
        if (metadataTypeId != null ? !metadataTypeId.equals(that.metadataTypeId) : that.metadataTypeId != null)
            return false;
        if (operation != that.operation) return false;
        if (orgId != null ? !orgId.equals(that.orgId) : that.orgId != null) return false;
        if (organizationName != null ? !organizationName.equals(that.organizationName) : that.organizationName != null)
            return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (selected != null ? !selected.equals(that.selected) : that.selected != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return orgId != null ? orgId.hashCode() : 0;
    }
}
