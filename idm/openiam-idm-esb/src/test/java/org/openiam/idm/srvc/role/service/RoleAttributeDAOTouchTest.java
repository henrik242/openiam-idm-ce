package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.domain.RoleAttributeEntity;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class RoleAttributeDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private RoleAttributeDAO roleAttributeDAO;

    @Test
    public void touchAdd() {
        roleAttributeDAO.add(new RoleAttributeEntity());
    }

    @Test
    public void touchDeleteRoleAttributes() {
        roleAttributeDAO.deleteRoleAttributes("", "");
    }

    @Test
    public void touchFindByExample() {
        RoleAttributeEntity roleAttributeEntity = new RoleAttributeEntity();
        roleAttributeDAO.add(roleAttributeEntity);
        roleAttributeDAO.findByExample(roleAttributeEntity);
    }

    @Test
    public void touchFindById() {
        roleAttributeDAO.findById("");
    }

    @Test
    public void touchRemove() {
        RoleAttributeEntity roleAttributeEntity = new RoleAttributeEntity();
        roleAttributeDAO.add(roleAttributeEntity);
        roleAttributeDAO.remove(roleAttributeEntity);
    }

    @Test
    public void touchUpdate() {
        RoleAttributeEntity roleAttributeEntity = new RoleAttributeEntity();
        roleAttributeDAO.add(roleAttributeEntity);
        roleAttributeDAO.update(roleAttributeEntity);
    }
}
