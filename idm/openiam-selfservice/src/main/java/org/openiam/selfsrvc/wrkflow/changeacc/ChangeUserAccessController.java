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
import org.openiam.selfsrvc.hire.NewHireCommand;
import org.openiam.selfsrvc.wrkflow.AbstractFormWorkflowController;
import org.openiam.selfsrvc.wrkflow.WorkflowRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class ChangeUserAccessController extends AbstractFormWorkflowController {


    public ChangeUserAccessController() {
        super();
    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        return loadResourceInformation(request);

    }

    protected Map loadResourceInformation(HttpServletRequest request) {
        log.info("referenceData called.");

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        List<Role> roleList = roleDataService.getAllRoles().getRoleList();
        List<Resource> resourceList = resourceDataService.getResourcesByType("MANAGED_SYS");
        List<Group> groupList = groupManager.getAllGroups().getGroupList();


        Map model = new HashMap();

        model.put("roleList", roleList);
        model.put("resourceList", resourceList);
        model.put("groupList", groupList);


        return model;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ChangeUserAccessCommand cmd = new ChangeUserAccessCommand();


        return cmd;


    }




    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        HttpSession session = request.getSession();

        ChangeUserAccessCommand identityCmd = (ChangeUserAccessCommand) command;

        WorkflowRequest wrkFlowRequest = (WorkflowRequest) request.getSession().getAttribute("wrkflowRequest");

        ProvisionRequest provReq = createRequest(wrkFlowRequest,identityCmd);

        provRequestService.addRequest(provReq);
        // implementation pending

        IdmAuditLog auditLog = new IdmAuditLog(
                new Date(System.currentTimeMillis()), "PENDING_REQUEST",
                "SUCCESS", null, null,
                request.getRemoteHost(), 1, null, null,
                null, (String)request.getSession().getAttribute("login"), "CHANGE_ACCESS_WORFKLOW",
                ProvisionRequest.CHANGE_ACCESS_WORKFLOW, provReq.getRequestTitle(), null,
                null, null, (String)request.getSession().getAttribute("domain"), provReq.getRequestorId() );

        auditLog.setRequestId(provReq.getRequestId());
        auditLog.setSessionId(request.getSession().getId());


        auditService.addLog(auditLog);

        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("name", getUserName(provReq) );
        return mav;


    }

    protected ProvisionRequest createRequest(WorkflowRequest wrkFlowRequest, ChangeUserAccessCommand identityCmd) {
        ProvisionRequest req = new ProvisionRequest();
        Date curDate = new Date(System.currentTimeMillis());
        boolean addOperation = false;

        String workflowResourceId = wrkFlowRequest.getWorkflowResId();
        String personId = wrkFlowRequest.getPersonId();
        String requestorId = wrkFlowRequest.getRequestorId();

        Resource wrkflowResource = resourceDataService.getResource(workflowResourceId);

        User userData = userManager.getUserWithDependent(personId, true).getUser();

        // put the user information into a consistent object that we can serialize
        ProvisionUser pUser = new ProvisionUser(userData);


        // update the object based on the change requests
        if ( "ADD".equalsIgnoreCase(identityCmd.getOperation())) {
            addOperation = true;

        }
        if ( identityCmd.getRoleId() != null && !identityCmd.getRoleId().isEmpty()) {

            pUser.setMemberOfRoles(getRoleList(identityCmd.getRoleId(), addOperation));

        }

        if ( identityCmd.getResourceId() != null && !identityCmd.getResourceId().isEmpty()) {

            UserResourceAssociation ura = new UserResourceAssociation();
            ura.setResourceId(identityCmd.getResourceId());
            if (addOperation) {
                ura.setOperation(AttributeOperationEnum.ADD);
            }else {
                ura.setOperation(AttributeOperationEnum.DELETE);
            }
            Resource res = resourceDataService.getResource(identityCmd.getResourceId());

            ura.setManagedSystemId(res.getManagedSysId());
            ura.setResourceName(res.getName());

            List<UserResourceAssociation> uraList = new ArrayList<UserResourceAssociation>();
            uraList.add(ura);

            pUser.setUserResourceList(uraList);
        }

        if ( identityCmd.getGroupId() != null && !identityCmd.getGroupId().isEmpty() ) {


            Group g = new Group();
            if (addOperation) {
                g.setOperation(AttributeOperationEnum.ADD);
            }else {
                g.setOperation(AttributeOperationEnum.DELETE);
            }
            g.setGrpId(identityCmd.getGroupId());

            List<Group> groupList = new ArrayList<Group>();
            groupList.add(g);

            pUser.setMemberOfGroups(groupList);

        }


        String userAsXML = toXML(pUser);

        User requestor = userManager.getUserWithDependent(requestorId, false).getUser();

        // build the request object

        req.setRequestId(null);
        req.setStatus("PENDING");
        req.setStatusDate(curDate);
        req.setRequestDate(curDate);
        req.setRequestType(workflowResourceId);
        req.setWorkflowName(wrkflowResource.getName());
        req.setRequestorId(requestorId);
        req.setRequestorFirstName(requestor.getFirstName());
        req.setRequestorLastName(requestor.getLastName());


        req.setRequestTitle(wrkflowResource.getDescription() + " FOR:" + userData.getFirstName() + " " + userData.getLastName());
        req.setRequestReason(wrkFlowRequest.getDescription());


        req.setRequestXML(userAsXML);

        // add a user to the request - this is the person that we are terminating
        Set<RequestUser> reqUserSet = req.getRequestUsers();
        RequestUser reqUser = new RequestUser();
        reqUser.setFirstName(userData.getFirstName());
        reqUser.setLastName(userData.getLastName());
        reqUser.setUserId(userData.getUserId());
        reqUser.setDeptCd(userData.getDeptCd());
        reqUserSet.add(reqUser);


        RequestApprover reqApprover = getApprover(workflowResourceId, userData);
        req.getRequestApprovers().add(reqApprover);

        notifyApprover(req, reqUser, requestorId, userData);

        return req;


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
