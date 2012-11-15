package org.openiam.idm.srvc.grp.service;

import org.openiam.idm.srvc.grp.domain.UserGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserGroupDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserGroupDAO userGroupDAO;

    @Test
    public void touchAdd() {
        userGroupDAO.add(new UserGroupEntity());
    }

    @Test
    public void touchFindById() {
        userGroupDAO.findById("");
    }

    @Test
    public void touchFindUserByGroup() {
        userGroupDAO.findUserByGroup("");
    }

    @Test
    public void touchFindUserInGroup() {
        userGroupDAO.findUserInGroup("", "");
    }

    @Test
    public void touchRemove() {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupDAO.add(userGroupEntity);
        userGroupDAO.remove(userGroupEntity);
    }

    @Test
    public void touchRemoveUserFromGroup() {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupDAO.add(userGroupEntity);
        userGroupDAO.removeUserFromGroup("", "");
    }

    @Test
    public void touchUpdate() {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupDAO.add(userGroupEntity);
        userGroupDAO.update(userGroupEntity);
    }
}
