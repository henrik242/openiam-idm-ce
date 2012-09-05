package org.openiam.webconsole.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * User: Alexander Duckardt<br/>
 * Date: 8/25/12
 */
public abstract class AbstractWebConsoleProperties implements
        IWebConsoleProperties {
    @Autowired
    private Environment env;

    @Override
    public String getServerUrl() {
        return env.getProperty("base.server.url");
    }

    @Override
    public String getResourceServerUrl() {
        return env.getProperty("base.resourceServerUrl");
    }

    @Override
    public String getContextRoot() {
        return env.getProperty("frontend.contextRoot", "/");
    }

    @Override
    public int getNotificationDelay() {
        return env.getProperty("frontend.notificationDelay", Integer.class, 0);
    }
}
