package org.openiam.idm.srvc.org.service;

import javax.jws.*;

//import diamelle.common.continfo.*;
//import diamelle.base.prop.*;

import java.util.*;
import java.rmi.*;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.openiam.dozer.converter.OrganizationAttributeDozerConverter;
import org.openiam.dozer.converter.OrganizationDozerConverter;
import org.openiam.dozer.converter.UserAffiliationDozerConvertor;
import org.openiam.dozer.converter.UserDozerConverter;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;
import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.dto.*;
import org.openiam.idm.srvc.org.domain.UserAffiliationEntity;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.service.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <code>OrganizationManager</code> provides a service level interface to the
 * Organization components and its dependant objects as well as search
 * capability.<br>
 * <p/>
 * Note: The spring configuration file defines MetadataTypes are used to identify Departments and Divisions in the org list.
 *
 * @author OpenIAm
 * @version 2
 */

@WebService(endpointInterface = "org.openiam.idm.srvc.org.service.OrganizationDataService",
        targetNamespace = "urn:idm.openiam.org/srvc/org/service",
        portName = "OrganizationDataWebServicePort",
        serviceName = "OrganizationDataWebService")
public class OrganizationDataServiceImpl implements OrganizationDataService {

    protected OrganizationDAO orgDao;

    protected OrganizationAttributeDAO orgAttrDao;
    protected UserAffiliationDAO orgAffiliationDao;

    @Autowired
    protected UserDAO userDAO;

    @Autowired
    private OrganizationDozerConverter organizationDozerConverter;

    @Autowired
    private OrganizationAttributeDozerConverter organizationAttributeDozerConverter;

