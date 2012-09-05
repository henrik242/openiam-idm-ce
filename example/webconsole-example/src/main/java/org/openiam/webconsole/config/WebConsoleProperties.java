package org.openiam.webconsole.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * User: Alexander Duckardt<br/>
 * Date: 8/25/12
 */
@Configuration
@PropertySource("classpath:/META-INF/webconsole.properties")
public class WebConsoleProperties extends AbstractWebConsoleProperties {

}
