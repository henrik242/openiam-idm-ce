package org.openiam.selfsrvc.wrkflow.terminate;


import org.openiam.base.AttributeOperationEnum;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserConstant;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.dto.UserResourceAssociation;
import org.openiam.selfsrvc.wrkflow.AbstractFormWorkflowController;
import org.openiam.selfsrvc.wrkflow.WorkflowRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class ChangeUserStatusController extends AbstractFormWorkflowController {


    public ChangeUserStatusController() {
        super();
    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        List<String> statusList = new LinkedList<String>();
        statusList.add(UserStatusEnum.DELETED.getValue());
        statusList.add(UserStatusEnum.LEAVE.getValue());
        statusList.add(UserStatusEnum.RETIRED.getValue());
        statusList.add(UserStatusEnum.TERMINATE.getValue());

        Map model = new HashMap();

        model.put("userStatusList", statusList);

        return model;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ChangeUserStatusCommand cmd = new ChangeUserStatusCommand();

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");
        if ( wrkFlowRequest != null) {

            String personId = wrkFlowRequest.getPersonId();
            cmd.setSelectedUser(userManager.getUserWithDependent(personId, false).getUser());

        }

        return cmd;


    }




    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        HttpSession session = request.getSession();

        ChangeUserStatusCommand cmd = (ChangeUserStatusCommand) command;
        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");

        ProvisionRequest provReq = createRequest(wrkFlowRequest, cmd);

        provRequestService.addRequest(provReq);


        // implementation pending

        IdmAuditLog auditLog = new IdmAuditLog(
                new Date(System.currentTimeMillis()), "PENDING_REQUEST",
                "SUCCESS", null, null,
                request.getRemoteHost(), 1, null, null,
                null, (String)request.getSession().getAttribute("login"), "CHANGE_USER_STATUS_WORFKLOW",
                ProvisionRequest.CHANGE_USER_STATUS_WORKFLOW, provReq.getRequestTitle(), null,
                null, null, (String)request.getSession().getAttribute("domain"), provReq.getRequestorId() );

        auditLog.setRequestId(provReq.getRequestId());
        auditLog.setSessionId(request.getSession().getId());


        auditService.createLog(auditLog);

        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("name", getUserName(provReq) );
        return mav;


    }

    protected ProvisionRequest createRequest(WorkflowRequest wrkFlowRequest, ChangeUserStatusCommand cmd) {

        ProvisionUser pUser = buildUserObject(wrkFlowRequest);
        pUser.setStatus(cmd.getNewStatus());


        ProvisionRequest pReq = buildRequest(wrkFlowRequest, pUser);

        wrkFlowRequest.getWorkflowResId();

        RequestApprover reqApprover = getApprover(wrkFlowRequest.getWorkflowResId(), pUser);
        pReq.getRequestApprovers().add(reqApprover);

        notifyApprover(pReq, pUser);

        return pReq;

    }






    public RoleDataWebService getRoleDataService() {
        return roleDataService;
    }

    public void setRoleDataService(RoleDataWebService roleDataService) {
        this.roleDataService = roleDataService;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public GroupDataWebService getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }
}
