package org.openiam.idm.srvc.res.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.openiam.idm.srvc.res.dto.PrivilegeDef;

import javax.naming.InitialContext;
import java.util.List;

import static org.hibernate.criterion.Example.create;

/**
 * Created by IntelliJ IDEA.
 * User: arun
 * Date: 8/5/11
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrivilegeDefDAOImpl implements PrivilegeDefDAO {
    
    /** The Constant log. */
    private static final Log log = LogFactory.getLog(PrivilegeDefDAOImpl.class);
	
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

	
    public void persist(PrivilegeDef transientInstance) {
        log.debug("persisting PrivilegeDef instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(PrivilegeDef instance) {
        log.debug("attaching dirty PrivilegeDef instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(PrivilegeDef instance) {
        log.debug("attaching clean PrivilegeDef instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(PrivilegeDef persistentInstance) {
        log.debug("deleting PrivilegeDef instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public PrivilegeDef merge(PrivilegeDef detachedInstance) {
        log.debug("merging PrivilegeDef instance");
        try {
            PrivilegeDef result = (PrivilegeDef) sessionFactory.getCurrentSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public PrivilegeDef findById(java.lang.String id) {
        log.debug("getting PrivilegeDef instance with id: " + id);
        try {
            PrivilegeDef instance = (PrivilegeDef) sessionFactory.getCurrentSession().get(
                    "PrivilegeDef", id);
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

    public List<PrivilegeDef> findByExample(PrivilegeDef instance) {
        log.debug("finding PrivilegeDef instance by example");
        try {
            List<PrivilegeDef> results = (List<PrivilegeDef>) sessionFactory
                    .getCurrentSession().createCriteria(
                            "PrivilegeDef").add(
                            create(instance)).list();
            log.debug("find by example successful, result size: "
                    + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
	
    //==================================================================
	
	
    public PrivilegeDef add(PrivilegeDef instance) {
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

    public void remove(PrivilegeDef instance) {
        log.debug("deleting instance");
        try {
            sessionFactory.getCurrentSession().delete(instance);
            log.debug("delete successful");
        } catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }			
    }

    public PrivilegeDef update(PrivilegeDef instance) {
        log.debug("merging instance. id = " + instance.getPrivilegeId());
        try {
            sessionFactory.getCurrentSession().merge(instance);
            log.debug("merge successful");
            return instance;
        } catch (HibernateException re) {
            log.error("merge failed", re);
            throw re;
        }		
    }

    public List<PrivilegeDef> findAllPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("from PrivilegeDef a " +
                " order by a.privilegeId asc");
        qry.setCacheable(true);
        qry.setCacheRegion("query.resource.findAllPrivileges");
        List<PrivilegeDef> result = (List<PrivilegeDef>)qry.list();
		
		
        return result;
    }
	
    public int removeAllPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("delete from PrivilegeDef");
        return qry.executeUpdate();
    }
	


    
}
