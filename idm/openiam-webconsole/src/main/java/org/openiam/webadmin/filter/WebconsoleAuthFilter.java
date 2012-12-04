package org.openiam.webadmin.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.SSOToken;
import org.openiam.idm.srvc.auth.service.AuthenticationConstants;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
//import org.openiam.selfsrvc.util.*;


/**
 * <p>
 * <code>SelfServeAuthFilter</code> <font face="arial"> is a Filter which checks user
 * authentication. If the userId is in session, he/she has been authenticated.
 * If not authenticated, authentication is checked and then userId is set in session
 * If userId is not provided in the request object, control is passed to the
 * the login application and the Filter chain is terminated.
 * <p/>
 * <p/>
 * </font>
 * </p>
 */

public class WebconsoleAuthFilter implements Filter {

    private FilterConfig filterConfig = null;
    private String serviceId = null;

    private String expirePage = null;
    private String excludePath = null;
    private String rootMenu = null;


    protected AuthenticationService authService = null;
    protected LoginDataWebService loginDataWebService = null;
    protected NavigatorDataWebService navigationDataService = null;
    protected UserDataWebService userMgr;

    private static final Log log = LogFactory.getLog(WebconsoleAuthFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;

        // the expire page is the url of the page to display if the session has expired.
        this.expirePage = filterConfig.getInitParameter("expirePage");
    }


    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * SSO into webconsole if a valid token is passed in.
     * NO token validation if a token is not present. In the future, all requests should have token validation
     *
     * @param servletRequest
     * @param servletResponse
     * @param chain
     * @throws IOException
     * @throws ServletException
     */


    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        ServletContext context = getFilterConfig().getServletContext();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("WebconsoleAuthFilter()...doFilter start");

        if("POST".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // get the application context
        WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
        authService = (AuthenticationService) webContext.getBean("authServiceClient");
        loginDataWebService = (LoginDataWebService) webContext.getBean("loginServiceClient");
        navigationDataService = (NavigatorDataWebService) webContext.getBean("navServiceClient");
        userMgr = (UserDataWebService) webContext.getBean("userServiceClient");

        String userId = null;
        String principal = null;

        HttpSession session = request.getSession();

        String url = request.getRequestURI();

        if ( isExcludeObject(url) ) {

            log.info("WebconsoleAuthFilter()...Exclude object found. Pass through");

            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // validate the token. If the token is not valid then redirect to the login page
        // invalidate the session





        String token = servletRequest.getParameter("tk");


        if (token == null || token.length() == 0) {

            chain.doFilter(servletRequest, servletResponse);
            return;

        }

        // get the user in the token and make sure that user in the token is the same as the one in the session
        //if (sessionUserId != null && sessionUserId.length() > 0) {

        try {

            Response resp = authService.renewToken(principal, token, AuthenticationConstants.OPENIAM_TOKEN);
            if (resp.getStatus() == ResponseStatus.FAILURE) {
                log.info("Token renewal failed:" + userId + " - " + token);
                session.invalidate();
                response.sendRedirect(request.getContextPath() + expirePage);
                return;

            }
            SSOToken ssoToken = (SSOToken) resp.getResponseValue();
            String newToken = ssoToken.getToken();
            session.setAttribute("token", newToken);

            String decString = (String) loginDataWebService.decryptPassword(token).getResponseValue();

            log.info("Extracting user information from token");

            StringTokenizer tokenizer = new StringTokenizer(decString, ":");
            if (tokenizer.hasMoreTokens()) {
                String decUserId = tokenizer.nextToken();

                Login l = loginDataWebService.getPrimaryIdentity(decUserId).getPrincipal();
                principal = l.getId().getLogin();
                userId = decUserId;

                session.setAttribute("userId", userId);
                session.setAttribute("login", principal);

                User usr = userMgr.getUserWithDependent(userId, true).getUser();
                session.setAttribute("userObj", usr);

                List<Menu> menuList = navigationDataService.menuGroupByUser(rootMenu, userId, "en").getMenuList();
                session.setAttribute("permissions", menuList);

            } else {

                /* expired  token */
                log.info("Invalid token - session has expired.");

                session.invalidate();
                response.sendRedirect(request.getContextPath() + expirePage);
                return;

            }


        } catch (Exception e) {

            e.printStackTrace();

            log.error(e);
            session.invalidate();
            response.sendRedirect(request.getContextPath() + expirePage);
            return;
        }
        //}

        chain.doFilter(servletRequest, servletResponse);

        log.info("WebconsoleAuthFilter()...doFilter end");


    }

    // avoid the warning message that tomcat throws when a parameter does not exist
    public String getParam(ServletRequest servletRequest, String param) {
        Enumeration names  =  servletRequest.getParameterNames();

        while ( names.hasMoreElements() ) {
            String e =  (String)names.nextElement();
            if (e.equalsIgnoreCase(param)) {
                return servletRequest.getParameter(param);
                
            }
            
            
        }
        return null;
        
        
        
    }


    public boolean isExcludeObject(String url) {
        return url.endsWith(".jpg") || url.endsWith(".css") || url.endsWith(".gif");
    }

    public boolean isPublicUrl(String url) {
        return url.contains(excludePath);
    }

    // Not in Filter interface, but weblogic is asking for these two methods
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    // in older version instead of init() setFilterConfig is being called
    public void setFilterConfig(FilterConfig f) {
        this.filterConfig = f;
        try {
            this.init(this.filterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}