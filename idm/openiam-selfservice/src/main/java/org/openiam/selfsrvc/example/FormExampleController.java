package org.openiam.selfsrvc.example;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.user.dto.User;
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
import java.util.Date;
import java.util.List;


/**
 * In environments where the organization is unable to come up an algorithm to link IDs to a user, they can allow Users
 * to manually link their IDs together.
 * <p/>
 * If a user edits their existing ID, then we will only them to change their identity. They cannot change their password
 * using this US.
 * <p/>
 * For New ID, the system will authenticate the ID to ensure that its a valid ID and only then will it accept the change.
 */
public class FormExampleController extends CancellableFormController {

    private GroupDataWebService groupManager;


    private static final Log log = LogFactory.getLog(FormExampleController.class);


    public FormExampleController() {
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


        FormExampleCommand cmd = new FormExampleCommand();

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        List<Group> groupList = groupManager.getUserInGroupsAsFlatList(userId).getGroupList();

        for (Group g : groupList) {

            if ("MODIFY".equalsIgnoreCase(g.getGrpName())) {
                cmd.setReadOnly(false);
            }else {
                cmd.setReadOnly(true);
            }




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

        FormExampleCommand identityCmd = (FormExampleCommand) command;

        String btnClicked = request.getParameter("btn");

        return new ModelAndView("pub/confirm");

    }


    public GroupDataWebService getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }
}
