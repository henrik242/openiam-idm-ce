package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.domain.RolePolicyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class RolePolicyDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private RolePolicyDAO rolePolicyDAO;

    @Test
    public void touchAdd() {
        rolePolicyDAO.add(new RolePolicyEntity());
    }

    @Test
    public void touchFindById() {
        rolePolicyDAO.findById("");
    }

    @Test
    public void touchFindRolePolicies() {
        rolePolicyDAO.findRolePolicies("", "");
    }

    @Test
    public void touchRemove() {
        RolePolicyEntity policyEntity = new RolePolicyEntity();
        rolePolicyDAO.add(policyEntity);
        rolePolicyDAO.remove(policyEntity);
    }

    @Test
    public void touchUpdate() {
        RolePolicyEntity policyEntity = new RolePolicyEntity();
        rolePolicyDAO.add(policyEntity);
        rolePolicyDAO.update(policyEntity);
    }

}
