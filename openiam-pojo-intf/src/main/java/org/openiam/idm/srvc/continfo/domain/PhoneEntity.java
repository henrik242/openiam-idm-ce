package org.openiam.idm.srvc.continfo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.user.domain.UserEntity;

@Entity
@Table(name = "PHONE")
public class PhoneEntity {
     @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "PHONE_ID", length = 32, nullable = false)
    private String phoneId;

    @Column(name="ACTIVE")
    private Boolean isActive = Boolean.TRUE;

    @Column(name="AREA_CD", length=10)
    private String areaCd;

    @Column(name="COUNTRY_CD", length=3)
    private String countryCd;

    @Column(name="DESCRIPTION", length=100)
    private String description;

    @Column(name="IS_DEFAULT")
    private Integer isDefault = new Integer(0);

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    private UserEntity parent;

    @Column(name="PARENT_TYPE", length=30)
    private String parentType;

    @Column(name="PHONE_EXT", length=20)
    private String phoneExt;

    @Column(name="PHONE_NBR", length=50)
    private String phoneNbr;

    @Column(name="NAME", length=40)
    private String name;

    @Column(name="PHONE_TYPE", length=20)
    private String phoneType;

    public PhoneEntity() {
    }

    public PhoneEntity(Phone phone, UserEntity parent) {
      this.phoneId = phone.getPhoneId();
      this.isActive = phone.isActive();
      this.areaCd = phone.getAreaCd();
      this.countryCd = phone.getCountryCd();
      this.description = phone.getDescription();
      this.isDefault = phone.getIsDefault();
      this.parent = parent;
      this.parentType = phone.getParentType();
      this.phoneExt = phone.getPhoneExt();
      this.phoneNbr = phone.getPhoneNbr();
      this.name = phone.getName();
      this.phoneType = phone.getPhoneType();
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getAreaCd() {
        return areaCd;
    }

    public void setAreaCd(String areaCd) {
        this.areaCd = areaCd;
    }

    public String getCountryCd() {
        return countryCd;
    }

    public void setCountryCd(String countryCd) {
        this.countryCd = countryCd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDefault() {
        return isDefault;
    }

    public void setDefault(Integer aDefault) {
        isDefault = aDefault;
    }

    public UserEntity getParent() {
        return parent;
    }

    public void setParent(UserEntity parent) {
        this.parent = parent;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneEntity that = (PhoneEntity) o;

        if (areaCd != null ? !areaCd.equals(that.areaCd) : that.areaCd != null) return false;
        if (countryCd != null ? !countryCd.equals(that.countryCd) : that.countryCd != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (parentType != null ? !parentType.equals(that.parentType) : that.parentType != null) return false;
        if (phoneExt != null ? !phoneExt.equals(that.phoneExt) : that.phoneExt != null) return false;
        if (phoneId != null ? !phoneId.equals(that.phoneId) : that.phoneId != null) return false;
        if (phoneNbr != null ? !phoneNbr.equals(that.phoneNbr) : that.phoneNbr != null) return false;
        if (phoneType != null ? !phoneType.equals(that.phoneType) : that.phoneType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = phoneId != null ? phoneId.hashCode() : 0;
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (areaCd != null ? areaCd.hashCode() : 0);
        result = 31 * result + (countryCd != null ? countryCd.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (parentType != null ? parentType.hashCode() : 0);
        result = 31 * result + (phoneExt != null ? phoneExt.hashCode() : 0);
        result = 31 * result + (phoneNbr != null ? phoneNbr.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0);
        return result;
    }
}
