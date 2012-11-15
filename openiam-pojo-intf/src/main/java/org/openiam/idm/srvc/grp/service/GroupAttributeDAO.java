package org.openiam.idm.srvc.grp.service;

import org.openiam.idm.srvc.grp.domain.GroupAttributeEntity;

import java.util.List;


/**
 * Data access object interface for GroupAttribute.
 *
 * @author Suneet Shah
 */
public interface GroupAttributeDAO {

    /**
     * Return an OrganizationAttribute object for the id.
     *
     * @param id
     */
    GroupAttributeEntity findById(java.lang.String id);

    void add(GroupAttributeEntity instance);

    void update(GroupAttributeEntity instace);

    void remove(GroupAttributeEntity instance);

    /**
     * Return a list of GroupAttribute objects for the organization that is specified by the parentId
     *
     * @param parentId
     * @return
     */
    List<GroupAttributeEntity> findAttributesByParent(String parentId);

    /**
     * Removes all the GroupAttribute that are associated with the Parent object specified by the parentId.
     *
     * @param parentId
     * @return The number of entities deleted.
     */
    int removeAttributesByParent(String parentId);

    /**
     * Removes the attributes for a list of groups specified by the groupIdList. groupIdList is a string containing a concatenated
     * list of groupIds.
     *
     * @param groupIdList
     * @return The number of entities deleted.
     */
    public int removeAttributesForGroupList(String groupIdList);


}
