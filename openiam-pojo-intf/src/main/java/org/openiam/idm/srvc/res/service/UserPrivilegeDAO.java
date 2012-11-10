package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.UserPrivilegeEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: arun
 * Date: 8/8/11
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UserPrivilegeDAO {

    UserPrivilegeEntity findById(String id);

    List<UserPrivilegeEntity> findByExample(UserPrivilegeEntity instance);

    UserPrivilegeEntity add(UserPrivilegeEntity instance);

    void remove(UserPrivilegeEntity instance);

    UserPrivilegeEntity update(UserPrivilegeEntity instance);

    List<UserPrivilegeEntity> findAllUserPrivileges();

    int removeAllUserPrivileges();
}
