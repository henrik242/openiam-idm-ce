package org.openiam.am.srvc.resattr.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openiam.exception.data.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * User: Alexander Duckardt
 * Date: 8/15/12
 */

public abstract class AbstractGenericDao<Entity, PrimaryKey extends Serializable> implements GenericDao<Entity, PrimaryKey> {
    protected Class<Entity> persistentClass;
    protected final Log log = LogFactory.getLog(this.getClass());
    @Autowired
    protected SessionFactory sessionFactory;

    public AbstractGenericDao() {
        this.persistentClass = (Class<Entity>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
    public Class<Entity> getPersistentClass() {
        return persistentClass;
    }
    @Override
    public Entity findById(PrimaryKey id) throws Exception {
        log.debug("getting instance with id: " + String.valueOf(id));
        if (id != null) {
            return (Entity)this.sessionFactory.getCurrentSession().get(getPersistentClass(), id);
        }
        return null;
    }

    @Override
    public List<Entity> getAll() throws Exception {
        log.debug("getting all data from table: "+getPersistentClass().getName());
        Query qry = sessionFactory.getCurrentSession().createQuery("from "+ getPersistentClass().getName() +" log");
        return (List<Entity>)qry.list();
    }

    @Override
    @Transactional
    public Entity add(Entity instance) throws Exception {
        try {
            sessionFactory.getCurrentSession().persist(instance);
            return instance;
        }catch(DataAccessException dae) {
            dae.printStackTrace();
            log.error("Add operation failed.", dae);
            throw new DataException( dae.getMessage(), dae.getCause() );
        }
    }

    @Override
    @Transactional
    public Entity update(Entity instance) throws Exception {
        try {
            return (Entity)sessionFactory.getCurrentSession().merge(instance);
        }catch(DataAccessException dae) {
            log.error("Update operation failed.", dae);
            throw new DataException( dae.getMessage(), dae.getCause() );
        }
    }

    @Override
    @Transactional
    public void delete(Entity instance) throws Exception {
        try {
            sessionFactory.getCurrentSession().delete(instance);
        }catch(DataAccessException dae) {
            log.error("Remove operation failed.", dae);
            throw new DataException( dae.getMessage(), dae.getCause() );
        }
    }
    @Override
    public Session getCurrentSession(){
           return this.sessionFactory.getCurrentSession();
    }
}
