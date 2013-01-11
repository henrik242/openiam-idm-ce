package org.openiam.idm.srvc.continfo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.annotation.Generated;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestGroup;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.continfo.domain.IdentityQuestGroupEntity;
import org.openiam.idm.srvc.user.domain.UserEntity;

@Entity
@Table(name="IDENTITY_QUESTION")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DozerDTOCorrespondence(IdentityQuestion.class)
public class IdentityQuestionEntity extends org.openiam.base.BaseObject implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1802758764731284709L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name="IDENTITY_QUESTION_ID")
    private String identityQuestionId;
    @ManyToOne
    @JoinColumn(name="IDENTITY_QUEST_GRP_ID")
    private IdentityQuestGroupEntity identityQuestGrp;
    @Column(name="QUESTION_TEXT")
    private String questionText;
    @Column(name="REQUIRED")
    private Integer required = new Integer(0);
    @Column(name="ACTIVE")
    private Integer active = new Integer(1);
    @Column(name="USER_ID")
    private String userId;

    public IdentityQuestionEntity() {
    }

    public IdentityQuestionEntity(String identityQuestionId) {
        this.identityQuestionId = identityQuestionId;
    }

    public IdentityQuestionEntity(String identityQuestionId,
                            IdentityQuestGroupEntity identityQuestGrp, String questionText,
                            Integer required, String userId) {
        this.identityQuestionId = identityQuestionId;
        this.identityQuestGrp = identityQuestGrp;
        this.questionText = questionText;
        this.required = required;
        this.userId = userId;
    }

    public String getIdentityQuestionId() {
        return this.identityQuestionId;
    }

    public void setIdentityQuestionId(String identityQuestionId) {
        this.identityQuestionId = identityQuestionId;
    }

    public IdentityQuestGroupEntity getIdentityQuestGrp() {
        return this.identityQuestGrp;
    }

    public void setIdentityQuestGrp(IdentityQuestGroupEntity identityQuestGrp) {
        this.identityQuestGrp = identityQuestGrp;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getRequired() {
        return this.required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityQuestionEntity that = (IdentityQuestionEntity) o;

        if (identityQuestionId != null ? !identityQuestionId.equals(that.identityQuestionId) : that.identityQuestionId != null) return false;
        if (identityQuestGrp != null ? !identityQuestGrp.equals(that.identityQuestGrp) : that.identityQuestGrp != null) return false;
        if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = identityQuestionId != null ? identityQuestionId.hashCode() : 0;
        result = 31 * result + (identityQuestGrp != null ? identityQuestGrp.hashCode() : 0);
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}