package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;
import org.openiam.idm.srvc.pswd.domain.IdentityQuestionEntity;
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

    @Test
    public void touchFindById() {
    	identityQuestionDAO.findById("");
    }

    @Test
    public void touchRemove() {
    	IdentityQuestionEntity groupEntity = new IdentityQuestionEntity();
    	identityQuestionDAO.add(groupEntity);
    	identityQuestionDAO.remove(groupEntity);
    }


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
