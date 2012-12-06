package org.openiam.idm.srvc.role.service;

import org.openiam.idm.srvc.role.domain.RoleEmbeddableId;
import org.openiam.idm.srvc.role.domain.RoleEntity;
import org.openiam.idm.srvc.role.dto.RoleSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class RoleDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private RoleDAO roleDAO;

    @Test
    public void touchAdd() {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setRoleId(new RoleEmbeddableId("",""));
      roleDAO.add(roleEntity);
    }

    @Test
    public void touchAddGroupToRole() {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setRoleId(new RoleEmbeddableId("",""));
      roleDAO.add(roleEntity);
      roleDAO.addGroupToRole("","","");
    }

    @Test
    public void touchFindAllRoles() {
      roleDAO.findAllRoles();
    }

    @Test
    public void touchFindById() {
      roleDAO.findById(new RoleEmbeddableId("", ""));
    }

    @Test
    public void touchFindByParentId() {
      roleDAO.findByParentId("");
    }

    @Test
    public void touchFindIndirectUserRoles() {
      roleDAO.findIndirectUserRoles("");
    }

    @Test
    public void touchFindRolesInGroup() {
      roleDAO.findRolesInGroup("");
    }

    @Test
    public void touchFindRolesInService() {
      roleDAO.findRolesInService("");
    }

    @Test
    public void touchFindUserRoles() {
      roleDAO.findUserRoles("");
    }

    @Test
    public void touchFindUserRolesByService() {
      roleDAO.findUserRolesByService("", "");
    }

    @Test
    public void touchFindUsersInRole() {
      roleDAO.findUsersInRole("", "");
    }

    @Test
    public void touchRemove() {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setRoleId(new RoleEmbeddableId("",""));
      roleDAO.add(roleEntity);
      roleDAO.remove(roleEntity);
    }

    @Test
    public void touchRemoveAllGroupsFromRole() {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setRoleId(new RoleEmbeddableId("",""));
      roleDAO.add(roleEntity);
      roleDAO.removeAllGroupsFromRole("", "");
    }

    @Test
    public void touchRemoveGroupFromRole() {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setRoleId(new RoleEmbeddableId("",""));
      roleDAO.add(roleEntity);
      roleDAO.removeGroupFromRole("", "", "");
    }

    @Test
    public void touchSearch() {
      roleDAO.search(new RoleSearch());
    }

    @Test
    public void touchUpdate() {
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setRoleId(new RoleEmbeddableId("",""));
      roleDAO.add(roleEntity);
      roleDAO.update(roleEntity);
    }

}
