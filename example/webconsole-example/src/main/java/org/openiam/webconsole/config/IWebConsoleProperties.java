/**
 * 
 */
package org.openiam.webconsole.config;

/**
 * User: Alexander Duckardt<br/>
 * Date: 8/25/12
 */
public interface IWebConsoleProperties {

    public String getServerUrl();

    public String getResourceServerUrl();

    String getContextRoot();

    public int getNotificationDelay();
}
