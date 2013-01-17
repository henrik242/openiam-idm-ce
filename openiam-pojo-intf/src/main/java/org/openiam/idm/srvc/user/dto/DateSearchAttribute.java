package org.openiam.idm.srvc.user.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateSearchAttribute", propOrder = {
        "attributeName",
        "operation",
        "attributeValue"
})
public class DateSearchAttribute {
    String attributeName;
    @XmlSchemaType(name = "dateTime")
    Date attributeValue;
    String operation;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Date getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(Date attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    public String toString() {
        return "SearchAttribute{" +
                "attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
