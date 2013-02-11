package org.openiam.webadmin.user;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.msg.service.MailTemplateParameters;
import org.openiam.idm.srvc.msg.ws.NotificationRequest;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.synch.dto.BulkMigrationConfig;
import org.openiam.idm.srvc.synch.ws.AsynchIdentitySynchService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.service.AsynchUserProvisionService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller for the new hire form.
 *
 * @author suneet
 */
public class BulkProvisioningController extends AbstractWizardFormController {

    private GroupDataWebService groupManager;
    private RoleDataWebService roleDataService;
    private NavigatorDataWebService navigatorDataService;
    private UserDataWebService userDataWebService;

    private AsynchUserProvisionService provService;
    private OrganizationDataService orgManager;

    private ResourceDataService resourceDataService;
    private MetadataWebService metadataService;
    private AsynchIdentitySynchService syncClient;
    private MailService notificationService;

    private String resourceType;
    private String redirectView;


    private static final Log log = LogFactory.getLog(BulkProvisioningController.class);


    public BulkProvisioningController() {
        super();
    }


    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page) {

        BulkProvsioningValidator validator = (BulkProvsioningValidator) getValidator();
        switch (page) {
            case 0:
                validator.validateUserSelectionForm(command, errors);
                break;
            case 1:
                validator.validateOperation(command, errors);
                break;
        }

    }


    protected ModelAndView processFinish(HttpServletRequest request,
                                         HttpServletResponse response, Object command, BindException arg3)
            throws Exception {

        log.debug("In processFinish..");

        BulkProvisioningCommand cmd = (BulkProvisioningCommand) command;
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String login = (String) session.getAttribute("login");

        // populate the config object
        BulkMigrationConfig config = cmd.getConfig();
        config.setRequestorLogin(login);

        // start the provisioning process.
        if(BulkMigrationConfig.ACTION_SEND_EMAIL.equalsIgnoreCase(cmd.getActionType())) {
            String selectedNotificationName = cmd.getSelectedNotificationName();

            HashMap<String,String> mailParameters = new HashMap<String,String>();
            StringBuilder ids = new StringBuilder();
            StringBuilder emails = new StringBuilder();
            boolean first = true;
            for(String selUserId : cmd.getSelectedUserIds()) {
                if(!first) {
                    ids.append(",");
                }
                ids.append("'").append(selUserId).append("'");

                User recipient = userDataWebService.getUserWithDependent(selUserId, true).getUser();
                if(StringUtils.isNotEmpty(recipient.getEmail())) {
                    if(!first) {
                        emails.append(",");
                    }
                    emails.append(recipient.getEmail());
                }
                if(first) {
                    first = false;
                }
            }

            mailParameters.put(MailTemplateParameters.USER_IDS.value(), ids.toString());
            mailParameters.put(MailTemplateParameters.TO.value(), emails.toString());

            notificationService.sendNotificationRequest(new NotificationRequest(selectedNotificationName,mailParameters));
        } else {
            syncClient.bulkUserMigration(config);
        }
        /*BulkMigrationConfig config = new BulkMigrationConfig(cmd.getLastName(), cmd.getCompanyId(), cmd.getDeptId(),
                cmd.getDivision(), cmd.getAttributeName(), cmd.getAttributeValue(),
                null, cmd.getOperation(), cmd.getTargetRole(), cmd.getTargetResource());

        if (cmd.getUserStatus() != null) {
            config.setUserStatus(cmd.getUserStatus().toString());
        }
        if (cmd.getActionType() != null && !cmd.getActionType().isEmpty()) {
            config.setActionType(cmd.getActionType());
        }
        if (cmd.getNewPassword() != null && !cmd.getNewPassword().isEmpty()) {
            config.setNewPassword(cmd.getNewPassword());
        }

        if (cmd.getRole() != null && !cmd.getRole().isEmpty()) {
            config.setRole(cmd.getRole());
        }
        */




        return new ModelAndView(new RedirectView(redirectView, true));


    }


    protected ModelAndView processCancel(HttpServletRequest request,
                                         HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        Map model = new HashMap();
        model.put("message", "Request to reset the password has been canceled");
        return new ModelAndView("pub/cancel");

    }


    @Override
    protected Map referenceData(HttpServletRequest request, int page) throws Exception {

        log.info("in referenceData");

        switch (page) {
            case 0:
                return loadUserSearchValues(request);
            case 1:
                return loadTargetSystemValue(request);

        }
        return null;
    }

    protected Map loadUserSearchValues(HttpServletRequest request) {
        log.info("referenceData:loadUserTypes called.");

        HttpSession session = request.getSession();


        Map model = new HashMap();
        model.put("orgList", orgManager.getOrganizationList(null, "ACTIVE")); // orgManager.getTopLevelOrganizations() );
        // get the divisions
        model.put("divList", orgManager.allDivisions(null));
        // load the department list
        model.put("deptList", orgManager.allDepartments(null));

        model.put("elementList", getComleteMetadataElementList());

        model.put("roleList", roleDataService.getAllRoles().getRoleList());


        return model;

    }


    protected Map loadTargetSystemValue(HttpServletRequest request) {
        log.info("referenceData:loadUserInformation called.");

        HttpSession session = request.getSession();

        List<Resource> resourceList = resourceDataService.getResourcesByType(resourceType);
        Map model = new HashMap();
        if (resourceList != null) {
            model.put("resourceList", resourceList);
        }

        model.put("roleList", roleDataService.getAllRoles().getRoleList());


        return model;

    }


    private MetadataElement[] getComleteMetadataElementList() {
        log.info("getUserMetadataTypes called.");

        return metadataService.getAllElementsForCategoryType("USER_TYPE").getMetadataElementAry();

    }


    public GroupDataWebService getGroupManager() {
        return groupManager;
    }


    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }


    public RoleDataWebService getRoleDataService() {
        return roleDataService;
    }


    public void setRoleDataService(RoleDataWebService roleDataService) {
        this.roleDataService = roleDataService;
    }


    public NavigatorDataWebService getNavigatorDataService() {
        return navigatorDataService;
    }


    public void setNavigatorDataService(NavigatorDataWebService navigatorDataService) {
        this.navigatorDataService = navigatorDataService;
    }


    public OrganizationDataService getOrgManager() {
        return orgManager;
    }


    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }


    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }


    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }


    public AsynchUserProvisionService getProvService() {
        return provService;
    }

    public void setProvService(AsynchUserProvisionService provService) {
        this.provService = provService;
    }

    public MetadataWebService getMetadataService() {
        return metadataService;
    }

    public void setMetadataService(MetadataWebService metadataService) {
        this.metadataService = metadataService;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public AsynchIdentitySynchService getSyncClient() {
        return syncClient;
    }

    public void setSyncClient(AsynchIdentitySynchService syncClient) {
        this.syncClient = syncClient;
    }

    public String getRedirectView() {
        return redirectView;
    }

    public void setRedirectView(String redirectView) {
        this.redirectView = redirectView;
    }

    public MailService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(MailService notificationService) {
        this.notificationService = notificationService;
    }

    public UserDataWebService getUserDataWebService() {
        return userDataWebService;
    }

    public void setUserDataWebService(UserDataWebService userDataWebService) {
        this.userDataWebService = userDataWebService;
    }
}
