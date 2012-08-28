package org.openiam.webconsole.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class MainServiceController extends BaseServiceController {

    protected MainServiceController(Validator validator) {
        super(validator);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(HttpSession session,
                HttpServletRequest httpRequest,
                HttpServletResponse httpResponse, Model model) throws Exception {
        return "index";
    }

}
