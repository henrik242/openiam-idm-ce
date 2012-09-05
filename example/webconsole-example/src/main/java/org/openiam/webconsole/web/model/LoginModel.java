package org.openiam.webconsole.web.model;

import java.io.Serializable;

import org.openiam.idm.srvc.auth.dto.Subject;

/**
 * 
 * @author Alexander Duckardt <br/>
 *         Sep 5, 2012
 */
public class LoginModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String password;
    private String principal;
    private String domainId;
    private String pin;
    private String otp = null;
    private String resultCode;
    private Subject subject;
    private String po;
    private String clientIP;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    @Override
    public String toString() {
        return "LoginModel [password=" + password + ", principal=" + principal
                + ", domainId=" + domainId + ", pin=" + pin + ", otp=" + otp
                + ", resultCode=" + resultCode + ", subject=" + subject
                + ", po=" + po + ", clientIP=" + clientIP + "]";
    }

}
