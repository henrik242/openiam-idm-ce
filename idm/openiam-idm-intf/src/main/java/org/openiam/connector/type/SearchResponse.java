package org.openiam.connector.type;

import org.openiam.provision.type.ExtensibleAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchResponse", propOrder = {
    "userList"
})
public class SearchResponse extends ResponseType
{
    List<UserValue> userList;

    public List<UserValue> getUserList() {
        return userList;
    }

    public void setUserList(List<UserValue> userList) {
        this.userList = userList;
    }
}
