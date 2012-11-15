package org.openiam.idm.srvc.grp.service;

import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;

import java.util.List;


/**
 * Data access object interface for Group.
 *
 * @author Suneet Shah
 */
public interface GroupDAO {

    /**
     * Return an Group object for the id.
     *
     * @param id
     */
    GroupEntity findById(java.lang.String id);

    GroupEntity findById(java.lang.String id, boolean dependants);

    /**
     * Creates a new group.
     *
     * @param instance
     * @return - Number of records created. 0 if add failed to add any records
     */
    void add(GroupEntity instance);

    /**
     * Updates an existing group
     *
     * @param instance
     * @return - Number of records created. 0 if update failed to update any records
     */
    void update(GroupEntity instance);

    /**
     * Removes an existings group.
     *
     * @param instance
     * @return - Returns the number of records removed. 0 if no records were removed.
     */
    int remove(GroupEntity instance);

    /**
     * Adds a user to a group.
     * @param groupId
     * @param user
     * @return - Returns the number of users added to the group. 0 if users were added to the group.
     */
//	int addUserToGroup(String groupId, String user);
    /**
     * Removes a user from a group.
     * @param groupId
     * @param user
     * @return  - Returns the number of users removed from a group. 0 if no users were removed from the group.
     */
//	int removeUserFromGroup(String groupId, String user);


    /**
     * Returns an Group object that is the parent of the orgId specified.
     * Return null is no parent organizations are found.
     *
     * @param groupId
     * @return - null if no parent group is found.
     */
    GroupEntity findParent(String groupId, boolean dependants);


    /**
     * Returns a List of Group objects that are sub-groups of the specified
     * parentGroupId. Returns null if no children are found.
     *
     * @param parentGroupId
     * @return
     */
    List<GroupEntity> findChildGroup(String parentGroupId);

    /**
     * Return a list of root level group object. Root level group object do not have parent groups.
     *
     * @return
     */
    List<GroupEntity> findRootGroups();

    /**
     * Returns a list of Groups that a user is associated with
     *
     * @return
     */
    List<GroupEntity> findGroupsForUser(String userId);


    /**
     * Removes the groups specified by the groupIdList. groupIdList is a string containing a concatenated
     * list of groupIds.
     *
     * @param groupIdList
     * @return The number of entities deleted.
     */
    public int removeGroupList(String groupIdList);

    public List<GroupEntity> findAllGroups();

    //public User findUserInGroup(String groupId,String userId);

    public List<GroupEntity> findGroupNotLinkedToUser(String userId, String parentGroupId);

    public List<GroupEntity> search(GroupSearch search);


}
