package org.openiam.idm.srvc.role.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.role.dto.RolePolicy;

@Entity
@Table(name="ROLE_POLICY")
@DozerDTOCorrespondence(RolePolicy.class)
public class RolePolicyEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="ROLE_POLICY_ID", length=32)
    private String rolePolicyId;

    @Column(name="ROLE_ID", length=32)
    private String roleId;

    @Column(name="NAME", length=40)
    private String name;

    @Column(name="VALUE1", length=40)
    private String value1;

    @Column(name="VALUE2", length=40)
    private String value2;

    @Column(name="ACTION", length=20)
    private String action;

    @Column(name="EXECUTION_ORDER")
    private Integer executionOrder;

    @Column(name="ACTION_QUALIFIER")
    private String actionQualifier;

    @Column(name="POLICY_SCRIPT",length=100)
    private String policyScript;

    @Column(name="SERVICE_ID",length=20)
    private String serviceId;

    public RolePolicyEntity() {
    }

    public String getRolePolicyId() {
        return rolePolicyId;
    }

    public void setRolePolicyId(String rolePolicyId) {
        this.rolePolicyId = rolePolicyId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public String getActionQualifier() {
        return actionQualifier;
    }

    public void setActionQualifier(String actionQualifier) {
        this.actionQualifier = actionQualifier;
    }

    public String getPolicyScript() {
        return policyScript;
    }

    public void setPolicyScript(String policyScript) {
        this.policyScript = policyScript;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
