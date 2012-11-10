package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourceUserEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ResourceUserDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private ResourceUserDAO resourceUserDAO;

    @Test
    public void touchAdd() {
        ResourceUserEntity resourceUserEntity = new ResourceUserEntity();
        resourceUserEntity.setId(new ResourceUserEmbeddableId("", "", ""));
        resourceUserDAO.add(resourceUserEntity);
    }

    @Test
    public void touchFindAllResourceForUsers() {
        resourceUserDAO.findAllResourceForUsers("");
    }

    @Test
    public void touchFindAllResourceUsers() {
        resourceUserDAO.findAllResourceUsers();
    }

    @Test
    public void touchFindByExample() {
        ResourceUserEntity resourceUserEntity = new ResourceUserEntity();
        resourceUserEntity.setId(new ResourceUserEmbeddableId("", "", ""));
        resourceUserDAO.findByExample(resourceUserEntity);
    }

    @Test
    public void touchFindById() {
        resourceUserDAO.findById(new ResourceUserEmbeddableId("", "", ""));
    }

    @Test
    public void touchRemove() {
        ResourceUserEntity resourceUserEntity = new ResourceUserEntity();
        resourceUserEntity.setId(new ResourceUserEmbeddableId("", "", ""));
        resourceUserDAO.add(resourceUserEntity);
        resourceUserDAO.remove(resourceUserEntity);
    }

    @Test
    public void touchRemoveAllResourceUsers() {
        resourceUserDAO.removeAllResourceUsers();
    }

    @Test
    public void touchRemoveUserFromAllResources() {

        resourceUserDAO.removeUserFromAllResources("1");
    }

    @Test
    public void touchUpdate() {
        ResourceUserEntity resourceUserEntity = new ResourceUserEntity();
        resourceUserEntity.setId(new ResourceUserEmbeddableId("", "", ""));
        resourceUserDAO.add(resourceUserEntity);
        resourceUserDAO.update(resourceUserEntity);
    }

}
