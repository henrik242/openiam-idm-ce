package org.openiam.idm.srvc.res.service;

import org.openiam.idm.srvc.res.domain.ResourceRoleEmbeddableId;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * Smoke Test for DAO service of ResourceRole entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ResourceRoleDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private ResourceRoleDAO resourceRoleDAO;

    @Test
    public void touchAdd() {
       ResourceRoleEntity entity = new ResourceRoleEntity();
       entity.setId(new ResourceRoleEmbeddableId("","","",""));
       resourceRoleDAO.add(entity);
    }

    @Test
    public void touchFindAllResourceRoles() {
       resourceRoleDAO.findAllResourceRoles();
    }

    @Test
    public void touchFindByExample() {
       ResourceRoleEntity roleEntity = new ResourceRoleEntity();
       resourceRoleDAO.findByExample(roleEntity);
    }

    @Test
    public void touchFindById() {
       resourceRoleDAO.findById(new ResourceRoleEmbeddableId("","","",""));
    }

    @Test
    public void touchFindResourcesForRole() {
       resourceRoleDAO.findResourcesForRole("","");
    }

    @Test
    public void touchFindRolesForResource() {
       resourceRoleDAO.findRolesForResource("");
    }

    @Test
    public void touchRemove() {
       ResourceRoleEntity roleEntity = new ResourceRoleEntity();
       roleEntity.setId(new ResourceRoleEmbeddableId("","","",""));
       resourceRoleDAO.add(roleEntity);
       resourceRoleDAO.remove(roleEntity);
    }

    @Test
    public void touchRemoveAllResourceRoles() {
       resourceRoleDAO.removeAllResourceRoles();
    }

    @Test
    public void touchRemoveResourceRole() {
       resourceRoleDAO.removeResourceRole("","");
    }
}
