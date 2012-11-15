package org.openiam.idm.srvc.res.service;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

import static org.hibernate.criterion.Example.create;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openiam.exception.data.ObjectNotFoundException;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.openiam.idm.srvc.res.domain.ResourceRoleEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.res.domain.ResourceTypeEntity;
import org.openiam.idm.srvc.res.dto.*;
import org.openiam.idm.srvc.role.domain.UserRoleEntity;


/**
 * DAO Implementation for Resources.
 */
public class ResourceDAOImpl implements ResourceDAO {

    private static final Log log = LogFactory.getLog(ResourceDAOImpl.class);
    private ResourceTypeDAO resourceTypeDao;
    private ResourcePropDAO resourcePropDao;
    private ResourceRoleDAO resourceRoleDao;
//	private ResourceUserDAO resourceUserDao;

    private SessionFactory sessionFactory;

    // DAO operations ================================================

    public ResourceTypeDAO getResourceTypeDao() {
        return resourceTypeDao;
    }

    public void setResourceTypeDao(ResourceTypeDAO resourceTypeDao) {
        this.resourceTypeDao = resourceTypeDao;
    }

    public ResourcePropDAO getResourcePropDao() {
        return resourcePropDao;
    }

    public void setResourcePropDao(ResourcePropDAO resourcePropDao) {
        this.resourcePropDao = resourcePropDao;
    }

    public ResourceRoleDAO getResourceRoleDao() {
        return resourceRoleDao;
    }

    public void setResourceRoleDao(ResourceRoleDAO resourceRoleDao) {
        this.resourceRoleDao = resourceRoleDao;
    }

//	public ResourceUserDAO getResourceUserDao() {
//		return resourceUserDao;
//	}
//
//	public void setResourceUserDao(ResourceUserDAO resourceUserDao) {
//		this.resourceUserDao = resourceUserDao;
//	}
//	

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

    // Basic operations =====================================

    public void persist(ResourceEntity transientInstance) {
        log.debug("persisting Resource instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }
    }


    public void remove(ResourceEntity persistentInstance) {
        log.debug("deleting Resource instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (HibernateException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ResourceEntity update(ResourceEntity detachedInstance) {
        log.debug("merging Resource instance");
        try {
            ResourceEntity result = (ResourceEntity) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");

            //Hibernate.initialize(result.getResourceType());
            //Hibernate.initialize(result.getResourceProps());
            //Hibernate.initialize(result.getResourceRoles());

            return result;
        } catch (HibernateException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ResourceEntity findById(java.lang.String id) {
        log.debug("getting Resource instance with id: " + id);
        try {
            ResourceEntity instance = (ResourceEntity) sessionFactory.getCurrentSession()
                    .get(ResourceEntity.class, id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }


            if (instance != null) {
                Hibernate.initialize(instance.getResourceType());
                Hibernate.initialize(instance.getResourceProps());
                Hibernate.initialize(instance.getResourceRoles());
                Hibernate.initialize(instance.getChildResources());
                Hibernate.initialize(instance.getEntitlements());
                Hibernate.initialize(instance.getResourceGroups());
            }
            return instance;
        } catch (HibernateException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<ResourceEntity> findByExample(ResourceEntity instance) {
        log.debug("finding Resource instance by example");
        try {
            List<ResourceEntity> resources = (List<ResourceEntity>) sessionFactory
                    .getCurrentSession().createCriteria(ResourceEntity.class).add(
                            create(instance)).list();
            log.debug("find by example successful, result size: "
                    + resources.size());

            for (ResourceEntity r : resources) {

                Hibernate.initialize(r.getResourceType());
                Hibernate.initialize(r.getResourceProps());
                Hibernate.initialize(r.getResourceRoles());
                Hibernate.initialize(r.getChildResources());
                Hibernate.initialize(r.getEntitlements());
                Hibernate.initialize(r.getResourceGroups());
            }

            return resources;
        } catch (HibernateException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    // Resource operations ==================================================================


    public ResourceEntity add(ResourceEntity instance) {
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

    public List<ResourceEntity> findAllResources() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class).addOrder(Order.asc("resourceId"));

        List<ResourceEntity> result = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : result) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return result;
    }

    public List<ResourceEntity> findResourcesByName(String resourceName) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.like("name", resourceName));
        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findResourcesByName");
        List<ResourceEntity> result = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : result) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return result;
    }


    public ResourceEntity findResourceByName(String resourceName) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.eq("name", resourceName))
                .addOrder(Order.asc("displayOrder"));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findResourceByName");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        if (resources != null && !resources.isEmpty()) {
            ResourceEntity r = resources.get(0);
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
            return r;
        }
        return null;

    }


