package org.openiam.idm.srvc.role.dto;

import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.BaseObject;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupSet;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;


/**
 * <p>Java class for role complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="role">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="createdBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groups" type="{urn:idm.openiam.org/srvc/grp/dto}groupSet" minOccurs="0"/>
 *         &lt;element name="id" type="{urn:idm.openiam.org/srvc/role/dto}roleId" minOccurs="0"/>
 *         &lt;element name="provisionObjName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parentRoleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roleAttributes" type="{urn:idm.openiam.org/srvc/role/dto}roleAttributeSet" minOccurs="0"/>
 *         &lt;element name="roleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userAssociationMethod" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="users" type="{urn:idm.openiam.org/srvc/user/dto}userSet" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role", propOrder = {
        "createDate",
        "createdBy",
        "description",
        "groups",
        "id",
        "provisionObjName",
        "parentRoleId",
        "roleAttributes",
        "roleName",
        "userAssociationMethod",
        "metadataTypeId",
        "ownerId",
        "inheritFromParent",
        "status",
        "childRoles",
        "selected",
        "internalRoleId",
        "operation",
        "startDate",
        "endDate",
        "rolePolicy"
})
@XmlRootElement(name = "Role")
@XmlSeeAlso({
        RoleId.class,
        Group.class,
        RoleAttribute.class,
        RolePolicy.class
})
public class Role extends BaseObject implements Comparable<Role> {

    /**
     *
     */
    private static final long serialVersionUID = -3903402630611423082L;

    protected AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    @XmlSchemaType(name = "dateTime")
    protected Date createDate;
    protected String createdBy;
    protected String description;
    @XmlJavaTypeAdapter(org.openiam.idm.srvc.grp.dto.GroupSetAdapter.class)
    protected Set<Group> groups = new HashSet<Group>(0);
    protected RoleId id;
    protected String provisionObjName;
    protected String parentRoleId;
    @XmlJavaTypeAdapter(org.openiam.idm.srvc.role.dto.RoleAttributeSetAdapter.class)
    protected Set<RoleAttribute> roleAttributes = new HashSet<RoleAttribute>(0);

    protected Set<RolePolicy> rolePolicy = new HashSet<RolePolicy>();

    protected String roleName;
    protected int userAssociationMethod = RoleConstant.UN_ASSIGNED;

    protected String status;
    protected Boolean selected = new Boolean(false);

    protected String metadataTypeId;

    protected String ownerId;
    protected Integer inheritFromParent;
    protected String internalRoleId;
    protected List<Role> childRoles = new ArrayList<Role>(0);


    @XmlSchemaType(name = "dateTime")
    protected Date startDate;
    @XmlSchemaType(name = "dateTime")
    protected Date endDate;

    public Role() {
    }


    public Role(RoleId id) {
        this.id = id;
    }


    /**
     * Gets the value of the createDate property.
     *
     * @return possible object is
     *         {@link XMLGregorianCalendar }
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setCreateDate(Date value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the createdBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the groups property.
     *
     * @return possible object is
     *         {@link GroupSet }
     */
    public Set<org.openiam.idm.srvc.grp.dto.Group> getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     *
     * @param value allowed object is
     *              {@link GroupSet }
     */
    public void setGroups(Set<org.openiam.idm.srvc.grp.dto.Group> value) {
        this.groups = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link RoleId }
     */
    public RoleId getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link RoleId }
     */
    public void setId(RoleId value) {
        this.id = value;
    }

    /**
     * Gets the value of the provisionObjName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProvisionObjName() {
        return provisionObjName;
    }

    /**
     * Sets the value of the provisionObjName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProvisionObjName(String value) {
        this.provisionObjName = value;
    }

    /**
     * Gets the value of the parentRoleId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getParentRoleId() {
        return parentRoleId;
    }

    /**
     * Sets the value of the parentRoleId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setParentRoleId(String value) {
        this.parentRoleId = value;
    }

    /**
     * Gets the value of the roleAttributes property.
     *
     * @return possible object is
     *         {@link RoleAttributeSet }
     */
    public Set<org.openiam.idm.srvc.role.dto.RoleAttribute> getRoleAttributes() {
        return roleAttributes;
    }

    /**
     * Sets the value of the roleAttributes property.
     *
     * @param value allowed object is
     *              {@link RoleAttributeSet }
     */
    public void setRoleAttributes(Set<org.openiam.idm.srvc.role.dto.RoleAttribute> value) {
        this.roleAttributes = value;
    }

    /**
     * Gets the value of the roleName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the value of the roleName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRoleName(String value) {
        this.roleName = value;
    }

    /**
     * Gets the value of the userAssociationMethod property.
     */
    public int getUserAssociationMethod() {
        return userAssociationMethod;
    }

    /**
     * Sets the value of the userAssociationMethod property.
     */
    public void setUserAssociationMethod(int value) {
        this.userAssociationMethod = value;
    }


    public String getMetadataTypeId() {
        return metadataTypeId;
    }


    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }


    public String getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    public Integer getInheritFromParent() {
        return inheritFromParent;
    }


    public void setInheritFromParent(Integer inheritFromParent) {
        this.inheritFromParent = inheritFromParent;
    }

    @Override
    public String toString() {
        return "Role{" +
                "operation=" + operation +
                ", createDate=" + createDate +
                ", createdBy='" + createdBy + '\'' +
                ", description='" + description + '\'' +
                ", groups=" + groups +
                ", id=" + id +
                ", provisionObjName='" + provisionObjName + '\'' +
                ", parentRoleId='" + parentRoleId + '\'' +
                ", roleAttributes=" + roleAttributes +
                ", rolePolicy=" + rolePolicy +
                ", roleName='" + roleName + '\'' +
                ", userAssociationMethod=" + userAssociationMethod +
                ", status='" + status + '\'' +
                ", selected=" + selected +
                ", metadataTypeId='" + metadataTypeId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", inheritFromParent=" + inheritFromParent +
                ", internalRoleId='" + internalRoleId + '\'' +
                ", childRoles=" + childRoles +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setRoleStatus(RoleStatus status) {
        this.status = status.toString();
    }


    public List<Role> getChildRoles() {
        return childRoles;
    }


    public void setChildRoles(List<Role> childRoles) {
        this.childRoles = childRoles;
    }


    public String getInternalRoleId() {
        return internalRoleId;
    }


    public void setInternalRoleId(String internalRoleId) {
        this.internalRoleId = internalRoleId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (userAssociationMethod != role.userAssociationMethod) return false;
        if (childRoles != null ? !childRoles.equals(role.childRoles) : role.childRoles != null) return false;
        if (createDate != null ? !createDate.equals(role.createDate) : role.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(role.createdBy) : role.createdBy != null) return false;
        if (description != null ? !description.equals(role.description) : role.description != null) return false;
        if (endDate != null ? !endDate.equals(role.endDate) : role.endDate != null) return false;
        if (groups != null ? !groups.equals(role.groups) : role.groups != null) return false;
        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (inheritFromParent != null ? !inheritFromParent.equals(role.inheritFromParent) : role.inheritFromParent != null)
            return false;
        if (internalRoleId != null ? !internalRoleId.equals(role.internalRoleId) : role.internalRoleId != null)
            return false;
        if (metadataTypeId != null ? !metadataTypeId.equals(role.metadataTypeId) : role.metadataTypeId != null)
            return false;
        if (operation != role.operation) return false;
        if (ownerId != null ? !ownerId.equals(role.ownerId) : role.ownerId != null) return false;
        if (parentRoleId != null ? !parentRoleId.equals(role.parentRoleId) : role.parentRoleId != null) return false;
        if (provisionObjName != null ? !provisionObjName.equals(role.provisionObjName) : role.provisionObjName != null)
            return false;
        if (roleAttributes != null ? !roleAttributes.equals(role.roleAttributes) : role.roleAttributes != null)
            return false;
        if (roleName != null ? !roleName.equals(role.roleName) : role.roleName != null) return false;
        if (rolePolicy != null ? !rolePolicy.equals(role.rolePolicy) : role.rolePolicy != null) return false;
        if (selected != null ? !selected.equals(role.selected) : role.selected != null) return false;
        if (startDate != null ? !startDate.equals(role.startDate) : role.startDate != null) return false;
        if (status != null ? !status.equals(role.status) : role.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Set<RolePolicy> getRolePolicy() {
        return rolePolicy;
    }


    public void setRolePolicy(Set<RolePolicy> rolePolicy) {
        this.rolePolicy = rolePolicy;
    }


    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public int compareTo(Role o) {
        if (getRoleName() == null || o == null) {
            return Integer.MIN_VALUE;
        }
        return getRoleName().compareTo(o.getRoleName());
    }


}


