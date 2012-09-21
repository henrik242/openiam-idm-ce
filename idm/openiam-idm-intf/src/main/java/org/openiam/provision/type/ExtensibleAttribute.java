/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

package org.openiam.provision.type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.BaseAttributeContainer;
import org.openiam.util.StringUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The content carried by the most SPML requests includes an Extensible type to all
 * for flexibility in carrying the data. ExtensibleAttribute provides an
 * attribute model to capture a wide variety of data.
 * 
 * @author Suneet Shah
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtensibleAttribute", propOrder = {
    "name",
    "value",
    "metadataElementId",
    "operation",
    "multivalued",
    "valueList",
    "dataType",
    "objectType",
    "attributeContainer"

})
public class ExtensibleAttribute  implements Serializable {

    private static final long serialVersionUID = 8402148961330001942L;
    protected String name;
    protected String value;
    protected String metadataElementId;
    protected int operation;
    protected boolean multivalued = false;
    protected List<String> valueList;
    protected BaseAttributeContainer attributeContainer;
    protected String dataType;
    protected String objectType;

    protected static final Log log = LogFactory.getLog(ExtensibleAttribute.class);

    
    public ExtensibleAttribute() {
        
    }
    public ExtensibleAttribute(String name, String value) {
        this.name = name;
        setValue(value);
        operation = ModificationAttribute.add;
    }
    public ExtensibleAttribute(String name, String value, String metadataElementId) {
        this.name = name;
        setValue(value);
        this.metadataElementId = metadataElementId;
        operation = ModificationAttribute.add;
    }
    
    public ExtensibleAttribute(String name, String value, int operation, String dataType) {
        super();
        this.name = name;
        this.operation = operation;
        setValue(value);
        this.dataType = dataType;
    }

     public ExtensibleAttribute(String name, List<String> val, int operation, String dataType) {
        super();
        this.name = name;
        this.operation = operation;
        setValueList(val);
        multivalued = true;
        this.dataType = dataType;

    }

    public ExtensibleAttribute(String name, BaseAttributeContainer val, int operation, String dataType) {
        super();
        this.name = name;
        this.operation = operation;

        this.attributeContainer = val;
        this.dataType = dataType;

        log.debug("Extensible attribute created: multivalue");

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
    public int getOperation() {
        return operation;
    }
    public void setOperation(int operation) {
        this.operation = operation;
    }


    public boolean isMultivalued() {
        return multivalued;
    }

    public void setMultivalued(boolean multivalued) {
        this.multivalued = multivalued;
    }

    public List<String> getValueList() {
        if (valueList == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        for (String val : valueList) {
            list.add(StringUtil.fromBase64(val));
        }
        return list;
    }

    public void setValueList(List<String> list) {
        if (list == null) {
            valueList = null;
        } else {
            valueList = new ArrayList<String>();
            for (String val : list) {
                valueList.add(StringUtil.toBase64(val));
            }
        }
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "ExtensibleAttribute{" +
                "name='" + name + '\'' +
                ", value='" + getValue() + '\'' +
                ", metadataElementId='" + metadataElementId + '\'' +
                ", operation=" + operation +
                ", multivalued=" + multivalued +
                ", valueList=" + getValueList() +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    public BaseAttributeContainer getAttributeContainer() {
        return attributeContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtensibleAttribute)) return false;

        ExtensibleAttribute that = (ExtensibleAttribute) o;

        if (multivalued != that.multivalued) return false;
        if (operation != that.operation) return false;
        if (attributeContainer != null ? !attributeContainer.equals(that.attributeContainer) : that.attributeContainer != null)
            return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;
        if (metadataElementId != null ? !metadataElementId.equals(that.metadataElementId) : that.metadataElementId != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (objectType != null ? !objectType.equals(that.objectType) : that.objectType != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (valueList != null ? !valueList.equals(that.valueList) : that.valueList != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (metadataElementId != null ? metadataElementId.hashCode() : 0);
        result = 31 * result + operation;
        result = 31 * result + (multivalued ? 1 : 0);
        result = 31 * result + (valueList != null ? valueList.hashCode() : 0);
        result = 31 * result + (attributeContainer != null ? attributeContainer.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (objectType != null ? objectType.hashCode() : 0);
        return result;
    }

    public void setAttributeContainer(BaseAttributeContainer attributeContainer) {
        this.attributeContainer = attributeContainer;
    }


}
