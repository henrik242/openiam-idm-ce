package org.openiam.idm.srvc.res.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.res.dto.UserPrivilege;

@Entity
@Table(name = "USER_PRIVILEGE")
@DozerDTOCorrespondence(UserPrivilege.class)
public class UserPrivilegeEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "USER_PRIVILEGE_ID", length = 32)
    private String userPrivilegeId;

    @Column(name = "USER_ID", length = 40)
    private String userId;

    @Column(name = "RESOURCE_ID", length = 40)
    private String resourceId;
   // private PrivilegeDef privilege;

    @Column(name = "PERMIT", length = 40)
    private boolean permit;

    @Column(name = "START_DATE", length = 40)
    private Date startDate;

    @Column(name = "END_DATE", length = 40)
    private Date endDate;

    public UserPrivilegeEntity() {
    }

    public String getUserPrivilegeId() {
        return userPrivilegeId;
    }

    public void setUserPrivilegeId(String userPrivilegeId) {
        this.userPrivilegeId = userPrivilegeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
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
}
