package org.openiam.webconsole.config;

/**
 * @author Sergei Barinov
 * 
 */
public class ErrorCode {
    public static final String OK = "success";
    public static final String GENERIC_ERROR = "generic.error";
    public static final String SESSION_EXPIRED = "session.expired";
    public static final String FAILURE = "failure";
    public static final String PASSWORD_IS_REQUIRED = "error.password.required";
    public static final String PRINCIPAL_IS_REQUIRED = "error.principal.required";
    public static final String PRINCIPAL_IS_INVALID = "principal.invalid";
    public static final String INVALID_DOMAIN = "service.not.valid";
    public static final String INVALID_PASSWORD = "password.invalid";
    public static final String INVALID_USER_STATUS = "user.status.invalid";
    public static final String LOGIN_DISABLED = "login.disabled";
    public static final String LOGIN_LOCKED = "login.locked";
    public static final String PASSWORD_EXPIRED = "password.expired";
    public static final String SERVICE_NOT_FOUND = "service.not.found";
    public static final String INVALID_CONFIGURATION = "configuration.invalid";
}
