package org.openiam.webadmin.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
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
 * Controller for the User Search
 *
 * @author suneet
 */
public class UserSearchController extends CancellableFormController {

    private RoleDataWebService roleDataService;
    private NavigatorDataWebService navigatorDataService;
    private UserDataWebService userDataWebService;
    private OrganizationDataService orgManager;

    private ResourceDataService resourceDataService;
    private MetadataWebService metadataService;

    private String resourceType;
    private String redirectView;


    private static final Log log = LogFactory.getLog(UserSearchController.class);


    public UserSearchController() {
        super();
    }

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(), true));
    }


    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        UserSearchCommand cmd = new UserSearchCommand();

        populateRefData(cmd);


        return cmd;

    }

    private void populateRefData(UserSearchCommand cmd) {
        cmd.setDeptList( orgManager.allDepartments(null));
        cmd.setDivList( orgManager.allDivisions(null));
        cmd.setOrgList(orgManager.getOrganizationList(null, "ACTIVE"));
        cmd.setRoleList(roleDataService.getAllRoles().getRoleList());
        cmd.setElementList(getComleteMetadataElementList());
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors) throws Exception {


        UserSearchCommand cmd = (UserSearchCommand) command;

        ModelAndView model = new ModelAndView(getSuccessView());

        if (cmd.getOrgList() == null || cmd.getOrgList().isEmpty()) {
            populateRefData(cmd);
        }

        model.addObject("userSearchCmd", cmd);

        List<User> userList = userDataWebService.search(cmd.buildSearch()).getUserList();
        if (userList != null) {
            model.addObject("userList", userList);
            model.addObject("resultsize", userList.size());

        } else {

            model.addObject("userList", Collections.EMPTY_LIST);
            model.addObject("resultsize", 0);

        }



        return model;

    }



    private MetadataElement[] getComleteMetadataElementList() {
        log.info("getUserMetadataTypes called.");

        return metadataService.getAllElementsForCategoryType("USER_TYPE").getMetadataElementAry();

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


    public String getRedirectView() {
        return redirectView;
    }

    public void setRedirectView(String redirectView) {
        this.redirectView = redirectView;
    }


    public UserDataWebService getUserDataWebService() {
        return userDataWebService;
    }

    public void setUserDataWebService(UserDataWebService userDataWebService) {
        this.userDataWebService = userDataWebService;
    }


}
