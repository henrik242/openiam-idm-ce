package org.openiam.webconsole.web.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openiam.webconsole.config.ErrorCode;
import org.openiam.webconsole.web.constant.NotificationType;
import org.openiam.webconsole.web.model.NotificationModel;

/**
 * @author Alexander Duckardt
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class CommonWebResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String errorCode = ErrorCode.OK;
    private T value;
    private LinkedHashSet<NotificationModel> notifications;
    private String url;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return the notifications
     */
    public LinkedHashSet<NotificationModel> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications
     *            the notifications to set
     */
    public void setNotifications(LinkedHashSet<NotificationModel> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param error
     * @param message
     */
    public void addNotification(NotificationType error, String message) {
        addNotification(error, message, null);
    }

    public void addNotification(NotificationType error, String message,
            String elementId) {
        if (notifications == null)
            notifications = new LinkedHashSet<NotificationModel>();
        notifications.add(new NotificationModel(error, message, elementId));
    }

    public void addNotification(NotificationType error, String message,
            String elementId, boolean isStick) {
        if (notifications == null)
            notifications = new LinkedHashSet<NotificationModel>();
        notifications.add(new NotificationModel(error, message, elementId,
                isStick));
    }

    public void addNotification(NotificationType error, String message,
            String elementId, int delay) {
        if (notifications == null)
            notifications = new LinkedHashSet<NotificationModel>();
        notifications.add(new NotificationModel(error, message, elementId,
                delay));
    }

    public void addNotification(NotificationType error, String message,
            String elementId, int delay, boolean isClosable) {
        if (notifications == null)
            notifications = new LinkedHashSet<NotificationModel>();
        notifications.add(new NotificationModel(error, message, elementId,
                delay, isClosable));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        StringBuilder builder = new StringBuilder();
        builder.append("CommonWebResponse [notifications=")
                .append(notifications != null ? toString(notifications, maxLen)
                        : null)
                .append(", url=")
                .append(url)
                .append(", errorCode=")
                .append(this.getErrorCode())
                .append(", value=")
                .append(this.getValue() == null ? "null" : this.getValue()
                        .toString()).append("]");
        return builder.toString();
    }

    private String toString(Collection<?> collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
                && i < maxLen; i++) {
            if (i > 0)
                builder.append(", ");
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

}
