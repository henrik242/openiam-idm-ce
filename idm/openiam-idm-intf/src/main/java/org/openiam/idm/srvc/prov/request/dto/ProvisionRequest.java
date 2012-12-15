package org.openiam.idm.srvc.prov.request.dto;

// Generated Jan 9, 2009 5:33:58 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.user.dto.User;
/**
 * Domain object for a provisioning request
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvisionRequest", propOrder = {
    "requestId",
    "requestorId",
    "requestorFirstName",
    "requestorLastName",
    "requestDate",
    "status",
    "statusDate",
    "requestReason",
    "requestType",
    "requestXML",
    "managedResourceId",
    "changeAccessBy",
    "newRoleId",
    "newServiceId",
    "requestApprovers",
    "requestUsers",
    "requestForOrgId",
    "requestTitle",
        "workflowName"
})
public class ProvisionRequest implements java.io.Serializable {

	protected String requestId;
	protected Date requestDate;
	protected String status;

    @XmlSchemaType(name = "dateTime")
	protected Date statusDate;
	protected String requestReason;
	protected String requestType;
    protected String workflowName;
	protected String requestXML;
	protected String managedResourceId;
    protected String requestorId;

    protected String requestorFirstName;
    protected String requestorLastName;


    protected String requestTitle;


	protected String changeAccessBy;
	protected String newRoleId;
	protected String newServiceId;

    protected String requestForOrgId;


	protected Set<RequestApprover> requestApprovers = new HashSet<RequestApprover>(0);
	protected Set<RequestUser> requestUsers = new HashSet<RequestUser>(0);

    public static final String NEW_USER_WORKFLOW = "260";
    public static final String TERMINATE_USER_WORKFLOW = "261";
    public static final String CHANGE_ACCESS_WORKFLOW = "262";
    public static final String SELF_REGISTRATION_WORKFLOW = "255";


	public ProvisionRequest() {
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProvisionRequest)) return false;

        ProvisionRequest that = (ProvisionRequest) o;

        if (changeAccessBy != null ? !changeAccessBy.equals(that.changeAccessBy) : that.changeAccessBy != null)
            return false;
        if (managedResourceId != null ? !managedResourceId.equals(that.managedResourceId) : that.managedResourceId != null)
            return false;
        if (newRoleId != null ? !newRoleId.equals(that.newRoleId) : that.newRoleId != null) return false;
        if (newServiceId != null ? !newServiceId.equals(that.newServiceId) : that.newServiceId != null) return false;
        if (requestDate != null ? !requestDate.equals(that.requestDate) : that.requestDate != null) return false;
        if (requestForOrgId != null ? !requestForOrgId.equals(that.requestForOrgId) : that.requestForOrgId != null)
            return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (requestReason != null ? !requestReason.equals(that.requestReason) : that.requestReason != null)
            return false;
        if (requestTitle != null ? !requestTitle.equals(that.requestTitle) : that.requestTitle != null) return false;
        if (requestType != null ? !requestType.equals(that.requestType) : that.requestType != null) return false;
        if (requestXML != null ? !requestXML.equals(that.requestXML) : that.requestXML != null) return false;
        if (requestorFirstName != null ? !requestorFirstName.equals(that.requestorFirstName) : that.requestorFirstName != null)
            return false;
        if (requestorId != null ? !requestorId.equals(that.requestorId) : that.requestorId != null) return false;
        if (requestorLastName != null ? !requestorLastName.equals(that.requestorLastName) : that.requestorLastName != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (statusDate != null ? !statusDate.equals(that.statusDate) : that.statusDate != null) return false;
        if (workflowName != null ? !workflowName.equals(that.workflowName) : that.workflowName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestId != null ? requestId.hashCode() : 0;
        result = 31 * result + (requestDate != null ? requestDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (statusDate != null ? statusDate.hashCode() : 0);
        result = 31 * result + (requestReason != null ? requestReason.hashCode() : 0);
        result = 31 * result + (requestType != null ? requestType.hashCode() : 0);
        result = 31 * result + (workflowName != null ? workflowName.hashCode() : 0);
        result = 31 * result + (requestXML != null ? requestXML.hashCode() : 0);
        result = 31 * result + (managedResourceId != null ? managedResourceId.hashCode() : 0);
        result = 31 * result + (requestorId != null ? requestorId.hashCode() : 0);
        result = 31 * result + (requestorFirstName != null ? requestorFirstName.hashCode() : 0);
        result = 31 * result + (requestorLastName != null ? requestorLastName.hashCode() : 0);
        result = 31 * result + (requestTitle != null ? requestTitle.hashCode() : 0);
        result = 31 * result + (changeAccessBy != null ? changeAccessBy.hashCode() : 0);
        result = 31 * result + (newRoleId != null ? newRoleId.hashCode() : 0);
        result = 31 * result + (newServiceId != null ? newServiceId.hashCode() : 0);
        result = 31 * result + (requestForOrgId != null ? requestForOrgId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProvisionRequest{" +
                "requestId='" + requestId + '\'' +
                ", requestDate=" + requestDate +
                ", status='" + status + '\'' +
                ", statusDate=" + statusDate +
                ", requestReason='" + requestReason + '\'' +
                ", requestType='" + requestType + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", requestXML='" + requestXML + '\'' +
                ", managedResourceId='" + managedResourceId + '\'' +
                ", requestorId='" + requestorId + '\'' +
                ", requestorFirstName='" + requestorFirstName + '\'' +
                ", requestorLastName='" + requestorLastName + '\'' +
                ", requestTitle='" + requestTitle + '\'' +
                ", changeAccessBy='" + changeAccessBy + '\'' +
                ", newRoleId='" + newRoleId + '\'' +
                ", newServiceId='" + newServiceId + '\'' +
                ", requestForOrgId='" + requestForOrgId + '\'' +
                ", requestApprovers=" + requestApprovers +
                ", requestUsers=" + requestUsers +
                '}';
    }

    public ProvisionRequest(String requestId) {
		this.requestId = requestId;
	}



    public String getRequestId() {
		return this.requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}



	public Date getRequestDate() {
		return this.requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return this.statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getRequestReason() {
		return this.requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}



	public Set<RequestUser> getRequestUsers() {
		return this.requestUsers;
	}

	public void setRequestUsers(Set<RequestUser> requestUserLists) {
		this.requestUsers = requestUserLists;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Set<RequestApprover> getRequestApprovers() {
		return requestApprovers;
	}

	public void setRequestApprovers(Set<RequestApprover> requestApprovers) {
		this.requestApprovers = requestApprovers;
	}



	public String getChangeAccessBy() {
		return changeAccessBy;
	}

	public void setChangeAccessBy(String changeAccessBy) {
		this.changeAccessBy = changeAccessBy;
	}

	public String getRequestXML() {
		return requestXML;
	}

	public void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
	}

	public String getNewRoleId() {
		return newRoleId;
	}

	public void setNewRoleId(String newRoleId) {
		this.newRoleId = newRoleId;
	}

	public String getNewServiceId() {
		return newServiceId;
	}

	public void setNewServiceId(String newServiceId) {
		this.newServiceId = newServiceId;
	}

	public String getManagedResourceId() {
		return managedResourceId;
	}

	public void setManagedResourceId(String managedResourceId) {
		this.managedResourceId = managedResourceId;
	}

    public String getRequestForOrgId() {
        return requestForOrgId;
    }

    public void setRequestForOrgId(String requestForOrgId) {
        this.requestForOrgId = requestForOrgId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }


    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }


    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
}
