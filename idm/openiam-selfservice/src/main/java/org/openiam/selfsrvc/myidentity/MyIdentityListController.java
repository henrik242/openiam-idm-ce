package org.openiam.selfsrvc.myidentity;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.ws.MetadataElementArrayResponse;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserAttribute;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.idm.srvc.user.ws.UserResponse;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Displays the list of Identities that a user has.
 * This module is to be used in Environments where the end user need to enter their own identities and synchronization
 * is not suitable
 */
public class MyIdentityListController extends CancellableFormController {


    protected UserDataWebService userMgr;
    protected LoginDataWebService loginManager;

    protected ProvisionService provRequestService;
    protected NavigatorDataWebService navigationDataService;
    private ManagedSystemDataService managedSysService;

    private static final Log log = LogFactory.getLog(ManageMyIdentityController.class);


    public MyIdentityListController() {
        super();
    }


    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,true) );

    }


    @Override
    protected Object formBackingObject(HttpServletRequest request)		throws Exception {



        MyIdentityListCommand cmd = new MyIdentityListCommand();


        HttpSession session =  request.getSession();
        String userId = (String)session.getAttribute("userId");


        // get the list of systems
        ManagedSys[] sysAry =  managedSysService.getAllManagedSys();

        List<Login> principalList = loginManager.getLoginByUser(userId).getPrincipalList();
        if (principalList != null) {
            updatePrincipalWithSystemName( principalList, sysAry);
            cmd.setPrincipalList(  principalList);
        }

        return cmd;


    }

    @Override
    protected ModelAndView onCancel(Object command) throws Exception {
        return new ModelAndView(new RedirectView(getCancelView(),true));
    }





    private  void updatePrincipalWithSystemName(List<Login> principalList, ManagedSys[] sysAry ) {
        if (sysAry == null) {
            return;
        }
        for (Login l  : principalList) {
            // find the managedsys to get its name
            for ( ManagedSys m : sysAry) {

                if (m.getManagedSysId().equalsIgnoreCase(l.getId().getManagedSysId())) {
                    l.setManagedSysName(m.getName());
                }

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
