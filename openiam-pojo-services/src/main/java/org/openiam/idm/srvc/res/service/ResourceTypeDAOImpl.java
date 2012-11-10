package org.openiam.idm.srvc.res.service;

// Generated Mar 8, 2009 12:54:32 PM by Hibernate Tools 3.2.2.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.res.domain.ResourceTypeEntity;
import org.openiam.idm.srvc.res.dto.*;

/**
 * DAO Implementation for ResourceType
 */
public class ResourceTypeDAOImpl implements ResourceTypeDAO {

	private static final Log log = LogFactory.getLog(ResourceTypeDAOImpl.class);

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



	public void persist(ResourceTypeEntity transientInstance) {
		log.debug("persisting ResourceType instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ResourceTypeEntity persistentInstance) {
		log.debug("deleting ResourceType instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (HibernateException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ResourceTypeEntity update(ResourceTypeEntity detachedInstance) {
		log.debug("merging ResourceType instance");
		try {
			ResourceTypeEntity result = (ResourceTypeEntity) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (HibernateException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ResourceTypeEntity findById(java.lang.String id) {
		log.debug("getting ResourceType instance with id: " + id);
		try {
			ResourceTypeEntity instance = (ResourceTypeEntity) sessionFactory
					.getCurrentSession().get(
							ResourceTypeEntity.class,
							id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<ResourceTypeEntity> findByExample(ResourceTypeEntity instance) {
		log.debug("finding ResourceType instance by example");
		try {
			List<ResourceTypeEntity> results = (List<ResourceTypeEntity>) sessionFactory
					.getCurrentSession().createCriteria(ResourceTypeEntity.class)
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (HibernateException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public ResourceTypeEntity add(ResourceTypeEntity instance) {
		log.debug("persisting ResourceType instance");		
		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
			return instance;
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public List<ResourceTypeEntity> findAllResourceTypes() {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceTypeEntity.class)
                .addOrder(Order.asc("resourceTypeId"));

		List<ResourceTypeEntity> result = (List<ResourceTypeEntity>)criteria.list();
		
		return result;
	}
	
	public int removeAllResourceTypes() {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete from org.openiam.idm.srvc.res.domain.ResourceTypeEntity");
		return qry.executeUpdate();
	}
	
	
	
}
