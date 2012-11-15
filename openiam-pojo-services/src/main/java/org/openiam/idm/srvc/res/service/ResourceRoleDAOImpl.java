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
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.res.domain.ResourceRoleEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.role.dto.Role;


/**
 * DAO Implementation for ResourceRoles.
 * 
 */
public class ResourceRoleDAOImpl implements ResourceRoleDAO {

	private static final Log log = LogFactory.getLog(ResourceRoleDAOImpl.class);

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

	public void persist(ResourceRoleEntity transientInstance) {
		log.debug("persisting ResourceRole instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#remove(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#remove(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	public void remove(ResourceRoleEntity persistentInstance) {
		log.debug("deleting ResourceRole instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (HibernateException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#update(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#update(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	public ResourceRoleEntity update(ResourceRoleEntity detachedInstance) {
		log.debug("merging ResourceRole instance");
		try {
			ResourceRoleEntity result = (ResourceRoleEntity) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (HibernateException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ResourceRoleEntity findById(ResourceRoleEmbeddableId id) {
		log.debug("getting ResourceRole instance with id: " + id.getResourceId() + "-" + id.getRoleId() + "-" + id.getPrivilegeId());
		try {
			ResourceRoleEntity instance = (ResourceRoleEntity) sessionFactory.getCurrentSession()
					.get(ResourceRoleEntity.class, id);
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

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findByExample(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findByExample(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	public List<ResourceRoleEntity> findByExample(ResourceRoleEntity instance) {
		log.debug("finding ResourceRole instance by example");
		try {
			List<ResourceRoleEntity> results = (List<ResourceRoleEntity>) sessionFactory
					.getCurrentSession().createCriteria(ResourceRoleEntity.class).add(
							create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (HibernateException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	//==================================================================
	
	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#add(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#add(org.openiam.idm.srvc.res.dto.ResourceRole)
	 */
	public ResourceRoleEntity add(ResourceRoleEntity instance) {
		log.debug("persisting instance");
		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
			return instance;
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}		
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findAllResourceRoles()
	 */
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findAllResourceRoles()
	 */
	public List<ResourceRoleEntity> findAllResourceRoles() {
		Session session = sessionFactory.getCurrentSession();
		List<ResourceRoleEntity> result = (List<ResourceRoleEntity>)session.createCriteria(ResourceRoleEntity.class).list();
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#removeAllResourceRoles()
	 */
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#removeAllResourceRoles()
	 */
	public void removeAllResourceRoles() {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete from org.openiam.idm.srvc.res.domain.ResourceRoleEntity");
		qry.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.res.service.ResourceRoleDAO#findAllResourceForRole(java.lang.String, java.lang.String)
	 */
	public List<ResourceRoleEntity> findResourcesForRole(String domainId, String roleId) {
		try {
			Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(ResourceRoleEntity.class)
                    .add(Restrictions.eq("id.domainId",domainId))
                    .add(Restrictions.eq("id.roleId",roleId))
                    .addOrder(Order.asc("id.resourceId"));

			List<ResourceRoleEntity> result = (List<ResourceRoleEntity>)criteria.list();
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

    public List<Role> findRolesForResource(String resourceId) {
        try {
            Session session = sessionFactory.getCurrentSession();

            String sql = "  SELECT  role " +
                    "       FROM    ResourceRoleEntity resourceRole, RoleEntity role, ResourceEntity resource " +
                    "       WHERE   resource.resourceId = resourceRole.id.resourceId and " +
                    "        resourceRole.id.roleId = role.id.roleId AND " +
                    "        resourceRole.id.domainId = role.id.serviceId and " +
                    "        resource.resourceId = :resourceId  ";
            Query qry = session.createQuery(sql);
            qry.setString("resourceId", resourceId);


            List<Role> result = (List<Role>)qry.list();
            return result;
        } catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void removeResourceRole(String domainId, String roleId) {
        //To change body of implemented methods use File | Settings | File Templates.

        log.debug("removeResourceRole: domainId=" + domainId);
		log.debug("removeResourceRole: roleId=" + roleId);

		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.res.domain.ResourceRoleEntity rr " +
					" where rr.id.roleId = :roleId and rr.id.domainId = :domainId ");
		qry.setString("roleId", roleId);
		qry.setString("domainId", domainId);
		qry.executeUpdate();
    }


}

