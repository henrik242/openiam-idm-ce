package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.dto.UserNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UserNoteDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserNoteDAO userNoteDAO;

    @Test
    public void touchAttachClean() {
      UserNote usernote = new UserNote();
      userNoteDAO.persist(usernote);
      userNoteDAO.attachClean(usernote);
    }

    @Test
    public void touchAttachDirty() {
      UserNote usernote = new UserNote();
      userNoteDAO.persist(usernote);
      userNoteDAO.attachDirty(usernote);
    }

    @Test
    public void touchDelete() {
      userNoteDAO.delete(new UserNote());
    }

    @Test
    public void touchDeleteUserNotes() {
      userNoteDAO.deleteUserNotes("");
    }

    @Test
    public void touchFindByExample() {
      userNoteDAO.findByExample(new UserNote());
    }

    @Test
    public void touchFindById() {
      userNoteDAO.findById("");
    }

    @Test
    public void touchFindUserNotes() {
      userNoteDAO.findUserNotes("");
    }

    @Test
    public void touchMerge() {
      userNoteDAO.merge(new UserNote());
    }

    @Test
    public void touchPersist() {
      userNoteDAO.persist(new UserNote());
    }
}
