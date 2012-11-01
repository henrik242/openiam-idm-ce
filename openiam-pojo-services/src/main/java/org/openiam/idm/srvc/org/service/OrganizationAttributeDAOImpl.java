package org.openiam.idm.srvc.org.service;

// Generated Jun 12, 2007 10:46:15 PM by Hibernate Tools 3.2.0.beta8

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;

/**
 *  Data access object implementation for OrganizationAttribute. 
 * @see org.openiam.idm.srvc.org.dto.OrganizationAttribute
 * @author Hibernate Tools
 */
public class OrganizationAttributeDAOImpl implements OrganizationAttributeDAO {



	private static final Log log = LogFactory
			.getLog(OrganizationAttributeDAOImpl.class);

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


	public OrganizationAttributeEntity findById(java.lang.String id) {
		log.debug("getting CompanyAttribute instance with id: " + id);
		try {
			OrganizationAttributeEntity instance = (OrganizationAttributeEntity) sessionFactory
					.getCurrentSession().get(OrganizationAttributeEntity.class, id);
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

	public void add(OrganizationAttributeEntity instance) {
		log.debug("persisting OrganizationAttribute instance");
		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
		
	}

	public void remove(OrganizationAttributeEntity instance) {
		log.debug("deleting OrganizationAttribute instance");
		try {
			sessionFactory.getCurrentSession().delete(instance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}	
		
	}

	public void update(OrganizationAttributeEntity instance) {
		log.debug("merging Organization instance");
		try {
			sessionFactory.getCurrentSession().merge(instance);
			log.debug("merge successful");
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}		
	}
	
	/**
	 * Return a list of OrganizationAttribute objects for the organization that is specified by the parentId
	 * @param parentId
	 * @return
	 */
	public List<OrganizationAttributeEntity> findAttributesByParent(String parentId) {
		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(OrganizationAttributeEntity.class)
                .add(Restrictions.eq("organization.orgId",parentId))
                .addOrder(Order.asc("name"));
		List<OrganizationAttributeEntity> results = (List<OrganizationAttributeEntity>)criteria.list();
		return results;
	}
	/**
	 * Removes all the OrganizationAttributes that are associated with the Organization specified by the parentId.
	 * @param parentId
	 * @return The number of entities deleted.
	 */
	public int removeAttributesByParent(String parentId) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity oa " +
					" where oa.organization.orgId = :parentId ");
		qry.setString("parentId", parentId);
		return qry.executeUpdate();		
	}

	

}
