package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.dto.DelegationFilterSearch;
import org.openiam.idm.srvc.user.dto.UserSearch;

import java.util.Date;
import java.util.List;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;

/**
 * Data access interface for domain model class User.
 *
 * @author Suneet Shah
 * @see org.openiam.idm.srvc.user
 */
public interface UserDAO {

    public void add(UserEntity transientInstance);

    public void remove(UserEntity persistentInstance);

    public UserEntity update(UserEntity detachedInstance);

    public UserEntity findById(String id);

    public UserEntity findByName(String firstName, String lastName);

    public List<UserEntity> findByLastUpdateRange(Date startDate, Date endDate);

    public List<UserEntity> search(UserSearch search);

    public List<UserEntity> findByStatus(UserStatusEnum status);

    /* Methods to get staff and supervisors lists */
    public List<UserEntity> findStaff(String supervisorId);

    public List<UserEntity> findSupervisors(String staffId);

    public List<UserEntity> findByOrganization(String orgId);

    public List<UserEntity> findByDelegationProperties(DelegationFilterSearch search);


}
