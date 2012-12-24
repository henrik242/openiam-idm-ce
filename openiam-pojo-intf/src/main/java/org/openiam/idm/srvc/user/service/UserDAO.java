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

    void add(UserEntity transientInstance);

    void remove(UserEntity persistentInstance);

    UserEntity update(UserEntity detachedInstance);

    UserEntity findById(String id);

    UserEntity findByName(String firstName, String lastName);

    List<UserEntity> findByLastUpdateRange(Date startDate, Date endDate);

    List<UserEntity> search(UserSearch search);

    Integer searchCount(UserSearch search);

    List<UserEntity> findByStatus(UserStatusEnum status);

    /* Methods to get staff and supervisors lists */
    List<UserEntity> findStaff(String supervisorId);

    List<UserEntity> findSupervisors(String staffId);

    List<UserEntity> findByOrganization(String orgId);

    List<UserEntity> findByDelegationProperties(DelegationFilterSearch search);


}
