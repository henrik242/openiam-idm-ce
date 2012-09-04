package org.openiam.webconsole.config;

/**
 * @author Sergei Barinov
 * 
 */
public enum ErrorCode {
    OK("success"),
    GENERIC_ERROR("generic.error"),
    SESSION_EXPIRED("session.expired");

    private String labelCode;

    private ErrorCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getLabelCode() {
        return labelCode;
    }

}
