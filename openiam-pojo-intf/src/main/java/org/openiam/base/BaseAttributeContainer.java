package org.openiam.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;



/**
 * Base class that contains a collection of Attributes
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseAttributeContainer", propOrder = {
        "attributeList"
})
public class BaseAttributeContainer {

    List<BaseAttribute> attributeList = new ArrayList<BaseAttribute>();

    public List<BaseAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<BaseAttribute> attributeList) {
        this.attributeList = attributeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseAttributeContainer)) return false;

        BaseAttributeContainer that = (BaseAttributeContainer) o;

        if (attributeList != null ? !attributeList.equals(that.attributeList) : that.attributeList != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return attributeList != null ? attributeList.hashCode() : 0;
    }
}
