package org.openiam.idm.srvc.org.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.dto.UserAffiliation;


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
	public void add(UserAffiliation transientInstance) {
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
	public void remove(UserAffiliation persistentInstance) {
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
	public UserAffiliation update(UserAffiliation detachedInstance) {
		log.debug("merging UserRole instance");
		try {
			UserAffiliation result = (UserAffiliation) sessionFactory.getCurrentSession()
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
	public UserAffiliation findById(String id) {
		log.debug("getting UserRole instance with id: " + id);
		try {
			UserAffiliation instance = (UserAffiliation) sessionFactory.getCurrentSession()
					.get("org.openiam.idm.srvc.org.dto.UserAffiliation", id);
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

	public List<UserAffiliation> findUserOrgByUser(String userId) {
		
		
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("select ur from org.openiam.idm.srvc.org.dto.UserAffiliation ur " +
						" where ur.userId = :userId " +
						" order by ur.userId ");
		
		qry.setString("userId", userId);
		List<UserAffiliation> result = (List<UserAffiliation>)qry.list();
		if (result == null || result.size() == 0)
			return null;
		return result;			
	}
	
	public List<Organization> findOrgAffiliationsByUser(String userId) {
		

		
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("select org from org.openiam.idm.srvc.org.domain.OrganizationEntity as org, org.openiam.idm.srvc.org.dto.UserAffiliation ua " +
						" where ua.userId = :userId and ua.organizationId = org.orgId " +
						" order by org.organizationName ");
		
		qry.setString("userId",userId);

		List<Organization> result = (List<Organization>)qry.list();
		if (result == null || result.size() == 0)
			return null;
		return result;			
	}
	
	public void removeUserFromOrg(String orgId, String userId) {

		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.org.dto.UserAffiliation ur " +
					" where  ur.organizationId = :orgId and ur.userId = :userId ");
		qry.setString("orgId", orgId);
		qry.setString("userId", userId);
		qry.executeUpdate();	
	}

	public void removeAllUsersInOrg(String orgId) {

		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.org.dto.UserAffiliation ur " +
					" where ur.organizationId = :orgId  ");
		qry.setString("orgId", orgId);
		qry.executeUpdate();			
	}

}
