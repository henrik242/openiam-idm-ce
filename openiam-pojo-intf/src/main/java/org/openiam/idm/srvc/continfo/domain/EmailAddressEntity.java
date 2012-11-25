package org.openiam.idm.srvc.continfo.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.user.domain.UserEntity;

@Entity
@Table(name = "EMAIL_ADDRESS")
@DozerDTOCorrespondence(EmailAddress.class)
public class EmailAddressEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "EMAIL_ID", length = 32, nullable = false)
    private String emailId;

    @Column(name = "ACTIVE")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "EMAIL_ADDRESS", length = 320)
    private String emailAddress;

    @Column(name = "IS_DEFAULT")
    private Integer isDefault = 0;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private UserEntity parent;

    @Column(name = "PARENT_TYPE", length = 30)
    private String parentType;

    @Column(name = "NAME", length = 40)
    private String name;

    @Transient
    private AttributeOperationEnum operation = AttributeOperationEnum.NO_CHANGE;

    public EmailAddressEntity() {
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailAddressEntity that = (EmailAddressEntity) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
        if (emailId != null ? !emailId.equals(that.emailId) : that.emailId != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (parentType != null ? !parentType.equals(that.parentType) : that.parentType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = emailId != null ? emailId.hashCode() : 0;
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (parentType != null ? parentType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
