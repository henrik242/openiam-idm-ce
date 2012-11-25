package org.openiam.idm.srvc.grp.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.dozer.converter.GroupAttributeDozerConverter;
import org.openiam.dozer.converter.GroupDozerConverter;
import org.openiam.dozer.converter.UserDozerConverter;
import org.openiam.idm.srvc.grp.domain.GroupAttributeEntity;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.domain.UserGroupEntity;
import org.openiam.idm.srvc.grp.dto.*;

import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.service.UserDAO;

import org.openiam.exception.data.DataException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <code>GroupDataServiceImpl</code> provides a service to manage groups as
 * well as related objects such as Users. Groups are stored in an hierarchical
 * relationship. A user belongs to one or more groups.<br>
 * Groups are often modeled after an organizations structure.
 *
 * @author Suneet Shah
 * @version 2.0
 */

public class GroupDataServiceImpl implements GroupDataService {
    protected GroupDAO groupDao;
    protected GroupAttributeDAO groupAttrDao;
    protected UserGroupDAO userGroupDao;
    protected UserDAO userDao;

    private static final Log log = LogFactory.getLog(GroupDataServiceImpl.class);

    @Autowired
	private GroupDozerConverter groupDozerConverter;
    @Autowired
	private GroupAttributeDozerConverter groupAttributeDozerConverter;
    @Autowired
	private UserDozerConverter userDozerConverter;

    public GroupDataServiceImpl() {

    }

