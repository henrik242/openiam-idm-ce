package org.openiam.idm.srvc.grp.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.grp.dto.UserGroup;
import org.openiam.idm.srvc.user.domain.UserEntity;

@Entity
@Table(name = "USER_GRP")
@DozerDTOCorrespondence(UserGroup.class)
public class UserGroupEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "USER_GRP_ID", length = 32)
    private String userGrpId;

    @ManyToOne
    @JoinColumn(name = "GRP_ID")
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "CREATE_DATE", length = 19)
    private Date createDate;

    @Column(name = "CREATED_BY", length = 20)
    private String createdBy;

    public UserGroupEntity() {
    }

    public UserGroupEntity(UserEntity user, GroupEntity group) {
        this.user = user;
        this.group = group;
    }

    public String getUserGrpId() {
        return userGrpId;
    }

    public void setUserGrpId(String userGrpId) {
        this.userGrpId = userGrpId;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
