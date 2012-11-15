package org.openiam.idm.srvc.grp.service;

// Generated Jun 12, 2007 10:46:15 PM by Hibernate Tools 3.2.0.beta8

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.HibernateException;
import org.openiam.exception.data.DataException;
import org.openiam.exception.data.ObjectNotFoundException;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.domain.UserGroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;

import org.openiam.idm.srvc.user.service.UserDAO;
/**
 * Data access object interface for Group. 
 * @see org.openiam.idm.srvc.grp.dto.Group
 * @author Suneet Shah
 */
public class GroupDAOImpl implements org.openiam.idm.srvc.grp.service.GroupDAO {

	protected UserDAO userDao;

	private static final Log log = LogFactory.getLog(GroupDAOImpl.class);

	private SessionFactory sessionFactory;
	private Integer maxResultSetSize;

	
	public void setSessionFactory(SessionFactory session) {
		   this.sessionFactory = session;
	}

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void add(GroupEntity instance) {
		log.debug("persisting Group instance");
		try {
			sessionFactory.getCurrentSession().persist(instance);	
		} catch (HibernateException re) {
			log.error("Group save failed.", re);
			throw new DataException( re.getMessage(), re.getCause() ); 
		}		
	}
	


	public int remove(GroupEntity instance) {
		log.debug("deleting Group instance");
		try {
			sessionFactory.getCurrentSession().delete(instance);
			log.debug("Group id=" + instance.getGrpId() + " successfully updated.");
			return 1;
		} catch (HibernateException re) {
			log.error("Group delete failed", re);
			throw new DataException( re.getMessage(), re.getCause() ); 
		}		
	}

	public void update(GroupEntity instance) {
		log.debug("merging Group instance. GrpId = " + instance.getGrpId());
		try {
			sessionFactory.getCurrentSession().merge(instance);
			log.debug("Group id=" + instance.getGrpId() + " successfully updated.");
		} catch (HibernateException re) {
			log.error("Group update failed", re);
			throw new DataException( re.getMessage(), re.getCause() ); 
		}	
	}
	
	

	public GroupEntity findById(java.lang.String id) {
	
		return findById(id,false);
	
	}

	public GroupEntity findById(java.lang.String id, boolean dependants) {
		log.debug("getting Grp instance with id: " + id);
		try {
			GroupEntity instance = (GroupEntity) sessionFactory.getCurrentSession().get(
					GroupEntity.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			
			return instance;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw new DataException( re.getMessage(), re.getCause() ); 
		}
	}
	
	public List<GroupEntity> search(GroupSearch search) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(GroupEntity.class, "grp");
		crit.setMaxResults(maxResultSetSize);
		
		if (search.getGrpId() != null && search.getGrpId().length() > 0 ) {
			log.debug("search: grpId=" + search.getGrpId() );
			crit.add(Restrictions.eq("grpId",search.getGrpId()));
		}
		if (search.getGrpName() != null && search.getGrpName().length() > 0 ) {
			log.debug("search: grpName=" + search.getGrpName() );
			crit.add(Restrictions.like("grpName",search.getGrpName()));
		}
		if (search.getOwnerId() != null && search.getOwnerId().length() > 0 ) {
			log.debug("search: ownerId=" + search.getOwnerId() );
			crit.add(Restrictions.eq("ownerId",search.getOwnerId()));
		}
		if (search.getInternalGroupId() != null && search.getInternalGroupId().length() > 0 ) {
			log.debug("search: interGroupId=" + search.getInternalGroupId() );
			crit.add(Restrictions.eq("internalGroupId",search.getInternalGroupId()));
		}
		crit.addOrder(Order.asc("grpName"));
		
		List<GroupEntity> results = (List<GroupEntity>)crit.list();
		return results;		
	}
	
	public List<GroupEntity> findRootGroups() {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(GroupEntity.class)
                .add(Restrictions.isNull("parentGrpId"))
                .addOrder(Order.asc("grpId"));

	//	qry.setCacheable(true);
		List<GroupEntity> results = (List<GroupEntity>)criteria.list();
		return results;
	}

