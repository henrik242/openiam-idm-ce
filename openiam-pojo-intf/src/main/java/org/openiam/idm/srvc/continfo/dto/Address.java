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
import org.openiam.idm.srvc.user.dto.User;

// Generated Jun 12, 2007 10:46:13 PM by Hibernate Tools 3.2.0.beta8

/**
 * Address transfer object.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "address", propOrder = {
        "isActive",
        "bldgNumber",
        "streetDirection",
        "suite",
        "address1",
        "address2",
        "address3",
        "address4",
        "address5",
        "address6",
        "address7",
        "addressId",
        "city",
        "country",
        "description",
        "isDefault",
        "parent",
        "parentType",
        "postalCd",
        "state",
        "name",
        "operation"
})
@Entity
@Table(name = "ADDRESS")
public class Address implements java.io.Serializable {


    // Fields
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ADDRESS_ID", length = 32, nullable = false)
    private String addressId;

    @Transient
    protected AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    @Column(name="ACTIVE")
    protected Boolean isActive = Boolean.TRUE;

    @Column(name="BLDG_NUM", length=45)
    protected String bldgNumber;

    @Column(name="STREET_DIRECTION", length=20)
    protected String streetDirection;

    @Column(name="SUITE", length=20)
    protected String suite;

    @Column(name="ADDRESS1", length=45)
    protected String address1;

    @Column(name="ADDRESS2", length=45)
    protected String address2;

    @Column(name="ADDRESS3", length=45)
    protected String address3;

    @Column(name="ADDRESS4", length=45)
    protected String address4;

    @Column(name="ADDRESS5", length=45)
    protected String address5;

    @Column(name="ADDRESS6", length=45)
    protected String address6;

    @Column(name="ADDRESS7", length=45)
    protected String address7;

    @Column(name="CITY", length=45)
    protected String city;

    @Column(name="COUNTRY", length=30)
    protected String country;

    @Column(name="DESCRIPTION", length=100)
    protected String description;

    @Transient
    protected Integer isDefault = new Integer(0);

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    private User parent;

    @Column(name="PARENT_TYPE", length=30)
    protected String parentType;

    @Column(name="POSTAL_CD", length=10)
    protected String postalCd;

    @Column(name="STATE", length=15)
    protected String state;

    @Column(name="NAME", length=40)
    protected String name;

    @Transient
    protected String parentId;
    // Constructors


    /**
     * default constructor
     */
    public Address() {
    }

    /**
     * minimal constructor
     */
    public Address(String addressId) {
        this.addressId = addressId;
    }

    /**
     * full constructor
     */
    public Address(String addressId, String country, String address1,
                   String address2,
                   String address3, String address4, String address5, String address6, String address7,
                   String city, String state, String postalCd,
                   Integer isDefault, String description) {
        this.addressId = addressId;
        this.country = country;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.address6 = address6;
        this.address7 = address7;
        this.city = city;
        this.state = state;
        this.postalCd = postalCd;
        this.isDefault = isDefault;
        this.description = description;
    }


    @Override
    public String toString() {
        return "Address{" +
                "operation=" + operation +
                ", isActive=" + isActive +
                ", bldgNumber='" + bldgNumber + '\'' +
                ", streetDirection='" + streetDirection + '\'' +
                ", suite='" + suite + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", address4='" + address4 + '\'' +
                ", address5='" + address5 + '\'' +
                ", address6='" + address6 + '\'' +
                ", address7='" + address7 + '\'' +
                ", addressId='" + addressId + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", isDefault=" + isDefault +
                ", parentId='" + (parent != null ? parent.getUserId() : "") + '\'' +
                ", parentType='" + parentType + '\'' +
                ", postalCd='" + postalCd + '\'' +
                ", state='" + state + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void updateAddress(Address adr) {
        this.address1 = adr.getAddress1();
        this.address2 = adr.getAddress2();
        this.address3 = adr.getAddress3();
        this.address4 = adr.getAddress4();
        this.address5 = adr.getAddress5();
        this.address6 = adr.getAddress6();
        this.address7 = adr.getAddress7();
        this.bldgNumber = this.getBldgNumber();
        this.city = adr.getCity();
        this.country = adr.getCountry();
        this.description = adr.getDescription();
        this.isActive = adr.isActive();
        this.isDefault = adr.getIsDefault();
        this.name = adr.getName();
        this.postalCd = adr.getPostalCd();
        this.state = adr.getState();
        this.streetDirection = adr.getStreetDirection();
        this.suite = adr.getSuite();
    }

    // Property accessors
    public String getAddressId() {
        return this.addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCd() {
        return this.postalCd;
    }

    public void setPostalCd(String postalCd) {
        this.postalCd = postalCd;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the Id of the parent that owns this address. The parent may be another entity like a
     * USER, ORGANIZATION, etc
     *
     * @return
     */
    public String getParentId() {
        return parent != null ? parent.getUserId() : "";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    /**
     * Associates the address with a parent entity, such as USER or ORGANIZATION that owns this address.
     *
     * @return
     */
    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parentId = parent != null ? parent.getUserId() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (address1 != null ? !address1.equals(address.address1) : address.address1 != null) return false;
        if (address2 != null ? !address2.equals(address.address2) : address.address2 != null) return false;
        if (address3 != null ? !address3.equals(address.address3) : address.address3 != null) return false;
        if (address4 != null ? !address4.equals(address.address4) : address.address4 != null) return false;
        if (address5 != null ? !address5.equals(address.address5) : address.address5 != null) return false;
        if (address6 != null ? !address6.equals(address.address6) : address.address6 != null) return false;
        if (address7 != null ? !address7.equals(address.address7) : address.address7 != null) return false;
        if (addressId != null ? !addressId.equals(address.addressId) : address.addressId != null) return false;
        if (bldgNumber != null ? !bldgNumber.equals(address.bldgNumber) : address.bldgNumber != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (description != null ? !description.equals(address.description) : address.description != null) return false;
        if (isActive != null ? !isActive.equals(address.isActive) : address.isActive != null) return false;
        if (isDefault != null ? !isDefault.equals(address.isDefault) : address.isDefault != null) return false;
        if (name != null ? !name.equals(address.name) : address.name != null) return false;
        if (operation != address.operation) return false;
        if (parent != null ? !parent.equals(address.parent) : address.parent != null) return false;
        if (parentType != null ? !parentType.equals(address.parentType) : address.parentType != null) return false;
        if (postalCd != null ? !postalCd.equals(address.postalCd) : address.postalCd != null) return false;
        if (state != null ? !state.equals(address.state) : address.state != null) return false;
        if (streetDirection != null ? !streetDirection.equals(address.streetDirection) : address.streetDirection != null)
            return false;
        if (suite != null ? !suite.equals(address.suite) : address.suite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return addressId != null ? addressId.hashCode() : 0;
    }
}
