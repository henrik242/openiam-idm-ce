package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourcePropEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * Smoke Test for DAO service of ResourceProp entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ResourcePropDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private ResourcePropDAO resourcePropDAO;

    @Test
    public void touchAdd() {
        resourcePropDAO.add(new ResourcePropEntity());
    }

    @Test
    public void touchFindAllResourceProps() {
        resourcePropDAO.findAllResourceProps();
    }

    @Test
    public void touchFindByExample() {
        ResourcePropEntity propEntity = new ResourcePropEntity();
        resourcePropDAO.findByExample(propEntity);
    }

    @Test
    public void touchFindById() {
        resourcePropDAO.findById("");
    }

    @Test
    public void touchPersist() {
        resourcePropDAO.persist(new ResourcePropEntity());
    }

    @Test
    public void touchRemove() {
        ResourcePropEntity propEntity = new ResourcePropEntity();
        resourcePropDAO.add(propEntity);
        resourcePropDAO.remove(propEntity);
    }

    @Test
    public void touchRemoveAllResourceProps() {
        resourcePropDAO.removeAllResourceProps();
    }

    @Test
    public void touchUpdate() {
        ResourcePropEntity propEntity = new ResourcePropEntity();
        resourcePropDAO.add(propEntity);
        resourcePropDAO.update(propEntity);
    }

}