    @Autowired
    private UserAffiliationDozerConvertor userAffiliationDozerConvertor;
    /**
     * Returns a list of companies that match the search criteria.
     *
     * @param orgId
     * @return
     * @throws RemoteException
     */
    // List searchOrganization(OrganizationSearch search) throws
    // RemoteException;
    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#subOrganizations(java.lang.String)
      */
    public List<Organization> subOrganizations(String orgId) {
        if (orgId == null) {
            throw new NullPointerException("orgId is null");
        }
        List<OrganizationEntity> organizationEntityList = orgDao.findChildOrganization(orgId);
        //return testOrgList(orgId);
        return organizationDozerConverter.convertToDTOList(organizationEntityList, false);
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getTopLevelOrganizations()
      */
    public List<Organization> getTopLevelOrganizations() {
        List<OrganizationEntity> orgEntityList = orgDao.findRootOrganizations();

        return organizationDozerConverter.convertToDTOList(orgEntityList, true);
        //return testOrgList("myTopLevelOrg");
    }

    /**
     * Returns a list of all organizations based on a metadataType. The parentId parameter can be used to get
     * values that are nested further in the hierarchy. If parentId is null, the method will search only on the typeId and parentId
     * will be ignored.
     *
     * @param typeId
     * @param parentId
     * @return
     */
    public List<Organization> getOrganizationByType(String typeId, String parentId) {
        if (typeId == null)
            throw new NullPointerException("typeId is null");

        List<OrganizationEntity> orgEntityList = orgDao.findOrganizationByType(typeId, parentId);
        if (orgEntityList == null) {
            return null;
        }

        return organizationDozerConverter.convertToDTOList(orgEntityList, true);
    }

    public List<Organization> getOrganizationByClassification(String parentId, OrgClassificationEnum classification) {
        if (classification == null)
            throw new NullPointerException("classification is null");

        List<OrganizationEntity> orgEntityList = orgDao.findOrganizationByClassification(parentId, classification);
        if (orgEntityList == null) {
            return null;
        }

        return organizationDozerConverter.convertToDTOList(orgEntityList,true);
    }

    public List<Organization> allDepartments(String parentId) {
        return getOrganizationByClassification(parentId, OrgClassificationEnum.DEPARTMENT);
    }

    public List<Organization> allDivisions(String parentId) {
        return getOrganizationByClassification(parentId, OrgClassificationEnum.DIVISION);
    }


    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#addOrganization(org.openiam.idm.srvc.org.dto.Organization)
      */
    public Organization addOrganization(Organization org) {
        if (org == null)
            throw new NullPointerException("org object is null");
        OrganizationEntity entity = orgDao.add(organizationDozerConverter.convertToEntity(org, true));
        return organizationDozerConverter.convertToDTO(entity, true);
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#updateOrganization(org.openiam.idm.srvc.org.dto.Organization)
      */
    public void updateOrganization(Organization org) {
        if (org == null)
            throw new NullPointerException("org object is null");
        if (StringUtils.isEmpty(org.getOrgId()))
            throw new NullPointerException("org id is null");

        orgDao.update(organizationDozerConverter.convertToEntity(org, true));
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#removeOrganization(java.lang.String)
      */
    public void removeOrganization(String orgId) {
        if (orgId == null)
            throw new NullPointerException("orgId is null");

        OrganizationEntity entity = orgDao.findById(orgId);
        orgDao.remove(entity);

    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#isRootOrganization(java.lang.String)
      */
    public boolean isRootOrganization(String orgId) {
        if (orgId == null)
            throw new NullPointerException("orgId object is null");

        OrganizationEntity org = orgDao.findById(orgId);
        if (org == null) {
            return false;
        }
        if (org.getParentId() == null) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#containsChildren(java.lang.String)
      */
    public boolean containsChildren(String orgId) {
        if (orgId == null)
            throw new NullPointerException("orgId object is null");

        List<OrganizationEntity> orgList = orgDao.findChildOrganization(orgId);
        if (orgList != null && !orgList.isEmpty()) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getOrganization(java.lang.String)
      */
    public Organization getOrganization(String orgId) {
        if (orgId == null)
            throw new NullPointerException("orgId object is null");

        System.out.println("In Organization: orgDao=" + orgDao);

        OrganizationEntity org = orgDao.findById(orgId);
        if (org != null) {
            Hibernate.initialize(org.getAttributes());
        } else {
            return null;
        }
        return organizationDozerConverter.convertToDTO(org, true);

    }

    /* -------- Methods for Attributes ---------- */

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#addAttribute(org.openiam.idm.srvc.org.dto.OrganizationAttribute)
      */
    public void addAttribute(OrganizationAttribute attribute) {
        if (attribute == null)
            throw new NullPointerException("Attribute can not be null");
        if (attribute.getAttrId() == null) {
            throw new NullPointerException("Attribute id is null");
        }
        if (StringUtils.isEmpty(attribute.getOrganizationId())) {
            throw new NullPointerException(
                    "OrganizationId has not been associated with this attribute.");
        }
        orgAttrDao.add(organizationAttributeDozerConverter.convertToEntity(attribute, true));
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#updateAttribute(org.openiam.idm.srvc.org.dto.OrganizationAttribute)
      */
    public void updateAttribute(OrganizationAttribute attribute) {
        if (attribute == null)
            throw new NullPointerException("Attribute can not be null");
        if (attribute.getAttrId() == null) {
            throw new NullPointerException("Attribute id is null");
        }
        if (StringUtils.isEmpty(attribute.getOrganizationId())) {
            throw new NullPointerException(
                    "Org has not been associated with this attribute.");
        }
        orgAttrDao.update(organizationAttributeDozerConverter.convertToEntity(attribute, true));

    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getAllAttributes(java.lang.String)
      */
    public HashMap<String, OrganizationAttribute> getAllAttributes(String orgId) {

        if (orgId == null) {
            throw new NullPointerException("orgId is null");
        }

        OrganizationEntity org = this.orgDao.findById(orgId);

        HashMap<String, OrganizationAttributeEntity> attrEntityMap = (HashMap<String, OrganizationAttributeEntity>) org.getAttributes();
        if (attrEntityMap == null || attrEntityMap.isEmpty())
            return null;

        HashMap<String, OrganizationAttribute> attrMap = new HashMap<String, OrganizationAttribute>();
        for (Map.Entry<String, OrganizationAttributeEntity> entityEntry : attrEntityMap.entrySet()) {
            attrMap.put(entityEntry.getKey(), organizationAttributeDozerConverter.convertToDTO(entityEntry.getValue(), true));
        }
        return attrMap;

        //return this.attribMap(orgId);

    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getAttribute(java.lang.String)
      */
    public OrganizationAttribute getAttribute(String attrId) {
        if (attrId == null) {
            throw new NullPointerException("attrId is null");
        }
        OrganizationAttributeEntity attributeEntity = orgAttrDao.findById(attrId);
        //return this.orgAttrib(attrId);
        return organizationAttributeDozerConverter.convertToDTO(attributeEntity, true);
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#removeAttribute(org.openiam.idm.srvc.org.dto.OrganizationAttribute)
      */
    public void removeAttribute(OrganizationAttribute attr) {
        if (attr == null) {
            throw new NullPointerException("attr is null");
        }
        if (attr.getAttrId() == null) {
            throw new NullPointerException("attrId is null");
        }
        orgAttrDao.remove(organizationAttributeDozerConverter.convertToEntity(attr, true));
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#removeAllAttributes(java.lang.String)
      */
    public void removeAllAttributes(String orgId) {
        if (orgId == null) {
            throw new NullPointerException("orgId is null");
        }
        orgAttrDao.removeAttributesByParent(orgId);

    }


    /* User Affiliation */

    /**
     * Adds a user to a org using the UserOrg object.
     */
    public void assocUserToOrg(UserAffiliation userorg) {
        if (userorg.getOrganizationId() == null)
            throw new IllegalArgumentException("organizationId  is null");

        if (userorg.getUserId() == null)
            throw new IllegalArgumentException("userId object is null");

        userorg.setUserAffiliationId(null);

        this.orgAffiliationDao.add(userAffiliationDozerConvertor.convertToEntity(userorg, true));

    }


    public void updateUserOrgAssoc(UserAffiliation userorg) {
        if (userorg.getOrganizationId() == null)
            throw new IllegalArgumentException("organizationId  is null");

        if (userorg.getUserId() == null)
            throw new IllegalArgumentException("userId object is null");

        orgAffiliationDao.update(userAffiliationDozerConvertor.convertToEntity(userorg, true));

    }


    public List<Organization> getOrganizationsForUser(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        List<OrganizationEntity> entities = orgAffiliationDao.findOrgAffiliationsByUser(userId);

        return organizationDozerConverter.convertToDTOList(entities, false);
    }


    public void addUserToOrg(String orgId, String userId) {

        if (orgId == null)
            throw new IllegalArgumentException("organizationId  is null");

        if (userId == null)
            throw new IllegalArgumentException("userId object is null");
        UserEntity user = userDAO.findById(userId);
        OrganizationEntity org = orgDao.findById(orgId);
        UserAffiliationEntity ua = new UserAffiliationEntity(user, org);
        orgAffiliationDao.add(ua);
    }

    public boolean isUserAffilatedWithOrg(String orgId, String userId) {

        if (orgId == null)
            throw new IllegalArgumentException("organizationId  is null");

        if (userId == null)
            throw new IllegalArgumentException("userId object is null");

        List<OrganizationEntity> orgList = orgAffiliationDao.findOrgAffiliationsByUser(userId);

        if (orgList != null) {
            for (OrganizationEntity org : orgList) {
                if (org.getOrgId().equals(orgId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeUserFromOrg(String orgId, String userId) {
        if (orgId == null)
            throw new IllegalArgumentException("organizationId  is null");

        if (userId == null)
            throw new IllegalArgumentException("userId object is null");

        orgAffiliationDao.removeUserFromOrg(orgId, userId);
    }
    /* Spring methods */


    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getOrgAttrDao()
      */
    public OrganizationAttributeDAO getOrgAttrDao() {
        return orgAttrDao;
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#setOrgAttrDao(org.openiam.idm.srvc.org.service.OrganizationAttributeDAO)
      */
    public void setOrgAttrDao(OrganizationAttributeDAO orgAttrDao) {
        this.orgAttrDao = orgAttrDao;
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getOrgDao()
      */
    public OrganizationDAO getOrgDao() {
        return orgDao;
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#setOrgDao(org.openiam.idm.srvc.org.service.OrganizationDAO)
      */
    public void setOrgDao(OrganizationDAO orgDao) {
        this.orgDao = orgDao;
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#search(java.lang.String, java.lang.String)
      */
    public List<Organization> search(String name, String type, String classification, String internalOrgId) {
        List<OrganizationEntity> organizationEntities = orgDao.search(name, type, StringUtils.isNotEmpty(classification) ? OrgClassificationEnum.valueOf(classification) : null, internalOrgId);

        return organizationDozerConverter.convertToDTOList(organizationEntities, false);
    }

    public List<Organization> getAllOrganizations() {
        List<OrganizationEntity> organizationEntities = orgDao.findAllOrganization();

        return organizationDozerConverter.convertToDTOList(organizationEntities, false);
    }

    /* (non-Javadoc)
      * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getOrganizationList(java.lang.String, java.lang.String)
      */
    public List<Organization> getOrganizationList(String parentOrgId,
                                                  String status) {
        List<OrganizationEntity> organizationEntities = orgDao.findOrganizationByStatus(parentOrgId, status);

        return organizationDozerConverter.convertToDTOList(organizationEntities, false);
    }


    public UserAffiliationDAO getOrgAffiliationDao() {
        return orgAffiliationDao;
    }

    public void setOrgAffiliationDao(UserAffiliationDAO orgAffiliationDao) {
        this.orgAffiliationDao = orgAffiliationDao;
    }
}
