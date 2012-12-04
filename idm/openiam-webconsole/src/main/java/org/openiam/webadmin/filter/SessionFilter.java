package org.openiam.webadmin.filter;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    private List<String> urlList = new ArrayList<String>();

    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;

        // the expire page is the url of the page to display if the session has expired.
        this.expirePage = filterConfig.getInitParameter("expirePage");
        String urls = filterConfig.getInitParameter("exceptedServlets");

        this.urlList = new ArrayList<String>();
        for(String uri : urls.split(",")) {
            this.urlList.add(uri.trim());
        }
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        log.info("Webconsole SessionFilter...doFilter() called");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String redirectURL = request.getContextPath() + expirePage;
        String url = request.getRequestURI();
        String servletPath = request.getServletPath();

        boolean allowedRequest = false;
        log.info("Requested URL: " + url);

        if(urlList.contains(servletPath) || isStatic(url)) {
            allowedRequest = true;
        }

        HttpSession session = request.getSession(false);

        if (!allowedRequest) {
            log.info("Session object = " + session);

            if (null == session || StringUtils.isEmpty((String) session.getAttribute("userId"))) {
                response.sendRedirect(redirectURL);
                return;
            }
        }


        chain.doFilter(servletRequest, servletResponse);

        log.info("End of SessionFilter()");


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

    public boolean isStatic(String url) {
        return url.contains("images/") || url.contains(".css") || url.contains(".jpg") || url.contains(".gif");
    }


}