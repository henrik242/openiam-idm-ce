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
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class ChangeUserRoleController extends AbstractFormWorkflowController {


    public ChangeUserRoleController() {
        super();
    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        return loadResourceInformation(request);

    }

    protected Map loadResourceInformation(HttpServletRequest request) {
        log.info("referenceData called.");

        HttpSession session = request.getSession();

        List<Role> roleList = roleDataService.getAllRoles().getRoleList();

        Map model = new HashMap();

        model.put("roleList", roleList);

        return model;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ChangeUserRoleCommand cmd = new ChangeUserRoleCommand();

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");
        if ( wrkFlowRequest != null) {

            String personId = wrkFlowRequest.getPersonId();
            cmd.setSelectedUser(userManager.getUserWithDependent(personId, false).getUser());
            cmd.setCurrentRoleMemberships(roleDataService.getUserRolesAsFlatList(personId).getRoleList());


        }

        return cmd;


    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        HttpSession session = request.getSession();

        ChangeUserRoleCommand identityCmd = (ChangeUserRoleCommand) command;

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");

        ProvisionRequest provReq = createRequest(wrkFlowRequest,identityCmd);

        provRequestService.addRequest(provReq);
        // implementation pending

        IdmAuditLog auditLog = new IdmAuditLog(
                new Date(System.currentTimeMillis()), "PENDING_REQUEST",
                "SUCCESS", null, null,
                request.getRemoteHost(), 1, null, null,
                null, (String)request.getSession().getAttribute("login"), "CHANGE_ROLE_WORKFLOW",
                ProvisionRequest.CHANGE_ROLE_WORKFLOW, provReq.getRequestTitle(), null,
                null, null, (String)request.getSession().getAttribute("domain"), provReq.getRequestorId() );

        auditLog.setRequestId(provReq.getRequestId());
        auditLog.setSessionId(request.getSession().getId());


        auditService.createLog(auditLog);

        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("name", getUserName(provReq) );
        return mav;


    }

    protected ProvisionRequest createRequest(WorkflowRequest wrkFlowRequest, ChangeUserRoleCommand cmd) {

        boolean addOperation = false;

        if ("ADD".equalsIgnoreCase(cmd.getOperation())) {
            addOperation = true;

        }

        ProvisionUser pUser = buildUserObject(wrkFlowRequest);

        if ( cmd.getRoleId() != null && !cmd.getRoleId().isEmpty()) {

            pUser.setMemberOfRoles(getRoleList(cmd.getRoleId(), addOperation));

        }




        ProvisionRequest pReq = buildRequest(wrkFlowRequest, pUser);

        wrkFlowRequest.getWorkflowResId();

        RequestApprover reqApprover = getApprover(wrkFlowRequest.getWorkflowResId(), pUser);
        pReq.getRequestApprovers().add(reqApprover);

        notifyApprover(pReq, pUser);

        return pReq;

    }






    private List<Role> getRoleList(String role, boolean addOperation) {
        List<Role> roleList = new ArrayList<Role>();
        /* parse the role */
        String domainId = null;
        String roleId = null;

        StringTokenizer st = new StringTokenizer(role, "*");
        if (st.hasMoreTokens()) {
            domainId = st.nextToken();
        }
        if (st.hasMoreElements()) {
            roleId = st.nextToken();
        }
        RoleId id = new RoleId(domainId, roleId);
        Role r = new Role();
        r.setId(id);
        if (addOperation) {
            r.setOperation(AttributeOperationEnum.ADD);
        }else {
            r.setOperation(AttributeOperationEnum.DELETE);
        }

        roleList.add(r);

        return roleList;
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
