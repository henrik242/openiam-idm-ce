package org.openiam.idm.srvc.pswd.service;

import org.openiam.idm.srvc.continfo.domain.PasswordHistoryEntity;
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

	/*
	 * @Test public void touchFindAllGroups() {
	 * passwordHistoryDAO.findAllGroups(); }
	 */

	@Test
	public void touchFindById() {
		passwordHistoryDAO.findById("");
	}

	/*
	 * @Test public void touchFindByIdWithOutDependency() {
	 * passwordHistoryDAO.findById("", false); }
	 * 
	 * /*@Test public void touchFindChildGroup() {
	 * passwordHistoryDAO.findChildGroup(""); }
	 * 
	 * @Test public void touchFindGroupNotLinkedToUser() {
	 * passwordHistoryDAO.findGroupNotLinkedToUser("", ""); }
	 * 
	 * @Test public void touchFindGroupsForUser() {
	 * passwordHistoryDAO.findGroupsForUser(""); }
	 * 
	 * @Test public void touchFindParentWithDependency() { PasswordHistoryEntity
	 * groupEntity = new PasswordHistoryEntity();
	 * passwordHistoryDAO.add(groupEntity);
	 * passwordHistoryDAO.findParent(groupEntity.getGrpId(), false); }
	 * 
	 * @Test public void touchFindRootGroups() {
	 * passwordHistoryDAO.findRootGroups(); }
	 */

	@Test
	public void touchRemove() {
		PasswordHistoryEntity groupEntity = new PasswordHistoryEntity();
		passwordHistoryDAO.add(groupEntity);
		passwordHistoryDAO.remove(groupEntity);
	}

	/*
	 * @Test public void touchRemoveGroupList() { PasswordHistoryEntity
	 * groupEntity = new PasswordHistoryEntity();
	 * passwordHistoryDAO.add(groupEntity);
	 * passwordHistoryDAO.removeGroupList("1"); }
	 * 
	 * @Test public void touchSearch() { passwordHistoryDAO.search(new
	 * GroupSearch()); }
	 */

	@Test
	public void touchUpdate() {
		passwordHistoryDAO.update(new PasswordHistoryEntity());
	}

	public void touchFindPasswordHistoryByPrincipal() {
		passwordHistoryDAO.findPasswordHistoryByPrincipal("", "", "", 4);
	}
}
