package org.openiam.idm.srvc.org.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.org.dto.OrgClassificationEnum;
import org.openiam.idm.srvc.org.dto.Organization;

@Entity
@Table(name = "COMPANY")
@DozerDTOCorrespondence(Organization.class)
public class OrganizationEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="COMPANY_ID", length=32, nullable = false)
    private String orgId;

    @Column(name="ALIAS", length=100)
    private String alias;

    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL, mappedBy = "organization", fetch = FetchType.EAGER)
    @MapKey(name = "name")
    private Map<String, OrganizationAttributeEntity> attributes = new HashMap<String, OrganizationAttributeEntity>(0);

    @Column(name="CREATE_DATE", length=19)
    private Date createDate;

    @Column(name="CREATED_BY", length=20)
    private String createdBy;

    @Column(name="DESCRIPTION", length=100)
    private String description;

    @Column(name="DOMAIN_NAME", length=40)
    private String domainName;

    @Column(name="LDAP_STR")
    private String ldapStr;

    @Column(name="LST_UPDATE", length=19)
    private Date lstUpdate;

    @Column(name="LST_UPDATED_BY", length=20)
    private String lstUpdatedBy;

    @Column(name="TYPE_ID", length=20)
    private String metadataTypeId;

    @Column(name="COMPANY_NAME", length=200)
    private String organizationName;

    @Column(name="INTERNAL_COMPANY_ID")
    private String internalOrgId;

    @Column(name="PARENT_ID", length=32)
    private String parentId;

    @Column(name="STATUS", length=20)
    private String status;

    @Column(name="CLASSIFICATION", length=40)
    @Enumerated(EnumType.STRING)
    private OrgClassificationEnum classification;

    @Column(name="ABBREVIATION", length=20)
    private String abbreviation;

    @Column(name="SYMBOL", length=10)
    private String symbol;

    @Transient
    private Boolean selected = Boolean.FALSE;

    @Transient
    private AttributeOperationEnum operation;

    public OrganizationEntity() {
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, OrganizationAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, OrganizationAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getLdapStr() {
        return ldapStr;
    }

    public void setLdapStr(String ldapStr) {
        this.ldapStr = ldapStr;
    }

    public Date getLstUpdate() {
        return lstUpdate;
    }

    public void setLstUpdate(Date lstUpdate) {
        this.lstUpdate = lstUpdate;
    }

    public String getLstUpdatedBy() {
        return lstUpdatedBy;
    }

    public void setLstUpdatedBy(String lstUpdatedBy) {
        this.lstUpdatedBy = lstUpdatedBy;
    }

    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getInternalOrgId() {
        return internalOrgId;
    }

    public void setInternalOrgId(String internalOrgId) {
        this.internalOrgId = internalOrgId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrgClassificationEnum getClassification() {
        return classification;
    }

    public void setClassification(OrgClassificationEnum classification) {
        this.classification = classification;
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

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationEntity that = (OrganizationEntity) o;

        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null) return false;
        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
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
        if (orgId != null ? !orgId.equals(that.orgId) : that.orgId != null) return false;
        if (organizationName != null ? !organizationName.equals(that.organizationName) : that.organizationName != null)
            return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orgId != null ? orgId.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (domainName != null ? domainName.hashCode() : 0);
        result = 31 * result + (ldapStr != null ? ldapStr.hashCode() : 0);
        result = 31 * result + (lstUpdate != null ? lstUpdate.hashCode() : 0);
        result = 31 * result + (lstUpdatedBy != null ? lstUpdatedBy.hashCode() : 0);
        result = 31 * result + (metadataTypeId != null ? metadataTypeId.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (internalOrgId != null ? internalOrgId.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (classification != null ? classification.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        return result;
    }
}
