package org.openiam.webconsole.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.servlet.support.RequestContext;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class CommonWebUtil {

    public static boolean isSessionValid(HttpServletRequest request) {
        if (request.getRequestURI() != null) {
            return request.getRequestedSessionId() != null
                    && request.isRequestedSessionIdValid();
        }
        return false;
    }

    public static String getServerUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://")
                .append(request.getServerName());
        if (request.getServerPort() != 0 && request.getServerPort() != 80) {
            sb.append(":").append(request.getServerPort());
        }
        sb.append(request.getContextPath()).append("/");

        return sb.toString();
    }

    /**
     * @param request
     * @param key
     * @param object
     * @return
     */
    public static String getMessage(HttpServletRequest request, String key,
            String defaultMsg) {
        return getMessage(request, key, defaultMsg, null);
    }

    /**
     * @param request
     * @param key
     * @param defaultMsg
     * @param arguments
     * @return
     */
    public static String getMessage(HttpServletRequest request, String key,
            String defaultMsg, Object[] arguments) {
        RequestContext ctx = new RequestContext(request);
        return ctx.getMessage(key, arguments, defaultMsg);
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getUID(String... parameterList) {
        StringBuilder seed = new StringBuilder();
        seed.append(System.currentTimeMillis());
        seed.append(System.nanoTime());
        if (parameterList != null) {
            for (String parameter : parameterList)
                seed.append(parameter);
        }
        return DigestUtils.shaHex(seed.toString());
    }
}
