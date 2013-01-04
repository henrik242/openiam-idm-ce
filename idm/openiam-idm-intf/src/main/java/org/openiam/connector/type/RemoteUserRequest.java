package org.openiam.connector.type;

import org.openiam.provision.type.ExtensibleUser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoteUserRequest", propOrder = {
        "scriptHandler"
})
public class RemoteUserRequest extends UserRequest {
    @XmlElement
    private String scriptHandler;

    public RemoteUserRequest() {
    }

    public RemoteUserRequest(String userIdentity, String containerID, String targetID, String hostUrl, String hostPort, String hostLoginId, String hostLoginPassword, String operation, ExtensibleUser user) {
        super(userIdentity, containerID, targetID, hostUrl, hostPort, hostLoginId, hostLoginPassword, operation, user);
    }

    public String getScriptHandler() {
        return scriptHandler;
    }

    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }
}
