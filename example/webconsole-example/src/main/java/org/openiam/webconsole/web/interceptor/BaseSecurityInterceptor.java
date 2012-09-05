package org.openiam.webconsole.web.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openiam.webconsole.config.ErrorCode;
import org.openiam.webconsole.config.IWebConsoleProperties;
import org.openiam.webconsole.web.constant.CommonWebConstant;
import org.openiam.webconsole.web.constant.NotificationType;
import org.openiam.webconsole.web.model.NotificationModel;
import org.openiam.webconsole.web.util.CommonWebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class BaseSecurityInterceptor extends HandlerInterceptorAdapter {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private IWebConsoleProperties webConsoleProperties;

    /**
     * 
     */
    public BaseSecurityInterceptor(IWebConsoleProperties webConsoleProperties) {
        this.webConsoleProperties = webConsoleProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
     * (javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        log.debug("Security interceptor Handler");
        HttpSession session = request.getSession(true);
        Object userSession = session.getAttribute(CommonWebConstant.userSession
                .name());

        if (userSession == null || !CommonWebUtil.isSessionValid(request)) {
            List<NotificationModel> notns = (List<NotificationModel>) session
                    .getAttribute(CommonWebConstant.notifications.name());
            if (notns == null)
                notns = new ArrayList<NotificationModel>();
            notns.add(new NotificationModel(NotificationType.error,
                    CommonWebUtil.getMessage(request,
                            ErrorCode.SESSION_EXPIRED,
                            "Please authorize to access secure zone.")));
            session.setAttribute(CommonWebConstant.notifications.name(), notns);

            if ("GET".equals(request.getMethod())) {
                response.sendRedirect(webConsoleProperties.getContextRoot());
            } else
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
