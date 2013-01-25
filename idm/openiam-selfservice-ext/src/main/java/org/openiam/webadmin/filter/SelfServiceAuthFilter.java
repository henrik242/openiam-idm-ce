package org.openiam.webadmin.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
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
    private static ResourceBundle res = ResourceBundle.getBundle("securityconf");
    private String SELFSERVICE_BASE_URL = res.getString("SELFSERVICE_BASE_URL");
    private String SELFSERVICE_CONTEXT = res.getString("SELFSERVICE_CONTEXT");
    private String defaultLang = "en";

    private FilterConfig filterConfig = null;

    private UserDataWebService userServiceClient;
    private AuthenticationService authServiceClient;
    private LoginDataWebService loginServiceClient;

    private NavigatorDataWebService navServiceClient;

    private String expirePage;
    private String excludePath;
    private String publicLeftMenuGroup;
    private String publicRightMenuGroup1;
    private String publicRightMenuGroup2;
    private String publicRightMenuGroup3;
    private String leftMenuGroup;
    private String rightMenuGroup1;
    private String rightMenuGroup2;
    private String rightMenuGroup3;
    private String rootMenu;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.expirePage = filterConfig.getInitParameter("expirePage");
        this.excludePath = filterConfig.getInitParameter("excludePath");
        this.defaultLang = filterConfig.getInitParameter("defaultLang");
        this.rootMenu = filterConfig.getInitParameter("rootMenu");
        this.leftMenuGroup = filterConfig.getInitParameter("leftMenuGroup");
        this.rightMenuGroup1 = filterConfig.getInitParameter("rightMenuGroup1");
        this.rightMenuGroup2 = filterConfig.getInitParameter("rightMenuGroup2");
        this.rightMenuGroup3 = filterConfig.getInitParameter("rightMenuGroup3");
        this.publicLeftMenuGroup = filterConfig.getInitParameter("publicLeftMenuGroup");
        this.publicRightMenuGroup1 = filterConfig.getInitParameter("publicRightMenuGroup1");
        this.publicRightMenuGroup2 = filterConfig.getInitParameter("publicRightMenuGroup2");
        this.publicRightMenuGroup3 = filterConfig.getInitParameter("publicRightMenuGroup3");
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

        String backUrl = (String) session.getAttribute("token");
        if(StringUtils.isEmpty(backUrl)) {
            backUrl = servletRequest.getParameter("backUrl");
            if(StringUtils.isEmpty(backUrl)) {
                backUrl = SELFSERVICE_BASE_URL + "/" + SELFSERVICE_CONTEXT;
            }
            session.setAttribute("backUrl", backUrl);
        }

        if (url == null || url.equals("/") || url.endsWith("login.gsp") || isExcludeObject(url) || isPublicUrl(url)) {
            LOG.info("Pass through request for object");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        LOG.debug("Validating url: " + url);

        // validate the token. If the token is not valid then redirect to the login page
        // invalidate the session
        String token = (String) session.getAttribute("token");

        // if token was not found in Request parameters try to find in Cookies
        if(StringUtils.isEmpty(token)) {
            token = servletRequest.getParameter("tk");
            session.setAttribute("token", token);
        }

        String sessionUserId = (String) session.getAttribute("userId");

        if (StringUtils.isEmpty(sessionUserId) && StringUtils.isEmpty(token)) {
            // token is missing
            LOG.debug("token is null");
            response.sendRedirect(SELFSERVICE_BASE_URL+"/"+SELFSERVICE_CONTEXT+expirePage);
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
                if(StringUtils.isNotEmpty(decUserId)) {
                    session.setAttribute("userId", decUserId);

                    // get the menus that the user has permissions too
                    List<Menu> menuList = navServiceClient.menuGroupByUser(rootMenu, decUserId, "en").getMenuList();

                    session.setAttribute("permissions", menuList);

                    // user has been authentication - show the private menus
                    session.setAttribute("privateLeftMenuGroup",
                            navServiceClient.menuGroupSelectedByUser(leftMenuGroup, decUserId, defaultLang).getMenuList());
                    session.setAttribute("privateRightMenuGroup1",
                            navServiceClient.menuGroupSelectedByUser(rightMenuGroup1, decUserId, defaultLang).getMenuList());
                    session.setAttribute("privateRightMenuGroup2",
                            navServiceClient.menuGroupSelectedByUser(rightMenuGroup2, decUserId, defaultLang).getMenuList());

                    session.setAttribute("privateRightMenuGroup3",
                            navServiceClient.menuGroupSelectedByUser(rightMenuGroup3, decUserId, defaultLang).getMenuList());

                } else {
                    LOG.debug("Token validation failed...");
                    session.invalidate();
                    response.sendRedirect(SELFSERVICE_BASE_URL+"/"+SELFSERVICE_CONTEXT+expirePage);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.info("Token validation created exception failed");
            LOG.error(e);
            session.invalidate();
            response.sendRedirect(SELFSERVICE_BASE_URL+"/"+SELFSERVICE_CONTEXT+expirePage);
            return;
        }

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
            if(navServiceClient == null) {
                navServiceClient = (NavigatorDataWebService)webContext.getBean("navServiceClient");
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
