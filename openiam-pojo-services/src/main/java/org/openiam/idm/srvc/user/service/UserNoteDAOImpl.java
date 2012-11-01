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
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.user.domain.UserNoteEntity;

/**
 * Home object for domain model class UserNote.
 * @see org.openiam.idm.srvc.user.dto.UserNote
 * @author Hibernate Tools
 */
public class UserNoteDAOImpl implements UserNoteDAO {

	private static final Log log = LogFactory.getLog(UserNoteDAOImpl.class);

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

	public void persist(UserNoteEntity transientInstance) {
		log.debug("persisting UserNoteEntity instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UserNoteEntity instance) {
		log.debug("attaching dirty UserNoteEntity instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserNoteEntity instance) {
		log.debug("attaching clean UserNoteEntity instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UserNoteEntity persistentInstance) {
		log.debug("deleting UserNoteEntity instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserNoteEntity merge(UserNoteEntity detachedInstance) {
		log.debug("merging UserNote instance");
		try {
			UserNoteEntity result = (UserNoteEntity) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UserNoteEntity findById(java.lang.String id) {
		log.debug("getting UserNote instance with id: " + id);
		try {
			UserNoteEntity instance = (UserNoteEntity) sessionFactory.getCurrentSession()
					.get(UserNoteEntity.class, id);
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
	/**
	 * Delete all the notes associated with a user.
	 * @param userId
	 */
	public void deleteUserNotes(String userId) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.user.domain.UserNoteEntity un " +
                " where un.user.userId = :userId ");
		qry.setString("userId", userId);
		qry.executeUpdate();

		
	}

	public List<UserNoteEntity> findUserNotes(String userId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserNoteEntity.class)
                .add(Restrictions.eq("user.userId",userId))
                .addOrder(Order.asc("userNoteId"));
		List<UserNoteEntity> results = (List<UserNoteEntity>)criteria.list();
		return results;
	}	
	
	public List findByExample(UserNoteEntity instance) {
		log.debug("finding UserNoteEntity instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(UserNoteEntity.class)
                    .add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
