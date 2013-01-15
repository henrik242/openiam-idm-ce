package org.openiam.selfsrvc.wrkflow.changeacc;


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
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.dto.UserResourceAssociation;
import org.openiam.selfsrvc.wrkflow.AbstractFormWorkflowController;
import org.openiam.selfsrvc.wrkflow.WorkflowRequest;
import org.openiam.selfsrvc.wrkflow.terminate.ChangeUserStatusCommand;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class ChangeUserGroupController extends AbstractFormWorkflowController {


    public ChangeUserGroupController() {
        super();
    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        return loadResourceInformation(request);

    }

    protected Map loadResourceInformation(HttpServletRequest request) {

        List<Group> groupList = groupManager.getAllGroups().getGroupList();

        Map model = new HashMap();

        model.put("groupList", groupList);


        return model;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ChangeUserGroupCommand cmd = new ChangeUserGroupCommand();


        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");
        if ( wrkFlowRequest != null) {

            String personId = wrkFlowRequest.getPersonId();
            cmd.setSelectedUser(userManager.getUserWithDependent(personId, false).getUser());
            cmd.setCurrentGroupMemberships(groupManager.getUserInGroupsAsFlatList(personId).getGroupList());


        }

        return cmd;


    }




    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        HttpSession session = request.getSession();

        ChangeUserGroupCommand identityCmd = (ChangeUserGroupCommand) command;

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");

        ProvisionRequest provReq = createRequest(wrkFlowRequest,identityCmd);

        provRequestService.addRequest(provReq);
        // implementation pending

        IdmAuditLog auditLog = new IdmAuditLog(
                new Date(System.currentTimeMillis()), "PENDING_REQUEST",
                "SUCCESS", null, null,
                request.getRemoteHost(), 1, null, null,
                null, (String)request.getSession().getAttribute("login"), "CHANGE_GROUP_WORKFLOW",
                ProvisionRequest.CHANGE_GROUP_WORKFLOW, provReq.getRequestTitle(), null,
                null, null, (String)request.getSession().getAttribute("domain"), provReq.getRequestorId() );

        auditLog.setRequestId(provReq.getRequestId());
        auditLog.setSessionId(request.getSession().getId());


        auditService.createLog(auditLog);

        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("name", getUserName(provReq) );
        return mav;


    }

    protected ProvisionRequest createRequest(WorkflowRequest wrkFlowRequest, ChangeUserGroupCommand cmd) {

        boolean addOperation = false;

        if ( "ADD".equalsIgnoreCase(cmd.getOperation())) {
            addOperation = true;

        }

        ProvisionUser pUser = buildUserObject(wrkFlowRequest);

        if ( cmd.getGroupId() != null && !cmd.getGroupId().isEmpty() ) {


            Group g = new Group();
            if (addOperation) {
                g.setOperation(AttributeOperationEnum.ADD);
            }else {
                g.setOperation(AttributeOperationEnum.DELETE);
            }
            g.setGrpId(cmd.getGroupId());

            List<Group> groupList = new ArrayList<Group>();
            groupList.add(g);

            pUser.setMemberOfGroups(groupList);

        }


        ProvisionRequest pReq = buildRequest(wrkFlowRequest, pUser);

        wrkFlowRequest.getWorkflowResId();

        RequestApprover reqApprover = getApprover(wrkFlowRequest.getWorkflowResId(), pUser);
        pReq.getRequestApprovers().add(reqApprover);

        notifyApprover(pReq, pUser);

        return pReq;

    }








}
