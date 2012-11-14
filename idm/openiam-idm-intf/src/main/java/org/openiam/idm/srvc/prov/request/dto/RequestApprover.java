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

   /* public RequestApprover(RequestApproverEntity entity) {
        this.reqApproverId = entity.getReqApproverId();
        this.approverLevel = entity.getApproverLevel();
        this.approverType = entity.getApproverType();
        this.approverId = entity.getApproverId();
        this.roleDomain = entity.getRoleDomain();
        this.requestId = entity.getRequestId();
        this.actionDate = entity.getActionDate();
        this.action = entity.getAction();
        this.comment = entity.getComment();
        this.status = entity.getStatus();
        this.mngSysGroupId = entity.getMngSysGroupId();
        this.managedSysId = entity.getManagedSysId();
    }
    */

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
}
