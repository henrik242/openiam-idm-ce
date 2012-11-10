package org.openiam.selfsrvc.myidentity;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * In environments where the organization is unable to come up an algorithm to link IDs to a user, they can allow Users
 * to manually link their IDs together.
 *
 * If a user edits their existing ID, then we will only them to change their identity. They cannot change their password
 * using this US.
 *
 * For New ID, the system will authenticate the ID to ensure that its a valid ID and only then will it accept the change.
 */
public class ManageMyIdentityController extends CancellableFormController {


    protected UserDataWebService userMgr;
    protected LoginDataWebService loginManager;

    protected ProvisionService provRequestService;
    protected NavigatorDataWebService navigationDataService;
    private ManagedSystemDataService managedSysService;

    private static final Log log = LogFactory.getLog(ManageMyIdentityController.class);


    public ManageMyIdentityController() {
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
    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        ManageMyIdentityCommand cmd = new ManageMyIdentityCommand();


        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        ManagedSys[] mSysAry = managedSysService.getAllManagedSys();
        cmd.setManagedSysAry(mSysAry);


        String login = request.getParameter("login");
        String domain = request.getParameter("domain");
        String msys = request.getParameter("msys");

        if (login != null && !login.isEmpty()) {

            Login identity = loginManager.getLoginByManagedSys(domain, login, msys).getPrincipal();
            if (identity != null) {
                updatePrincipalWithSystemName(identity, mSysAry);
                cmd.setPrincipal(identity);
            }
        } else {
            Login newId = new Login();
            LoginId lId = new LoginId();
            lId.setDomainId("USR_SEC_DOMAIN");
            newId.setId(lId);

            newId.setUserId(userId);
            cmd.newRecord = true;

            cmd.setPrincipal(newId);
        }


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

        ManageMyIdentityCommand identityCmd = (ManageMyIdentityCommand) command;

        String btnClicked = request.getParameter("btn");


        UserResponse usrResp = userMgr.getUserWithDependent(userId, true);
        if (usrResp.getStatus() == ResponseStatus.SUCCESS) {
            usr = usrResp.getUser();
        }
        ProvisionUser pUser = new ProvisionUser(usr);

        List<Login> currentIdentityList = loginManager.getLoginByUser(userId).getPrincipalList();

        if (currentIdentityList == null) {
            currentIdentityList = new ArrayList<Login>();

        }
        // check if this is a new
        Login newId = identityCmd.getPrincipal();


        if (!identityCmd.newRecord) {
            for (Login l : currentIdentityList) {

                if ( l.getId().getManagedSysId().equalsIgnoreCase(newId.getId().getManagedSysId()) ) {
                    // update the identity with one provided by the user
                    if ("Delete".equalsIgnoreCase(btnClicked)) {
                        l.setOperation(AttributeOperationEnum.DELETE);
                    }else {

                        if (!newId.getId().getLogin().equalsIgnoreCase(l.getId().getLogin()))  {
                            l.getId().setLogin(newId.getId().getLogin());
                            l.setOperation(AttributeOperationEnum.REPLACE);
                        }
                    }
                    updateUser = true;

                }
            }
        }else {
            newId.setOperation(AttributeOperationEnum.ADD);
            currentIdentityList.add(newId);
            updateUser = true;
        }


        ProvisionUserResponse resp = null;

        if (updateUser) {
            pUser.setPrincipalList(currentIdentityList);

             resp = provRequestService.modifyUser(pUser);
        }

        if (resp == null || resp.getStatus() == ResponseStatus.SUCCESS) {
            return new ModelAndView(new RedirectView("/myIdentityList.selfserve?menuid=MY-ID-LIST&l=p", true));
        }

        return new ModelAndView(new RedirectView("/myIdentityDetail.selfserve?login=" + newId.getOrigPrincipalName()  +
                "&domain" + newId.getId().getDomainId() + "&msys=" + newId.getId().getManagedSysId(), true));

    }


    private void updatePrincipalWithSystemName(Login l, ManagedSys[] sysAry) {
        if (sysAry == null) {
            return;
        }
        // find the managedsys to get its name
        for (ManagedSys m : sysAry) {

            if (m.getManagedSysId().equalsIgnoreCase(l.getId().getManagedSysId())) {
                l.setManagedSysName(m.getName());
            }

        }


    }

    private void setOriginalIdentity(List<Login> currentPrincipalList) {

        for (Login l : currentPrincipalList) {

            if (l.getId().getLogin() != null) {
                 l.setOrigPrincipalName(l.getId().getLogin());
            }

        }

    }


    public UserDataWebService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataWebService userMgr) {
        this.userMgr = userMgr;
    }

    public LoginDataWebService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataWebService loginManager) {
        this.loginManager = loginManager;
    }

    public ProvisionService getProvRequestService() {
        return provRequestService;
    }

    public void setProvRequestService(ProvisionService provRequestService) {
        this.provRequestService = provRequestService;
    }

    public NavigatorDataWebService getNavigationDataService() {
        return navigationDataService;
    }

    public void setNavigationDataService(NavigatorDataWebService navigationDataService) {
        this.navigationDataService = navigationDataService;
    }

    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }
}
