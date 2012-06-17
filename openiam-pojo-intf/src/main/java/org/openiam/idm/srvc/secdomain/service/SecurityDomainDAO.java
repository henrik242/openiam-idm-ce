package org.openiam.idm.srvc.secdomain.service;

import org.openiam.idm.srvc.secdomain.dto.SecurityDomain;

import java.util.List;


/**
 * Data access object for domain model class SecurityDomain.
 *
 * @author suneetshah
 */

public interface SecurityDomainDAO {     //extends BaseDAO {

    public void add(SecurityDomain transientInstance);

    public void remove(SecurityDomain persistentInstance);

    public SecurityDomain update(SecurityDomain detachedInstance);

    public SecurityDomain findById(String id);

    public List<SecurityDomain> findAll();


}
