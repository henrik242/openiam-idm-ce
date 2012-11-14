package org.openiam.selfsrvc.prov;

import org.openiam.idm.srvc.res.dto.Resource;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Command object for the CreateRequest functionality
 *
 * @author suneet
 */
public class ChangeAccessCommand implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3001967685870249543L;

    Date startDate = new Date(System.currentTimeMillis());
    Date endDate;
    String requestorUserId;
    String reason;

    /* requestee */
    String userId;
    String userName;

    protected List<Resource> workflowList;
    protected String workflowResourceId;

    protected List<Resource> resourceList;


    public String getRequestorUserId() {
        return requestorUserId;
    }

    public void setRequestorUserId(String requestorUserId) {
        this.requestorUserId = requestorUserId;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }


    public List<Resource> getWorkflowList() {
        return workflowList;
    }

    public void setWorkflowList(List<Resource> workflowList) {
        this.workflowList = workflowList;
    }

    public String getWorkflowResourceId() {
        return workflowResourceId;
    }

    public void setWorkflowResourceId(String workflowResourceId) {
        this.workflowResourceId = workflowResourceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
