package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.dto.DelegationFilterSearch;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserSearch;

import java.util.Date;
import java.util.List;

/**
 * Data access interface for domain model class User.
 *
 * @author Suneet Shah
 * @see org.openiam.idm.srvc.user
 */
public interface UserDAO {

    public void add(User transientInstance);

    public void remove(User persistentInstance);

    public User update(User detachedInstance);

    public User findById(String id);

    public User findByName(String firstName, String lastName);

    public List<User> findByLastUpdateRange(Date startDate, Date endDate);

    public List<User> search(UserSearch search);

    public List<User> findByStatus(String status);

    /* Methods to get staff and supervisors lists */
    public List<User> findStaff(String supervisorId);

    public List<User> findSupervisors(String staffId);

    public List<User> findByOrganization(String orgId);

    public List<User> findByDelegationProperties(DelegationFilterSearch search);


}
