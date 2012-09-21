package org.openiam.base;

import org.openiam.util.StringUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;

/**
 * Base object for all attributes
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseAttribute", propOrder = {
        "attributeId",
        "selected",
        "name",
        "value",
        "parentId",
        "operationEnum"
})
public class BaseAttribute implements Serializable{

    protected String attributeId;
    protected String name;
    protected String value;
    protected String parentId;
    protected AttributeOperationEnum operationEnum;

    protected Boolean selected = false;

    public BaseAttribute() {
    }

    public BaseAttribute(String name, String value) {
        this(name, value, AttributeOperationEnum.ADD);
    }

    public BaseAttribute(String name, String value, AttributeOperationEnum operation) {
        this.name = name;
        setValue(value);
        this.operationEnum =  operation;
    }

    public BaseAttribute(String name, String value, String parentId, Boolean selected) {
        this.name = name;
        setValue(value);
        this.parentId = parentId;
        this.selected = selected;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return StringUtil.fromBase64(value);
    }

    public void setValue(String value) {
        // Values are base64 encoded internally to keep Mule happy.  Mule would otherwise throw exceptions when
        // values that are not really strings (e.g. binary values from Active Directory) are set here.
        this.value = StringUtil.toBase64(value);
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public AttributeOperationEnum getOperationEnum() {
        return operationEnum;
    }

    public void setOperationEnum(AttributeOperationEnum operationEnum) {
        this.operationEnum = operationEnum;
    }

    @Override
    public String toString() {
        return "BaseAttribute{" +
                "attributeId='" + attributeId + '\'' +
                ", name='" + name + '\'' +
                ", value='" + getValue() + '\'' +
                ", parentId='" + parentId + '\'' +
                ", operationEnum=" + operationEnum +
                ", selected=" + selected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseAttribute)) return false;

        BaseAttribute that = (BaseAttribute) o;

        if (attributeId != null ? !attributeId.equals(that.attributeId) : that.attributeId != null) return false;
        if (!name.equals(that.name)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attributeId != null ? attributeId.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
