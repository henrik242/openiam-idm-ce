package org.openiam.idm.srvc.continfo.domain;

// Generated Jan 23, 2010 1:06:13 AM by Hibernate Tools 3.2.2.GA



import javax.persistence.*;
import javax.annotation.Generated;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.pswd.dto.PasswordHistory;

@Entity
@Table(name="PWD_HISTORY")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DozerDTOCorrespondence(PasswordHistory.class)
public class PasswordHistoryEntity implements java.io.Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name="PWD_HISTORY_ID")
    private String pwdHistoryId;
    @Column(name="LOGIN")
    private String login;
    @Column(name="SERVICE_ID")
    private String serviceId;
    @Column(name="MANAGED_SYS_ID")
    private String managedSysId;
    @XmlSchemaType(name = "dateTime")
     @Column(name="DATE_CREATED")
    private Date dateCreated;
    @Column(name="PASSWORD")
    private String password;

    public PasswordHistoryEntity() {
    }

    public PasswordHistoryEntity(String login, String serviceId,
                           String managedSysId) {
        this.pwdHistoryId = pwdHistoryId;
        this.login = login;
        this.serviceId = serviceId;
        this.managedSysId = managedSysId;
        this.dateCreated = dateCreated;
    }

    public PasswordHistoryEntity(String pwdHistoryId, String login, String serviceId,
                           String managedSysId, Date dateCreated, String password) {
        this.pwdHistoryId = pwdHistoryId;
        this.login = login;
        this.serviceId = serviceId;
        this.managedSysId = managedSysId;
        this.dateCreated = dateCreated;
        this.password = password;
    }

    public String getPwdHistoryId() {
        return this.pwdHistoryId;
    }

    public void setPwdHistoryId(String pwdHistoryId) {
        this.pwdHistoryId = pwdHistoryId;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getManagedSysId() {
        return this.managedSysId;
    }

    public void setManagedSysId(String managedSysId) {
        this.managedSysId = managedSysId;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordHistoryEntity that = (PasswordHistoryEntity) o;

        if (pwdHistoryId != null ? !pwdHistoryId.equals(that.pwdHistoryId) : that.pwdHistoryId != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;
        if (managedSysId != null ? !managedSysId.equals(that.managedSysId) : that.managedSysId != null) return false;
        if (dateCreated != null ? !dateCreated.equals(that.dateCreated) : that.dateCreated != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = pwdHistoryId != null ? pwdHistoryId.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (managedSysId != null ? managedSysId.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