    public GroupDataServiceImpl(GroupDAO groupDao) {
        super();
        this.groupDao = groupDao;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getAllGroups(boolean)
      */
    public List<Group> getAllGroupsWithDependents(boolean subgroups) {
        List<GroupEntity> groupEntityList = new LinkedList<GroupEntity>();
        if (!subgroups) {
            groupEntityList = groupDao.findRootGroups();
        } else {
            if (subgroups) {
                groupEntityList = getRecursiveChildGroup(null, groupEntityList);
            }
        }
        return null;

    }

    /**
     * Returns a list of Group objects that is flat in structure.
     *
     * @return
     */
    public List<Group> getAllGroups() {
        List<GroupEntity> groupEntityList = groupDao.findAllGroups();

        return groupDozerConverter.convertToDTOList(groupEntityList, false);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#addGroup(org.openiam.idm.srvc.grp.dto.Group)
      */
    public Group addGroup(Group grp) {
        if (grp == null) {
            log.debug("Group object is null. Unable to add a new group.");
            throw new NullPointerException("Group is null");
        }
        try {
            GroupEntity entity = groupDozerConverter.convertToEntity(grp, true);
            groupDao.add(entity);
            grp.setGrpId(entity.getGrpId());
            log.debug("Group id=" + grp.getGrpId() + " successfully saved.");
            return grp;
        } catch (Exception re) {
            log.error("Group save failed.", re);
            throw new DataException(re.getMessage(), re.getCause());
        }

    }


    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getGroup(java.lang.String)
      */
    public Group getGroup(String grpId) {
        if (grpId == null)
            throw new NullPointerException("grp id is null");
        GroupEntity entity = groupDao.findById(grpId);
        return groupDozerConverter.convertToDTO(entity, true);
    }

    /**
     * This method retrieves an existing group along with an dependant objects that it may
     * contain. null is returned if the groupId is not found.
     *
     * @param grpId
     * @return
     */
    public Group getGroupWithDependants(String grpId) {
        if (grpId == null)
            throw new NullPointerException("grp id is null");
        GroupEntity grp = groupDao.findById(grpId, true);
        return groupDozerConverter.convertToDTO(grp, true);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#updateGroup(org.openiam.idm.srvc.grp.dto.Group)
      */
    public Group updateGroup(Group grp) {
        if (grp == null) {
            log.debug("Group object is null. Unable to add a new group.");
            throw new NullPointerException("Group is null");
        }
        if (grp.getGrpId() == null) {
            log.debug("grpId in null. Unable to add a new group.");
            throw new NullPointerException("grp.GroupId is null");
        }
        GroupEntity groupEntity = groupDozerConverter.convertToEntity(grp,true);
        groupDao.update(groupEntity);

        return groupDozerConverter.convertToDTO(groupEntity, true);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#removeGroup(java.lang.String)
      */
    public void removeGroup(String grpId) {

        if (grpId == null) {
            log.debug("grpId in null. Unable to add a new group.");
            throw new NullPointerException("grp.GroupId is null");
        }


        GroupEntity grp = new GroupEntity();
        grp.setGrpId(grpId);

        removeChildGroups(grpId);
        this.groupAttrDao.removeAttributesByParent(grpId);
        groupDao.remove(grp);

    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getChildGroups(java.lang.String,
      *      boolean)
      */
    public List<Group> getChildGroups(String parentGroupId, boolean subgroups) {
        List<GroupEntity> groupEntityList = new LinkedList<GroupEntity>();

        if (!subgroups) {
            groupEntityList = groupDao.findChildGroup(parentGroupId);
        } else {
            if (subgroups) {
                groupEntityList = getRecursiveChildGroup(parentGroupId, groupEntityList);
            }
        }

        return groupDozerConverter.convertToDTOList(groupEntityList, false);

    }

    private List<GroupEntity> getRecursiveChildGroup(String parentGroupId, List<GroupEntity> groupList) {

        if (parentGroupId == null) {
            groupList = groupDao.findRootGroups();
        } else {
            groupList = groupDao.findChildGroup(parentGroupId);
        }
        if (groupList == null || groupList.isEmpty()) {
            return null;
        }
        int size = groupList.size();
        for (int i = 0; i < size; i++) {
            GroupEntity grp = groupList.get(i);
            // check for child group
            grp.setSubGroup(getRecursiveChildGroup(grp.getGrpId(), groupList));
        }
        return groupList;
    }

    private String getRecursiveGroupId(String parentGroupId,
                                       List<GroupEntity> groupList) {
        StringBuffer groupIdBuf = new StringBuffer();

        groupList = groupDao.findChildGroup(parentGroupId);
        if (groupList == null || groupList.isEmpty()) {
            return null;
        }
        int size = groupList.size();
        for (int i = 0; i < size; i++) {
            GroupEntity grp = groupList.get(i);
            if (groupIdBuf.length() > 0) {
                groupIdBuf.append(",");
            }
            groupIdBuf.append(" '" + grp.getGrpId() + "' ");

            // check for child group

            String groupStr = getRecursiveGroupId(grp.getGrpId(), groupList);
            if (groupStr != null) {
                if (groupIdBuf.length() > 0) {
                    groupIdBuf.append(",");
                }
                groupIdBuf.append(groupStr);
            }
            log.debug("Groupids after = " + groupIdBuf.toString());
        }
        return groupIdBuf.toString();
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#removeChildGroups(java.lang.String)
      */
    public int removeChildGroups(String parentGroupId) {

        if (parentGroupId == null) {
            log.debug("parentGroupId in null");
            throw new NullPointerException("parentGroupId is null");
        }

        StringBuffer groupIdBuf = new StringBuffer();
        List<GroupEntity> groupList = new LinkedList<GroupEntity>();

        groupList = groupDao.findChildGroup(parentGroupId);

        if (groupList == null || groupList.isEmpty()) {
            // nothing to delete
            return 0;
        }
        int size = groupList.size();
        for (int i = 0; i < size; i++) {
            GroupEntity grp = groupList.get(i);
            if (groupIdBuf.length() > 0) {
                groupIdBuf.append(",");
            }
            groupIdBuf.append(" '" + grp.getGrpId() + "' ");

            String groupStr = getRecursiveGroupId(grp.getGrpId(), groupList);
            if (groupStr != null) {
                if (groupIdBuf.length() > 0) {
                    groupIdBuf.append(",");
                }
                groupIdBuf.append(groupStr);
            }

        }

        // remove dependant objects first
        String groupIdList = groupIdBuf.toString();
        this.groupAttrDao.removeAttributesForGroupList(groupIdList);
        return groupDao.removeGroupList(groupIdList);

    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getParentGroup(java.lang.String)
      */
    public Group getParentGroup(String groupId, boolean dependants) {
        if (groupId == null)
            throw new NullPointerException("parentGroupId id is null");

        GroupEntity entity = groupDao.findParent(groupId, dependants);
        return groupDozerConverter.convertToDTO(entity, true);
    }


    /**
     * /* --------------------- Group Methods Related to Users --------------
     */

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#isUserInGroup(java.lang.String)
      */
    public boolean isUserInGroup(String groupId, String userId) {
        if (groupId == null)
            throw new NullPointerException("parentGroupId id is null");

        // search the collection as we have hierarchical groups
        //List<UserGroup> u = userGroupDao.findUserInGroup(groupId, userId);
        List<Group> userGroupList = getUserInGroupsAsFlatList(userId);
        if (userGroupList == null) {
            return false;
        }
        for (Group grp : userGroupList) {
            if (grp.getGrpId().equalsIgnoreCase(groupId)) {
                return true;
            }

        }
        return false;

    }


    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getUserGroups(java.lang.String)
      */
    /*public List<Group> getUserInGroups(String userId) {
            if (userId == null)
                throw new NullPointerException("userId id is null");

            List<Group> groupList = groupDao.findGroupsForUser(userId);
            if (groupList == null || groupList.size() == 0)
                return null;
            return groupList;
        }
    */
    private GroupEntity getParentGroup(GroupEntity grp) {
        GroupEntity pGroup = groupDao.findById(grp.getParentGrpId());
        log.info("Got parent group = " + pGroup);
        if (pGroup != null) {
            // add the child group to the parentGroup
            pGroup.getSubGroup().add(grp);
        }
        if (pGroup.getParentGrpId() != null) {
            log.info("Found another parent group - make a recursive call. parentId =" + pGroup.getParentGrpId());
            return getParentGroup(pGroup);
        }
        return pGroup;
    }

    public List<Group> getUserInGroups(String userId) {
        if (userId == null)
            throw new NullPointerException("userId id is null");

        List<GroupEntity> groupList = groupDao.findGroupsForUser(userId);
        if (groupList == null || groupList.size() == 0)
            return null;
        // Check if these groups have any parent groups that we need to get
        List<GroupEntity> newGroupList = new LinkedList<GroupEntity>();
        for (GroupEntity grp : groupList) {
            log.info("Group id=" + grp.getGrpId() + " parentId=" + grp.getParentGrpId());
            if (grp.getParentGrpId() == null) {
                newGroupList.add(grp);
            } else {
                log.info("Get the parent group for parentId=" + grp.getParentGrpId());
                newGroupList.add(getParentGroup(grp));
            }
        }

        return groupDozerConverter.convertToDTOList(newGroupList, false);
    }

    private List<GroupEntity> getParentGroupFlat(GroupEntity grp) {
        List<GroupEntity> groupList = new LinkedList<GroupEntity>();
        GroupEntity pGroup = groupDao.findById(grp.getParentGrpId());
        log.info("Got parent group = " + pGroup);
        if (pGroup != null) {
            // add the child group to the list of groups
            groupList.add(pGroup);
        }
        if (pGroup.getParentGrpId() != null) {
            log.info("Found another parent group - make a recursive call. parentId =" + pGroup.getParentGrpId());
            groupList.addAll(getParentGroupFlat(pGroup));
        }
        return groupList;
    }

    public List<Group> getUserInGroupsAsFlatList(String userId) {
        if (userId == null)
            throw new NullPointerException("userId id is null");

        List<GroupEntity> groupList = groupDao.findGroupsForUser(userId);
        if (groupList == null || groupList.size() == 0)
            return null;
        // Check if these groups have any parent groups that we need to get
        List<GroupEntity> newGroupList = new LinkedList<GroupEntity>();
        for (GroupEntity grp : groupList) {
            log.info("Group id=" + grp.getGrpId() + " parentId=" + grp.getParentGrpId());
            if (grp.getParentGrpId() == null) {
                newGroupList.add(grp);
            } else {
                log.info("Get the parent group for parentId=" + grp.getParentGrpId());
                newGroupList.add(grp);
                newGroupList.addAll(getParentGroupFlat(grp));
            }
        }

        return groupDozerConverter.convertToDTOList(newGroupList, false);
    }


    /**
     * Return an array of Groups that a user does not belong to
     *
     * @param userId
     * @param nested        - True, traverse the group hierarchy.  False, search the current hierarchy
     * @param parentGroupId - Group where the traversing will start
     * @return
     */
    public List<Group> getGroupsNotLinkedToUser(String userId, String parentGroupId, boolean nested) {

        if (userId == null)
            throw new NullPointerException("userId id is null");

        List<GroupEntity> groupEntityList = groupDao.findGroupNotLinkedToUser(userId, parentGroupId);

        return groupDozerConverter.convertToDTOList(groupEntityList, false);
    }


    /**
     * This method adds the user to a group .<br>
     * For example:
     * <p/>
     * <code>
     * grpManager.addUserToGroup(groupId,userId);<br>
     * </code>
     *
     * @param userId  User to be added to group.
     * @param groupId Group to which user will be added .
     */
    public void addUserToGroup(String groupId, String userId) {
        if (groupId == null)
            throw new NullPointerException("grpId id is null");
        if (userId == null)
            throw new NullPointerException("userId id is null");

        log.debug("addUserToGroup:UserId = " + userId);
        log.debug("addUserToGroup:groupId = " + groupId);
        UserEntity user = userDao.findById(userId);
        GroupEntity group = groupDao.findById(groupId);
        UserGroupEntity ug = new UserGroupEntity(user, group);
        userGroupDao.add(ug);

    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getUsersByGroupId(java.lang.String)
      */
    public List<User> getUsersByGroup(String grpId) {
        if (grpId == null)
            throw new NullPointerException("grpId id is null");

        List<UserEntity> userEntities = userGroupDao.findUserByGroup(grpId);

        return userDozerConverter.convertToDTOList(userEntities, false);
    }


    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#removeUserFromGroup(java.lang.String,
      *      java.lang.String)
      */
    public void removeUserFromGroup(String groupId, String userId) {
        if (groupId == null)
            throw new NullPointerException("grpId id is null");
        if (userId == null)
            throw new NullPointerException("userId id is null");

        userGroupDao.removeUserFromGroup(groupId, userId);

    }


    /* -------- Methods for Attributes ---------- */

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#addAttribute(org.openiam.idm.srvc.grp.dto.GroupAttribute)
      */
    public void addAttribute(GroupAttribute attribute) {
        if (attribute == null)
            throw new NullPointerException("Attribute can not be null");

        groupAttrDao.add(groupAttributeDozerConverter.convertToEntity(attribute,true));

    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#updateAttribute(org.openiam.idm.srvc.grp.dto.GroupAttribute)
      */
    public void updateAttribute(GroupAttribute attribute) {
        if (attribute == null)
            throw new NullPointerException("Attribute can not be null");
        if (attribute.getId() == null) {
            throw new NullPointerException("Attribute id is null");
        }
        if (attribute.getGroupId() == null) {
            throw new NullPointerException(
                    "Group has not been associated with this attribute.");
        }

        groupAttrDao.update(groupAttributeDozerConverter.convertToEntity(attribute, true));

    }

    /**
     * Saves a collection of attributes. The method will determine if the add or update methods should be
     * called internally.
     */
    public void saveAttributes(GroupAttribute[] groupAttr) {
        if (groupAttr == null) {
            throw new NullPointerException("Attr array is null");
        }
        int size = groupAttr.length;
        for (int i = 0; i < size; i++) {
            if (groupAttr[i].getId() == null) {
                // add
                addAttribute(groupAttr[i]);
            } else {
                // update
                updateAttribute(groupAttr[i]);
            }
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getAllAttributes(java.lang.String)
      */
    public Map<String, GroupAttribute> getAllAttributes(String groupId) {

        Map<String, GroupAttribute> attrMap = null;

        if (groupId == null) {
            throw new NullPointerException("groupId is null");
        }

        GroupEntity grp = groupDao.findById(groupId);

        if (grp != null && grp.getAttributes() != null) {
            attrMap = new HashMap<String, GroupAttribute>();
            for (Map.Entry<String, GroupAttributeEntity> attributeEntityEntry : grp.getAttributes().entrySet()) {
                attrMap.put(attributeEntityEntry.getKey(), groupAttributeDozerConverter.convertToDTO(attributeEntityEntry.getValue(), true));
            }
        }
        return attrMap;

    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getAttribute(java.lang.String)
      */
    public GroupAttribute getAttribute(String attrId) {
        if (attrId == null) {
            throw new NullPointerException("attrId is null");
        }
        GroupAttributeEntity attributeEntity = groupAttrDao.findById(attrId);
        return groupAttributeDozerConverter.convertToDTO(attributeEntity, true);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#removeAttribute(org.openiam.idm.srvc.grp.dto.GroupAttribute)
      */
    public void removeAttribute(GroupAttribute attr) {
        if (attr == null) {
            throw new NullPointerException("attr is null");
        }
        if (attr.getId() == null) {
            throw new NullPointerException("attrId is null");
        }

        groupAttrDao.remove(groupAttributeDozerConverter.convertToEntity(attr, true));
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#removeAllAttributes(java.lang.String)
      */
    public void removeAllAttributes(String groupId) {
        if (groupId == null) {
            throw new NullPointerException("groupId is null");
        }
        this.groupAttrDao.removeAttributesByParent(groupId);
    }

    public List<Group> search(GroupSearch search) {
        if (search == null) {
            throw new NullPointerException("search object is null");
        }
        List<GroupEntity> entities = groupDao.search(search);

        return groupDozerConverter.convertToDTOList(entities, false);
    }

    /*--  Spring framework related getters and setters. ---*/

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getGroupAttrDao()
      */
    public GroupAttributeDAO getGroupAttrDao() {
        return groupAttrDao;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#setGroupAttrDao(org.openiam.idm.srvc.grp.service.GroupAttributeDAO)
      */
    public void setGroupAttrDao(GroupAttributeDAO groupAttrDao) {
        this.groupAttrDao = groupAttrDao;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#getGroupDao()
      */
    public GroupDAO getGroupDao() {
        return groupDao;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.openiam.idm.srvc.grp.service.GroupDataService#setGroupDao(org.openiam.idm.srvc.grp.service.GroupDAO)
      */
    public void setGroupDao(GroupDAO groupDao) {
        this.groupDao = groupDao;
    }


    public UserDAO getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    public UserGroupDAO getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDAO userGroupDao) {
        this.userGroupDao = userGroupDao;
    }


}
