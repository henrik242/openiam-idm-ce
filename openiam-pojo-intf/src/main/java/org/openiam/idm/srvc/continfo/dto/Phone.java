package org.openiam.idm.srvc.continfo.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.base.AttributeOperationEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openiam.idm.srvc.continfo.domain.PhoneEntity;
import org.openiam.idm.srvc.user.domain.UserEntity;

// Generated Jun 12, 2007 10:46:13 PM by Hibernate Tools 3.2.0.beta8

/**
 * Phone transfer object
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "phone", propOrder = {
        "isActive",
        "areaCd",
        "countryCd",
        "description",
        "isDefault",
        "parentId",
        "parentType",
        "phoneExt",
        "phoneId",
        "phoneNbr",
        "phoneType",
        "name",
        "operation"
})

public class Phone implements java.io.Serializable {

    // Fields
    protected AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    private String phoneId;

    protected Boolean isActive = Boolean.TRUE;

    protected String areaCd;

    protected String countryCd;

    protected String description;

    protected Integer isDefault = new Integer(0);

    protected String parentType;

    protected String phoneExt;

    protected String phoneNbr;

    protected String name;

    protected String phoneType;

    protected String parentId;
    // Constructors

    /**
     * default constructor
     */
    public Phone() {
    }

    /**
     * minimal constructor
     */
    public Phone(String phoneId) {
        this.phoneId = phoneId;
    }

    public Phone(String name, String phoneNbr, String phoneExt, String areaCd, String parentType, String parentId, String countryCd) {
        this.name = name;
        this.phoneNbr = phoneNbr;
        this.phoneExt = phoneExt;
        this.areaCd = areaCd;
        this.parentType = parentType;
        this.parentId = parentId;
        this.countryCd = countryCd;
    }

    public Phone(PhoneEntity phoneEntity) {
        this.phoneId = phoneEntity.getPhoneId();
        this.isActive = phoneEntity.getActive();
        this.areaCd = phoneEntity.getAreaCd();
        this.countryCd = phoneEntity.getCountryCd();
        this.description = phoneEntity.getDescription();
        this.isDefault = phoneEntity.getDefault();
        this.parentType = phoneEntity.getParentType();
        this.phoneExt = phoneEntity.getPhoneExt();
        this.name = phoneEntity.getName();
        this.phoneType = phoneEntity.getPhoneType();
        this.parentId = phoneEntity.getParent() != null ? phoneEntity.getParent().getUserId() : "";
        this.phoneNbr = phoneEntity.getPhoneNbr();

    }

    /**
     * full constructor
     */
/*    public Phone(String phoneId, String areaCd, String countryCd,
                 String description, String phoneNbr, String phoneExt,
                 Integer isDefault, String addressId) {
        this.phoneId = phoneId;
        this.areaCd = areaCd;
        this.countryCd = countryCd;
        this.description = description;
        this.phoneNbr = phoneNbr;
        this.phoneExt = phoneExt;
        this.isDefault = isDefault;
    }*/

    public void updatePhone(Phone ph) {
        this.areaCd = ph.getAreaCd();
        this.countryCd = ph.getCountryCd();
        this.description = ph.getDescription();
        this.isActive = ph.isActive;
        this.isDefault = ph.getIsDefault();
        this.name = ph.getName();
        this.phoneExt = ph.getPhoneExt();
        this.phoneNbr = ph.getPhoneNbr();
        this.phoneType = ph.getPhoneType();
    }

    // Property accessors
    public String getPhoneId() {
        return this.phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getAreaCd() {
        return this.areaCd;
    }

    public void setAreaCd(String areaCd) {
        this.areaCd = areaCd;
    }

    public String getCountryCd() {
        return this.countryCd;
    }

    public void setCountryCd(String countryCd) {
        this.countryCd = countryCd;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNbr() {
        return this.phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public String getPhoneExt() {
        return this.phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }


    /**
     * Returns the Id of the parent that owns this address. The parent may be another entity like a
     * USER, ORGANIZATION, etc
     *
     * @return
     */
    public String getParentId() {
        return this.parentId;
    }

    /**
     * Returns the type of the parent.
     *
     * @return
     */
    public String getParentType() {
        return parentType;
    }

    /**
     * Sets the type of the parent.  While the parent type can be anything you choose, a few
     * constants are defined in the ContactConstants clss.
     *
     * @param parentType
     */
    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    /**
     * Indicates if the address is currently active if the value is
     * true and inactive if the value false.
     *
     * @return
     */
    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    /**
     * Associates the address with a parent entity, such as USER or ORGANIZATION that owns this address.
     *
     * @return
     */


    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        if (areaCd != null ? !areaCd.equals(phone.areaCd) : phone.areaCd != null) return false;
        if (countryCd != null ? !countryCd.equals(phone.countryCd) : phone.countryCd != null) return false;
        if (description != null ? !description.equals(phone.description) : phone.description != null) return false;
        if (isActive != null ? !isActive.equals(phone.isActive) : phone.isActive != null) return false;
        if (isDefault != null ? !isDefault.equals(phone.isDefault) : phone.isDefault != null) return false;
        if (name != null ? !name.equals(phone.name) : phone.name != null) return false;
        if (operation != phone.operation) return false;
        if (parentId != null ? !parentId.equals(phone.parentId) : phone.parentId != null) return false;
        if (parentType != null ? !parentType.equals(phone.parentType) : phone.parentType != null) return false;
        if (phoneExt != null ? !phoneExt.equals(phone.phoneExt) : phone.phoneExt != null) return false;
        if (phoneId != null ? !phoneId.equals(phone.phoneId) : phone.phoneId != null) return false;
        if (phoneNbr != null ? !phoneNbr.equals(phone.phoneNbr) : phone.phoneNbr != null) return false;
        if (phoneType != null ? !phoneType.equals(phone.phoneType) : phone.phoneType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (phoneId != null ? phoneId.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (areaCd != null ? areaCd.hashCode() : 0);
        result = 31 * result + (countryCd != null ? countryCd.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        result = 31 * result + (parentType != null ? parentType.hashCode() : 0);
        result = 31 * result + (phoneExt != null ? phoneExt.hashCode() : 0);
        result = 31 * result + (phoneNbr != null ? phoneNbr.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "operation=" + operation +
                ", isActive=" + isActive +
                ", areaCd='" + areaCd + '\'' +
                ", countryCd='" + countryCd + '\'' +
                ", description='" + description + '\'' +
                ", isDefault=" + isDefault + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentType='" + parentType + '\'' +
                ", phoneExt='" + phoneExt + '\'' +
                ", phoneId='" + phoneId + '\'' +
                ", phoneNbr='" + phoneNbr + '\'' +
                ", name='" + name + '\'' +
                ", phoneType='" + phoneType + '\'' +
                '}';
    }
}
