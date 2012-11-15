package org.openiam.idm.srvc.role.service;


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.role.domain.UserRoleEntity;
import org.openiam.idm.srvc.role.dto.UserRole;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.dto.User;

import static org.hibernate.criterion.Example.create;

/**
 * DAO implementation for the UserRole. Manages the relationship between user and role.
 * @see org.openiam.idm.srvc.role.dto.UserRole
 * @author Hibernate Tools
 */
public class UserRoleDAOImpl implements UserRoleDAO {

	private static final Log log = LogFactory.getLog(UserRoleDAOImpl.class);

	private SessionFactory sessionFactory;

	
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

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.UserRoleDAO#add(org.openiam.idm.srvc.role.dto.UserRole)
	 */
	public void add(UserRoleEntity transientInstance) {
		log.debug("persisting UserRole instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.UserRoleDAO#remove(org.openiam.idm.srvc.role.dto.UserRole)
	 */
	public void remove(UserRoleEntity persistentInstance) {
		log.debug("deleting UserRole instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.UserRoleDAO#update(org.openiam.idm.srvc.role.dto.UserRole)
	 */
	public UserRoleEntity update(UserRoleEntity detachedInstance) {
		log.debug("merging UserRole instance");
		try {
			UserRoleEntity result = (UserRoleEntity) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.UserRoleDAO#findById(java.lang.String)
	 */
	public UserRoleEntity findById(java.lang.String id) {
		log.debug("getting UserRole instance with id: " + id);
		try {
			UserRoleEntity instance = (UserRoleEntity) sessionFactory.getCurrentSession()
					.get(UserRoleEntity.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<UserRoleEntity> findUserRoleByUser(String userId) {
		
		
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserRoleEntity.class)
                .add(Restrictions.eq("userId",userId))
                .addOrder(Order.asc("roleId"));

		List<UserRoleEntity> result = (List<UserRoleEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result;			
	}
	
	public List<UserEntity> findUserByRole(String domainId, String roleId) {
		
		log.debug("findUserByRole: domainId=" + domainId);
		log.debug("findUserByRole: roleId=" + roleId);
		
		Session session = sessionFactory.getCurrentSession();

		Query qry = session.createQuery("select usr from org.openiam.idm.srvc.user.domain.UserEntity as usr, UserRoleEntity ur " +
						" where ur.serviceId = :domainId and ur.roleId = :roleId and ur.userId = usr.userId " +
						" order by usr.lastName, usr.firstName ");
		
		qry.setString("domainId",domainId);
		qry.setString("roleId",roleId);
		List<UserEntity> result = (List<UserEntity>)qry.list();
		if (result == null || result.size() == 0)
			return null;
		return result;			
	}
	
	public void removeUserFromRole(String domainId, String roleId,	String userId) {
		log.debug("removeUserFromRole: userId=" + userId);
		log.debug("removeUserFromRole: roleId=" + roleId);
		
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.role.domain.UserRoleEntity ur " +
					" where ur.roleId = :roleId and ur.serviceId = :domainId and ur.userId = :userId ");
		qry.setString("roleId", roleId);
		qry.setString("domainId", domainId);
		qry.setString("userId", userId);
		qry.executeUpdate();	
	}

	public void removeAllUsersInRole(String domainId, String roleId) {
		log.debug("removeUserFromRole: roleId=" + roleId);
		
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.role.domain.UserRoleEntity ur " +
					" where ur.roleId = :roleId and ur.serviceId = :domainId ");
		qry.setString("roleId", roleId);
		qry.setString("domainId", domainId);
		qry.executeUpdate();			
	}

}
