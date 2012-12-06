package org.openiam.idm.srvc.msg.service;

public enum MailTemplateParameters {
    TO("to"),
    BCC("bcc"),
    CC("cc"),
    FROM("from"),
    SUBJECT("subject"),
    USER_NAME("userName"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    COMPANY_NAME("companyName"),
    DEPT_NAME("deptName"),
    LOGIN_NAME("lginName"),
    PASSWORD("password"),
    EMAIL("email"),
    PHONE("phone"),
    IDENTITY("identity"),
    USER_ID("userId"),
    USER_IDS("userIds"),
    SERVICE_HOST("serviceHost"),
    SERVICE_CONTEXT("serviceContext"),
    REQUEST_ID("requestId"),
    REQUEST_REASON("requestReason"),
    REQUESTER("requester"),
    TARGET_USER("targetUser"),
    BASE_URL("baseUrl"),
    TOKEN("token");

    private final String value;

    private MailTemplateParameters(String value) {
        this.value = value;
    }
    public String value() {
        return this.value;
    }
}
