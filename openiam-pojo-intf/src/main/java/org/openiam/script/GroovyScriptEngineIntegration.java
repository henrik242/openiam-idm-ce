/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

package org.openiam.script;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author suneet
 */
public class GroovyScriptEngineIntegration implements ScriptIntegration {

    protected static final Log log = LogFactory.getLog(GroovyScriptEngineIntegration.class);

    static protected ResourceBundle res = ResourceBundle.getBundle("securityconf");
    static String[] roots = null;
    static GroovyScriptEngine gse = null;

    Binding binding = new Binding();

    public GroovyScriptEngineIntegration() {
    }

    static public void init() {
        if (gse == null) {
            String scriptPath = res.getString("scriptRoot");
            roots = new String[]{scriptPath};
            try {
                gse = new GroovyScriptEngine(roots);
            } catch (IOException io) {
                log.error("Could not instantiate GroovyScriptEngine", io);
            }
        }
    }

    @Override
    public Object execute(Map<String, Object> bindingMap, String scriptName) {
        init();

        try {
            if (bindingMap != null) {
                for (String key : bindingMap.keySet()) {
                    binding.setVariable(key, bindingMap.get(key));
                }
            }
            gse.run(scriptName, binding);
            return binding.getVariable("output");
        } catch (ScriptException se) {
            log.error("Could not run script " + scriptName, se);
        } catch (ResourceException re) {
            log.error("Resource problem for " + scriptName, re);
        }
        return null;
    }

    @Override
    public Object instantiateClass(Map<String, Object> bindingMap, String scriptName) throws IOException {
        log.info("instantiateClass called.");

        GroovyClassLoader gcl = new GroovyClassLoader();
        try {
            String scriptPath = res.getString("scriptRoot");
            String fullPath = scriptPath + scriptName;
            Class cl = gcl.parseClass(new File(fullPath));
            Object instance = cl.newInstance();

            if (bindingMap != null) {
                for (String key : bindingMap.keySet()) {
                    try {
                        Field field = cl.getField(key);
                        field.set(instance, bindingMap.get(key));
                    } catch (Exception e) {
                        log.info("Ignoring field " + key + " in script " + scriptName + ", error: " + e.toString());
                    }
                }
            }
            return instance;

        } catch (IllegalAccessException ia) {
            log.error("Access problems when instantiating class " + scriptName, ia);
        } catch (InstantiationException ia) {
            log.error("Instantiation problems when instantiating class " + scriptName, ia);
        }
        return null;
    }
}