    public int removeAllResources() {
        Session session = sessionFactory.getCurrentSession();
        Query qry = session.createQuery("delete from org.openiam.idm.srvc.res.domain.ResourceEntity");
        return qry.executeUpdate();
    }


    public List<ResourceEntity> findResourcesByProperty(String propName, String propValue) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .createAlias("resourceProps", "rp")
                .add(Restrictions.eq("rp.name", propName))
                .add(Restrictions.eq("rp.propValue", propValue));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findResourceByProperty");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;

    }


    public ResourceEntity findResourceByProperties(List<ResourceProp> propList) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .createAlias("resourceProps", "rp");

        if (propList.size() > 0) {
            Criterion criterion = Restrictions.and(Restrictions.eq("rp.name", propList.get(0).getName()), Restrictions.eq("rp.propValue", propList.get(0).getPropValue()));
            for (int i = 1; i < propList.size(); i++) {
                criterion = Restrictions.or(criterion, Restrictions.and(Restrictions.eq("rp.name", propList.get(i).getName()), Restrictions.eq("rp.propValue", propList.get(i).getPropValue())));
            }
        }

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findResourceByProperties");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        if (resources != null && resources.size() > 0) {
            ResourceEntity r = resources.get(0);
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
            return r;
        }

        return null;


    }


    // Resource Type ---------------------------------------------------------------


    public ResourceTypeEntity findTypeOfResource(String resourceId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.eq("resourceId",resourceId))
                .setProjection(Projections.property("resourceType"));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findTypeOfResource");
        ResourceTypeEntity t = (ResourceTypeEntity) criteria.uniqueResult();

        return t;
    }


    // ResourceProp-----------------------------------------------------------------


    public int removePropertiesByResource(String resourceId) {
        Session session = sessionFactory.getCurrentSession();

        Query qry = session.createQuery(
                "delete from org.openiam.idm.srvc.res.domain.ResourcePropEntity as resourceProp " +
                        "where resourceProp.resourceId = :resourceId ");

        qry.setString("resourceId", resourceId);
        return qry.executeUpdate();
    }


    public List<ResourcePropEntity> findResourceProperties(String resourceId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourcePropEntity.class)
                .add(Restrictions.eq("resourceId",resourceId));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findResourceProperties");
        List<ResourcePropEntity> result = (List<ResourcePropEntity>) criteria.list();

        return result;
    }


    // Resource additional operations ====================================================

    public List<ResourceEntity> getResourcesByType(String resourceTypeId) {
        Session session = sessionFactory.getCurrentSession();

        //org.hibernate.SQLQuery qry = session.createSQLQuery(
        //		"select r.resource_id, r.resource_type_id, r.description, r.name, r.resource_parent, r.branch_id, " +
        //		"r.category_id, r.display_order, r.node_level, r.sensitive_app " +
        //		"FROM resource r where r.resource_type_id = :resourceTypeId " +
        //		"order by r.name " );

        //qry.addEntity(Resource.class);

        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.eq("resourceType.resourceTypeId",resourceTypeId))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.asc("displayOrder"));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.getResourceByType");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;
    }


    public List<ResourceEntity> getUserResourcesByType(String userId, String resourceTypeId) {
        Session session = sessionFactory.getCurrentSession();


        String sql = "SELECT res.* " +
                " FROM RES res JOIN RESOURCE_ROLE rl ON (res.RESOURCE_ID = rl.RESOURCE_ID) " +
                " JOIN USER_ROLE ur ON (rl.ROLE_ID = ur.ROLE_ID) " +
                " WHERE res.RESOURCE_TYPE_ID = :typeId AND ur.USER_ID = :userId " +
                "    ORDER BY res.NAME asc";

        SQLQuery qry = session.createSQLQuery(sql);
        qry.addEntity(ResourceEntity.class);
        qry.setString("typeId", resourceTypeId);
        qry.setString("userId", userId);

        List<ResourceEntity> resources = (List<ResourceEntity>) qry.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;

    }

    public List<ResourceEntity> getResourcesByCategory(String categoryId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.eq("categoryId",categoryId))
                .addOrder(Order.asc("displayOrder"));
        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.getResourcesByCategory");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;
    }


    public List<ResourceEntity> getResourcesByBranch(String branchId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.eq("branchId",branchId))
                .addOrder(Order.asc("displayOrder"));


        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.getResourcesByBranch");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;
    }


    public List<ResourceEntity> getChildResources(String resourceId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.eq("resourceParent",resourceId))
                .addOrder(Order.asc("displayOrder"));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.getChildResources");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;
    }


    public List<ResourceEntity> getRootResources() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                .add(Restrictions.or(Restrictions.isNull("resourceParent"),Restrictions.isEmpty("resourceParent")))
                .addOrder(Order.asc("displayOrder"));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.getRootResources");
        List<ResourceEntity> resources = (List<ResourceEntity>) criteria.list();

        for (ResourceEntity r : resources) {
            Hibernate.initialize(r.getResourceType());
            Hibernate.initialize(r.getResourceProps());
            Hibernate.initialize(r.getResourceRoles());
            Hibernate.initialize(r.getChildResources());
            Hibernate.initialize(r.getEntitlements());
            Hibernate.initialize(r.getResourceGroups());
        }

        return resources;
    }


    public int removeResourcesByType(String resourceTypeId) {
        Session session = sessionFactory.getCurrentSession();

        Query qry = session.createQuery(
                "delete from org.openiam.idm.srvc.res.domain.ResourceEntity as resource " +
                        "where resource.resourceType.resourceTypeId = :resourceTypeId");

        qry.setString("resourceTypeId", resourceTypeId);
        return qry.executeUpdate();
    }


    public int removeResourcesByCategory(String categoryId) {
        Session session = sessionFactory.getCurrentSession();

        Query qry = session.createQuery(
                "delete from org.openiam.idm.srvc.res.domain.ResourceEntity as resource " +
                        "where resource.categoryId = :categoryId ");

        qry.setString("categoryId", categoryId);
        return qry.executeUpdate();
    }

    public int removeResourcesByBranch(String branchId) {
        Session session = sessionFactory.getCurrentSession();

        Query qry = session.createQuery(
                "delete from org.openiam.idm.srvc.res.domain.ResourceEntity as resource " +
                        "where resource.branchId = :branchId ");

        qry.setString("branchId", branchId);
        return qry.executeUpdate();
    }


