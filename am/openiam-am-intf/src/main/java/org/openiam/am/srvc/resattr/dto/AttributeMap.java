package org.openiam.am.srvc.resattr.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeMap", propOrder = {
        "attributeMapId",
        "resourceId",
        "accessManagerAttributeName",
        "policyUrl"
})
public class AttributeMap extends Attribute {
    private String attributeMapId;
    private String resourceId;
    private String accessManagerAttributeName;
    private String policyUrl;

    // Constructors

    /**
     * default constructor
     */
    public AttributeMap() {
    }

    public String getAttributeMapId() {
        return attributeMapId;
    }

    public void setAttributeMapId(String attributeMapId) {
        this.attributeMapId = attributeMapId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }


    public String getAccessManagerAttributeName() {
        return accessManagerAttributeName;
    }

    public void setAccessManagerAttributeName(String accessManagerAttributeName) {
        this.accessManagerAttributeName = accessManagerAttributeName;
    }

    public String getPolicyUrl() {
        return policyUrl;
    }

    public void setPolicyUrl(String policyUrl) {
        this.policyUrl = policyUrl;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("AttributeMap [");
        sb.append("attributeMapId=").append(attributeMapId);
        sb.append(", resourceId=").append(resourceId);
        sb.append(", accessManagerAttributeName=").append(accessManagerAttributeName);
        sb.append(", policyUrl=").append(policyUrl);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
