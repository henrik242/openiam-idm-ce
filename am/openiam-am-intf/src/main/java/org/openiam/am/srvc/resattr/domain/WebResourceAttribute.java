package org.openiam.am.srvc.resattr.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: Alexander Duckardt
 * Date: 8/14/12
 */
@Entity
@Table(name = "WEB_RESOURCE_ATTRIBUTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WebResourceAttribute implements Serializable {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="ATTRIBUTE_MAP_ID", length=32, nullable = false)
    private String attributeMapId;
    @Column(name="RESOURCE_ID", length=32, nullable = false)
    private String resourceId;
    @Column(name="TARGET_ATTRIBUTE_NAME", length=100, nullable = false)
    private String targetAttributeName;
    @Column(name="AM_ATTRIBUTE_NAME", length=100, nullable = true)
    private String amAttributeName;
    @Column(name="AM_POLICY_URL", length=100, nullable = true)
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
