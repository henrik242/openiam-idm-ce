package org.openiam.idm.srvc.role.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.res.domain.ResourceEntity;
import org.openiam.idm.srvc.res.domain.ResourceRoleEntity;
import org.openiam.idm.srvc.res.dto.ResourceRole;
import org.openiam.idm.srvc.role.dto.UserRole;

@Entity
@Table(name="USER_ROLE")
public class UserRoleEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="USER_ROLE_ID", length=32)
    private String userRoleId;

    @Column(name="USER_ID",length=32,nullable=false)
    private String userId;

    @Column(name="SERVICE_ID",length=32,nullable=false)
    private String serviceId;

    @Column(name="ROLE_ID",length=32,nullable=false)
    private String roleId;

    @Column(name="STATUS",length=20)
    private String status;

    @Column(name="CREATE_DATE",length=19)
    private Date createDate;

    @Column(name="START_DATE",length=19)
    private Date startDate;

    @Column(name="END_DATE",length=19)
    private Date endDate;

    @Column(name="CREATED_BY",length=19)
    private String createdBy;

    public UserRoleEntity() {
    }

    public UserRoleEntity(String userId, String serviceId, String roleId) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.roleId = roleId;
        status = "ACTIVE";
        long curTime = System.currentTimeMillis();
        createDate = new Date(curTime);
        startDate = new Date(curTime);
    }

    public UserRoleEntity(UserRole userRole) {
        this.userRoleId = userRole.getUserRoleId();
        this.userId = userRole.getUserId();
        this.roleId = userRole.getRoleId();
        this.status = userRole.getStatus();
        this.createDate = userRole.getCreateDate();
        this.startDate = userRole.getStartDate();
        this.endDate = userRole.getEndDate();
        this.createdBy = userRole.getCreatedBy();
        this.serviceId = userRole.getServiceId();
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

}
