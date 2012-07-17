package org.openiam.idm.srvc.res.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

import javax.naming.InitialContext;
import java.util.List;

import static org.hibernate.criterion.Example.create;

import org.openiam.idm.srvc.res.dto.*;

/**
 * Created by IntelliJ IDEA.
 * User: arun
 * Date: 8/5/11
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserPrivilegeDAOImpl implements UserPrivilegeDAO {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(UserPrivilegeDAOImpl.class);
	
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

	
    public void persist(UserPrivilege transientInstance) {
        log.debug("persisting UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(UserPrivilege instance) {
        log.debug("attaching dirty UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(UserPrivilege instance) {
        log.debug("attaching clean UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(UserPrivilege persistentInstance) {
        log.debug("deleting UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public UserPrivilege merge(UserPrivilege detachedInstance) {
        log.debug("merging UserPrivilege instance");
        try {
            UserPrivilege result = (UserPrivilege) sessionFactory.getCurrentSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public UserPrivilege findById(java.lang.String id) {
        log.debug("getting UserPrivilege instance with id: " + id);
        try {
            UserPrivilege instance = (UserPrivilege) sessionFactory.getCurrentSession().get(
                    "org.openiam.idm.srvc.res.dto.UserPrivilege", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");			
            }
			
           // Hibernate.initialize(instance.getPrivilege());

            return instance;
			
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<UserPrivilege> findByExample(UserPrivilege instance) {
        log.debug("finding UserPrivilege instance by example");
        try {
            List<UserPrivilege> results = (List<UserPrivilege>) sessionFactory
                    .getCurrentSession().createCriteria(
                            "UserPrivilege").add(
                            create(instance)).list();
            log.debug("find by example successful, result size: "
                    + results.size());

            for (UserPrivilege obj:results) {
               // Hibernate.initialize(obj.());
            }

            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
	
    //==================================================================
	
	
    public UserPrivilege add(UserPrivilege instance) {
        log.debug("persisting instance");
        try {
            sessionFactory.getCurrentSession().persist(instance);
            log.debug("persist successful");

           // Hibernate.initialize(instance.getPrivilege());

            return instance;
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }		
    }

    public void remove(UserPrivilege instance) {
        log.debug("deleting instance");
        try {
            sessionFactory.getCurrentSession().delete(instance);
            log.debug("delete successful");
        } catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }			
    }

    public UserPrivilege update(UserPrivilege instance) {
        log.debug("merging instance. id = " + instance.getUserPrivilegeId());
        try {
            sessionFactory.getCurrentSession().merge(instance);
            log.debug("merge successful");

        //    Hibernate.initialize(instance.getPrivilege());

            return instance;
        } catch (HibernateException re) {
            log.error("merge failed", re);
            throw re;
        }		
    }

    public List<UserPrivilege> findAllUserPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("from UserPrivilege a " +
                " order by a.userId asc");
        qry.setCacheable(true);
        qry.setCacheRegion("query.resource.findAllUserPrivileges");
        List<UserPrivilege> result = (List<UserPrivilege>)qry.list();
		
        for (UserPrivilege obj:result) {
            //Hibernate.initialize(obj.getPrivilege());
        }

        return result;
    }
	
    public int removeAllUserPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("delete from UserPrivilege");
        return qry.executeUpdate();
    }
	



}
