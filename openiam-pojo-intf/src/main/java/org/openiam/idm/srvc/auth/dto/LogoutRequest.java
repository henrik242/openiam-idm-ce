package org.openiam.idm.srvc.auth.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Request to Logout a user
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogoutRequest", propOrder = {
        "domainId",
        "principal"
})
public class LogoutRequest {
    String domainId;
    String principal;

    public LogoutRequest() {
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
