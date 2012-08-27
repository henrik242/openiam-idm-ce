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
}
