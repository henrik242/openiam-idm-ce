package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.pswd.domain.PasswordHistoryEntity;
import org.openiam.idm.srvc.pswd.dto.PasswordHistory;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.openiam.idm.srvc.pswd.service.PasswordHistoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
		"classpath:dozer-application-context-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class PasswordHistoryDAOTouchTest extends
		AbstractTransactionalTestNGSpringContextTests {
	@Autowired
	private PasswordHistoryDAO passwordHistoryDAO;

	@Test
	public void touchAdd() {
		passwordHistoryDAO.add(new PasswordHistoryEntity());
	}

	@Test
	public void touchFindById() {
		passwordHistoryDAO.findById("");
	}


	@Test
	public void touchRemove() {
		PasswordHistoryEntity groupEntity = new PasswordHistoryEntity();
		passwordHistoryDAO.add(groupEntity);
		passwordHistoryDAO.remove(groupEntity);
	}


	@Test
	public void touchUpdate() {
		passwordHistoryDAO.update(new PasswordHistoryEntity());
	}

	public void touchFindPasswordHistoryByPrincipal() {
		passwordHistoryDAO.findPasswordHistoryByPrincipal("", "", "", 4);
	}
}
