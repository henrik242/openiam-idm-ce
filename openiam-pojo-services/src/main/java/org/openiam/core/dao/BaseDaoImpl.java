package org.openiam.core.dao;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.hibernate.criterion.Projections.projectionList;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.eq;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {
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

    protected BaseDaoImpl(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    @SuppressWarnings({"unchecked"})
    public T findById(Long id) {
        if (id == null) {
            return null;
        }
        return (T) this.getSession().get(domainClass, id);
    }

    @SuppressWarnings({"unchecked"})
    public T findById(Long id, String... fetchFields) {
        if (id == null) {
            return null;
        }
        Criteria criteria = sessionFactory
                .getCurrentSession()
                .createCriteria(domainClass)
                .add(eq("id", id))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (fetchFields != null) {
            for (String field : fetchFields) {
                criteria.setFetchMode(field, FetchMode.JOIN);
            }
        }
        return (T) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public Collection<T> findByIds(Collection<Long> ids, String... fetchFields) {
        if (ids == null || ids.isEmpty()) {
            return emptyList();
        }
        Criteria criteria = sessionFactory
                .getCurrentSession()
                .createCriteria(domainClass)
                .add(Restrictions.in("id", ids))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (fetchFields != null) {
            for (String field : fetchFields) {
                criteria.setFetchMode(field, FetchMode.JOIN);
            }
        }
        Method method = null;
        List<T> entities = criteria.list();
        Map<Long, T> mapById = new HashMap<Long, T>(entities.size());
        for (T entity : entities) {
            try {
                if (method == null) {
                    method = entity.getClass().getMethod("getId");
                }
                mapById.put((Long) method.invoke(entity), entity);
            } catch (Throwable t) {
                return entities;
            }
        }
        List<T> sortedResult = new ArrayList<T>(entities.size());
        for (Long id : ids) {
            T entity = mapById.get(id);
            if (entity != null) {
                sortedResult.add(mapById.get(id));
            }
        }
        return sortedResult;
    }

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

    public int count(Criteria criteria) {
        return ((Number) criteria.setProjection(
                projectionList()
                        .add(rowCount())
        ).uniqueResult()).intValue();
    }
}

