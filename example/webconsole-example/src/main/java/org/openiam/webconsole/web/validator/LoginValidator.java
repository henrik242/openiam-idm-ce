package org.openiam.webconsole.web.validator;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.auth.dto.AuthenticationRequest;
import org.openiam.idm.srvc.auth.service.AuthenticationConstants;
import org.openiam.idm.srvc.auth.service.AuthenticationService;
import org.openiam.idm.srvc.auth.ws.AuthenticationResponse;
import org.openiam.webconsole.config.ErrorCode;
import org.openiam.webconsole.web.model.LoginModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {
    private static final String securityDomain = "IDM";
    @Autowired
    protected AuthenticationService authenticationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(LoginModel.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginModel loginModel = (LoginModel) target;

        if (!StringUtils.hasText(loginModel.getPrincipal()))
            errors.rejectValue("principal", ErrorCode.PRINCIPAL_IS_REQUIRED,
                    ErrorCode.PRINCIPAL_IS_REQUIRED);

        if (!StringUtils.hasText(loginModel.getPassword()))
            errors.rejectValue("password", ErrorCode.PASSWORD_IS_REQUIRED,
                    ErrorCode.PASSWORD_IS_REQUIRED);
        if (errors.hasErrors())
            return;

        String login = null;
        if (StringUtils.hasText(loginModel.getPo())) {
            login = loginModel.getPo() + "-" + loginModel.getPrincipal();
        } else {
            login = loginModel.getPrincipal();
        }

        String nodeIP = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            nodeIP = addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // authenticate the user
        AuthenticationRequest authReq = new AuthenticationRequest(
                securityDomain, login.trim(), loginModel.getPassword(),
                loginModel.getClientIP(), nodeIP);
        AuthenticationResponse resp = authenticationService.login(authReq);

        if (resp.getStatus() == ResponseStatus.SUCCESS) {
            loginModel.setSubject(resp.getSubject());
        } else {
            // error in authentication
            int errCode = resp.getAuthErrorCode();
            String errorCode = ErrorCode.PRINCIPAL_IS_INVALID;
            switch (errCode) {
            case AuthenticationConstants.RESULT_INVALID_DOMAIN:
                errorCode = ErrorCode.INVALID_DOMAIN;
                break;
            case AuthenticationConstants.RESULT_INVALID_LOGIN:
                errorCode = ErrorCode.PRINCIPAL_IS_INVALID;
                break;
            case AuthenticationConstants.RESULT_INVALID_PASSWORD:
                errorCode = ErrorCode.INVALID_PASSWORD;
                break;
            case AuthenticationConstants.RESULT_INVALID_USER_STATUS:
                errorCode = ErrorCode.INVALID_USER_STATUS;
                break;
            case AuthenticationConstants.RESULT_LOGIN_DISABLED:
                errorCode = ErrorCode.LOGIN_DISABLED;
                break;
            case AuthenticationConstants.RESULT_LOGIN_LOCKED:
                errorCode = ErrorCode.LOGIN_LOCKED;
                break;
            case AuthenticationConstants.RESULT_PASSWORD_EXPIRED:
                errorCode = ErrorCode.PASSWORD_EXPIRED;
                break;
            case AuthenticationConstants.RESULT_SERVICE_NOT_FOUND:
                errorCode = ErrorCode.SERVICE_NOT_FOUND;
                break;
            case AuthenticationConstants.RESULT_INVALID_CONFIGURATION:
                errorCode = ErrorCode.INVALID_CONFIGURATION;
                break;
            }
            errors.rejectValue("principal", errorCode, errorCode);
        }
    }
}
