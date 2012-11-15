package org.openiam.idm.srvc.grp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;

@Entity
@Table(name="GRP_ATTRIBUTES")
public class GroupAttributeEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="ID",length=32)
    private String id;

    @Column(name="NAME",length=20)
    private String name;

    @Column(name="VALUE")
    private String value;

    @Column(name="METADATA_ID",length=20)
    private String metadataElementId;

    @Column(name="GRP_ID",length=32)
    private String groupId;

    public GroupAttributeEntity() {
    }

    public GroupAttributeEntity(GroupAttribute groupAttribute) {
        this.id = groupAttribute.getId();
        this.name = groupAttribute.getName();
        this.value = groupAttribute.getValue();
        this.metadataElementId = groupAttribute.getMetadataElementId();
        this.groupId = groupAttribute.getGroupId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMetadataElementId() {
        return metadataElementId;
    }

    public void setMetadataElementId(String metadataElementId) {
        this.metadataElementId = metadataElementId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
