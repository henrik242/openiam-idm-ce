package org.openiam.am.srvc.webres.dto;

import java.io.Serializable;

/**
 * User: Alexander Duckardt
 * Date: 8/14/12
 */
public class WebResourceAttribute implements Serializable {
    private String attributeMapId;
    private String resourceId;
    private String targetAttributeName;
    private String amAttributeName;
    private String amPolicyUrl;

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

    public String getTargetAttributeName() {
        return targetAttributeName;
    }

    public void setTargetAttributeName(String targetAttributeName) {
        this.targetAttributeName = targetAttributeName;
    }

    public String getAmAttributeName() {
        return amAttributeName;
    }

    public void setAmAttributeName(String amAttributeName) {
        this.amAttributeName = amAttributeName;
    }

    public String getAmPolicyUrl() {
        return amPolicyUrl;
    }

    public void setAmPolicyUrl(String amPolicyUrl) {
        this.amPolicyUrl = amPolicyUrl;
    }
}
