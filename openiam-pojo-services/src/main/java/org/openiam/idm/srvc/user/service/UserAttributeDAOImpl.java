package org.openiam.idm.srvc.user.service;

// Generated Jun 12, 2007 10:46:15 PM by Hibernate Tools 3.2.0.beta8

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
import org.openiam.idm.srvc.user.domain.UserAttributeEntity;

/**
 * Home object for domain model class UserAttribute.
 * @see org.openiam.idm.srvc.user.dto.UserAttribute
 * @author Hibernate Tools
 */
public class UserAttributeDAOImpl implements UserAttributeDAO {

	private static final Log log = LogFactory.getLog(UserAttributeDAOImpl.class);

	private SessionFactory sessionFactory;

	
	public void setSessionFactory(SessionFactory session) {
		   this.sessionFactory = session;
	}
	
	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void add(UserAttributeEntity transientInstance) {
		log.debug("persisting UserAttribute instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UserAttributeEntity instance) {
		log.debug("attaching dirty UserAttribute instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserAttributeEntity instance) {
		log.debug("attaching clean UserAttribute instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void remove(UserAttributeEntity persistentInstance) {
		log.debug("deleting UserAttribute instance");
		try {
			log.info("delete attribute=" + persistentInstance);
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserAttributeEntity update(UserAttributeEntity detachedInstance) {
		log.debug("merging UserAttribute instance");
		try {
			UserAttributeEntity result = (UserAttributeEntity) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UserAttributeEntity findById(java.lang.String id) {
		log.debug("getting UserAttribute instance with id: " + id);
		try {
			UserAttributeEntity instance = (UserAttributeEntity) sessionFactory
					.getCurrentSession().get(UserAttributeEntity.class, id);
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
	
	public List<UserAttributeEntity> findUserAttributes(String userId) {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserAttributeEntity.class).add(Restrictions.eq("user.userId",userId)).addOrder(Order.asc("name"));
		List<UserAttributeEntity> results = (List<UserAttributeEntity>)criteria.list();
		return results;
	}
	
	public void deleteUserAttributes(String userId) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.user.domain.UserAttributeEntity ua " +
					" where ua.user.userId = :userId ");
		qry.setString("userId", userId);
		qry.executeUpdate();

		
	}

}

