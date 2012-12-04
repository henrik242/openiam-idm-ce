package org.openiam.selfsrvc.prov;


import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.grp.ws.GroupResponse;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.prov.request.ws.RequestWebService;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.role.ws.RoleResponse;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.ws.UserResponse;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.dto.UserResourceAssociation;
import org.openiam.provision.service.ProvisionService;
import org.openiam.selfsrvc.wrkflow.AbstractCompleteRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

// temp


public class RequestDetailController extends CancellableFormController implements ApplicationContextAware {

    protected RequestWebService provRequestService;
    protected UserDataWebService userManager;
    protected RoleDataWebService roleDataService;
    protected GroupDataWebService groupManager;
    protected ProvisionService provisionService;
    protected MailService mailService;
    protected LoginDataWebService loginManager;
    protected ResourceDataService resourceDataService;
    protected ManagedSystemDataService managedSysService;
    protected OrganizationDataService orgManager;
    protected Map workflowApprovalMap;

    protected static ApplicationContext ac;

    private static final Log log = LogFactory.getLog(RequestDetailController.class);

    public RequestDetailController() {

    }


    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(), true));
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        String requestId = ServletRequestUtils.getStringParameter(request, "requestId");
        ProvisionRequest req = provRequestService.getRequest(requestId).getRequest();
        Set<RequestUser> reqUserSet = req.getRequestUsers();


        RequestDetailCommand reqDetailCommand = new RequestDetailCommand();
        reqDetailCommand.setRequest(req);

        String userAsXML = req.getRequestXML();


        ProvisionUser pUser = null;

        if (userAsXML != null) {
            pUser = this.fromXML(userAsXML);

            reqDetailCommand.setUserDetail(pUser);
            String companyId = pUser.getCompanyId();
            if (companyId != null && companyId.length() > 0) {
                Organization org = orgManager.getOrganization(companyId);
                if (org != null) {
                    reqDetailCommand.setOrgName(org.getOrganizationName());
                }
            }

            if (pUser != null) {
                // if this is an existing user, then get their details

                groupMembership(pUser.getMemberOfGroups(), reqDetailCommand);
                roleMembership(pUser.getMemberOfRoles(), reqDetailCommand);
                resourceMembership(pUser.getUserResourceList(), reqDetailCommand);

            }
        }

        if (req.getRequestorId() == null || req.getRequestorId().isEmpty()) {
            reqDetailCommand.setRequestor(null);

        } else {
            if (req.getRequestorId() != null && req.getRequestorId().length() > 0) {

                UserResponse userResp = userManager.getUserWithDependent(req.getRequestorId(), false);
                reqDetailCommand.setRequestor(userResp.getUser());
            }
        }


        return reqDetailCommand;


    }

    private ProvisionUser fromXML(String userAsXML) {
        XStream xstream = new XStream();
        ProvisionUser user = (ProvisionUser) xstream.fromXML(userAsXML);
        return user;

    }

    /**
     * If there is a request for a group then add to the command object so that its easier to show the on the
     * requestDetail page
     *
     * @param groupList
     * @param reqDetailCommand
     */

    private void groupMembership(List<Group> groupList, RequestDetailCommand reqDetailCommand) {
        StringBuilder msg = new StringBuilder();

        if (groupList != null && !groupList.isEmpty()) {

            Group g = groupList.get(0);


            GroupResponse groupResponse = groupManager.getGroup(g.getGrpId());

            if ( g != null) {

               if ( g.getOperation() == AttributeOperationEnum.ADD ) {
                   msg.append("ADD GROUP ");

               }else {
                   msg.append("REMOVE GROUP ");
               }

                if (groupResponse.getStatus() == ResponseStatus.SUCCESS) {

                    msg.append( groupResponse.getGroup().getGrpName());
                    reqDetailCommand.setChangeDescription(msg.toString());
                }
            }



        }

    }

    /**
     * If there is a request for a role then add to the command object so that its easier to show the on the
     * requestDetail page
     *
     * @param roleList
     * @param reqDetailCommand
     */

    private void roleMembership(List<Role> roleList, RequestDetailCommand reqDetailCommand) {
        StringBuilder msg = new StringBuilder();

        if (roleList != null && !roleList.isEmpty()) {

            Role r = roleList.get(0);

            if ( r.getOperation() == AttributeOperationEnum.ADD ) {
                msg.append("ADD ROLE ");

            }else {
                msg.append("REMOVE ROLE ");
            }

            RoleResponse resp = roleDataService.getRole(r.getId().getServiceId(), r.getId().getRoleId());
            if (resp.getStatus() == ResponseStatus.SUCCESS) {

                msg.append( resp.getRole().getId().getRoleId());
                reqDetailCommand.setChangeDescription(msg.toString());

            }
        }

    }

    /**
     * If there is a request for a resource then add to the command object so that its easier to show the on the
     * requestDetail page
     *
     * @param resourceAssociation
     * @param reqDetailCommand
     */

    private void resourceMembership(List<UserResourceAssociation> resourceAssociation, RequestDetailCommand reqDetailCommand) {
        if (resourceAssociation != null && !resourceAssociation.isEmpty()) {

            UserResourceAssociation r = resourceAssociation.get(0);

            if (r.getOperation() != null) {
                reqDetailCommand.setOperation(r.getOperation().toString());
            }

            reqDetailCommand.setResource(resourceDataService.getResource(r.getResourceId()));

        }

    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        System.out.println("onSubmit called.");
        String status = null;
        Date curDate = new Date(System.currentTimeMillis());

        RequestDetailCommand requestDetailCmd = (RequestDetailCommand) command;

        String reqId = requestDetailCmd.getRequestId();
        int indx = reqId.indexOf(",");
        if (indx != -1) {
            reqId = reqId.substring(0, indx);
        }

        String userId = (String) request.getSession().getAttribute("userId");


        System.out.println("Request id = " + reqId);

        ProvisionRequest req = provRequestService.getRequest(reqId).getRequest();


        String btn = ServletRequestUtils.getStringParameter(request, "btn");

        if (btn.equalsIgnoreCase("Approve")) {
            status = "APPROVED";
        } else if (btn.equalsIgnoreCase("Claim")) {
            status = "CLAIMED";
        } else {
            status = "REJECTED";
        }
        req.setStatus(status);
        req.setStatusDate(curDate);

        // update the action of this approver.
        Set<org.openiam.idm.srvc.prov.request.dto.RequestApprover> requestApprovers = req.getRequestApprovers();
        for (org.openiam.idm.srvc.prov.request.dto.RequestApprover ra : requestApprovers) {
            if (ra.getApproverId().equalsIgnoreCase(userId)) {

                ra.setAction(status);
                ra.setActionDate(curDate);
                ra.setComment(requestDetailCmd.comment);
            }

        }

        provRequestService.updateRequest(req);


        String reqAsXML = req.getRequestXML();
        ProvisionUser pUser = this.fromXML(reqAsXML);

        String login = (String) request.getSession().getAttribute("login");
        String domain = (String) request.getSession().getAttribute("domain");
        pUser.setRequestClientIP(request.getRemoteHost());
        pUser.setRequestorLogin(login);
        pUser.setRequestorDomain(domain);

        AbstractCompleteRequest completeRequest = createRequestObject((String) workflowApprovalMap.get(req.getRequestType()));
        completeRequest.init(getApplicationContext());

        if (btn.equalsIgnoreCase("Approve")) {

            completeRequest.approveRequest(pUser, req, userId);
        } else {
            // request was rejected
            completeRequest.rejectRequest(pUser, req, userId);
        }


        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("requestDetailCmd", requestDetailCmd);

        return mav;


    }

    private AbstractCompleteRequest createRequestObject(String className) {

        try {

            Class cls = Class.forName(className);
            return (AbstractCompleteRequest) cls.newInstance();

        } catch (IllegalAccessException ia) {
            log.error(ia.getMessage(), ia);

        } catch (InstantiationException ie) {
            log.error(ie.getMessage(), ie);
        } catch (ClassNotFoundException ce) {
            log.error(ce.getMessage(), ce);
        }
        return null;

    }





    public UserDataWebService getUserManager() {
        return userManager;
    }


    public void setUserManager(UserDataWebService userManager) {
        this.userManager = userManager;
    }


    public RoleDataWebService getRoleDataService() {
        return roleDataService;
    }


    public void setRoleDataService(RoleDataWebService roleDataService) {
        this.roleDataService = roleDataService;
    }


    public GroupDataWebService getGroupManager() {
        return groupManager;
    }


    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }


    public ProvisionService getProvisionService() {
        return provisionService;
    }


    public void setProvisionService(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }


    public RequestWebService getProvRequestService() {
        return provRequestService;
    }


    public void setProvRequestService(RequestWebService provRequestService) {
        this.provRequestService = provRequestService;
    }


    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public LoginDataWebService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataWebService loginManager) {
        this.loginManager = loginManager;
    }

    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }

    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }

    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public OrganizationDataService getOrgManager() {
        return orgManager;
    }

    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }

    public Map getWorkflowApprovalMap() {
        return workflowApprovalMap;
    }

    public void setWorkflowApprovalMap(Map workflowApprovalMap) {
        this.workflowApprovalMap = workflowApprovalMap;
    }
}