// ResourceRole--------------------------------------------------------------------

    public List<ResourceRoleEntity> findResourceRolesByResource(String resourceId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceRoleEntity.class)
                .add(Restrictions.eq("id.resourceId",resourceId));

        criteria.setCacheable(true);
        criteria.setCacheRegion("query.resource.findResourceRoleByResource");
        List<ResourceRoleEntity> result = (List<ResourceRoleEntity>) criteria.list();

        return result;
    }

    public List<ResourceEntity> findResourcesForRole(String domainId, String roleId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ResourceEntity.class)
                 .createAlias("resourceRoles","rr")
                 .add(Restrictions.eq("rr.id.domainId",domainId))
                 .add(Restrictions.eq("rr.id.roleId",roleId))
                 .addOrder(Order.asc("managedSysId"))
                 .addOrder(Order.asc("name"));

            criteria.setCacheable(true);
            criteria.setCacheRegion("query.resource.findResourcesForRole");
            List<ResourceEntity> result = (List<ResourceEntity>) criteria.list();
            if (result == null || result.isEmpty()) {
                log.debug("get successful, no instance found");
                return null;
            }
            log.debug("get successful, resource instances found");
            return result;
    }

    public List<ResourceEntity> findResourcesForUserRole(String userId) {
        Session session = sessionFactory.getCurrentSession();

        String select = " select DISTINCT r.RESOURCE_ID, r.RESOURCE_TYPE_ID, " +
                " r.DESCRIPTION, r.NAME, r.RESOURCE_PARENT, " +
                " r.BRANCH_ID, r.CATEGORY_ID, r.DISPLAY_ORDER, " +
                " r.NODE_LEVEL, r.SENSITIVE_APP, r.MANAGED_SYS_ID," +
                " r.URL, r.RES_OWNER_GROUP_ID, r.RES_OWNER_USER_ID " +
                " FROM  USER_ROLE ur, RESOURCE_ROLE rr, RES r " +
                " WHERE ur.USER_ID = :userId and ur.SERVICE_ID = rr.SERVICE_ID AND ur.ROLE_ID = rr.ROLE_ID AND " +
                "       rr.RESOURCE_ID = r.RESOURCE_ID";

        try {


            SQLQuery qry = session.createSQLQuery(select);
            qry.addEntity(ResourceEntity.class);
            qry.setString("userId", userId);

            List<ResourceEntity> result = (List<ResourceEntity>) qry.list();
            if (result == null || result.isEmpty()) {
                log.debug("get successful, no instance found");
                return null;
            }
            log.debug("get successful, resource instances found");


            for (ResourceEntity r : result) {
                Hibernate.initialize(r.getResourceType());
                Hibernate.initialize(r.getResourceProps());
                Hibernate.initialize(r.getResourceRoles());
                Hibernate.initialize(r.getChildResources());
                Hibernate.initialize(r.getEntitlements());
                Hibernate.initialize(r.getResourceGroups());
            }

            return result;
        } catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }
    }


    public List<ResourceEntity> findResourcesForRoles(String domainId, List<String> roleIdList) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query qry = session.createQuery(
                    "SELECT distinct resource  " +
                            "FROM org.openiam.idm.srvc.res.domain.ResourceEntity as resource " +
                            "	inner join resource.resourceRoles as rr " +
                            " where rr.id.domainId = :domainId and rr.id.roleId in ( :roleIdList ) " +
                            " order by resource.managedSysId, resource.name  "
            );

            qry.setString("domainId", domainId);
            qry.setParameterList("roleIdList", roleIdList);

            qry.setCacheable(true);
            qry.setCacheRegion("query.resource.findResourcesForRole");
            List<ResourceEntity> result = (List<ResourceEntity>) qry.list();
            if (result == null || result.isEmpty()) {
                log.debug("get successful, no instance found");
                return null;
            }
            log.debug("get successful, resource instances found");
            return result;
        } catch (HibernateException re) {
            log.error("persist failed", re);
            throw re;
        }


    }


    public void addResourceRolePrivilege(String resourceId, String roleId, String privilegeId) {

        ResourceRoleEmbeddableId rrId = new ResourceRoleEmbeddableId();
        rrId.setResourceId(resourceId);
        rrId.setRoleId(roleId);
        rrId.setPrivilegeId(privilegeId);

        ResourceRoleEntity p = resourceRoleDao.findById(rrId);

        ResourceEntity resource = findById(resourceId);
        if(resource != null && resource.getResourceRoles() != null){
            resource.getResourceRoles().add(p);
        }
        try {
            sessionFactory.getCurrentSession().merge(resource);
            log.debug("persist successful");
        } catch (HibernateException re) {
            re.printStackTrace();
            log.error("persist failed", re);
            throw re;
        }
    }


    public void removeResourceRolePrivilege(String resourceId, String roleId, String privilegeId) {

        ResourceEntity resource = findById(resourceId);

        if (resource == null) {
            log.error("Resource not found for resourceId =" + resourceId);
            throw new ObjectNotFoundException();
        }

        Set<ResourceRoleEntity> all = resource.getResourceRoles();
        if (all == null || all.isEmpty()) {
            return;
        }

        for (Iterator<ResourceRoleEntity> it = all.iterator(); it.hasNext(); ) {
            ResourceRoleEntity rr = it.next();

            if ((rr.getId().getResourceId().equalsIgnoreCase(resourceId))
                    && (rr.getId().getRoleId().equalsIgnoreCase(roleId))
                    && (rr.getId().getPrivilegeId().equalsIgnoreCase(privilegeId))) {

                it.remove();
            }
        }

    }


    public int removeResourceRolePrivileges(String resourceId) {
        Session session = sessionFactory.getCurrentSession();

        Query qry = session.createQuery(
                "delete from org.openiam.idm.srvc.res.domain.ResourceRoleEntity as resourceRole " +
                        "where resourceRole.resourceId = :resourceId ");

        qry.setString("resourceId", resourceId);
        return qry.executeUpdate();

    }

}


