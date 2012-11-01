package org.openiam.idm.srvc.org.service;

import javax.jws.*;

//import diamelle.common.continfo.*;
//import diamelle.base.prop.*;

import java.util.*;
import java.rmi.*;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;
import org.openiam.idm.srvc.org.domain.OrganizationEntity;
import org.openiam.idm.srvc.org.dto.*;

/**
 * <code>OrganizationManager</code> provides a service level interface to the
 * Organization components and its dependant objects as well as search
 * capability.<br>
 * 
 * Note: The spring configuration file defines MetadataTypes are used to identify Departments and Divisions in the org list.
 * 
 * @author OpenIAm
 * @version 2
 */

@WebService(endpointInterface="org.openiam.idm.srvc.org.service.OrganizationDataService",
		targetNamespace="urn:idm.openiam.org/srvc/org/service",
		portName = "OrganizationDataWebServicePort",
		serviceName="OrganizationDataWebService")
public class OrganizationDataServiceImpl implements OrganizationDataService {

	protected OrganizationDAO orgDao;

	protected OrganizationAttributeDAO orgAttrDao;
    protected UserAffiliationDAO orgAffiliationDao;
	
	
	


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
        List<Organization> organizationList = new LinkedList<Organization>();
        for(OrganizationEntity entity : organizationEntityList) {
           organizationList.add(new Organization(entity));
        }
		//return testOrgList(orgId);
        return organizationList;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getTopLevelOrganizations()
	 */
	public List<Organization> getTopLevelOrganizations() {
		List<OrganizationEntity> orgEntityList = orgDao.findRootOrganizations();
		List<Organization> orgList = null;
        if(orgEntityList != null) {
            orgList = new LinkedList<Organization>();
		// initialize the collections
		    for ( OrganizationEntity org :orgEntityList) {
			    Hibernate.initialize(org.getAttributes());
                orgList.add(new Organization(org));
		    }
        }
		return orgList;
		//return testOrgList("myTopLevelOrg");
	}
	
