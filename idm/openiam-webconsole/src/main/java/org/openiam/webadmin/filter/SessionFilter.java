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
    private String excludePath = null;
    private static final Log log = LogFactory.getLog(SessionFilter.class);


    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;

        // the expire page is the url of the page to display if the session has expired.
        this.expirePage = filterConfig.getInitParameter("expirePage");
        excludePath = filterConfig.getInitParameter("excludePath");


    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        String userId = null;


        ServletContext context = getFilterConfig().getServletContext();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        boolean loginPage = false;


        String url = request.getRequestURI();


        if (url == null || url.equals("/") || url.endsWith("index.do")
                || url.endsWith("login.cnt") || url.endsWith("index.jsp")) {
           loginPage = true;
           // chain.doFilter(servletRequest, servletResponse);
           // return;

            //boolean isJsp = url.endsWith(".jsp");
        }

        if (!loginPage && isCode(url) && !isExcludePath(url)) {
            if (session == null || session.isNew()) {
                response.sendRedirect(request.getContextPath() + expirePage);
                return;
            } else {
                

                if (session.getAttribute("userId") == null || ((String) session.getAttribute("userId")).isEmpty()) {
                    response.sendRedirect(request.getContextPath() + expirePage);
                    return;
                }

            }

        }



        chain.doFilter(servletRequest, servletResponse);


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

    public boolean isExcludePath(String url) {

        if (url == null) {
            return false;
        }

        if (url.contains(excludePath)) {
            System.out.println("exclude found..");
            return true;
        }
        return false;
    }
}