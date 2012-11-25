package org.openiam.idm.srvc.user.domain;

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
import org.openiam.idm.srvc.user.dto.UserNote;

@Entity
@Table(name = "USER_NOTE")
@DozerDTOCorrespondence(UserNote.class)
public class UserNoteEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "USER_NOTE_ID", length = 32, nullable = false)
    private String userNoteId;

    @Column(name="CREATE_DATE", length=19)
    private Date createDate;

    @Column(name="CREATED_BY", length=20)
    private String createdBy;

    @Column(name="DESCRIPTION", length=1000)
    private String description;

    @Column(name="NOTE_TYPE", length=20)
    private String noteType;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private UserEntity user;

    public UserNoteEntity() {
    }

    public String getUserNoteId() {
        return userNoteId;
    }

    public void setUserNoteId(String userNoteId) {
        this.userNoteId = userNoteId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNoteEntity that = (UserNoteEntity) o;

        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (noteType != null ? !noteType.equals(that.noteType) : that.noteType != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (userNoteId != null ? !userNoteId.equals(that.userNoteId) : that.userNoteId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userNoteId != null ? userNoteId.hashCode() : 0;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (noteType != null ? noteType.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
