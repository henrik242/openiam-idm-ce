package org.openiam.idm.srvc.role.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleConstant;

@Entity
@Table(name="ROLE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DozerDTOCorrespondence(Role.class)
public class RoleEntity implements Comparable<RoleEntity> {

    @EmbeddedId
    private RoleEmbeddableId roleId;

    @Column(name="ROLE_NAME",length=80)
    private String roleName;

	@Column(name="CREATE_DATE",length=19)
    private Date createDate;

    @Column(name="CREATED_BY",length=20)
    private String createdBy;

    @Column(name="ROLE_END_DATE",length=19)
    private Date endDate;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PROVISION_OBJ_NAME",length=80)
    private String provisionObjName;

    @Column(name="TYPE_ID",length=20)
    private String metadataTypeId;

    @Column(name = "PARENT_ROLE_ID", length = 32, nullable = true)
    private String parentRoleId;

	@ManyToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinTable(name="GRP_ROLE",
	    joinColumns={@JoinColumn(name="ROLE_ID"),@JoinColumn(name="SERVICE_ID")},
	    inverseJoinColumns={@JoinColumn(name="GRP_ID")})
	@Fetch(FetchMode.SELECT)
    private Set<GroupEntity> groups = new HashSet<GroupEntity>(0);

	@OneToMany(fetch=FetchType.EAGER,orphanRemoval=true,cascade={CascadeType.ALL})
    @JoinColumns({
             @JoinColumn(name="ROLE_ID"),
             @JoinColumn(name="SERVICE_ID")
    })
    private Set<RoleAttributeEntity> roleAttributes = new HashSet<RoleAttributeEntity>(0);

	@OneToMany(fetch=FetchType.EAGER,orphanRemoval=true,cascade=CascadeType.ALL)
	@JoinColumns({
             @JoinColumn(name="ROLE_ID"),
             @JoinColumn(name="SERVICE_ID")
    })
    private Set<RolePolicyEntity> rolePolicy = new HashSet<RolePolicyEntity>();

	@Column(name="STATUS",length=20)
    private String status;

    @Column(name="OWNER_ID",length=32)
    private String ownerId;

    @Column(name="INTERNAL_ROLE_ID")
    private String internalRoleId;

    @Transient
    private Boolean selected = Boolean.FALSE;

    @Column(name="INHERIT_FROM_PARENT")
    private Integer inheritFromParent;

    @Transient
    private List<RoleEntity> childRoles = new LinkedList<RoleEntity>();
    @Transient
    private int userAssociationMethod = RoleConstant.UN_ASSIGNED;
    @Transient
    private AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    public RoleEntity() {
    }

    public RoleEmbeddableId getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleEmbeddableId roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getProvisionObjName() {
        return provisionObjName;
    }

    public void setProvisionObjName(String provisionObjName) {
        this.provisionObjName = provisionObjName;
    }

    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    public String getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(String parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    public Set<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupEntity> groups) {
        this.groups = groups;
    }

    public Set<RoleAttributeEntity> getRoleAttributes() {
        return roleAttributes;
    }

    public void setRoleAttributes(Set<RoleAttributeEntity> roleAttributes) {
        this.roleAttributes = roleAttributes;
    }

    public Set<RolePolicyEntity> getRolePolicy() {
        return rolePolicy;
    }

    public void setRolePolicy(Set<RolePolicyEntity> rolePolicy) {
        this.rolePolicy = rolePolicy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getInternalRoleId() {
        return internalRoleId;
    }

    public void setInternalRoleId(String internalRoleId) {
        this.internalRoleId = internalRoleId;
    }

    public Integer getInheritFromParent() {
        return inheritFromParent;
    }

    public void setInheritFromParent(Integer inheritFromParent) {
        this.inheritFromParent = inheritFromParent;
    }

    public List<RoleEntity> getChildRoles() {
        return childRoles;
    }

    public void setChildRoles(List<RoleEntity> childRoles) {
        this.childRoles = childRoles;
    }

    public int getUserAssociationMethod() {
        return userAssociationMethod;
    }

    public void setUserAssociationMethod(int userAssociationMethod) {
        this.userAssociationMethod = userAssociationMethod;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int compareTo(RoleEntity o) {
        if (getRoleName() == null || o == null) {
            return Integer.MIN_VALUE;
        }
        return this.getRoleName().compareTo(o.getRoleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleEntity entity = (RoleEntity) o;

        if (createDate != null ? !createDate.equals(entity.createDate) : entity.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(entity.createdBy) : entity.createdBy != null) return false;
        if (description != null ? !description.equals(entity.description) : entity.description != null) return false;
        if (internalRoleId != null ? !internalRoleId.equals(entity.internalRoleId) : entity.internalRoleId != null)
            return false;
        if (metadataTypeId != null ? !metadataTypeId.equals(entity.metadataTypeId) : entity.metadataTypeId != null)
            return false;
        if (ownerId != null ? !ownerId.equals(entity.ownerId) : entity.ownerId != null) return false;
        if (provisionObjName != null ? !provisionObjName.equals(entity.provisionObjName) : entity.provisionObjName != null)
            return false;
        if (roleId != null ? !roleId.equals(entity.roleId) : entity.roleId != null) return false;
        if (roleName != null ? !roleName.equals(entity.roleName) : entity.roleName != null) return false;
        if (status != null ? !status.equals(entity.status) : entity.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (provisionObjName != null ? provisionObjName.hashCode() : 0);
        result = 31 * result + (metadataTypeId != null ? metadataTypeId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (internalRoleId != null ? internalRoleId.hashCode() : 0);
        return result;
    }

}
