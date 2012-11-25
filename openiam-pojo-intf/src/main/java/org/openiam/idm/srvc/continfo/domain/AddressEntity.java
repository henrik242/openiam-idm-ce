package org.openiam.idm.srvc.continfo.domain;

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
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.user.domain.UserEntity;

@Entity
@Table(name = "ADDRESS")
@DozerDTOCorrespondence(Address.class)
public class AddressEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ADDRESS_ID", length = 32, nullable = false)
    private String addressId;

    @Column(name = "ACTIVE")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "BLDG_NUM", length = 45)
    private String bldgNumber;

    @Column(name = "STREET_DIRECTION", length = 20)
    private String streetDirection;

    @Column(name = "SUITE", length = 20)
    private String suite;

    @Column(name = "ADDRESS1", length = 45)
    private String address1;

    @Column(name = "ADDRESS2", length = 45)
    private String address2;

    @Column(name = "ADDRESS3", length = 45)
    private String address3;

    @Column(name = "ADDRESS4", length = 45)
    private String address4;

    @Column(name = "ADDRESS5", length = 45)
    private String address5;

    @Column(name = "ADDRESS6", length = 45)
    private String address6;

    @Column(name = "ADDRESS7", length = 45)
    private String address7;

    @Column(name = "CITY", length = 45)
    private String city;

    @Column(name = "COUNTRY", length = 30)
    private String country;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private UserEntity parent;

    @Column(name = "PARENT_TYPE", length = 30)
    private String parentType;

    @Column(name = "POSTAL_CD", length = 10)
    private String postalCd;

    @Column(name = "STATE", length = 15)
    private String state;

    @Column(name = "NAME", length = 40)
    private String name;

    @Transient
    private Integer isDefault = 0;

    @Transient
    private AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    public AddressEntity() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getBldgNumber() {
        return bldgNumber;
    }

    public void setBldgNumber(String bldgNumber) {
        this.bldgNumber = bldgNumber;
    }

    public String getStreetDirection() {
        return streetDirection;
    }

    public void setStreetDirection(String streetDirection) {
        this.streetDirection = streetDirection;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
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

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getAddress6() {
        return address6;
    }

    public void setAddress6(String address6) {
        this.address6 = address6;
    }

    public String getAddress7() {
        return address7;
    }

    public void setAddress7(String address7) {
        this.address7 = address7;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPostalCd() {
        return postalCd;
    }

    public void setPostalCd(String postalCd) {
        this.postalCd = postalCd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefault() {
        return isDefault;
    }

    public void setDefault(Integer aDefault) {
        isDefault = aDefault;
    }

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressEntity that = (AddressEntity) o;

        if (address1 != null ? !address1.equals(that.address1) : that.address1 != null) return false;
        if (address2 != null ? !address2.equals(that.address2) : that.address2 != null) return false;
        if (address3 != null ? !address3.equals(that.address3) : that.address3 != null) return false;
        if (address4 != null ? !address4.equals(that.address4) : that.address4 != null) return false;
        if (address5 != null ? !address5.equals(that.address5) : that.address5 != null) return false;
        if (address6 != null ? !address6.equals(that.address6) : that.address6 != null) return false;
        if (address7 != null ? !address7.equals(that.address7) : that.address7 != null) return false;
        if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null) return false;
        if (bldgNumber != null ? !bldgNumber.equals(that.bldgNumber) : that.bldgNumber != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (parentType != null ? !parentType.equals(that.parentType) : that.parentType != null) return false;
        if (postalCd != null ? !postalCd.equals(that.postalCd) : that.postalCd != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (streetDirection != null ? !streetDirection.equals(that.streetDirection) : that.streetDirection != null)
            return false;
        if (suite != null ? !suite.equals(that.suite) : that.suite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = addressId != null ? addressId.hashCode() : 0;
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (bldgNumber != null ? bldgNumber.hashCode() : 0);
        result = 31 * result + (streetDirection != null ? streetDirection.hashCode() : 0);
        result = 31 * result + (suite != null ? suite.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (address3 != null ? address3.hashCode() : 0);
        result = 31 * result + (address4 != null ? address4.hashCode() : 0);
        result = 31 * result + (address5 != null ? address5.hashCode() : 0);
        result = 31 * result + (address6 != null ? address6.hashCode() : 0);
        result = 31 * result + (address7 != null ? address7.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (parentType != null ? parentType.hashCode() : 0);
        result = 31 * result + (postalCd != null ? postalCd.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
