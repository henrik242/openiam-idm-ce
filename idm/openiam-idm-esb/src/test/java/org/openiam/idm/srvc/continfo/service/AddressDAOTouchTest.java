package org.openiam.idm.srvc.continfo.service;

import org.openiam.idm.srvc.continfo.dto.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AddressDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private AddressDAO addressDAO;

    @Test
    private void touchAdd() {
        addressDAO.add(new Address());
    }

    @Test
    private void touchFindById() {
        addressDAO.findById("");
    }

    @Test
    private void touchFindByName() {
        addressDAO.findByName("","","");
    }

    @Test
    private void touchFindByParent() {
        addressDAO.findByParent("","");
    }

    @Test
    private void touchFindByParentAsList() {
        addressDAO.findByParentAsList("","");
    }

    @Test
    private void touchFindByDefault() {
        addressDAO.findDefault("","");
    }
}
