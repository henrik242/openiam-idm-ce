package org.openiam.webconsole.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openiam.webconsole.config.ErrorCode;
import org.openiam.webconsole.config.IWebConsoleProperties;
import org.openiam.webconsole.web.constant.CommonWebConstant;
import org.openiam.webconsole.web.constant.NotificationType;
import org.openiam.webconsole.web.dto.CommonWebResponse;
import org.openiam.webconsole.web.model.NotificationModel;
import org.openiam.webconsole.web.util.CommonWebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseServiceController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ObjectMapper mapper;
    protected Validator validator;

    @Autowired
    protected IWebConsoleProperties webConsoleProperties;

    /**
     * @param validator
     */
    @Autowired
    protected BaseServiceController(Validator validator) {
        this.validator = validator;
    }

    @ModelAttribute("resourceServerUrl")
    public String getResourceUrl() {
        String val = webConsoleProperties.getResourceServerUrl();
        return val;
    }

    @ModelAttribute("contextRoot")
    public String getContextRoot() {
        return webConsoleProperties.getContextRoot();
    }

    @ModelAttribute("serverUrl")
    public String getServerUrl() {
        return webConsoleProperties.getServerUrl();
    }

    protected void catchGetException(HttpSession session,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Exception ex) throws IOException {
        log.error(ex.getMessage(), ex);
        session.setAttribute("exception", ex);
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/exception");
    }

    public <T> Map<String, T> getModelMap(T responseModel) {
        log.info("Return response Model : {}",
                (responseModel != null ? responseModel.toString() : "null"));
        return responseModel != null ? Collections.singletonMap(
                CommonWebConstant.response.name(), responseModel) : null;
    }

    protected String getMessage(HttpServletRequest request, String key) {
        return CommonWebUtil.getMessage(request, key, null);
    }

    protected String getMessage(HttpServletRequest request, String key,
            String defaultMsg) {
        return CommonWebUtil.getMessage(request, key, defaultMsg, null);
    }

    protected String getMessage(HttpServletRequest request, String key,
            String defaultMsg, Object[] arguments) {
        return CommonWebUtil.getMessage(request, key, defaultMsg, arguments);

    }

    protected void addNotification(NotificationType type, String messageCode,
            HttpServletRequest request) {
        addNotification(type, messageCode, request, null);
    }

    protected void addNotification(NotificationType type, String messageCode,
            HttpServletRequest request, String elementId) {
        addNotification(
                new NotificationModel(type, getMessage(request, messageCode,
                        messageCode), elementId,
                        webConsoleProperties.getNotificationDelay()), request);
    }

    @SuppressWarnings("unchecked")
    protected void addNotification(NotificationModel notification,
            HttpServletRequest request) {
        LinkedHashSet<NotificationModel> notifications = (LinkedHashSet<NotificationModel>) request
                .getAttribute(CommonWebConstant.notifications.name());
        if (notifications == null)
            notifications = new LinkedHashSet<NotificationModel>();
        notifications.add(notification);
        request.setAttribute(CommonWebConstant.notifications.name(),
                notifications);
    }

    protected void addErrorNotification(String errorCode,
            HttpServletRequest request) {
        addNotification(new NotificationModel(NotificationType.error,
                getMessage(request, errorCode, ErrorCode.GENERIC_ERROR), null,
                webConsoleProperties.getNotificationDelay()), request);
    }

    protected void addSuccessNotification(String successCode,
            HttpServletRequest request) {
        addNotification(new NotificationModel(NotificationType.success,
                getMessage(request, successCode, ErrorCode.OK), null,
                webConsoleProperties.getNotificationDelay()), request);
    }

    // protected <T> void parseValidationResult(HttpServletRequest request,
    // BindingResult result, CommonWebResponse<T> responseModel) {
    // if (result.hasErrors()) {
    // responseModel.setErrorCode(ErrorCode.FAILURE);
    // for (FieldError error : result.getFieldErrors()) {
    // log.info("Validation error: " + error.toString());
    // responseModel.addNotification(NotificationType.error,
    // getMessage(request, error.getDefaultMessage()),
    // error.getField());
    // }
    // }
    // }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    CommonWebResponse<String> handleAjaxValidationError(
            MethodArgumentNotValidException e, HttpServletRequest request,
            HttpServletResponse response) {
        CommonWebResponse<String> responseModel = new CommonWebResponse<String>();
        if (e.getBindingResult().hasErrors()) {
            responseModel.setErrorCode(ErrorCode.FAILURE);
            for (FieldError error : e.getBindingResult().getFieldErrors()) {
                log.info("Validation error: " + error.toString());
                responseModel.addNotification(NotificationType.error,
                        getMessage(request, error.getDefaultMessage()),
                        error.getField());
            }
        }
        return responseModel;
    }

    // protected <T> boolean validateModel(T requestModel,
    // HttpServletRequest request,
    // CommonWebResponse<? extends Object> responseModel) {
    // return validateModel(requestModel, request, responseModel, null);
    //
    // }
    //
    // protected <T> boolean validateModel(T requestModel,
    // HttpServletRequest request,
    // CommonWebResponse<? extends Object> responseModel, String idElement) {
    // log.info("Perform Model validation for request model: "
    // + requestModel.toString());
    // Set<ConstraintViolation<T>> result = validator.validate(requestModel);
    // if (!result.isEmpty()) {
    // for (ConstraintViolation<T> error : result) {
    // log.info("Validation error: " + error.toString());
    // responseModel.addNotification(NotificationType.error,
    // getMessage(request, error.getMessage()), idElement);
    // }
    // return true;
    // }
    // return false;
    // }

    public void writeResponse(HttpServletResponse response,
            Object responseModel, String callbackFunction)
            throws JsonGenerationException, JsonMappingException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(callbackFunction).append("(")
                .append(mapper.writeValueAsString(responseModel)).append(")");
        response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
    }

    protected void catchException(CommonWebResponse<String> response,
            Exception ex) throws IOException {
        log.error(ex.getMessage(), ex);
        LinkedHashSet<NotificationModel> notifications = new LinkedHashSet<NotificationModel>();
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getClass().getSimpleName()).append(" ")
                .append(ex.getMessage());
        notifications.add(new NotificationModel(NotificationType.error, sb
                .toString()));
        response.setNotifications(notifications);
        response.setErrorCode(ErrorCode.GENERIC_ERROR);
    }

    protected String getRealPath(HttpServletRequest httpRequest, String path) {
        return httpRequest.getServletContext().getRealPath(path)
                + File.separator;
    }

}
