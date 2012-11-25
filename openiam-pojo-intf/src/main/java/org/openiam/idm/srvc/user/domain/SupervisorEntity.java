package org.openiam.idm.srvc.user.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.user.dto.Supervisor;

@Entity
@Table(name = "ORG_STRUCTURE")
@DozerDTOCorrespondence(Supervisor.class)
public class SupervisorEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ORG_STRUCTURE_ID", length = 32, nullable = false)
    private String orgStructureId;

    @Column(name = "COMMENTS")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "STAFF_ID")
    private UserEntity employee;

    @Column(name = "END_DATE", length = 19)
    private Date endDate;

    @Column(name = "IS_PRIMARY_SUPER")
    private Integer isPrimarySuper = new Integer(0);

    @Column(name = "START_DATE", length = 19)
    private Date startDate;

    @Column(name = "STATUS", length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "SUPERVISOR_ID")
    private UserEntity supervisor;

    @Column(name = "SUPERVISOR_TYPE", length = 20)
    private String supervisorType;

    public SupervisorEntity() {
    }

    public String getOrgStructureId() {
        return orgStructureId;
    }

    public void setOrgStructureId(String orgStructureId) {
        this.orgStructureId = orgStructureId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public UserEntity getEmployee() {
        return employee;
    }

    public void setEmployee(UserEntity employee) {
        this.employee = employee;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPrimarySuper() {
        return isPrimarySuper;
    }

    public void setPrimarySuper(Integer primarySuper) {
        isPrimarySuper = primarySuper;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserEntity getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(UserEntity supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisorType() {
        return supervisorType;
    }

    public void setSupervisorType(String supervisorType) {
        this.supervisorType = supervisorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupervisorEntity that = (SupervisorEntity) o;

        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (isPrimarySuper != null ? !isPrimarySuper.equals(that.isPrimarySuper) : that.isPrimarySuper != null)
            return false;
        if (orgStructureId != null ? !orgStructureId.equals(that.orgStructureId) : that.orgStructureId != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (supervisor != null ? !supervisor.equals(that.supervisor) : that.supervisor != null) return false;
        if (supervisorType != null ? !supervisorType.equals(that.supervisorType) : that.supervisorType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orgStructureId != null ? orgStructureId.hashCode() : 0;
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (isPrimarySuper != null ? isPrimarySuper.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (supervisor != null ? supervisor.hashCode() : 0);
        result = 31 * result + (supervisorType != null ? supervisorType.hashCode() : 0);
        return result;
    }
}
