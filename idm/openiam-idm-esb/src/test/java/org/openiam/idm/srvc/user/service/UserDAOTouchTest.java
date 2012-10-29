package org.openiam.idm.srvc.user.service;

import java.util.Date;
import org.openiam.idm.srvc.user.dto.DelegationFilterSearch;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


@ContextConfiguration(locations={"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
  @Autowired
  private UserDAO userDAO;

  @BeforeMethod
  public void init() {

  }

  @Test
  public void touchAdd() {
      userDAO.add(new User());
  }

  @Test
  public void touchFindByDelegationProperties() {
      userDAO.findByDelegationProperties(new DelegationFilterSearch());
  }

  @Test
  public void touchFindById() {
      userDAO.findById("");
  }

  @Test
  public void touchFindByLastUpdateRange() {
      userDAO.findByLastUpdateRange(new Date(), new Date());
  }

  @Test
  public void touchFindByName() {
      userDAO.findByName("", "");
  }

  @Test
  public void touchFindByOrganization() {
      userDAO.findByOrganization("");
  }

  @Test
  public void touchFindByStatus() {
      userDAO.findByStatus("");
  }

  @Test
  public void touchFindStaff() {
      userDAO.findStaff("");
  }

  @Test
  public void touchFindSupervisors() {
      userDAO.findSupervisors("");
  }

  @Test
  public void touchRemove() {
      userDAO.remove(new User());
  }

  @Test
  public void touchSearch() {
      userDAO.search(new UserSearch());
  }

  @Test
  public void touchUpdate() {
      User user = new User();
      userDAO.add(user);
      userDAO.update(user);
  }

}
