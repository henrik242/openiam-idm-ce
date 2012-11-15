package org.openiam.idm.srvc.role.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.role.dto.RoleAttribute;

@Entity
@Table(name="ROLE_ATTRIBUTE")
public class RoleAttributeEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="ROLE_ATTR_ID", length=32)
    private String roleAttrId;

    @Column(name="ROLE_ID", length=32)
    private String roleId;

    @Column(name="METADATA_ID",length=20)
    private String metadataElementId;

    @Column(name="NAME", length=20)
    private String name;

    @Column(name="VALUE")
    private String value;

    @Column(name="ATTR_GROUP",length=20)
    private String attrGroup;

    @Column(name="SERVICE_ID",length=20)
    private String serviceId;

    public RoleAttributeEntity() {
    }

    public RoleAttributeEntity(RoleAttribute attribute) {
        this.roleAttrId = attribute.getRoleAttrId();
        this.roleId = attribute.getRoleId();
        this.metadataElementId = attribute.getMetadataElementId();
        this.name = attribute.getName();
        this.value = attribute.getValue();
        this.attrGroup = attribute.getAttrGroup();
        this.serviceId = attribute.getServiceId();
    }

    public String getRoleAttrId() {
        return roleAttrId;
    }

    public void setRoleAttrId(String roleAttrId) {
        this.roleAttrId = roleAttrId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttrGroup() {
        return attrGroup;
    }

    public void setAttrGroup(String attrGroup) {
        this.attrGroup = attrGroup;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
