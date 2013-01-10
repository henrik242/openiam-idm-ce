package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.continfo.domain.IdentityQuestGroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
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

    /*@Test
    public void touchFindAllGroups() {
    	identityQuestGroupDAO.findAllGroups();
    }*/

    @Test
    public void touchFindByIdWithDependency() {
    	identityQuestGroupDAO.findById("");
    }

/*    @Test
    public void touchFindByIdWithOutDependency() {
    	identityQuestGroupDAO.findById("", false);
    }

    @Test
    public void touchFindChildGroup() {
    	identityQuestGroupDAO.findChildGroup("");
    }

    @Test
    public void touchFindGroupNotLinkedToUser() {
    	identityQuestGroupDAO.findGroupNotLinkedToUser("", "");
    }

    @Test
    public void touchFindGroupsForUser() {
    	identityQuestGroupDAO.findGroupsForUser("");
    }

    @Test
    public void touchFindParentWithDependency() {
    	IdentityQuestGroupEntity groupEntity = new IdentityQuestGroupEntity();
    	identityQuestGroupDAO.add(groupEntity);
    	identityQuestGroupDAO.findParent(groupEntity.getGrpId(), false);
    }

    @Test
    public void touchFindRootGroups() {
    	identityQuestGroupDAO.findRootGroups();
    }
*/
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
    	//identityQuestGroupDAO.removeGroupList(groupEntity);
    }

  /*  @Test
    public void touchSearch() {
    	identityQuestGroupDAO.search(new GroupSearch());
    }
*/
    @Test
    public void touchUpdate() {
    	identityQuestGroupDAO.update(new IdentityQuestGroupEntity());
    }

}
