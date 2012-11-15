package org.openiam.idm.srvc.grp.service;

import org.openiam.idm.srvc.grp.domain.GroupAttributeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class GroupAttributeDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private GroupAttributeDAO groupAttrDAO;

    @Test
    public void touchAdd() {
        groupAttrDAO.add(new GroupAttributeEntity());
    }

    @Test
    public void touchFindAttributesByParent() {
        groupAttrDAO.findAttributesByParent("");
    }

    @Test
    public void touchFindById() {
        groupAttrDAO.findById("");
    }

    @Test
    public void touchRemove() {
        GroupAttributeEntity groupAttributeEntity = new GroupAttributeEntity();
        groupAttrDAO.add(groupAttributeEntity);
        groupAttrDAO.remove(groupAttributeEntity);
    }

    @Test
    public void touchRemoveAttributesByParent() {
        groupAttrDAO.removeAttributesByParent("");
    }

    @Test
    public void touchRemoveAttributesForGroupList() {
        groupAttrDAO.removeAttributesForGroupList("1");
    }

    @Test
    public void touchUpdate() {
        GroupAttributeEntity groupAttributeEntity = new GroupAttributeEntity();
        groupAttrDAO.add(groupAttributeEntity);
        groupAttrDAO.update(groupAttributeEntity);
    }

}
