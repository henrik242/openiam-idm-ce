package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.domain.UserAttributeEntity;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserAttributeDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserAttributeDAO userAttributeDAO;

    @Test
    public void touchAdd() {
        userAttributeDAO.add(new UserAttributeEntity());
    }

    @Test
    public void touchAttachClean() {
        UserAttributeEntity userAttribute = new UserAttributeEntity();
        userAttributeDAO.add(userAttribute);
        userAttributeDAO.attachClean(userAttribute);
    }

    @Test
    public void touchAttachDirty() {
        userAttributeDAO.attachDirty(new UserAttributeEntity());
    }

    @Test
    public void touchDeleteUserAttributes() {
        userAttributeDAO.deleteUserAttributes("");
    }

    @Test
    public void touchFindById() {
        userAttributeDAO.findById("");
    }

    @Test
    public void touchFindUserAttributes() {
        userAttributeDAO.findUserAttributes("");
    }

    @Test
    public void touchRemove() {
        userAttributeDAO.remove(new UserAttributeEntity());
    }

    @Test
    public void touchUpdate() {
        userAttributeDAO.update(new UserAttributeEntity());
    }
}