	/**
	 * Returns a list of all organizations based on a metadataType. The parentId parameter can be used to get 
	 * values that are nested further in the hierarchy. If parentId is null, the method will search only on the typeId and parentId 
	 * will be ignored.
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
	   List<Organization> orgList = new LinkedList<Organization>();
       for ( OrganizationEntity org :orgEntityList) {
			Hibernate.initialize(org.getAttributes());
            orgList.add(new Organization(org));
		}
	   return orgList;
   }

   public List<Organization> getOrganizationByClassification(String parentId, OrgClassificationEnum classification) {
		if (classification == null)
			throw new NullPointerException("classification is null");
		
	   List<OrganizationEntity> orgEntityList = orgDao.findOrganizationByClassification(parentId, classification);
	   if (orgEntityList == null) {
		   return null;
	   }
	   List<Organization> orgList = new LinkedList<Organization>();
       for ( OrganizationEntity org :orgEntityList) {
			Hibernate.initialize(org.getAttributes());
            orgList.add(new Organization(org));
	   }
	   return orgList;
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
        OrganizationEntity entity = orgDao.add(new OrganizationEntity(org));
		return entity != null ? new Organization(entity) : null;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.org.service.OrganizationDataService#updateOrganization(org.openiam.idm.srvc.org.dto.Organization)
	 */
	public void updateOrganization(Organization org) {
		if (org == null)
			throw new NullPointerException("org object is null");
		if (StringUtils.isEmpty(org.getOrgId()))
			throw new NullPointerException("org id is null");

        orgDao.update(new OrganizationEntity(org));
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.org.service.OrganizationDataService#removeOrganization(java.lang.String)
	 */
	public void removeOrganization(String orgId) {
		if (orgId == null)
			throw new NullPointerException("orgId is null");

		Organization instance = new Organization();
		instance.setOrgId(orgId);  // dont need if new Organization(orgId) constructor available
		orgDao.remove(new OrganizationEntity(instance));

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
		
		System.out.println("In Organization: orgDao=" + orgDao );
		
		OrganizationEntity org = orgDao.findById(orgId);
		if (org != null) {
			Hibernate.initialize( org.getAttributes() ); 
		}else {
			return null;
		}
		return new Organization(org);
		
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
        OrganizationEntity organization = orgDao.findById(attribute.getOrganizationId());
		orgAttrDao.add(new OrganizationAttributeEntity(attribute,organization));
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
        OrganizationEntity organization = orgDao.findById(attribute.getOrganizationId());
		orgAttrDao.update(new OrganizationAttributeEntity(attribute,organization));

	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getAllAttributes(java.lang.String)
	 */
	public HashMap<String, OrganizationAttribute> getAllAttributes(String orgId) {

		if (orgId == null) {
			throw new NullPointerException("orgId is null");
		}
		
		OrganizationEntity org = this.orgDao.findById(orgId);
		
		HashMap<String, OrganizationAttributeEntity> attrEntityMap = (HashMap<String, OrganizationAttributeEntity>)org.getAttributes();
		if (attrEntityMap != null && attrEntityMap.isEmpty())
			return null;

        HashMap<String, OrganizationAttribute> attrMap = new HashMap<String, OrganizationAttribute>();
        for(Map.Entry<String, OrganizationAttributeEntity> entityEntry : attrEntityMap.entrySet()) {
           attrMap.put(entityEntry.getKey(), new OrganizationAttribute(entityEntry.getValue()));
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
        return attributeEntity != null ? new OrganizationAttribute(attributeEntity) : null;
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
        OrganizationEntity organization = orgDao.findById(attr.getOrganizationId());
		orgAttrDao.remove(new OrganizationAttributeEntity(attr,organization));
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
        this.orgAffiliationDao.add(userorg);

	}


	public void updateUserOrgAssoc(UserAffiliation userorg) {
		if (userorg.getOrganizationId() == null)
			throw new IllegalArgumentException("organizationId  is null");

		if (userorg.getUserId() == null)
			throw new IllegalArgumentException("userId object is null");

        orgAffiliationDao.update(userorg);

	}


	public List<Organization> getOrganizationsForUser(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId is null");
		}

        return orgAffiliationDao.findOrgAffiliationsByUser(userId);

	}


	public void addUserToOrg(String orgId, String userId) {

		if (orgId == null)
			throw new IllegalArgumentException("organizationId  is null");

		if (userId == null)
			throw new IllegalArgumentException("userId object is null");

        UserAffiliation ua = new UserAffiliation(userId,orgId);
        orgAffiliationDao.add(ua);


	}

	public boolean isUserAffilatedWithOrg(String orgId, String userId) {

		if (orgId == null)
			throw new IllegalArgumentException("organizationId  is null");

		if (userId == null)
			throw new IllegalArgumentException("userId object is null");

        List<Organization> orgList = orgAffiliationDao.findOrgAffiliationsByUser(userId);


		for (Organization org : orgList) {
            if (org.getOrgId().equals(orgId)) {
                return true;
            }
		}
		return false;
	}

	public void removeUserFromOrg(String orgId,	String userId) {
				if (orgId == null)
			throw new IllegalArgumentException("organizationId  is null");

		if (userId == null)
			throw new IllegalArgumentException("userId object is null");

        orgAffiliationDao.removeUserFromOrg(orgId,userId);
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
		List<OrganizationEntity> organizationEntities = orgDao.search(name, type, OrgClassificationEnum.valueOf(classification), internalOrgId);
        List<Organization> organizationList = new LinkedList<Organization>();
        for(OrganizationEntity entity : organizationEntities) {
           organizationList.add(new Organization(entity));
        }
        return organizationList;
	}
	
	public List<Organization> getAllOrganizations() {
		List<OrganizationEntity> organizationEntities = orgDao.findAllOrganization();
        List<Organization> organizationList = new LinkedList<Organization>();
        for(OrganizationEntity entity : organizationEntities) {
           organizationList.add(new Organization(entity));
        }
        return organizationList;
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.org.service.OrganizationDataService#getOrganizationList(java.lang.String, java.lang.String)
	 */
	public List<Organization> getOrganizationList(String parentOrgId,
			String status) {
		List<OrganizationEntity> organizationEntities = orgDao.findOrganizationByStatus(parentOrgId, status);
        List<Organization> organizationList = new LinkedList<Organization>();
        for(OrganizationEntity entity : organizationEntities) {
           organizationList.add(new Organization(entity));
        }
        return organizationList;
	}


    public UserAffiliationDAO getOrgAffiliationDao() {
        return orgAffiliationDao;
    }

    public void setOrgAffiliationDao(UserAffiliationDAO orgAffiliationDao) {
        this.orgAffiliationDao = orgAffiliationDao;
    }
}
