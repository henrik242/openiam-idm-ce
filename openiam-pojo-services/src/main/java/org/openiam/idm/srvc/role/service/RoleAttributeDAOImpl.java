package org.openiam.idm.srvc.role.service;
// Generated Mar 4, 2008 1:12:08 AM by Hibernate Tools 3.2.0.b11

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

import org.openiam.idm.srvc.role.domain.RoleAttributeEntity;

/**
 * Data access interface for domain model class RoleAttribute.
 * @see org.openiam.idm.srvc.role.dto.RoleAttribute
 */
public class RoleAttributeDAOImpl implements RoleAttributeDAO {

    private static final Log log = LogFactory.getLog(RoleAttributeDAOImpl.class);

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
    public void add(RoleAttributeEntity transientInstance) {
        log.debug("persisting RoleAttribute instance");
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
	 * @see org.openiam.idm.srvc.role.service.RoleAttributeDAO#remove(org.openiam.idm.srvc.role.dto.RoleAttribute)
	 */
    public void remove(RoleAttributeEntity persistentInstance) {
        log.debug("deleting RoleAttribute instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see org.openiam.idm.srvc.role.service.RoleAttributeDAO#update(org.openiam.idm.srvc.role.dto.RoleAttribute)
	 */
    public RoleAttributeEntity update(RoleAttributeEntity detachedInstance) {
        log.debug("merging RoleAttribute instance");
        try {
            RoleAttributeEntity result = (RoleAttributeEntity) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public RoleAttributeEntity findById( java.lang.String id) {
        log.debug("getting RoleAttribute instance with id: " + id);
        try {
            RoleAttributeEntity instance = (RoleAttributeEntity) sessionFactory.getCurrentSession()
                    .get(RoleAttributeEntity.class, id);
            if (instance == null) {
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
	 * @see org.openiam.idm.srvc.role.service.RoleAttributeDAO#findByExample(org.openiam.idm.srvc.role.dto.RoleAttribute)
	 */
    public List<RoleAttributeEntity> findByExample(RoleAttributeEntity instance) {
        log.debug("finding RoleAttribute instance by example");
        try {
            List<RoleAttributeEntity> results = (List<RoleAttributeEntity>) sessionFactory.getCurrentSession()
                    .createCriteria(RoleAttributeEntity.class)
                    .add( create(instance) )
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
    
	public void deleteRoleAttributes(String serviceId, String roleId) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.role.domain.RoleAttributeEntity ra " +
					" where ra.id.serviceId = :serviceId and ra.id.roleId = :roleId ");
		qry.setString("serviceId", serviceId);
		qry.setString("roleId",roleId);
		qry.executeUpdate();
	}


}

