package org.openiam.webconsole.web.model;

import java.io.Serializable;

import org.openiam.webconsole.web.constant.NotificationType;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class NotificationModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private String text;
    private String elementId;
    private int delay = 0;
    private boolean isClosable = false;

    /**
     * @param type
     * @param text
     */
    public NotificationModel(NotificationType type, String text) {
        this(type, text, null);
    }

    public NotificationModel(NotificationType type, String text,
            String elementId) {
        this(type, text, elementId, 0);
    }

    public NotificationModel(NotificationType type, String text,
            String elementId, int delay) {
        this(type, text, elementId, delay, false);
    }

    public NotificationModel(NotificationType type, String text,
            String elementId, boolean isClosable) {
        this(type, text, elementId, 0, isClosable);
    }

    public NotificationModel(NotificationType type, String text,
            String elementId, int delay, boolean isClosable) {
        this.type = type.name();
        this.text = text;
        this.elementId = elementId;
        this.delay = delay;
        this.isClosable = isClosable;
    }

    /**
     * @return the elementId
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * @param elementId
     *            the elementId to set
     */
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * @return the isClosable
     */
    public boolean isClosable() {
        return isClosable;
    }

    /**
     * @param isClosable
     *            the isClosable to set
     */
    public void setIsClosable(boolean isClosable) {
        this.isClosable = isClosable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NotificationModel [type=").append(type)
                .append(", text=").append(text).append(", elementId=")
                .append(elementId).append(", delay=").append(delay)
                .append(", isClosable=").append(isClosable).append("]");
        return builder.toString();
    }

}
