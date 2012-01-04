
package org.openiam.connector.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserResponse", propOrder = {
    "user"
})
public class UserResponse   extends ResponseType
{
    protected UserValue user;

    public UserValue getUser() {
        return user;
    }

    public void setUser(UserValue user) {
        this.user = user;
    }
}