	public List<GroupEntity> findAllGroups() {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(GroupEntity.class)
                .addOrder(Order.asc("grpName"));

	//	qry.setCacheable(true);
		List<GroupEntity> results = (List<GroupEntity>)criteria.list();
		return results;
	}

	


	
	public List<GroupEntity> findChildGroup(String parentGroupId) {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(GroupEntity.class)
                .add(Restrictions.eq("parentGrpId", parentGroupId))
                .addOrder(Order.asc("grpId"));

	//	qry.setCacheable(true);
		List<GroupEntity> results = (List<GroupEntity>)criteria.list();
		return results;
	}

	/**
	 * Removes the groups specified by the groupIdList. groupIdList is a string containing a concatenated
	 * list of groupIds.
	 * @param groupIdList
	 * @return The number of entities deleted.
	 */
	public int removeGroupList(String groupIdList) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.grp.domain.GroupEntity g  " +
					" where g.grpId in (" + groupIdList + ")" );
		return qry.executeUpdate();		
	}	
	
	public GroupEntity findParent(String groupId, boolean dependants) {
				
		// get the group object for the groupId
		GroupEntity curGroup = findById(groupId);
		// TODO Throw exception if the curGroup is null. That means that there is no group for this groupId
		if (curGroup == null) {
			log.error("Group for groupId=" + groupId + "  not found.");
			throw new ObjectNotFoundException();
		}
		
		if (curGroup.getParentGrpId() == null ) {
			log.debug("groupId=" + groupId + " does not contain a parentGroupId");
			return null;
		}
		
		// get the parent group object
		
		GroupEntity parentGroup = findById(curGroup.getParentGrpId());
		if (parentGroup == null) {
			log.error("Group for parent groupId=" + curGroup.getParentGrpId()  + "  not found.");
			throw new ObjectNotFoundException();					
		}

		return parentGroup;
		
	}
	
	/**
	 * Returns a list of Groups that a user is associated with
	 * @return
	 */
	public List<GroupEntity> findGroupsForUser(String userId) {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserGroupEntity.class)
                .add(Restrictions.eq("user.userId",userId))
                .setProjection(Projections.property("group"));

	//	Query qry = session.createQuery("select grp  from Group as grp, UserGroup ug " +
	//					" where ug.userId = :userId and grp.grpId = ug.grpId ");
		



		List<GroupEntity> result = (List<GroupEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result;			
	}
	
	public List<GroupEntity> findGroupNotLinkedToUser(String userId, String parentGroupId) {
		
	   	Session session = sessionFactory.getCurrentSession();
	    
    	try{
    		SQLQuery qry = session.createSQLQuery("SELECT  GRP_ID, GRP_NAME, CREATE_DATE, CREATED_BY, COMPANY_ID,  " +
    				" PARENT_GRP_ID, INHERIT_FROM_PARENT, PROVISION_METHOD, PROVISION_OBJ_NAME, " +
    				" TYPE_ID, GROUP_CLASS, GROUP_DESC, STATUS, LAST_UPDATE, LAST_UPDATED_BY, INTERNAL_GROUP_ID, OWNER_ID  " +
				 "  FROM 	GRP g  " +
				 "  WHERE g.GRP_ID NOT IN (SELECT GRP_ID FROM USER_GRP ug WHERE ug.USER_ID = :userId ) ");
	    	
	    	
	    	qry.addEntity(GroupEntity.class);
			qry.setString("userId", userId);
			
	
			List<GroupEntity> result = (List<GroupEntity>) qry.list();
			if (result == null || result.size() == 0)
				return null;
			return result;		
	    }catch(HibernateException re) {
			log.error("findGroupNotLinkedToUser", re);
			throw new DataException( re.getMessage(), re.getCause() );      		
		}

		
	}





	public UserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	public Integer getMaxResultSetSize() {
		return maxResultSetSize;
	}

	public void setMaxResultSetSize(Integer maxResultSetSize) {
		this.maxResultSetSize = maxResultSetSize;
	}
	
}


