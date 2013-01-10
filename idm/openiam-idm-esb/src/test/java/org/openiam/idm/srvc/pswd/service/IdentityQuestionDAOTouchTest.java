package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.continfo.domain.IdentityQuestionEntity;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;
import org.openiam.idm.srvc.pswd.service.IdentityQuestionDAO;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class IdentityQuestionDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IdentityQuestionDAO identityQuestionDAO;

    @Test
    public void touchAdd() {
    	identityQuestionDAO.add(new IdentityQuestionEntity());
    }

    /*@Test
    public void touchFindAllGroups() {
    	identityQuestionDAO.findAllGroups();
    }*/

    @Test
    public void touchFindById() {
    	identityQuestionDAO.findById("");
    }

    /*@Test
    public void touchFindByIdWithOutDependency() {
    	identityQuestionDAO.findById("", false);
    }

/*    @Test
    public void touchFindChildGroup() {
    	identityQuestionDAO.findChildGroup("");
    }

    @Test
    public void touchFindGroupNotLinkedToUser() {
    	identityQuestionDAO.findGroupNotLinkedToUser("", "");
    }

    @Test
    public void touchFindGroupsForUser() {
    	identityQuestionDAO.findGroupsForUser("");
    }

    @Test
    public void touchFindParentWithDependency() {
    	IdentityQuestionEntity groupEntity = new IdentityQuestionEntity();
    	identityQuestionDAO.add(groupEntity);
    	identityQuestionDAO.findParent(groupEntity.getGrpId(), false);
    }

    @Test
    public void touchFindRootGroups() {
    	identityQuestionDAO.findRootGroups();
    }
*/
    @Test
    public void touchRemove() {
    	IdentityQuestionEntity groupEntity = new IdentityQuestionEntity();
    	identityQuestionDAO.add(groupEntity);
    	identityQuestionDAO.remove(groupEntity);
    }

    /*@Test
    public void touchRemoveGroupList() {
    	IdentityQuestionEntity groupEntity = new IdentityQuestionEntity();
    	identityQuestionDAO.add(groupEntity);
    	identityQuestionDAO.removeGroupList("1");
    }

    @Test
    public void touchSearch() {
    	identityQuestionDAO.search(new GroupSearch());
    }*/

    @Test
    public void touchUpdate() {
    	identityQuestionDAO.update(new IdentityQuestionEntity());
    }
    
    @Test
    public void touchFindAllQuestions(){
    	identityQuestionDAO.findAllQuestions();
    }
    @Test
    public void touchFindAllQuestionsByQuestionGroup(){
    	identityQuestionDAO.findAllQuestionsByQuestionGroup("GLOBAL");
    }
    @Test
    public void touchFindAllQuestionsByUser(){
    	identityQuestionDAO.findAllQuestionsByUser("");
    }
}
