package org.openiam.am.srvc.resattr.dao;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

/**
 * User: Alexander Duckardt
 * Date: 8/15/12
 */
public interface GenericDao<Entity, PrimaryKey extends Serializable> {

    /**
     * Get data by primary key
     * @param id
     * @return
     * @throws Exception
     */
    public Entity findById(PrimaryKey id)throws Exception;

    /**
     * Gets all data from database
     * @return
     * @throws Exception
     */
    public List<Entity> getAll() throws Exception;
    /**
     * Adds a new instance
     *
     * @param instance
     */
    public Entity add(Entity instance)throws Exception;

    /**
     * Updates an existing instance
     *
     * @param instance
     */
    public Entity update(Entity instance)throws Exception;

    /**
     * Removes an existing instance
     *
     * @param instance
     */
    public void delete(Entity instance)throws Exception;
    /**
     * Removes  data by primary key
     *
     * @param id
     */
    public void delete(PrimaryKey id)throws Exception;

    public Session getCurrentSession();
}
