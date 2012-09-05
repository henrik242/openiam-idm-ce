package org.openiam.webconsole.web.model;

import java.io.Serializable;
import java.util.EnumMap;

import org.openiam.webconsole.web.constant.CommonWebConstant;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class UserSessionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private EnumMap<CommonWebConstant, Object> attributes = new EnumMap<CommonWebConstant, Object>(
            CommonWebConstant.class);

    public void setAttribute(CommonWebConstant key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(CommonWebConstant key) {
        return attributes.get(key);
    }

    public Object popAttribute(CommonWebConstant key) {
        return attributes.remove(key);
    }

    public void clear() {
        attributes.clear();
    }

    public void setLogin(String login) {
        this.setAttribute(CommonWebConstant.login, login);
    }

    public String getLogin() {
        return (String) this.getAttribute(CommonWebConstant.login);
    }

    public void setUserId(String userId) {
        this.setAttribute(CommonWebConstant.userId, userId);
    }

    public String getUserId() {
        return (String) this.getAttribute(CommonWebConstant.userId);
    }

    public void setToken(String token) {
        this.setAttribute(CommonWebConstant.token, token);
    }

    public String getToken() {
        return (String) this.getAttribute(CommonWebConstant.token);
    }

    public void setDomainId(String domainId) {
        this.setAttribute(CommonWebConstant.domainId, domainId);
    }

    public String getDomainId() {
        return (String) this.getAttribute(CommonWebConstant.domainId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[attributes=");
        builder.append(attributes);
        builder.append("]");
        return builder.toString();
    }

}