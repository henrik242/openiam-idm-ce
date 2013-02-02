package org.openiam.idm.srvc.org.service;

import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

/**
 * Smoke Test for DAO service of Organization entity
 *
 * @author vitaly.yakunin
 */
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:dozer-application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class OrganizationDAOTouchTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private OrganizationDAO orgDAO;

    @Test
    public void touchAdd() {
        orgDAO.add(new OrganizationEntity());
    }

    @Test
    public void touchFindAllOrganization() {
        orgDAO.findAllOrganization();
    }

    @Test
    public void touchFindById() {
        orgDAO.findById("");
    }

    @Test
    public void touchFindChildOrganization() {
        orgDAO.findChildOrganization("");
    }

    @Test
    public void touchFindOrganizationByClassification() {
        orgDAO.findOrganizationByClassification("", null);
    }

    @Test
    public void touchFindOrganizationByStatus() {
        orgDAO.findOrganizationByStatus("", "");
    }

    @Test
    public void touchFindOrganizationByType() {
        orgDAO.findOrganizationByType("", "");
    }

    @Test
    public void touchFindParent() {
        orgDAO.findParent("");
    }

    @Test
    public void touchFindRootOrganizations() {
        orgDAO.findRootOrganizations();
    }

    @Test
    public void touchRemove() {
        OrganizationEntity organization = new OrganizationEntity();
        orgDAO.add(organization);
        orgDAO.remove(organization);
    }

    @Test
    public void touchSearch() {
        orgDAO.search("", "", null, "");
    }

    @Test
    public void touchUpdate() {
        OrganizationEntity organization = new OrganizationEntity();
        orgDAO.add(organization);
        orgDAO.update(organization);
    }
}
