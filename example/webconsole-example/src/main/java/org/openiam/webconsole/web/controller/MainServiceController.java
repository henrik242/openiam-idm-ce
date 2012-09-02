package org.openiam.webconsole.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.openiam.webconsole.web.model.LoginModel;
import org.openiam.webconsole.web.util.CommonWebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainServiceController extends BaseServiceController {

    @Autowired
    protected MainServiceController(Validator validator) {
        super(validator);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {
        LoginModel loginModel = new LoginModel();
        loginModel.setClientIP(CommonWebUtil.getClientIpAddr(request));
        model.addAttribute("loginModel", new LoginModel());
        return "index";
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public String getDashboard(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {
        return "dashboard";
    }
}
