package org.openiam.webadmin.user;

import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.user.dto.DateSearchAttribute;
import org.openiam.idm.srvc.user.dto.UserSearch;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.idm.srvc.role.dto.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Command object for the UserSearch
 *
 * @author suneet
 */
public class UserSearchCommand implements Serializable {


    private String lastName;
    private String organizationId;
    private String deptId;
    private String division;
    private String attributeName;
    private String attributeValue;
    private UserStatusEnum userStatus;
    private UserStatusEnum secondaryStatus;
    private String email;
    private String principalName;

    /* role is used to select the users for this operation */
    private String role;


    private int resultSetSize;


    private Date lastLoginDate;
    private String dateOperation;

    // Used for reference data
    private List<Organization> orgList;
    private List<Organization> divList;
    private List<Organization> deptList;
    private List<Role> roleList;
    private MetadataElement[] elementList;




    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }


    public boolean isSearchDefined() {

        if (lastName != null && !lastName.isEmpty()) {
            return true;
        }

        if (organizationId != null && !organizationId.isEmpty()) {
            return true;
        }

        if (deptId != null && !deptId.isEmpty()) {
            return true;
        }

        if (deptId != null && !deptId.isEmpty()) {
            return true;
        }

        if (attributeName != null && !attributeName.isEmpty()) {
            return true;
        }


        if (attributeValue != null && !attributeValue.isEmpty()) {
            return true;
        }

        if (role != null && !role.isEmpty()) {
            return true;

        }

        if (lastLoginDate != null) {
            return true;

        }
        if (dateOperation != null && dateOperation.equals("IS NULL")) {
            return true;
        }
        if (userStatus != null) {
            return true;
        }

        if (secondaryStatus != null) {
            return true;
        }

        if (principalName != null && !principalName.isEmpty()) {
            return true;
        }

        if (email != null && !email.isEmpty()) {
            return true;
        }

        return false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public UserSearch buildSearch() {

        UserSearch search = new UserSearch();

        if (lastName != null && !lastName.isEmpty()) {
            search.setLastName(lastName + "%");
        }
        if (organizationId != null && !organizationId.isEmpty()) {
            search.setOrgId(organizationId);
        }
        if (principalName != null && !principalName.isEmpty()) {
            search.setPrincipal(principalName + "%");
        }

        if (email != null && !email.isEmpty()) {
            search.setEmailAddress(email + "%");
        }

        if (deptId != null && !deptId.isEmpty()) {
            search.setDeptCd(deptId);
        }

        if (division != null && !division.isEmpty()) {
            search.setDivision(division);
        }


        if (attributeName != null && !attributeName.isEmpty()) {
            String attrName = attributeName;

            // both attribute name and value need to be provided to do a search on a particular attribute

            if (attributeValue != null && !attributeValue.isEmpty()) {

                search.setAttributeName(attrName);
                search.setAttributeValue(attributeValue + "%");

            }

        }

        if (userStatus != null) {
            search.setStatus(userStatus.toString());
        }

        if (secondaryStatus != null) {
            search.setStatus(secondaryStatus.toString());
        }

        if (lastLoginDate != null) {
            DateSearchAttribute dateSearchAttribute = new DateSearchAttribute();
            dateSearchAttribute.setAttributeName(UserDataService.LAST_LOGIN);
            dateSearchAttribute.setOperation(dateOperation);
            dateSearchAttribute.setAttributeValue(lastLoginDate);
            search.addDateSearchAttribute(dateSearchAttribute);
        }
        if (dateOperation != null && dateOperation.equals("IS NULL")) {
            DateSearchAttribute dateSearchAttribute = new DateSearchAttribute();
            dateSearchAttribute.setAttributeName(UserDataService.LAST_LOGIN);
            dateSearchAttribute.setOperation(dateOperation);
            search.addDateSearchAttribute(dateSearchAttribute);
        }
        // allow selection by a role
        if (role != null && !role.isEmpty()) {

            int indx = role.indexOf("*");
            String roleId = role.substring(indx + 1, role.length());
            String domainId = role.substring(0, indx);

            List<String> roleList = new ArrayList<String>();
            roleList.add(roleId);
            search.setRoleIdList(roleList);
            search.setDomainId(domainId);
        }
        if (resultSetSize != 0) {
            search.setMaxResultSize(resultSetSize);
        }


        return search;

    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }

    public UserStatusEnum getSecondaryStatus() {
        return secondaryStatus;
    }

    public void setSecondaryStatus(UserStatusEnum secondaryStatus) {
        this.secondaryStatus = secondaryStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public int getResultSetSize() {
        return resultSetSize;
    }

    public void setResultSetSize(int resultSetSize) {
        this.resultSetSize = resultSetSize;
    }

    public List<Organization> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<Organization> orgList) {
        this.orgList = orgList;
    }

    public List<Organization> getDivList() {
        return divList;
    }

    public void setDivList(List<Organization> divList) {
        this.divList = divList;
    }

    public List<Organization> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<Organization> deptList) {
        this.deptList = deptList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public MetadataElement[] getElementList() {
        return elementList;
    }

    public void setElementList(MetadataElement[] elementList) {
        this.elementList = elementList;
    }
}




