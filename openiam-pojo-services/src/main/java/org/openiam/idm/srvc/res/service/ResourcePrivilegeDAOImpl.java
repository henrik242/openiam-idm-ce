package org.openiam.idm.srvc.res.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;

import javax.naming.InitialContext;
import java.util.List;

/**
 * DAO implementation to capture the entitlements that are linked to a resource.
 * User: arun
 */
public class ResourcePrivilegeDAOImpl implements ResourcePrivilegeDAO {
    
    /** The Constant log. */
    private static final Log log = LogFactory.getLog(ResourcePrivilegeDAOImpl.class);
	
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

	




	
    //==================================================================
	
	
    public ResourcePrivilegeEntity add(ResourcePrivilegeEntity instance) {
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

    public void remove(ResourcePrivilegeEntity instance) {
        log.debug("deleting instance");
        try {
            sessionFactory.getCurrentSession().delete(instance);
            log.debug("delete successful");
        } catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }			
    }

    public ResourcePrivilegeEntity update(ResourcePrivilegeEntity instance) {

        try {
            sessionFactory.getCurrentSession().merge(instance);
            log.debug("merge successful");
            return instance;
        } catch (HibernateException re) {
            log.error("merge failed", re);
            throw re;
        }		
    }


    @Override
    public ResourcePrivilegeEntity findById(String id) {
        log.debug("getting ResourceProp instance with id: " + id);
        try {
            ResourcePrivilegeEntity instance = (ResourcePrivilegeEntity) sessionFactory.getCurrentSession()
                    .get(ResourcePrivilegeEntity.class, id);
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

    @Override
    public List<ResourcePrivilegeEntity> findPrivilegesByResourceId(String resourceId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(ResourcePrivilegeEntity.class)
                    .add(Restrictions.eq("resourceId",resourceId))
                    .addOrder(Order.asc("name"));

            List<ResourcePrivilegeEntity> result = (List<ResourcePrivilegeEntity>)criteria.list();
            if (result == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return result;
        } catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }

    }

    @Override
    public List<ResourcePrivilegeEntity> findPrivilegesByEntitlementType(String resourceId, String type) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(ResourcePrivilegeEntity.class)
                    .add(Restrictions.eq("resourceId",resourceId))
                    .add(Restrictions.eq("privilegeType",type))
                    .addOrder(Order.asc("name"));

            List<ResourcePrivilegeEntity> result = (List<ResourcePrivilegeEntity>)criteria.list();
            if (result == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return result;
        } catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
}
