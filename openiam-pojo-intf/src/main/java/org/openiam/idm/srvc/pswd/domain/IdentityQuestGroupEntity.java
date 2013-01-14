package org.openiam.idm.srvc.pswd.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestGroup;


@Entity
@Table(name="IDENTITY_QUEST_GRP")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DozerDTOCorrespondence(IdentityQuestGroup.class)
public class IdentityQuestGroupEntity extends org.openiam.base.BaseObject implements java.io.Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "IDENTITY_QUEST_GRP_ID")
    private String identityQuestGrpId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "COMPANY_OWNER_ID")
    private String companyOwnerId;
    @XmlSchemaType(name = "dateTime")
     @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @XmlSchemaType(name = "dateTime")
     @Column(name = "LAST_UPDATE")
    private Date lastUpdate;
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @OneToMany(fetch=FetchType.LAZY, orphanRemoval=true, cascade = {CascadeType.ALL}, mappedBy="identityQuestGrp")
    private Set<IdentityQuestionEntity> identityQuestions = new HashSet<IdentityQuestionEntity>(
            0);

    public IdentityQuestGroupEntity() {
    }

    public IdentityQuestGroupEntity(String identityQuestGrpId) {
        this.identityQuestGrpId = identityQuestGrpId;
    }

    public IdentityQuestGroupEntity(String identityQuestGrpId, String name,
                              String status, String companyOwnerId, Date createDate,
                              String createdBy, Date lastUpdate, String lastUpdatedBy,
                              Set<IdentityQuestionEntity> identityQuestions) {
        this.identityQuestGrpId = identityQuestGrpId;
        this.name = name;
        this.status = status;
        this.companyOwnerId = companyOwnerId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.identityQuestions = identityQuestions;
    }
   
    public String getIdentityQuestGrpId() {
        return this.identityQuestGrpId;
    }

    public void setIdentityQuestGrpId(String identityQuestGrpId) {
        this.identityQuestGrpId = identityQuestGrpId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyOwnerId() {
        return this.companyOwnerId;
    }

    public void setCompanyOwnerId(String companyOwnerId) {
        this.companyOwnerId = companyOwnerId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Set<IdentityQuestionEntity> getIdentityQuestions() {
        return this.identityQuestions;
    }

    public void setIdentityQuestions(Set<IdentityQuestionEntity> identityQuestions) {
        this.identityQuestions = identityQuestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityQuestGroupEntity that = (IdentityQuestGroupEntity) o;

        if (identityQuestGrpId != null ? !identityQuestGrpId.equals(that.identityQuestGrpId) : that.identityQuestGrpId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (companyOwnerId != null ? !companyOwnerId.equals(that.companyOwnerId) : that.companyOwnerId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;
        if (lastUpdatedBy != null ? !lastUpdatedBy.equals(that.lastUpdatedBy) : that.lastUpdatedBy != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = identityQuestGrpId != null ? identityQuestGrpId.hashCode() : 0;
       
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (companyOwnerId != null ? companyOwnerId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastUpdatedBy != null ? lastUpdatedBy.hashCode() : 0);
        return result;
    }
}