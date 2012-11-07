package org.openiam.idm.srvc.org.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.org.dto.OrgClassificationEnum;
import org.openiam.idm.srvc.org.dto.Organization;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.org.dto.OrgClassificationEnum;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.dto.OrganizationAttribute;

@Entity
@Table(name = "COMPANY")
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

    public OrganizationEntity() {
    }

    public OrganizationEntity(Organization organization) {
        this.orgId = organization.getOrgId();
        this.alias = organization.getAlias();
        for(Map.Entry<String, OrganizationAttribute> attributeEntityEntry : organization.getAttributes().entrySet()) {
            this.attributes.put(attributeEntityEntry.getKey(), new OrganizationAttributeEntity(attributeEntityEntry.getValue(),this));
        }
        this.createDate = organization.getCreateDate();
        this.createdBy = organization.getCreatedBy();
        this.description = organization.getDescription();
        this.domainName = organization.getDomainName();
        this.ldapStr = organization.getLdapStr();
        this.lstUpdate = organization.getLstUpdate();
        this.lstUpdatedBy = organization.getLstUpdatedBy();
        this.metadataTypeId = organization.getMetadataTypeId();
        this.organizationName = organization.getOrganizationName();
        this.internalOrgId = organization.getInternalOrgId();
        this.parentId = organization.getParentId();
        this.status = organization.getStatus();
        this.classification = organization.getClassification();
        this.abbreviation = organization.getAbbreviation();
        this.symbol = organization.getSymbol();
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
}
