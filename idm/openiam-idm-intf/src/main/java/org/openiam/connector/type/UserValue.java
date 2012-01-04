package org.openiam.connector.type;

import org.openiam.provision.type.ExtensibleAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserValue", propOrder = {
    "userIdentity",
    "attributeList"
})
public class UserValue extends ResponseType
{

    protected String userIdentity;
    List<ExtensibleAttribute> attributeList;

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

    public List<ExtensibleAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<ExtensibleAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}
