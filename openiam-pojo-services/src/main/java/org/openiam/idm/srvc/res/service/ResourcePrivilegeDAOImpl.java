package org.openiam.idm.srvc.res.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.openiam.idm.srvc.res.dto.ResourcePrivilege;

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
	
	
    public ResourcePrivilege add(ResourcePrivilege instance) {
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

    public void remove(ResourcePrivilege instance) {
        log.debug("deleting instance");
        try {
            sessionFactory.getCurrentSession().delete(instance);
            log.debug("delete successful");
        } catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }			
    }

    public ResourcePrivilege update(ResourcePrivilege instance) {

        try {
            sessionFactory.getCurrentSession().merge(instance);
            log.debug("merge successful");
            return instance;
        } catch (HibernateException re) {
            log.error("merge failed", re);
            throw re;
        }		
    }

    public List<ResourcePrivilege> findAllPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("from ResourcePrivilege a " +
                " order by a.ResourcePrivilege asc");
        qry.setCacheable(true);
        qry.setCacheRegion("query.resource.findAllPrivileges");
        List<ResourcePrivilege> result = (List<ResourcePrivilege>)qry.list();
		
		
        return result;
    }
	
    public int removeAllPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("delete from ResourcePrivilege");
        return qry.executeUpdate();
    }


    @Override
    public ResourcePrivilege findById(String id) {
        log.debug("getting ResourceProp instance with id: " + id);
        try {
            ResourcePrivilege instance = (ResourcePrivilege) sessionFactory.getCurrentSession()
                    .get("org.openiam.idm.srvc.res.dto.ResourcePrivilege", id);
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
    public List<ResourcePrivilege> findPrivilegesByResourceId(String resourceId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query qry = session.createQuery("from org.openiam.idm.srvc.res.dto.ResourcePrivilege rp " +
                    " where rp.resourceId = :resourceId " +
                    " order by rp.name asc");
            qry.setString("resourceId", resourceId);


            List<ResourcePrivilege> result = (List<ResourcePrivilege>)qry.list();
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
    public List<ResourcePrivilege> findPrivilegesByEntitlementType(String resourceId, String type) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query qry = session.createQuery("from org.openiam.idm.srvc.res.dto.ResourcePrivilege rp " +
                    " where rp.resourceId = :resourceId and rp.privilegeType = :type" +
                    " order by rp.name asc");
            qry.setString("resourceId", resourceId);
            qry.setString("type", type);


            List<ResourcePrivilege> result = (List<ResourcePrivilege>)qry.list();
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
