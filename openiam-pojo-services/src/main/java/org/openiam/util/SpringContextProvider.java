package org.openiam.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextProvider implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = ctx;
    }

    public static Object getBean(final String beanName) {
        return ctx.getBean(beanName);
    }

    public static void autowire(final Object toAutowire) {
        if (toAutowire != null) {
            ctx.getAutowireCapableBeanFactory().autowireBean(toAutowire);
        }
    }

    @Override
    public void finalize() {
        ctx = null;
    }
}
