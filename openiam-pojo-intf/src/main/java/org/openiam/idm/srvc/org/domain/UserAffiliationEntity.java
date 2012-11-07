package org.openiam.idm.srvc.org.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.idm.srvc.org.dto.UserAffiliation;
import org.openiam.idm.srvc.user.domain.UserEntity;

@Entity
@Table(name = "USER_AFFILIATION")
public class UserAffiliationEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "USER_AFFILIATION_ID", length = 32, nullable = false)
    private String userAffiliationId;

    @ManyToOne
    @JoinColumn(name="USER_ID", referencedColumnName="USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="COMPANY_ID", referencedColumnName="COMPANY_ID")
    private OrganizationEntity organization;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "CREATE_DATE", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "START_DATE", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "END_DATE", length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "CREATED_BY", length = 20)
    private String createdBy;

    public UserAffiliationEntity() {
    }

    public UserAffiliationEntity(UserEntity user, OrganizationEntity organization) {
        this.user = user;
        this.organization = organization;
        this.status = "ACTIVE";
        long curTime = System.currentTimeMillis();
        this.createDate = new Date(curTime);
        this.startDate = new Date(curTime);
    }

    public UserAffiliationEntity(UserAffiliation affiliation, UserEntity user, OrganizationEntity organization) {
        this.userAffiliationId = affiliation.getUserAffiliationId();
        this.status = affiliation.getStatus();
        this.createDate = affiliation.getCreateDate();
        this.startDate = affiliation.getStartDate();
        this.createdBy = affiliation.getCreatedBy();
        this.organization = organization;
        this.user = user;
    }

    public String getUserAffiliationId() {
        return userAffiliationId;
    }

    public void setUserAffiliationId(String userAffiliationId) {
        this.userAffiliationId = userAffiliationId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
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
}
