package org.openiam.idm.srvc.prov.request.dto;

// Generated Jan 9, 2009 5:33:58 PM by Hibernate Tools 3.2.2.GA

//import org.openiam.idm.srvc.prov.request.domain.RequestApproverEntity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Object represents an approver for a request.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestApprover", propOrder = {
    "reqApproverId",
    "approverId",
    "approverLevel",
    "approverType",
    "requestId",
    "actionDate",
    "action",
    "comment",
    "status",
    "mngSysGroupId",
    "managedSysId",
    "roleDomain",
    "applyDelegationFilter"
})
public class RequestApprover implements java.io.Serializable {

	protected String reqApproverId;

	protected Integer approverLevel;
    /* user, supervisor, role */
	protected String approverType;
    /* id of the approver - if its role then its the role ID. If its a user or supervisor its the user id  */
    protected String approverId;
    protected String roleDomain;

	protected String requestId;

    @XmlSchemaType(name = "dateTime")
	protected Date actionDate;
	protected String action;
	protected String comment;
	protected String status;
	
	protected String mngSysGroupId;
	protected String managedSysId;
    protected int applyDelegationFilter = 0;


	public RequestApprover() {
	}

    public RequestApprover(String approverId, Integer approverLevel,
                           String approverType, String status) {
        this.approverId = approverId;
        this.approverLevel = approverLevel;
        this.approverType = approverType;
        this.status = status;

    }



	public String getReqApproverId() {
		return reqApproverId;
	}


	public void setReqApproverId(String reqApproverId) {
		this.reqApproverId = reqApproverId;
	}


	public String getApproverId() {
		return approverId;
	}


	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}


	


	public String getApproverType() {
		return approverType;
	}


	public void setApproverType(String approverType) {
		this.approverType = approverType;
	}


	public String getRequestId() {
		return requestId;
	}


	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	public Date getActionDate() {
		return actionDate;
	}


	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getMngSysGroupId() {
		return mngSysGroupId;
	}


	public void setMngSysGroupId(String mngSysGroupId) {
		this.mngSysGroupId = mngSysGroupId;
	}


	public String getManagedSysId() {
		return managedSysId;
	}


	public void setManagedSysId(String managedSysId) {
		this.managedSysId = managedSysId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getApproverLevel() {
		return approverLevel;
	}


	public void setApproverLevel(Integer approverLevel) {
		this.approverLevel = approverLevel;
	}

    public String getRoleDomain() {
        return roleDomain;
    }

    public void setRoleDomain(String roleDomain) {
        this.roleDomain = roleDomain;
    }

    public int getApplyDelegationFilter() {
        return applyDelegationFilter;
    }

    public void setApplyDelegationFilter(int applyDelegationFilter) {
        this.applyDelegationFilter = applyDelegationFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestApprover)) return false;

        RequestApprover that = (RequestApprover) o;

        if (applyDelegationFilter != that.applyDelegationFilter) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (actionDate != null ? !actionDate.equals(that.actionDate) : that.actionDate != null) return false;
        if (approverId != null ? !approverId.equals(that.approverId) : that.approverId != null) return false;
        if (approverLevel != null ? !approverLevel.equals(that.approverLevel) : that.approverLevel != null)
            return false;
        if (approverType != null ? !approverType.equals(that.approverType) : that.approverType != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (managedSysId != null ? !managedSysId.equals(that.managedSysId) : that.managedSysId != null) return false;
        if (mngSysGroupId != null ? !mngSysGroupId.equals(that.mngSysGroupId) : that.mngSysGroupId != null)
            return false;
        if (reqApproverId != null ? !reqApproverId.equals(that.reqApproverId) : that.reqApproverId != null)
            return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (roleDomain != null ? !roleDomain.equals(that.roleDomain) : that.roleDomain != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reqApproverId != null ? reqApproverId.hashCode() : 0;
        result = 31 * result + (approverLevel != null ? approverLevel.hashCode() : 0);
        result = 31 * result + (approverType != null ? approverType.hashCode() : 0);
        result = 31 * result + (approverId != null ? approverId.hashCode() : 0);
        result = 31 * result + (roleDomain != null ? roleDomain.hashCode() : 0);
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (actionDate != null ? actionDate.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (mngSysGroupId != null ? mngSysGroupId.hashCode() : 0);
        result = 31 * result + (managedSysId != null ? managedSysId.hashCode() : 0);
        result = 31 * result + applyDelegationFilter;
        return result;
    }

    @Override
    public String toString() {
        return "RequestApprover{" +
                "reqApproverId='" + reqApproverId + '\'' +
                ", approverLevel=" + approverLevel +
                ", approverType='" + approverType + '\'' +
                ", approverId='" + approverId + '\'' +
                ", roleDomain='" + roleDomain + '\'' +
                ", requestId='" + requestId + '\'' +
                ", actionDate=" + actionDate +
                ", action='" + action + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", mngSysGroupId='" + mngSysGroupId + '\'' +
                ", managedSysId='" + managedSysId + '\'' +
                ", applyDelegationFilter=" + applyDelegationFilter +
                '}';
    }
}
