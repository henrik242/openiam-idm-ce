package org.openiam.webconsole.web.model;

import java.io.Serializable;
import java.util.List;

import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.dto.GroupAttribute;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.idm.srvc.org.dto.Organization;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class GroupDetailCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Group> childGroup = null;
    private List<GroupAttribute> attributeList;
    private Group parentGroup;
    private Group group;

    private List<Organization> orgList;
    private MetadataType[] typeList;

    public List<Group> getChildGroup() {
        return childGroup;
    }

    public void setChildGroup(List<Group> childGroup) {
        this.childGroup = childGroup;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Organization> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<Organization> orgList) {
        this.orgList = orgList;
    }

    public MetadataType[] getTypeList() {
        return typeList;
    }

    public void setTypeList(MetadataType[] typeList) {
        this.typeList = typeList;
    }

    public List<GroupAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<GroupAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}
