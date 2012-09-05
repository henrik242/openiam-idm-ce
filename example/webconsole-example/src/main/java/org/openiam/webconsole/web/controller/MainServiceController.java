package org.openiam.webconsole.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openiam.webconsole.web.constant.CommonWebConstant;
import org.openiam.webconsole.web.model.UserSessionModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: Alexander Duckardt<br/>
 * Date: 8/25/12
 */
@Controller
public class MainServiceController extends BaseServiceController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {
        UserSessionModel userSession = (UserSessionModel) session
                .getAttribute(CommonWebConstant.userSession.name());
        if (userSession == null)
            return getLoginPage(session, request, response, model);
        else
            return "redirect:secure/dashboard";
    }

    @RequestMapping(value = "secure/dashboard", method = RequestMethod.GET)
    public String getDashboard(HttpSession session,
            UserSessionModel userSession, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {
        return "secure/dashboard";
    }
}
