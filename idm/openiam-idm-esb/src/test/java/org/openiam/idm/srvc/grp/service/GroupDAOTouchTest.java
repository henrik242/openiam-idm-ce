package org.openiam.idm.srvc.grp.service;

import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.GroupSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class GroupDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private GroupDAO groupDAO;

    @Test
    public void touchAdd() {
        groupDAO.add(new GroupEntity());
    }

    @Test
    public void touchFindAllGroups() {
        groupDAO.findAllGroups();
    }

    @Test
    public void touchFindByIdWithDependency() {
        groupDAO.findById("", true);
    }

    @Test
    public void touchFindByIdWithOutDependency() {
        groupDAO.findById("", false);
    }

    @Test
    public void touchFindChildGroup() {
        groupDAO.findChildGroup("");
    }

    @Test
    public void touchFindGroupNotLinkedToUser() {
        groupDAO.findGroupNotLinkedToUser("", "");
    }

    @Test
    public void touchFindGroupsForUser() {
        groupDAO.findGroupsForUser("");
    }

    @Test
    public void touchFindParentWithDependency() {
        GroupEntity groupEntity = new GroupEntity();
        groupDAO.add(groupEntity);
        groupDAO.findParent(groupEntity.getGrpId(), false);
    }

    @Test
    public void touchFindRootGroups() {
        groupDAO.findRootGroups();
    }

    @Test
    public void touchRemove() {
        GroupEntity groupEntity = new GroupEntity();
        groupDAO.add(groupEntity);
        groupDAO.remove(groupEntity);
    }

    @Test
    public void touchRemoveGroupList() {
        GroupEntity groupEntity = new GroupEntity();
        groupDAO.add(groupEntity);
        groupDAO.removeGroupList("1");
    }

    @Test
    public void touchSearch() {
        groupDAO.search(new GroupSearch());
    }

    @Test
    public void touchUpdate() {
        groupDAO.update(new GroupEntity());
    }

}
