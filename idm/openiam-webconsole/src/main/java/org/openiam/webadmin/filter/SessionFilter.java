package org.openiam.webadmin.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * <p>
 * <code>SessionFilter</code> <font face="arial"> is a Filter that session for a users session expiring.
 * If it has expired, then it gracefully displays a session expiration message.
 * <p/>
 * </font>
 * </p>
 */

public class SessionFilter implements javax.servlet.Filter {

    private FilterConfig filterConfig = null;
    private String expirePage = null;
    private static final Log log = LogFactory.getLog(SessionFilter.class);


    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;

        // the expire page is the url of the page to display if the session has expired.
        this.expirePage = filterConfig.getInitParameter("expirePage");



    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        log.info("SessionFilter()...start");




        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String redirectURL = request.getContextPath() + expirePage;

        log.info("Redirect URL :" + redirectURL);

        HttpSession session = request.getSession(false);
        boolean loginPage = false;


        String url = request.getRequestURI();

        log.info("Requested URL: " + url);


        if (url == null || url.equals("/") || url.endsWith("index.do")
                || url.endsWith("login.cnt") || url.endsWith("index.jsp")) {
           loginPage = true;

        }


        String userId;

        if (!loginPage && isCode(url) ) {

            log.info("Session object = " + session);

            if (session == null) {
                log.info("Session object is null. Redirecting to the login page");
                response.sendRedirect(redirectURL);
                return;
            }


            log.info("Checking session. isNew?" + session.isNew());


           if (  session.getAttribute("userId") != null) {

               userId = (String)session.getAttribute("userId");

               log.info(" - UserId : " + userId);

               if ( userId == null ||  userId.isEmpty()) {

                   log.info("User ID is null. Redirect to login page");

                    response.sendRedirect(redirectURL);
                    return;
               }
           }else {

               log.info("Redirect to login page");

               response.sendRedirect(redirectURL);
               return;
           }


        }



        chain.doFilter(servletRequest, servletResponse);

        log.info("SessionFilter()...end");


    }

    public javax.servlet.FilterConfig getFilterConfig() {
        return filterConfig;
    }

    // in older version instead of init() setFilterConfig is being called
    public void setFilterConfig(javax.servlet.FilterConfig f) {
        this.filterConfig = f;
        try {
            this.init(this.filterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCode(String url) {

        if (url.contains(".jsp") || url.contains(".do") || url.contains(".report") || url.contains(".cnt")) {
            return true;
        }
        return false;
    }


}