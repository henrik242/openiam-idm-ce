package org.openiam.idm.srvc.continfo.domain;

// Generated Jan 23, 2010 1:06:13 AM by Hibernate Tools 3.2.2.GA



import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;

@Entity
@Table(name="USER_IDENTITY_ANS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DozerDTOCorrespondence(UserIdentityAnswer.class)
public class UserIdentityAnswerEntity extends org.openiam.base.BaseObject implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8841064146448209034L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name="IDENTITY_ANS_ID")
    protected String identityAnsId;
    @Column(name="IDENTITY_QUESTION_ID")
    protected String identityQuestionId;
    @Column(name="QUESTION_TEXT")
    protected String questionText;
    @Column(name="USER_ID")
    protected String userId;
    @Column(name="QUESTION_ANSWER")
    protected String questionAnswer;

    public UserIdentityAnswerEntity() {
    }

    public UserIdentityAnswerEntity(String identityAnsId) {
        this.identityAnsId = identityAnsId;
    }

    public UserIdentityAnswerEntity(String identityAnsId,
                              String questionText,
                              String userId, String questionAnswer) {
        this.identityAnsId = identityAnsId;
        this.questionText = questionText;
        this.userId = userId;
        this.questionAnswer = questionAnswer;
    }

    public String getIdentityAnsId() {
        return this.identityAnsId;
    }

    public void setIdentityAnsId(String identityAnsId) {
        this.identityAnsId = identityAnsId;
    }


    public String getQuestionText() {
        return this.questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionAnswer() {
        return this.questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getIdentityQuestionId() {
        return identityQuestionId;
    }

    public void setIdentityQuestionId(String identityQuestionId) {
        this.identityQuestionId = identityQuestionId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserIdentityAnswerEntity that = (UserIdentityAnswerEntity) o;

        if (identityAnsId != null ? !identityAnsId.equals(that.identityAnsId) : that.identityAnsId != null) return false;
        if (identityQuestionId != null ? !identityQuestionId.equals(that.identityQuestionId) : that.identityQuestionId != null) return false;
        if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (questionAnswer != null ? !questionAnswer.equals(that.questionAnswer) : that.questionAnswer != null) return false;
        if (identityQuestionId != null ? !identityQuestionId.equals(that.identityQuestionId) : that.identityQuestionId != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = identityAnsId != null ? identityAnsId.hashCode() : 0;
        result = 31 * result + (identityQuestionId != null ? identityQuestionId.hashCode() : 0);
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (questionAnswer != null ? questionAnswer.hashCode() : 0);
        result = 31 * result + (identityQuestionId != null ? identityQuestionId.hashCode() : 0);
        return result;
    }
}
