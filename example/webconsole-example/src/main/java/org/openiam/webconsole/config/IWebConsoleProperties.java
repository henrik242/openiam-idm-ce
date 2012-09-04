/**
 * 
 */
package org.openiam.webconsole.config;

/**
 * @author Alexander Duckardt
 * 
 */
public interface IWebConsoleProperties {

    public String getServerUrl();

    public String getResourceServerUrl();

    String getContextRoot();

    public int getNotificationDelay();
}
