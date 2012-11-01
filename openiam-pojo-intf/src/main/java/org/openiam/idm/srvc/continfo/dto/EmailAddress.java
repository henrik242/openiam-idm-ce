package org.openiam.idm.srvc.continfo.dto;

import org.openiam.base.AttributeOperationEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;

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

public class EmailAddress implements java.io.Serializable {

    // Fields
    protected AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    private String emailId;

    protected Boolean isActive = Boolean.TRUE;

    protected String description;

    protected String emailAddress;

    protected Integer isDefault = new Integer(0);

    protected String parentType;

    protected String name;

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

    public EmailAddress(EmailAddressEntity emailAddressEntity) {
        this.emailId = emailAddressEntity.getEmailId();
        this.isActive = emailAddressEntity.getActive();
        this.description = emailAddressEntity.getDescription();
        this.emailAddress = emailAddressEntity.getEmailAddress();
        this.isDefault = emailAddressEntity.getDefault();
        this.parentType = emailAddressEntity.getParentType();
        this.name = emailAddressEntity.getName();
        this.parentId = emailAddressEntity.getParent() != null ? emailAddressEntity.getParent().getUserId() : "";
    }

    public EmailAddress(String emailAddress, String name, String parentId, String parentType, Integer aDefault) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.parentId = parentId;
        this.parentType = parentType;
        this.isDefault = aDefault;
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
        return parentId;
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

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
                ", parentId='" + parentId +
                ", parentType='" + parentType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailAddress that = (EmailAddress) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
        if (emailId != null ? !emailId.equals(that.emailId) : that.emailId != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (operation != that.operation) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (parentType != null ? !parentType.equals(that.parentType) : that.parentType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        result = 31 * result + (parentType != null ? parentType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }
}
