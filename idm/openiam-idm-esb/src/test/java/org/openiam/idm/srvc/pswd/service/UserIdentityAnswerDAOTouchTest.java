package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.continfo.domain.UserIdentityAnswerEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;
import org.openiam.idm.srvc.pswd.service.UserIdentityAnswerDAO;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserIdentityAnswerDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserIdentityAnswerDAO userIdentityAnswerDAO;

    @Test
    public void touchAdd() {
    	userIdentityAnswerDAO.add(new UserIdentityAnswerEntity());
    }

    /*@Test
    public void touchFindAllGroups() {
    	userIdentityAnswerDAO.findAllGroups();
    }*/

    @Test
    public void touchFindById() {
    	userIdentityAnswerDAO.findById("");
    }

    /*@Test
    public void touchFindByIdWithOutDependency() {
    	userIdentityAnswerDAO.findById("", false);
    }

    /*@Test
    public void touchFindChildGroup() {
    	userIdentityAnswerDAO.findChildGroup("");
    }

    @Test
    public void touchFindGroupNotLinkedToUser() {
    	userIdentityAnswerDAO.findGroupNotLinkedToUser("", "");
    }

    @Test
    public void touchFindGroupsForUser() {
    	userIdentityAnswerDAO.findGroupsForUser("");
    }

    @Test
    public void touchFindParentWithDependency() {
    	UserIdentityAnswerEntity groupEntity = new UserIdentityAnswerEntity();
    	userIdentityAnswerDAO.add(groupEntity);
    	userIdentityAnswerDAO.findParent(groupEntity.getGrpId(), false);
    }

    @Test
    public void touchFindRootGroups() {
    	userIdentityAnswerDAO.findRootGroups();
    }*/

    @Test
    public void touchRemove() {
    	UserIdentityAnswerEntity groupEntity = new UserIdentityAnswerEntity();
    	userIdentityAnswerDAO.add(groupEntity);
    	userIdentityAnswerDAO.delete(groupEntity);
    }

    /*@Test
    public void touchRemoveGroupList() {
    	UserIdentityAnswerEntity groupEntity = new UserIdentityAnswerEntity();
    	userIdentityAnswerDAO.add(groupEntity);
    	userIdentityAnswerDAO.removeGroupList("1");
    }

    @Test
    public void touchSearch() {
    	userIdentityAnswerDAO.search(new GroupSearch());
    }*/

    @Test
    public void touchUpdate() {
    	userIdentityAnswerDAO.update(new UserIdentityAnswerEntity());
    }
    
    @Test
    public void touchFindAnswersByUser(){
    	userIdentityAnswerDAO.findAnswersByUser("");
    }

}
