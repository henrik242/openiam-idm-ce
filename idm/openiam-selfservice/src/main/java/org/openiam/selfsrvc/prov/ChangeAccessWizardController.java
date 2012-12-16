package org.openiam.selfsrvc.prov;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.openiam.selfsrvc.AppConfiguration;
import org.openiam.selfsrvc.wrkflow.WorkflowRequest;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller for the new hire form.
 *
 * @author suneet
 */
public class ChangeAccessWizardController extends CancellableFormController {


    protected UserDataWebService userMgr;
    protected ResourceDataService resourceDataService;
    protected AppConfiguration configuration;
    protected String resourceName;

    private RoleDataWebService roleDataService;
    private GroupDataWebService groupManager;
    protected ProvisionService provisionService;
    protected OrganizationDataService orgManager;
    protected Map<String, String> workflowUrl;


    String defaultDomainId;
    String menuGroup;

    private static final Log log = LogFactory.getLog(ChangeAccessWizardController.class);


    public ChangeAccessWizardController() {
        super();
    }


    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }


    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView("/welcomePage.selfserve", true));


    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap();

        List<Resource> processResourceList = resourceDataService.getResourcesByType(configuration.getWorkFlowResourceType());
        model.put("workflowList", processResourceList);

        return model;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ChangeAccessCommand changeCmd = (ChangeAccessCommand) command;

        String workflowId = changeCmd.getWorkflowResourceId();
        String personId = changeCmd.getUserId();

        WorkflowRequest wrkFlowRequest = new WorkflowRequest();
        wrkFlowRequest.setPersonId(personId);
        wrkFlowRequest.setWorkflowResId(workflowId);
        wrkFlowRequest.setRequestorId((String)request.getSession().getAttribute("userId"));

        request.getSession().setAttribute("wrkflowRequest", wrkFlowRequest);


        if (workflowId != null) {
            String url =  workflowUrl.get(workflowId);
            return new ModelAndView(new RedirectView(url, true));


        }


        return super.onSubmit(request, response, command, errors);    //To change body of overridden methods use File | Settings | File Templates.
    }




    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }


    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }


    public UserDataWebService getUserMgr() {
        return userMgr;
    }


    public void setUserMgr(UserDataWebService userMgr) {
        this.userMgr = userMgr;
    }


    public String getResourceName() {
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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


    public OrganizationDataService getOrgManager() {
        return orgManager;
    }


    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }

    public AppConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public Map<String, String> getWorkflowUrl() {
        return workflowUrl;
    }

    public void setWorkflowUrl(Map<String, String> workflowUrl) {
        this.workflowUrl = workflowUrl;
    }
}
