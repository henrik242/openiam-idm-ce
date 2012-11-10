package org.openiam.idm.srvc.res.service;

import java.util.Arrays;
import java.util.Collections;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ResourceDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private ResourceDAO resourceDAO;

    @Test
    public void touchAdd() {
        resourceDAO.add(new ResourceEntity());
    }
    @Test
    public void touchAddResourceRolePrivilege() {
        ResourceEntity entity = new ResourceEntity();
        resourceDAO.add(entity);
        resourceDAO.addResourceRolePrivilege(entity.getResourceId(), "", "");
    }

    @Test
    public void touchFindAllResources() {
        resourceDAO.findAllResources();
    }

    @Test
    public void touchFindByExample() {
        resourceDAO.findByExample(new ResourceEntity());
    }

    @Test
    public void touchFindById() {
        resourceDAO.findById("");
    }

    @Test
    public void touchFindResourceByName() {
        resourceDAO.findResourceByName("");
    }

    @Test
    public void touchFindResourceByProperties() {
        resourceDAO.findResourceByProperties(Collections.EMPTY_LIST);
    }

    @Test
    public void touchFindResourceProperties() {
        resourceDAO.findResourceProperties("");
    }

    @Test
    public void touchFindResourceRolesByResource() {
        resourceDAO.findResourceRolesByResource("");
    }

    @Test
    public void touchFindResourcesByName() {
        resourceDAO.findResourcesByName("");
    }

    @Test
    public void touchFindResourcesByProperty() {
        resourceDAO.findResourcesByProperty("","");
    }

    @Test
    public void touchFindResourcesForRole() {
        resourceDAO.findResourcesForRole("","");
    }

    @Test
    public void touchFindResourcesForRoles() {
        resourceDAO.findResourcesForRoles("",Arrays.asList("1"));
    }

    @Test
    public void touchFindResourcesForUserRole() {
        resourceDAO.findResourcesForUserRole("");
    }

    @Test
    public void touchFindTypeOfResource() {
        resourceDAO.findTypeOfResource("");
    }

    @Test
    public void touchGetChildResources() {
        resourceDAO.getChildResources("");
    }

    @Test
    public void touchGetResourcesByType() {
        resourceDAO.getResourcesByType("");
    }


}
