package org.openiam.idm.srvc.continfo.service;

import org.openiam.idm.srvc.org.domain.UserAffiliationEntity;
import org.openiam.idm.srvc.org.service.UserAffiliationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserAffiliationDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserAffiliationDAO userAffiliationDAO;

    @Test
    public void touchAdd() {
        userAffiliationDAO.add(new UserAffiliationEntity());
    }

    @Test
    public void touchFindById() {
        userAffiliationDAO.findById("");
    }

    @Test
    public void touchFindOrgAffiliationsByUser() {
        userAffiliationDAO.findOrgAffiliationsByUser("");
    }

    @Test
    public void touchFindUserOrgByUser() {
        userAffiliationDAO.findUserOrgByUser("");
    }

    @Test
    public void touchRemove() {
        UserAffiliationEntity affiliationEntity = new UserAffiliationEntity();
        userAffiliationDAO.add(affiliationEntity);
        userAffiliationDAO.remove(affiliationEntity);
    }

    @Test
    public void touchRemoveAllUsersInOrg() {
        userAffiliationDAO.removeAllUsersInOrg("");
    }

    @Test
    public void touchRemoveUserFromOrg() {
        userAffiliationDAO.removeUserFromOrg("","");
    }

    @Test
    public void touchUpdate() {
        UserAffiliationEntity affiliationEntity = new UserAffiliationEntity();
        userAffiliationDAO.add(affiliationEntity);
        userAffiliationDAO.update(affiliationEntity);
    }
}
