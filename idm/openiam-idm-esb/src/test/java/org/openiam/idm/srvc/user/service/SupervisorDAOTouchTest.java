package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.domain.SupervisorEntity;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class SupervisorDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private SupervisorDAO supervisorDao;

    @Test
    public void touchAdd() {
        supervisorDao.add(new SupervisorEntity());
    }

    @Test
    public void touchFindByExample() {
        supervisorDao.findByExample(new SupervisorEntity());
    }

    @Test
    public void touchFindById() {
        supervisorDao.findById("");
    }

    @Test
    public void touchFindEmployees() {
        supervisorDao.findEmployees("");
    }

    @Test
    public void touchFindPrimarySupervisor() {
        supervisorDao.findPrimarySupervisor("");
    }

    @Test
    public void touchFindSupervisors() {
        supervisorDao.findSupervisors("");
    }

    @Test
    public void touchRemove() {
        supervisorDao.remove(new SupervisorEntity());
    }

    @Test
    public void touchUpdate() {
        supervisorDao.update(new SupervisorEntity());
    }
}
