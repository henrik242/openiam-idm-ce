package org.eclipse.birt.report.listener;

import java.lang.reflect.Field;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContextEvent;

/**
 * HACK to overwrite web application init parameter "BIRT_VIEWER_WORKING_FOLDER"
 */
public class ViewerServletContextListenerExt extends ViewerServletContextListener {
    private static ResourceBundle res = ResourceBundle.getBundle("securityconf");
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            Field contextField = event.getServletContext().getClass().getDeclaredField("context");
            contextField.setAccessible(true);
            Object object = contextField.get(event.getServletContext());
            Field initParametersField = object.getClass().getDeclaredField("parameters");
            initParametersField.setAccessible(true);
            ConcurrentHashMap parameters = (ConcurrentHashMap)initParametersField.get(object);
            parameters.put("BIRT_VIEWER_WORKING_FOLDER", res.getString("reportRoot"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.contextInitialized(event);
    }
}
