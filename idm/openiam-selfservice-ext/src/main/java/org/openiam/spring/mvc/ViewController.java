package org.openiam.spring.mvc;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewController {
    String handleRequest(Model model, org.springframework.web.bind.annotation.RequestMethod method, HttpServletRequest request, HttpServletResponse response);
}
