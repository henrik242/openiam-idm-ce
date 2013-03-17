package org.openiam.selfsrvc.reg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.spring.mvc.ViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/pub/registration")
public class RegistrationController {
    private static final Log LOG = LogFactory.getLog(RegistrationController.class);

    @Autowired
    @Qualifier("registrationGroovyControllerScript")
    private ViewController registrationGroovyControllerScript;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    public String getEditForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Edit registration form called.");
        model.addAttribute("registrationCommand", new RegistrationCommand());
        return registrationGroovyControllerScript.handleRequest(model, org.springframework.web.bind.annotation.RequestMethod.GET, request, response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    public String save(Model model, RegistrationCommand command, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if(org.mule.util.StringUtils.isNotEmpty(command.getSubmitAction()) && "_cancel".equals(command.getSubmitAction())) {
            String backUrl = (String)request.getSession().getAttribute("backUrl");
            return "redirect:"+backUrl;
        }
        RegistrationValidator.validate(command, result);
        if (!result.hasErrors()) {
            return registrationGroovyControllerScript.handleRequest(model, org.springframework.web.bind.annotation.RequestMethod.POST, request, response);
        } else {
            model.addAttribute("registrationCommand", command);
            model.addAttribute("errors",result);
            return registrationGroovyControllerScript.handleRequest(model, org.springframework.web.bind.annotation.RequestMethod.GET, request, response);
        }
    }



}
