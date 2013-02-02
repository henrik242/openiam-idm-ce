package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourcePrivilegeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * Smoke Test for DAO service of ResourcePrivilege entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ResourcePrivilegeDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private ResourcePrivilegeDAO resourcePrivilegeDAO;

    @Test
    public void touchAdd() {
        resourcePrivilegeDAO.add(new ResourcePrivilegeEntity());
    }

    @Test
    public void touchFindById() {
        resourcePrivilegeDAO.findById("");
    }

    @Test
    public void touchFindPrivilegesByEntitlementType() {
        resourcePrivilegeDAO.findPrivilegesByEntitlementType("", "");
    }

    @Test
    public void touchFindPrivilegesByResourceId() {
        resourcePrivilegeDAO.findPrivilegesByResourceId("");
    }

    @Test
    public void touchRemove() {
        ResourcePrivilegeEntity privilegeEntity = new ResourcePrivilegeEntity();
        resourcePrivilegeDAO.add(privilegeEntity);
        resourcePrivilegeDAO.remove(privilegeEntity);
    }

    @Test
    public void touchUpdate() {
        ResourcePrivilegeEntity privilegeEntity = new ResourcePrivilegeEntity();
        resourcePrivilegeDAO.add(privilegeEntity);
        resourcePrivilegeDAO.update(privilegeEntity);
    }

}
