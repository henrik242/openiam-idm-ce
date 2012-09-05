package org.openiam.webconsole.web.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openiam.webconsole.web.constant.CommonWebConstant;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class BaseCleanNotificationInterceptor extends HandlerInterceptorAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
     * (javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws ServletException {
        toggleNotification(request, CommonWebConstant.notifications.name());
        return true;
    }

    /**
     * @param request
     * @param name
     */
    private void toggleNotification(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(true);
        Object val = session.getAttribute(name);
        if (val != null) {
            request.setAttribute(name, val);
            session.removeAttribute(name);
        }
    }

}
