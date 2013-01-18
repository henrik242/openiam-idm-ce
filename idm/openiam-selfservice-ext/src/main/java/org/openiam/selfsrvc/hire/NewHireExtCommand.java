package org.openiam.selfsrvc.hire;

import java.util.Date;

public class NewHireExtCommand {
    private String submitAction;

    private String metadataTypeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String companyId;
    private String title;
    private String supervisorId;
    private String supervisorName;
    private String roleId;
    private String deptCd;
    private String jobCode;
    private String employeeType;
    private Date lastDate;
    private String groupId;
    private String locationCd;
    private String bldgNum;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCd;
    private String email1;
    private String workAreaCode;
    private String workPhone;
    private String cellAreaCode;
    private String cellPhone;
    private String faxAreaCode;
    private String faxPhone;
    private Date startDate;

    private String[] metaAttrId =  new String[]{};
    private String[] metaAttrValue =  new String[]{};
    private String[] metaAttrName = new String[]{};
    private String[] metaAttrRequired = new String[]{};

    public NewHireExtCommand() {
    }

    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDeptCd() {
        return deptCd;
    }

    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getMetaAttrId() {
        return metaAttrId;
    }

    public void setMetaAttrId(String[] metaAttrId) {
        this.metaAttrId = metaAttrId;
    }

    public String[] getMetaAttrValue() {
        return metaAttrValue;
    }

    public void setMetaAttrValue(String[] metaAttrValue) {
        this.metaAttrValue = metaAttrValue;
    }

    public String[] getMetaAttrName() {
        return metaAttrName;
    }

    public void setMetaAttrName(String[] metaAttrName) {
        this.metaAttrName = metaAttrName;
    }

    public String[] getMetaAttrRequired() {
        return metaAttrRequired;
    }

    public void setMetaAttrRequired(String[] metaAttrRequired) {
        this.metaAttrRequired = metaAttrRequired;
    }

    public String getLocationCd() {
        return locationCd;
    }

    public void setLocationCd(String locationCd) {
        this.locationCd = locationCd;
    }

    public String getBldgNum() {
        return bldgNum;
    }

    public void setBldgNum(String bldgNum) {
        this.bldgNum = bldgNum;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCd() {
        return postalCd;
    }

    public void setPostalCd(String postalCd) {
        this.postalCd = postalCd;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getWorkAreaCode() {
        return workAreaCode;
    }

    public void setWorkAreaCode(String workAreaCode) {
        this.workAreaCode = workAreaCode;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getCellAreaCode() {
        return cellAreaCode;
    }

    public void setCellAreaCode(String cellAreaCode) {
        this.cellAreaCode = cellAreaCode;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getFaxAreaCode() {
        return faxAreaCode;
    }

    public void setFaxAreaCode(String faxAreaCode) {
        this.faxAreaCode = faxAreaCode;
    }

    public String getFaxPhone() {
        return faxPhone;
    }

    public void setFaxPhone(String faxPhone) {
        this.faxPhone = faxPhone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSubmitAction() {
        return submitAction;
    }

    public void setSubmitAction(String submitAction) {
        this.submitAction = submitAction;
    }
}
