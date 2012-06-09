package org.openiam.provision.dto;

import org.openiam.base.AttributeOperationEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * UserResourceAssociation is used to provision and de-provision a user from resources directly (without using a role)
 * User: suneetshah
 * Date: 6/6/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserResourceAssociation", propOrder = {
        "resourceId",
        "resourceName",
        "managedSystemId",
        "operation"
})
public class UserResourceAssociation {

    private String resourceId;
    private String resourceName;
    private String managedSystemId;

    // default to ADD
    private AttributeOperationEnum operation = AttributeOperationEnum.ADD;



    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getManagedSystemId() {
        return managedSystemId;
    }

    public void setManagedSystemId(String managedSystemId) {
        this.managedSystemId = managedSystemId;
    }

    public AttributeOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(AttributeOperationEnum operation) {
        this.operation = operation;
    }


}
