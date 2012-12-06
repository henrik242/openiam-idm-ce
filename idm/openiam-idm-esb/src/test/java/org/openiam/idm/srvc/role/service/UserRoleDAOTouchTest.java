package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.domain.UserRoleEntity;
import org.openiam.idm.srvc.role.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserRoleDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Test
    public void touchAdd() {
       UserRoleEntity entity = new UserRoleEntity();
       entity.setRoleId("");
       entity.setUserId("");
       entity.setServiceId("");
       userRoleDAO.add(entity);
    }

    @Test
    public void touchFindById() {
       userRoleDAO.findById("");
    }

    @Test
    public void touchFindUserByRole() {
       userRoleDAO.findUserByRole("", "");
    }

    @Test
    public void touchFindUserRoleByUser() {
       userRoleDAO.findUserRoleByUser("");
    }

    @Test
    public void touchRemove() {
       UserRoleEntity entity = new UserRoleEntity();
       entity.setRoleId("");
       entity.setUserId("");
       entity.setServiceId("");
       userRoleDAO.add(entity);
       userRoleDAO.remove(entity);
    }

    @Test
    public void touchRemoveAllUsersInRole() {
       userRoleDAO.removeAllUsersInRole("", "");
    }

    @Test
    public void touchRemoveUserFromRole() {
       userRoleDAO.removeUserFromRole("", "", "");
    }

    @Test
    public void touchUpdate() {
       UserRoleEntity entity = new UserRoleEntity();
       entity.setRoleId("");
       entity.setUserId("");
       entity.setServiceId("");
       userRoleDAO.add(entity);
       userRoleDAO.update(entity);
    }

}
