package org.openiam.idm.srvc.res.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

import javax.naming.InitialContext;
import java.util.List;

import static org.hibernate.criterion.Example.create;

import org.hibernate.criterion.Order;
import org.openiam.idm.srvc.res.domain.UserPrivilegeEntity;

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

	
    public void persist(UserPrivilegeEntity transientInstance) {
        log.debug("persisting UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(UserPrivilegeEntity instance) {
        log.debug("attaching dirty UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(UserPrivilegeEntity instance) {
        log.debug("attaching clean UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(UserPrivilegeEntity persistentInstance) {
        log.debug("deleting UserPrivilege instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public UserPrivilegeEntity merge(UserPrivilegeEntity detachedInstance) {
        log.debug("merging UserPrivilege instance");
        try {
            UserPrivilegeEntity result = (UserPrivilegeEntity) sessionFactory.getCurrentSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public UserPrivilegeEntity findById(java.lang.String id) {
        log.debug("getting UserPrivilege instance with id: " + id);
        try {
            UserPrivilegeEntity instance = (UserPrivilegeEntity) sessionFactory.getCurrentSession().get(
                    UserPrivilegeEntity.class, id);
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

    public List<UserPrivilegeEntity> findByExample(UserPrivilegeEntity instance) {
        log.debug("finding UserPrivilege instance by example");
        try {
            List<UserPrivilegeEntity> results = (List<UserPrivilegeEntity>) sessionFactory
                    .getCurrentSession().createCriteria(
                            UserPrivilegeEntity.class).add(
                            create(instance)).list();
            log.debug("find by example successful, result size: "
                    + results.size());

            for (UserPrivilegeEntity obj:results) {
               // Hibernate.initialize(obj.());
            }

            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
	
    //==================================================================
	
	
    public UserPrivilegeEntity add(UserPrivilegeEntity instance) {
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

    public void remove(UserPrivilegeEntity instance) {
        log.debug("deleting instance");
        try {
            sessionFactory.getCurrentSession().delete(instance);
            log.debug("delete successful");
        } catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }			
    }

    public UserPrivilegeEntity update(UserPrivilegeEntity instance) {
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

    public List<UserPrivilegeEntity> findAllUserPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserPrivilegeEntity.class)
                .addOrder(Order.asc("userId"));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findAllUserPrivileges");
        List<UserPrivilegeEntity> result = (List<UserPrivilegeEntity>)criteria.list();
		
        for (UserPrivilegeEntity obj:result) {
            //Hibernate.initialize(obj.getPrivilege());
        }

        return result;
    }
	
    public int removeAllUserPrivileges() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("delete from UserPrivilegeEntity");
        return qry.executeUpdate();
    }
	



}
