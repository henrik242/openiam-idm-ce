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
 * EmailAddress transfer object
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailAddress", propOrder = {
        "isActive",
        "description",
        "emailAddress",
        "emailId",
        "isDefault",
        "parentId",
        "parentType",
        "name",
        "operation"
})
@Entity
@Table(name = "EMAIL_ADDRESS")
public class EmailAddress implements java.io.Serializable {

    // Fields
    @Transient
    protected AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "EMAIL_ID", length = 32, nullable = false)
    private String emailId;

    @Column(name="ACTIVE")
    protected Boolean isActive = Boolean.TRUE;

    @Column(name="DESCRIPTION", length=100)
    protected String description;

    @Column(name="EMAIL_ADDRESS", length=320)
    protected String emailAddress;

    @Column(name="IS_DEFAULT")
    protected Integer isDefault = new Integer(0);

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    private User parent;

    @Column(name="PARENT_TYPE", length=30)
    protected String parentType;

    @Column(name="NAME", length=40)
    protected String name;

    @Transient
    protected String parentId;
    // Constructors

    /**
     * default constructor
     */
    public EmailAddress() {
    }

    /**
     * minimal constructor
     */
    public EmailAddress(String emailId) {
        this.emailId = emailId;
    }

    public EmailAddress(String emailAddress, String name, User parent, String parentType, Integer aDefault) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.parent = parent;
        this.parentType = parentType;
        isDefault = aDefault;
    }

    /**
     * full constructor
     */
    public EmailAddress(String emailId, String description,
                        String emailAddress, Integer isDefault) {
        this.emailId = emailId;
        this.description = description;
        this.emailAddress = emailAddress;
        this.isDefault = isDefault;
    }

    public void updateEmailAddress(EmailAddress emailAdr) {
        this.description = emailAdr.getDescription();
        this.emailAddress = emailAdr.getEmailAddress();
        this.isActive = emailAdr.isActive();
        this.isDefault = emailAdr.getIsDefault();
        this.name = emailAdr.getName();
    }

    public String getParentId() {
        return parent != null ? parent.getUserId() : "";
    }

    // Property accessors
    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parent = parent;
        this.parentId = parent != null ? parent.getUserId() : "";
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

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
                "operation=" + operation +
                ", isActive=" + isActive +
                ", description='" + description + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailId='" + emailId + '\'' +
                ", isDefault=" + isDefault +
                ", parent='" + (parent != null ? parent.getUserId() : "") + '\'' +
                ", parentType='" + parentType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailAddress)) return false;

        EmailAddress that = (EmailAddress) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
        if (emailId != null ? !emailId.equals(that.emailId) : that.emailId != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (operation != that.operation) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (parentType != null ? !parentType.equals(that.parentType) : that.parentType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return emailId != null ? emailId.hashCode() : 0;
    }
}
