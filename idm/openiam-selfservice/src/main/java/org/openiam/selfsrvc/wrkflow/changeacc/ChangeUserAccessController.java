package org.openiam.selfsrvc.wrkflow.changeacc;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.cd.dto.ReferenceData;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.loc.dto.Location;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.ws.UserResponse;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.resp.ProvisionUserResponse;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * In environments where the organization is unable to come up an algorithm to link IDs to a user, they can allow Users
 * to manually link their IDs together.
 *
 * If a user edits their existing ID, then we will only them to change their identity. They cannot change their password
 * using this US.
 *
 * For New ID, the system will authenticate the ID to ensure that its a valid ID and only then will it accept the change.
 */
public class ChangeUserAccessController extends CancellableFormController {


    protected RoleDataWebService roleDataService;
    protected ResourceDataService resourceDataService;

    private static final Log log = LogFactory.getLog(ChangeUserAccessController.class);


    public ChangeUserAccessController() {
        super();
    }


    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));

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

        Map model = new HashMap();

        model.put("roleList", roleList);
        model.put("resourceList", resourceList);


        return model;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ChangeUserAccessCommand cmd = new ChangeUserAccessCommand();



        return cmd;


    }

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(), true));
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        User usr = null;
        boolean updateUser = false;
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        ChangeUserAccessCommand identityCmd = (ChangeUserAccessCommand) command;

        // implementation pending

        ModelAndView mav = new ModelAndView(getSuccessView());
        return mav;


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
}
