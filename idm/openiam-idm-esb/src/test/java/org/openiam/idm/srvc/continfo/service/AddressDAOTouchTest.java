package org.openiam.idm.srvc.continfo.service;

import org.openiam.idm.srvc.continfo.domain.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * Smoke Test for DAO service for Address entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AddressDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private AddressDAO addressDAO;

    @Test
    private void touchAdd() {
        addressDAO.add(new AddressEntity());
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
