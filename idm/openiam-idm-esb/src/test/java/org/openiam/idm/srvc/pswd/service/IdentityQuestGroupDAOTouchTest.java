package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.openiam.idm.srvc.pswd.domain.IdentityQuestGroupEntity;
import org.openiam.idm.srvc.pswd.service.IdentityQuestGroupDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class IdentityQuestGroupDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IdentityQuestGroupDAO identityQuestGroupDAO;

    @Test
    public void touchAdd() {
    	identityQuestGroupDAO.add(new IdentityQuestGroupEntity());
    }

    @Test
    public void touchFindByIdWithDependency() {
    	identityQuestGroupDAO.findById("");
    }

    @Test
    public void touchRemove() {
    	IdentityQuestGroupEntity groupEntity = new IdentityQuestGroupEntity();
    	identityQuestGroupDAO.add(groupEntity);
    	identityQuestGroupDAO.remove(groupEntity);
    }

    @Test
    public void touchRemoveGroupList() {
    	IdentityQuestGroupEntity groupEntity = new IdentityQuestGroupEntity();
    	identityQuestGroupDAO.add(groupEntity);
    }

    @Test
    public void touchUpdate() {
    	identityQuestGroupDAO.update(new IdentityQuestGroupEntity());
    }

}
