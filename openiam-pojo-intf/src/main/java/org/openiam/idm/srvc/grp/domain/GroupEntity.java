package org.openiam.idm.srvc.grp.domain;

import java.util.*;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import org.openiam.base.AttributeOperationEnum;

import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.role.dto.Role;

@Entity
@Table(name = "GRP")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DozerDTOCorrespondence(Group.class)
public class GroupEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="GRP_ID", length=32)
    private String grpId;

    @Column(name="TYPE_ID",length=20)
    private String metadataTypeId;

    @Column(name="GRP_NAME",length=80)
    private String grpName;

    @Column(name="CREATE_DATE",length=19)
    private Date createDate;

    @Column(name="CREATED_BY",length=20)
    private String createdBy;

    @Column(name="COMPANY_ID",length=20)
    private String companyId;

    @Column(name="OWNER_ID",length=20)
    private String ownerId;

    @Column(name="PARENT_GRP_ID",length=20)
    private String parentGrpId;

    @Column(name="INHERIT_FROM_PARENT")
    @Type(type="boolean")
    private Boolean inheritFromParent;

    @Column(name="PROVISION_METHOD",length=20)
    private String provisionMethod;

    @Column(name="PROVISION_OBJ_NAME",length=80)
    private String provisionObjName;

    @Column(name="GROUP_CLASS",length=40)
    private String groupClass;

    @Column(name="GROUP_DESC",length=80)
    private String description;

    @Column(name="STATUS",length=20)
    private String status;

    @Column(name="LAST_UPDATE",length=19)
    private Date lastUpdate;

    @Column(name="LAST_UPDATED_BY",length=20)
    private String lastUpdatedBy;

    @Column(name="INTERNAL_GROUP_ID",length=32)
    private String internalGroupId = null;

    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name="GRP_ID", referencedColumnName="GRP_ID")
    @MapKeyColumn(name="name")
    private Map<String, GroupAttributeEntity> attributes = new HashMap<String, GroupAttributeEntity>(0);

    @Transient
    private List<GroupEntity> subGroup = new LinkedList<GroupEntity>();
    @Transient
    private AttributeOperationEnum operation;
    @Transient
    private Set<Role> roles = new HashSet<org.openiam.idm.srvc.role.dto.Role>(0);

    public GroupEntity() {
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getParentGrpId() {
        return parentGrpId;
    }

    public void setParentGrpId(String parentGrpId) {
        this.parentGrpId = parentGrpId;
    }

    public Boolean getInheritFromParent() {
        return inheritFromParent;
    }

    public void setInheritFromParent(Boolean inheritFromParent) {
        this.inheritFromParent = inheritFromParent;
    }

    public String getProvisionMethod() {
        return provisionMethod;
    }

    public void setProvisionMethod(String provisionMethod) {
        this.provisionMethod = provisionMethod;
    }

    public String getProvisionObjName() {
        return provisionObjName;
    }

    public void setProvisionObjName(String provisionObjName) {
        this.provisionObjName = provisionObjName;
    }

    public String getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(String groupClass) {
        this.groupClass = groupClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getInternalGroupId() {
        return internalGroupId;
    }

    public void setInternalGroupId(String internalGroupId) {
        this.internalGroupId = internalGroupId;
    }

    public Map<String, GroupAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, GroupAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public List<GroupEntity> getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(List<GroupEntity> subGroup) {
        this.subGroup = subGroup;
    }

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupEntity that = (GroupEntity) o;

        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (groupClass != null ? !groupClass.equals(that.groupClass) : that.groupClass != null) return false;
        if (grpId != null ? !grpId.equals(that.grpId) : that.grpId != null) return false;
        if (grpName != null ? !grpName.equals(that.grpName) : that.grpName != null) return false;
        if (inheritFromParent != null ? !inheritFromParent.equals(that.inheritFromParent) : that.inheritFromParent != null)
            return false;
        if (internalGroupId != null ? !internalGroupId.equals(that.internalGroupId) : that.internalGroupId != null)
            return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;
        if (lastUpdatedBy != null ? !lastUpdatedBy.equals(that.lastUpdatedBy) : that.lastUpdatedBy != null)
            return false;
        if (metadataTypeId != null ? !metadataTypeId.equals(that.metadataTypeId) : that.metadataTypeId != null)
            return false;
        if (ownerId != null ? !ownerId.equals(that.ownerId) : that.ownerId != null) return false;
        if (parentGrpId != null ? !parentGrpId.equals(that.parentGrpId) : that.parentGrpId != null) return false;
        if (provisionMethod != null ? !provisionMethod.equals(that.provisionMethod) : that.provisionMethod != null)
            return false;
        if (provisionObjName != null ? !provisionObjName.equals(that.provisionObjName) : that.provisionObjName != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = grpId != null ? grpId.hashCode() : 0;
        result = 31 * result + (metadataTypeId != null ? metadataTypeId.hashCode() : 0);
        result = 31 * result + (grpName != null ? grpName.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (parentGrpId != null ? parentGrpId.hashCode() : 0);
        result = 31 * result + (inheritFromParent != null ? inheritFromParent.hashCode() : 0);
        result = 31 * result + (provisionMethod != null ? provisionMethod.hashCode() : 0);
        result = 31 * result + (provisionObjName != null ? provisionObjName.hashCode() : 0);
        result = 31 * result + (groupClass != null ? groupClass.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        result = 31 * result + (lastUpdatedBy != null ? lastUpdatedBy.hashCode() : 0);
        result = 31 * result + (internalGroupId != null ? internalGroupId.hashCode() : 0);
        return result;
    }
}
