package org.openiam.connector.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemotePasswordRequest", propOrder = {
        "scriptHandler"
})
public class RemotePasswordRequest extends PasswordRequest {
    @XmlElement
    private String scriptHandler;

    public RemotePasswordRequest() {
    }

    public RemotePasswordRequest(String currentPassword, String password, String userIdentity) {
        super(currentPassword, password, userIdentity);
    }

    public String getScriptHandler() {
        return scriptHandler;
    }

    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }
}
