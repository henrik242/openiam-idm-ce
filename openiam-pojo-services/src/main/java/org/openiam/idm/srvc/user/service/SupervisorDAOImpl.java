package org.openiam.idm.srvc.user.service;
// Generated Feb 18, 2008 3:56:08 PM by Hibernate Tools 3.2.0.b11


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.user.domain.SupervisorEntity;

import static org.hibernate.criterion.Example.create;

/**
 * Data Access Object implementation for domain model class Supervisor.
 * @see org.openiam.idm.srvc.user.dto.Supervisor
 */
public class SupervisorDAOImpl implements SupervisorDAO  {

    private static final Log log = LogFactory.getLog(SupervisorDAOImpl.class);

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
	 * @see org.openiam.idm.srvc.user.service.SupervisorDAO#add(org.openiam.idm.srvc.user.dto.Supervisor)
	 */
    public void add(SupervisorEntity transientInstance) {
        log.debug("persisting Supervisor instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.user.service.SupervisorDAO#remove(org.openiam.idm.srvc.user.dto.Supervisor)
	 */
    public void remove(String id) {
        log.debug("deleting Supervisor instance");
        try {
            SupervisorEntity persistentInstance = (SupervisorEntity)sessionFactory.getCurrentSession().get(SupervisorEntity.class,id);
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.user.service.SupervisorDAO#update(org.openiam.idm.srvc.user.dto.Supervisor)
	 */
    public SupervisorEntity update(SupervisorEntity detachedInstance) {
        log.debug("merging Supervisor instance");
        try {
            SupervisorEntity result = (SupervisorEntity) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.user.service.SupervisorDAO#findById(java.lang.String)
	 */
    public SupervisorEntity findById( java.lang.String id) {
        log.debug("getting Supervisor instance with id: " + id);
        try {
            SupervisorEntity instance = (SupervisorEntity) sessionFactory.getCurrentSession()
                    .get(SupervisorEntity.class, id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.user.service.SupervisorDAO#findByExample(org.openiam.idm.srvc.user.dto.Supervisor)
	 */
    public List<SupervisorEntity> findByExample(SupervisorEntity instance) {
        log.debug("finding Supervisor instance by example");
        try {
            List<SupervisorEntity> results = (List<SupervisorEntity>) sessionFactory.getCurrentSession()
                    .createCriteria(SupervisorEntity.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .add(create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
    
    /**
     * Returns a list of Supervisor objects that represents the employees or users for this supervisor
     * @param supervisorId
     * @return
     */
    public List<SupervisorEntity> findEmployees(String supervisorId) {
    	Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(SupervisorEntity.class)
                .add(Restrictions.eq("supervisor.userId",supervisorId))
                .addOrder(Order.asc("supervisor.userId"))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    	List<SupervisorEntity> results = (List<SupervisorEntity>)criteria.list();

    	// initalize the objects in the collection
    	
    	int listSize = results.size();
    	for (int i=0; i<listSize; i++) {
    		SupervisorEntity supr = results.get(i);
    		org.hibernate.Hibernate.initialize(supr.getSupervisor());
    		org.hibernate.Hibernate.initialize(supr.getEmployee());
    	}
    	
    	return results;
    	
    }

    public List<SupervisorEntity> findSupervisors(String employeeId) {
    	Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(SupervisorEntity.class)
                .add(Restrictions.eq("employee.userId",employeeId))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    	List<SupervisorEntity> results = (List<SupervisorEntity>)criteria.list();
    	return results;    	
    }
    
    public SupervisorEntity findPrimarySupervisor(String employeeId) {
    	Session session = sessionFactory.getCurrentSession();
    	Criteria criteria = session.createCriteria(SupervisorEntity.class)
                .add(Restrictions.eq("employee.userId",employeeId))
                .add(Restrictions.eq("isPrimarySuper",1))
                .addOrder(Order.asc("supervisor.userId"));

    	SupervisorEntity supr = (SupervisorEntity)criteria.uniqueResult();
    	if (supr == null)
    		return null;

    	org.hibernate.Hibernate.initialize(supr.getSupervisor());
    	org.hibernate.Hibernate.initialize(supr.getEmployee());
   	
    	return supr;
    	//List<Supervisor> results = (List<Supervisor>)qry.list();
    	//return results;        	
    }

}



