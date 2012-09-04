package org.openiam.webconsole.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Alexander Dukkardt
 * 
 */
@Configuration
@PropertySource("classpath:/META-INF/webconsole.properties")
public class WebConsoleProperties extends AbstractWebConsoleProperties {

}
