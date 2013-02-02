package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.UserPrivilegeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * Smoke Test for DAO service of UserPrivilege entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserPrivilegeDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserPrivilegeDAO userPrivilegeDAO;

    @Test
    public void touchAdd() {
        userPrivilegeDAO.add(new UserPrivilegeEntity());
    }

    @Test
    public void touchFindAllUserPrivileges() {
        userPrivilegeDAO.findAllUserPrivileges();
    }

    @Test
    public void touchFindByExample() {
        userPrivilegeDAO.findByExample(new UserPrivilegeEntity());
    }

    @Test
    public void touchFindById() {
        userPrivilegeDAO.findById("");
    }

    @Test
    public void touchRemove() {
        UserPrivilegeEntity privilegeEntity = new UserPrivilegeEntity();
        userPrivilegeDAO.add(privilegeEntity);
        userPrivilegeDAO.remove(privilegeEntity);
    }

    @Test
    public void touchRemoveAllUserPrivileges() {
        userPrivilegeDAO.removeAllUserPrivileges();
    }

    @Test
    public void touchUpdate() {
        UserPrivilegeEntity privilegeEntity = new UserPrivilegeEntity();
        userPrivilegeDAO.add(privilegeEntity);
        userPrivilegeDAO.update(privilegeEntity);
    }

}
