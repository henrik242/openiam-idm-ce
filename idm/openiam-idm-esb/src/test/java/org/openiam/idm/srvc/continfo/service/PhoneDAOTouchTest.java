package org.openiam.idm.srvc.continfo.service;

import java.util.Collections;
import org.openiam.idm.srvc.continfo.domain.PhoneEntity;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class PhoneDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private PhoneDAO phoneDAO;

    @Test
    public void touchAdd() {
        phoneDAO.add(new PhoneEntity());
    }

    @Test
    public void touchFindById() {
        phoneDAO.findById("");
    }

    @Test
    public void touchFindByName() {
        phoneDAO.findByName("", "", "");
    }

    @Test
    public void touchFindByParent() {
        phoneDAO.findByParent("", "");
    }

    @Test
    public void touchFindByParentAsList() {
        phoneDAO.findByParentAsList("", "");
    }

    @Test
    public void touchFindDefault() {
        phoneDAO.findDefault("", "");
    }

    @Test
    public void touchRemove() {
        phoneDAO.remove(new PhoneEntity());
    }

    @Test
    public void touchRemoveByParent() {
        phoneDAO.removeByParent("", "");
    }

    @Test
    public void touchSavePhoneMap() {
        phoneDAO.savePhoneMap("", "", Collections.EMPTY_MAP);
    }

    @Test
    public void touch() {
        phoneDAO.update(new PhoneEntity());
    }

}
