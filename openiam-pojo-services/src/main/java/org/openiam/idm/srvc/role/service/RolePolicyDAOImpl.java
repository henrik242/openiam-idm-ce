package org.openiam.idm.srvc.role.service;
// Generated Mar 4, 2008 1:12:08 AM by Hibernate Tools 3.2.0.b11


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.role.domain.RolePolicyEntity;

/**
 * Data access interface for domain model class RoleAttribute.
 * @see org.openiam.idm.srvc.role.dto.RoleAttribute
 */
public class RolePolicyDAOImpl implements RolePolicyDAO {

    private static final Log log = LogFactory.getLog(RolePolicyDAOImpl.class);

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
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.RoleAttributeDAO#add(org.openiam.idm.srvc.role.dto.RoleAttribute)
	 */
    public void add(RolePolicyEntity transientInstance) {
        log.debug("persisting RoleAttribute instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    

    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.RoleAttributeDAO#remove(org.openiam.idm.srvc.role.dto.RoleAttribute)
	 */
    public void remove(RolePolicyEntity persistentInstance) {
        log.debug("deleting RoleAttribute instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.RoleAttributeDAO#update(org.openiam.idm.srvc.role.dto.RoleAttribute)
	 */
    public RolePolicyEntity update(RolePolicyEntity detachedInstance) {
        log.debug("merging RoleAttribute instance");
        try {
        	RolePolicyEntity result = (RolePolicyEntity) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (HibernateException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public RolePolicyEntity findById( java.lang.String id) {
        log.debug("getting RoleAttribute instance with id: " + id);
        try {
        	RolePolicyEntity instance = (RolePolicyEntity) sessionFactory.getCurrentSession()
                    .get(RolePolicyEntity.class, id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (HibernateException re) {
            log.error("get failed", re);
            throw re;
        }
    }


	public List<RolePolicyEntity> findRolePolicies(String serviceId, String roleId) {
		 try {
			Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(RolePolicyEntity.class)
                     .add(Restrictions.eq("serviceId",serviceId))
                     .add(Restrictions.eq("roleId",roleId))
                     .addOrder(Order.asc("executionOrder"));

			List<RolePolicyEntity> results = (List<RolePolicyEntity>)criteria.list();
			return results;
		 }catch(HibernateException he ) {
	            log.error("get failed", he);
	            throw he;			 
		 }
		
	}
    

    


}

