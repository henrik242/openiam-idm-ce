package org.openiam.webadmin.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.SSOToken;
import org.openiam.idm.srvc.auth.service.AuthenticationConstants;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * <p>
 * <code>SelfServiceAuthFilter</code> <font face="arial"> is a Filter which checks user
 * authentication. If the userId is in session, he/she has been authenticated.
 * If not authenticated, authentication is checked and then userId is set in session
 * If userId is not provided in the request object, control is passed to the
 * the login application and the Filter chain is terminated.
 * <p/>
 * <p/>
 * </font>
 * </p>
 */
public class SelfServiceAuthFilter implements javax.servlet.Filter {

    private static final Log LOG = LogFactory.getLog(SelfServiceAuthFilter.class);

    private FilterConfig filterConfig = null;

    private UserDataWebService userServiceClient;
    private AuthenticationService authServiceClient;
    private LoginDataWebService loginServiceClient;

    private String expirePage;
    private String excludePath;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.expirePage = filterConfig.getInitParameter("expirePage");
        this.excludePath = filterConfig.getInitParameter("excludePath");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        LOG.debug("SelfServeAuthFilter:doFilter");


        ServletContext context = getFilterConfig().getServletContext();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (request.getMethod().equalsIgnoreCase("POST")) {
            LOG.info("Post operation - pass through request");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String url = request.getRequestURI();
        LOG.debug("* Requested url=" + url);

        if (url == null || url.equals("/") || url.endsWith("login.gsp") || isExcludeObject(url) || isPublicUrl(url)) {
            LOG.info("Pass through request for object");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        LOG.debug("Validating url: " + url);

        // validate the token. If the token is not valid then redirect to the login page
        // invalidate the session
        String token = servletRequest.getParameter("tk");
        if(StringUtils.isEmpty(token)) {
            token = (String) session.getAttribute("token");
        }
        String userId = servletRequest.getParameter("userId");
        String principal = servletRequest.getParameter("lg");

        String sessionUserId = (String) session.getAttribute("userId");


        if (StringUtils.isEmpty(sessionUserId) && StringUtils.isEmpty(token)) {
            // token is missing
            LOG.debug("token is null");
            response.sendRedirect(expirePage);
            return;

        }

        // get the user in the token and make sure that user in the token is the same as the one in the session
        LOG.debug("Validating token");
        try {
            //spring services beans initialization if needed
            sprinBeansInitialization(context);

            String decString = (String) loginServiceClient.decryptPassword(token).getResponseValue();

            StringTokenizer tokenizer = new StringTokenizer(decString, ":");
            if (tokenizer.hasMoreTokens()) {
                String decUserId = tokenizer.nextToken();
                if (decUserId == null || decUserId.isEmpty()) {

                    LOG.debug("Token validation failed...");

                    session.invalidate();
                    response.sendRedirect(expirePage);
                    return;
                }
                if (principal == null || principal.isEmpty()) {
                    Login l = loginServiceClient.getPrimaryIdentity(decUserId).getPrincipal();
                    principal = l.getId().getLogin();
                    userId = decUserId;

                    session.setAttribute("userId", userId);
                    session.setAttribute("login", principal);
                }
            }
        } catch (Exception e) {
            LOG.info("Token validation created exception failed");
            LOG.error(e);
            session.invalidate();
            response.sendRedirect(expirePage);
            return;
        }

        // token is valid, but renew it for this request
        String ip = request.getRemoteHost();
        Response resp = authServiceClient.renewToken(principal, token, AuthenticationConstants.OPENIAM_TOKEN, ip);
        if (resp.getStatus() == ResponseStatus.FAILURE) {
            LOG.debug("Token renewal failed:" + userId + " - " + token);
            session.invalidate();
            response.sendRedirect(expirePage);
            return;

        }
        LOG.debug("Token renewed");

        SSOToken ssoToken = (SSOToken) resp.getResponseValue();
        String newToken = ssoToken.getToken();
        LOG.info("New token: " + userId + " - " + newToken);
        session.setAttribute("token", newToken);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void sprinBeansInitialization(ServletContext context) {
        if(authServiceClient == null || loginServiceClient == null || userServiceClient == null) {
// get the application context
            WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
            if(authServiceClient == null) {
                authServiceClient =  (AuthenticationService)webContext.getBean("authServiceClient");
            }
            if(loginServiceClient == null) {
                loginServiceClient =  (LoginDataWebService)webContext.getBean("loginServiceClient");
            }
            if(userServiceClient == null) {
                userServiceClient =  (UserDataWebService)webContext.getBean("userServiceClient");
            }
        }
    }

    public boolean isExcludeObject(String url) {
        return url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".css") || url.endsWith(".gif") || url.endsWith(".png");
    }

    public boolean isPublicUrl(String url) {
        return url.contains(excludePath);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }
}
