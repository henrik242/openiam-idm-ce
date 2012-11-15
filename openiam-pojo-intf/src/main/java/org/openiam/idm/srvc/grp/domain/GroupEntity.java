package org.openiam.idm.srvc.grp.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;

@Entity
@Table(name = "GRP")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    public GroupEntity() {
    }

    public GroupEntity(Group group) {
        this.grpId = group.getGrpId();
        this.grpName = group.getGrpName();
        this.createDate = group.getCreateDate() != null ? group.getCreateDate() : new Date();
        this.createdBy = group.getCreatedBy();
        this.companyId = group.getCompanyId();
        this.ownerId = group.getOwnerId();
        this.inheritFromParent = group.getInheritFromParent();
        this.provisionMethod = group.getProvisionMethod();
        this.provisionObjName = group.getProvisionObjName();
        this.groupClass = group.getGroupClass();
        this.description = group.getDescription();
        this.status = group.getStatus();
        this.lastUpdate = group.getLastUpdate();
        this.metadataTypeId = group.getMetadataTypeId();
        this.internalGroupId = group.getInternalGroupId();
        for(Map.Entry<String, GroupAttribute> attributeEntry : group.getAttributes().entrySet()) {
            this.attributes.put(attributeEntry.getKey(), new GroupAttributeEntity(attributeEntry.getValue()));
        }
        for(Group groupEntity : group.getSubGroup()) {
           this.subGroup.add(new GroupEntity(groupEntity));
        }
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
}
