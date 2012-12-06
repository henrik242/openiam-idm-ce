package org.openiam.idm.srvc.continfo.service;

import java.util.Collections;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class EmailAddressDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private EmailAddressDAO emailAddressDAO;

    @Test
    public void touchAdd() {
        emailAddressDAO.add(new EmailAddressEntity());
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
        emailAddressDAO.remove(new EmailAddressEntity());
    }

    @Test
    public void touchRemoveByParent() {
        emailAddressDAO.removeByParent("", "");
    }

    @Test
    public void touch() {
        emailAddressDAO.saveEmailAddressMap("", "", Collections.<String, EmailAddressEntity>emptyMap());
    }

    @Test
    public void touchUpdate() {
        emailAddressDAO.update(new EmailAddressEntity());
    }
}
