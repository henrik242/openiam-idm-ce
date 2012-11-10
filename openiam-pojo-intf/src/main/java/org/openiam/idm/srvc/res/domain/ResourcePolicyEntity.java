package org.openiam.idm.srvc.res.domain;

// Generated Mar 8, 2009 12:54:32 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Object representing the association between a resource and a policy.
 */
@Entity
@Table(name="RESOURCE_POLICY")
public class ResourcePolicyEntity implements java.io.Serializable {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="RESOURCE_POLICY_ID", length=32)
    private String resourcePolicyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESOURCE_ID")
    private ResourceEntity resource;

    @Column(name="ROLE_ID",length=20)
    private String roleId;

    @Column(name="POLICY_START",length=19)
    private Date policyStart;

    @Column(name="POLICY_END",length=19)
    private Date policyEnd;

    @Column(name="APPLY_TO_CHILDREN")
    private Integer applyToChildren;

    public ResourcePolicyEntity() {
    }

    public ResourcePolicyEntity(String resourcePolicyId) {
        this.resourcePolicyId = resourcePolicyId;
    }


    public String getResourcePolicyId() {
        return this.resourcePolicyId;
    }

    public void setResourcePolicyId(String resourcePolicyId) {
        this.resourcePolicyId = resourcePolicyId;
    }

    public ResourceEntity getResource() {
        return resource;
    }

    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Date getPolicyStart() {
        return this.policyStart;
    }

    public void setPolicyStart(Date policyStart) {
        this.policyStart = policyStart;
    }

    public Date getPolicyEnd() {
        return this.policyEnd;
    }

    public void setPolicyEnd(Date policyEnd) {
        this.policyEnd = policyEnd;
    }

    public Integer getApplyToChildren() {
        return this.applyToChildren;
    }

    public void setApplyToChildren(Integer applyToChildren) {
        this.applyToChildren = applyToChildren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourcePolicyEntity that = (ResourcePolicyEntity) o;

        if (applyToChildren != null ? !applyToChildren.equals(that.applyToChildren) : that.applyToChildren != null)
            return false;
        if (policyEnd != null ? !policyEnd.equals(that.policyEnd) : that.policyEnd != null) return false;
        if (policyStart != null ? !policyStart.equals(that.policyStart) : that.policyStart != null) return false;
        if (resourcePolicyId != null ? !resourcePolicyId.equals(that.resourcePolicyId) : that.resourcePolicyId != null)
            return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resourcePolicyId != null ? resourcePolicyId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (policyStart != null ? policyStart.hashCode() : 0);
        result = 31 * result + (policyEnd != null ? policyEnd.hashCode() : 0);
        result = 31 * result + (applyToChildren != null ? applyToChildren.hashCode() : 0);
        return result;
    }
}
