package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;
import org.openiam.idm.srvc.pswd.domain.UserIdentityAnswerEntity;

/**
 * Smoke Test for DAO service of UserIdentityAnswer entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserIdentityAnswerDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserIdentityAnswerDAO userIdentityAnswerDAO;

    @Test
    public void touchAdd() {
    	userIdentityAnswerDAO.add(new UserIdentityAnswerEntity());
    }

    @Test
    public void touchFindById() {
    	userIdentityAnswerDAO.findById("");
    }


    @Test
    public void touchRemove() {
    	UserIdentityAnswerEntity groupEntity = new UserIdentityAnswerEntity();
    	userIdentityAnswerDAO.add(groupEntity);
    	userIdentityAnswerDAO.delete(groupEntity);
    }


    @Test
    public void touchUpdate() {
    	userIdentityAnswerDAO.update(new UserIdentityAnswerEntity());
    }
    
    @Test
    public void touchFindAnswersByUser(){
    	userIdentityAnswerDAO.findAnswersByUser("");
    }

}
