package org.openiam.idm.srvc.authz.dto;
// Generated Feb 18, 2008 3:56:06 PM by Hibernate Tools 3.2.0.b11



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;

/**
 * Login domain object
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthzRequest", propOrder = {
    "principalName",
    "domain",
    "resourceName",
    "attributeList",
    "action",
    "requestTime",
    "sessionId",
    "clientIp",
    "requestParameters"
})

public class AuthzRequest implements java.io.Serializable,  Cloneable{

    protected String principalName;
    protected String domain;
    String resourceName;
    List<AuthAttribute> attributeList;
    String action;
     @XmlSchemaType(name = "dateTime")
    protected Date requestTime;
    protected String sessionId;
    protected String clientIp;
    List<AuthAttribute> requestParameters;


    public AuthzRequest() {
        requestTime = new Date(System.currentTimeMillis());
    }


    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }



    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }



    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public List<AuthAttribute> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(List<AuthAttribute> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<AuthAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AuthAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}

