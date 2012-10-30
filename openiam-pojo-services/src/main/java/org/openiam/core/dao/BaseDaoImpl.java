package org.openiam.core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.eq;

public abstract class BaseDaoImpl<T, PrimaryKey extends Serializable> implements BaseDao<T, PrimaryKey> {
    protected final Class<T> domainClass;

    @Autowired
    protected SessionFactory sessionFactory;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BaseDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        Type arg;
        if (t instanceof ParameterizedType) {
            arg = ((ParameterizedType) t).getActualTypeArguments()[0];
        } else if (t instanceof Class) {
            arg = ((ParameterizedType) ((Class) t).getGenericSuperclass()).getActualTypeArguments()[0];

        } else {
            throw new RuntimeException("Can not handle type construction for '" + getClass() + "'!");
        }

        if (arg instanceof Class) {
            this.domainClass = (Class<T>) arg;
        } else if (arg instanceof ParameterizedType) {
            this.domainClass = (Class<T>) ((ParameterizedType) arg).getRawType();
        } else {
            throw new RuntimeException("Problem determining generic class for '" + getClass() + "'! ");
        }
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings({"unchecked"})
    public T findById(PrimaryKey id) {
        if (id == null) {
            return null;
        }
        return (T) this.getSession().get(domainClass, id);
    }

    @SuppressWarnings({"unchecked"})
    public T findById(PrimaryKey id, String... fetchFields) {
        if (id == null) {
            return null;
        }
        Criteria criteria = sessionFactory
                .getCurrentSession()
                .createCriteria(domainClass)
                .add(eq(getPKfieldName(), id))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (fetchFields != null) {
            for (String field : fetchFields) {
                criteria.setFetchMode(field, FetchMode.JOIN);
            }
        }
        return (T) criteria.uniqueResult();
    }

    protected abstract String getPKfieldName();

    @SuppressWarnings({"unchecked"})
    public List<T> findAll() {
        return sessionFactory.getCurrentSession()
                .createCriteria(domainClass)
                .list();
    }

    public Long countAll() {
        return ((Number)
                sessionFactory.getCurrentSession()
                        .createCriteria(domainClass)
                        .setProjection(rowCount())
                        .uniqueResult())
                .longValue();
    }

    public void save(T entity) {
        sessionFactory.getCurrentSession()
                .saveOrUpdate(entity);
    }

    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void save(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        for (T entity : entities) {
            session.saveOrUpdate(entity);
        }
    }

    @Transactional
    public void deleteAll() throws Exception{
        sessionFactory.getCurrentSession().createQuery("delete from "+this.domainClass.getName()).executeUpdate();
    }
}

