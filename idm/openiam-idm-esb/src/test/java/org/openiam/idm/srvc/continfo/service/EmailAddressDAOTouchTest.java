package org.openiam.idm.srvc.continfo.service;

import java.util.Collections;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class EmailAddressDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private EmailAddressDAO emailAddressDAO;

    @Test
    public void touchAdd() {
        emailAddressDAO.add(new EmailAddress());
    }

    @Test
    public void touchFindById() {
        emailAddressDAO.findById("");
    }

    @Test
    public void touchFindByName() {
        emailAddressDAO.findByName("", "", "");
    }

    @Test
    public void touchFindByParent() {
        emailAddressDAO.findByParent("", "");
    }

    @Test
    public void touchFindByParentAsList() {
        emailAddressDAO.findByParentAsList("", "");
    }

    @Test
    public void touchDefault() {
        emailAddressDAO.findDefault("", "");
    }

    @Test
    public void touchRemove() {
        emailAddressDAO.remove(new EmailAddress());
    }

    @Test
    public void touchRemoveByParent() {
        emailAddressDAO.removeByParent("", "");
    }

    @Test
    public void touch() {
        emailAddressDAO.saveEmailAddressMap("", "", Collections.<String, EmailAddress>emptyMap());
    }

    @Test
    public void touchUpdate() {
        emailAddressDAO.update(new EmailAddress());
    }
}
