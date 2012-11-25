package org.openiam.selfsrvc.prov;

import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.provision.dto.ProvisionUser;

import java.io.Serializable;
import java.util.List;

public class RequestDetailCommand implements Serializable {


    private String requestId;

    protected ProvisionRequest request;
    protected User requestor;
    protected String approverId;
    protected String comment;
    protected List<RequestApprover> requestApproverList;

    /* UserDetail is the user that this request is for */
    protected ProvisionUser userDetail;

    /* Organization name for this user.  Shown as a separate attribute because we want to show the name of the
     * Organization and the object only has the ID
      */
    protected String orgName;

    /* Change access details */
    protected String operation;

    protected Resource resource;
    protected Group group;
    protected Role role;


    private String submit;

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ProvisionRequest getRequest() {
        return request;
    }

    public void setRequest(ProvisionRequest request) {
        this.request = request;
    }

    public User getRequestor() {
        return requestor;
    }

    public void setRequestor(User requestor) {
        this.requestor = requestor;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<RequestApprover> getRequestApproverList() {
        return requestApproverList;
    }

    public void setRequestApproverList(List<RequestApprover> requestApproverList) {
        this.requestApproverList = requestApproverList;
    }


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public ProvisionUser getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(ProvisionUser userDetail) {
        this.userDetail = userDetail;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
