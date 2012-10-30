package org.openiam.idm.srvc.org.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.domain.UserAffiliationEntity;


import javax.naming.InitialContext;
import java.util.List;

/**
 * DAO implementation for the UserRole. Manages the relationship between user and role.
 * @see org.openiam.idm.srvc.role.dto.UserRole
 * @author Hibernate Tools
 */
public class UserAffiliationDAOImpl implements UserAffiliationDAO {

	private static final Log log = LogFactory.getLog(UserAffiliationDAOImpl.class);

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
	public void add(UserAffiliationEntity transientInstance) {
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
	public void remove(UserAffiliationEntity persistentInstance) {
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
	public UserAffiliationEntity update(UserAffiliationEntity detachedInstance) {
		log.debug("merging UserRole instance");
		try {
			UserAffiliationEntity result = (UserAffiliationEntity) sessionFactory.getCurrentSession()
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
	public UserAffiliationEntity findById(String id) {
		log.debug("getting UserRole instance with id: " + id);
		try {
			UserAffiliationEntity instance = (UserAffiliationEntity) sessionFactory.getCurrentSession()
					.get(UserAffiliationEntity.class, id);
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
	public List<UserAffiliationEntity> findUserOrgByUser(String userId) {

		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserAffiliationEntity.class)
                .createAlias("user","user")
                .add(Restrictions.eq("user.userId", userId))
                .addOrder(Order.asc("user.userId"));

		List<UserAffiliationEntity> result = (List<UserAffiliationEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result;
	}

	public List<OrganizationEntity> findOrgAffiliationsByUser(String userId) {
		Session session = sessionFactory.getCurrentSession();

		Query qry = session.createQuery("select org from org.openiam.idm.srvc.org.domain.OrganizationEntity as org, org.openiam.idm.srvc.org.domain.UserAffiliationEntity ua " +
						" where ua.user.userId = :userId and ua.organization.orgId = org.orgId " +
						" order by org.organizationName ");
		
		qry.setString("userId",userId);

		List<OrganizationEntity> result = (List<OrganizationEntity>)qry.list();
		if (result == null || result.size() == 0)
			return null;
		return result;			
	}
	
	public void removeUserFromOrg(String orgId, String userId) {

		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.org.domain.UserAffiliationEntity ur " +
					" where  ur.organization.orgId = :orgId and ur.user.userId = :userId ");
		qry.setString("orgId", orgId);
		qry.setString("userId", userId);
		qry.executeUpdate();
	}

	public void removeAllUsersInOrg(String orgId) {

		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.org.domain.UserAffiliationEntity ur " +
					" where ur.organization.orgId = :orgId  ");
		qry.setString("orgId", orgId);
		qry.executeUpdate();			
	}

}
