package org.openiam.selfsrvc.reg;

import org.openiam.idm.srvc.cd.dto.ReferenceData;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.loc.dto.Location;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.user.dto.User;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RegistrationCommand implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3001967685870249543L;
    private String firstName;
    private String middleInit;
    private String lastName;
    private String maidenName;
    private String nickname;
    private Date birthdate;
    private String companyId;
    private String title;
    private String sex;
    private String roleId;
    private String email1;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCd;
    private String workAreaCode;
    private String workPhone;
    private String cellPhone;
    private String cellAreaCode;
    private String faxAreaCode;
    private String faxPhone;
    private String altCellAreaCode;
    private String altCellNbr;
    private String homePhoneAreaCode;
    private String homePhoneNbr;

    public RegistrationCommand() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInit() {
        return middleInit;
    }

    public void setMiddleInit(String middleInit) {
        this.middleInit = middleInit;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
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

    public String getAltCellAreaCode() {
        return altCellAreaCode;
    }

    public void setAltCellAreaCode(String altCellAreaCode) {
        this.altCellAreaCode = altCellAreaCode;
    }

    public String getHomePhoneAreaCode() {
        return homePhoneAreaCode;
    }

    public void setHomePhoneAreaCode(String homePhoneAreaCode) {
        this.homePhoneAreaCode = homePhoneAreaCode;
    }

    public String getHomePhoneNbr() {
        return homePhoneNbr;
    }

    public void setHomePhoneNbr(String homePhoneNbr) {
        this.homePhoneNbr = homePhoneNbr;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAltCellNbr() {

        return altCellNbr;
    }

    public void setAltCellNbr(String altCellNbr) {
        this.altCellNbr = altCellNbr;
    }
}