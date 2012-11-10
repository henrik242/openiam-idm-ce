package org.openiam.idm.srvc.res.service;


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
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;


/**
 * DAO Implementation for ResourceProps.
 * 
 */
public class ResourcePropDAOImpl implements ResourcePropDAO  {

	private static final Log log = LogFactory.getLog(ResourcePropDAOImpl.class);

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

	public void persist(ResourcePropEntity transientInstance) {
		log.debug("persisting ResourceProp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}
	}


	public void remove(ResourcePropEntity persistentInstance) {
		log.debug("deleting ResourceProp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (HibernateException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ResourcePropEntity update(ResourcePropEntity detachedInstance) {
		log.debug("merging ResourceProp instance");
		try {
			ResourcePropEntity result = (ResourcePropEntity) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (HibernateException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ResourcePropEntity findById(java.lang.String id) {
		log.debug("getting ResourceProp instance with id: " + id);
		try {
			ResourcePropEntity instance = (ResourcePropEntity) sessionFactory.getCurrentSession()
					.get(ResourcePropEntity.class, id);
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

	public List<ResourcePropEntity> findByExample(ResourcePropEntity instance) {
		log.debug("finding ResourceProp instance by example");
		try {
			List<ResourcePropEntity> results = (List<ResourcePropEntity>) sessionFactory
					.getCurrentSession().createCriteria(ResourcePropEntity.class)
                    .add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (HibernateException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	//==================================================================
	
	
	public ResourcePropEntity add(ResourcePropEntity instance) {
		log.debug("persisting instance");
		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}		
	}

	public List<ResourcePropEntity> findAllResourceProps() {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourcePropEntity.class)
                .addOrder(Order.asc("resourcePropId"));

		List<ResourcePropEntity> result = (List<ResourcePropEntity>)criteria.list();
		
		return result;
	}
	
	public int removeAllResourceProps() {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete from org.openiam.idm.srvc.res.domain.ResourcePropEntity");
		return qry.executeUpdate();
	}
	
	
}
