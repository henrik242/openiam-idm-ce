package org.openiam.idm.srvc.service.service;

import org.openiam.idm.srvc.service.dto.Service;

import java.util.List;


/**
 * Data access object for domain model class Service.
 *
 * @author suneetshah
 * @see diamelle.common.service.Service
 */

public interface ServiceDAO {     //extends BaseDAO {

    public void persist(Service transientInstance);

    public void delete(Service persistentInstance);

    public Service merge(Service detachedInstance);

    public Service findById(String id);

    public List<Service> findAll();

    public List<Service> findChildServices(String serviceId);

    public List<Service> findServicesByType(String type);


    public void attachDirty(Service instance);

    public void attachClean(Service instance);


}
