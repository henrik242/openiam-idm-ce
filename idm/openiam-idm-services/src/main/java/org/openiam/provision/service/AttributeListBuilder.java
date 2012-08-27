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

/**
 *
 */
package org.openiam.provision.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.idm.srvc.auth.dto.Login;
import org.openiam.idm.srvc.auth.dto.LoginId;
import org.openiam.idm.srvc.mngsys.dto.AttributeMap;
import org.openiam.idm.srvc.policy.dto.Policy;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleUser;
import org.openiam.script.ScriptIntegration;
import org.openiam.base.BaseAttributeContainer;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Builds a list of attributes that are to be sent to the connectors.
 * This list can be generated from the groovy scripts that contain rules for provisioning or by sending
 * in the complete User object.
 *
 * @author suneet
 */
public class AttributeListBuilder {

    protected static final Log log = LogFactory.getLog(AttributeListBuilder.class);


    public ExtensibleUser buildFromRules(ProvisionUser pUser,
                                         List<AttributeMap> attrMap, ScriptIntegration se,
                                         String managedSysId, String domainId,
                                         Map<String, Object> bindingMap,
                                         String createdBy) {

        final ExtensibleUser extUser = new ExtensibleUser();


        if (attrMap != null) {

            if (log.isDebugEnabled()) {
                log.debug("buildFromRules: attrMap IS NOT null");
            }

            final Login identity = new Login();
            final LoginId loginId = new LoginId();

            // init values
            loginId.setDomainId(domainId);
            loginId.setManagedSysId(managedSysId);

            for (final AttributeMap attr : attrMap) {

                if (StringUtils.equalsIgnoreCase(attr.getStatus(), "IN-ACTIVE")) {
                    continue;
                }

                final Policy policy = attr.getAttributePolicy();
                final String url = policy.getRuleSrcUrl();
                if (url != null) {
                    Object output = se.execute(bindingMap, url);
                    if (output != null) {
                        final String objectType = attr.getMapForObjectType();
                        if (objectType != null) {
                            if (StringUtils.equalsIgnoreCase("PRINCIPAL", objectType)) {
                                if (log.isDebugEnabled()) {
                                    log.debug(String.format("buildFromRules: ManagedSysId=%s, login=%s", managedSysId, output));
                                }

                                loginId.setLogin((String) output);
                                extUser.setPrincipalFieldName(attr.getAttributeName());
                                extUser.setPrincipalFieldDataType(attr.getDataType());

                            }


                            if (StringUtils.equalsIgnoreCase(objectType, "USER") || StringUtils.equalsIgnoreCase(objectType, "PASSWORD")) {

                                if (log.isDebugEnabled()) {
                                    log.debug(String.format("buildFromRules: attribute: %s->%s", attr.getAttributeName(), output));
                                }

                                if (output instanceof String) {

                                    output = (StringUtils.isBlank((String) output)) ? attr.getDefaultValue() : output;
                                    extUser.getAttributes().add(new ExtensibleAttribute(attr.getAttributeName(), (String) output, 1, attr.getDataType()));

                                } else if (output instanceof Date) {
                                    final Date d = (Date) output;
                                    final String DATE_FORMAT = "MM/dd/yyyy";
                                    final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                                    extUser.getAttributes().add(new ExtensibleAttribute(attr.getAttributeName(), sdf.format(d), 1, attr.getDataType()));

                                } else if (output instanceof BaseAttributeContainer) {

                                    // process a complex object which can be passed to the connector

                                    ExtensibleAttribute newAttr = new ExtensibleAttribute(attr.getAttributeName(), (BaseAttributeContainer) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);
                                    extUser.getAttributes().add(newAttr);

                                } else {
                                    extUser.getAttributes().add(new ExtensibleAttribute(attr.getAttributeName(), (List) output, 1, attr.getDataType()));
                                }
                            }

                        }
                    }
                }
            }
            identity.setId(loginId);
            identity.setAuthFailCount(0);
            identity.setCreateDate(new Date(System.currentTimeMillis()));
            identity.setCreatedBy(createdBy);
            identity.setIsLocked(0);
            identity.setFirstTimeLogin(1);
            identity.setStatus("ACTIVE");
            if (pUser.getPrincipalList() == null) {
                List<Login> idList = new ArrayList<Login>();
                idList.add(identity);
                pUser.setPrincipalList(idList);
            } else {
                pUser.getPrincipalList().add(identity);
            }

        } else {
            log.debug("- attMap IS null");
        }

        // show the identities in the pUser object


        return extUser;


    }

    /**
     * Generate the principalName for a targetSystem
     *
     * @return
     */
    public String buildPrincipalName(List<AttributeMap> attrMap, ScriptIntegration se,
                                     Map<String, Object> bindingMap) {
        for (AttributeMap attr : attrMap) {
            Policy policy = attr.getAttributePolicy();
            String url = policy.getRuleSrcUrl();
            String objectType = attr.getMapForObjectType();
            if (objectType != null) {
                if (objectType.equalsIgnoreCase("PRINCIPAL")) {
                    if (url != null) {
                        return (String) se.execute(bindingMap, url);
                    }
                }
            }


        }
        return null;
    }

    public Login buildIdentity(List<AttributeMap> attrMap, ScriptIntegration se,
                               String managedSysId, String domainId,
                               Map<String, Object> bindingMap,
                               String createdBy) {

        Login newIdentity = new Login();
        LoginId newId = new LoginId();

        for (AttributeMap attr : attrMap) {
            Policy policy = attr.getAttributePolicy();
            String url = policy.getRuleSrcUrl();
            String objectType = attr.getMapForObjectType();
            if (objectType != null) {
                if (objectType.equalsIgnoreCase("PRINCIPAL")) {
                    if (url != null) {
                        String output = (String) se.execute(bindingMap, url);
                        newId.setLogin(output);
                    }
                }
                if (objectType.equalsIgnoreCase("PASSWORD")) {
                    if (url != null) {
                        String output = (String) se.execute(bindingMap, url);
                        newIdentity.setPassword(output);
                    }

                }
            }


        }
        if (newId.getLogin() == null) {
            return null;
        }
        newId.setDomainId(domainId);
        newId.setManagedSysId(managedSysId);
        newIdentity.setId(newId);
        newIdentity.setAuthFailCount(0);
        newIdentity.setCreateDate(new Date(System.currentTimeMillis()));
        newIdentity.setFirstTimeLogin(0);
        newIdentity.setIsLocked(0);
        newIdentity.setStatus("ACTIVE");
        newIdentity.setCreatedBy(createdBy);
        return newIdentity;

    }


    public ExtensibleUser buildModifyFromRules(ProvisionUser pUser,
                                               Login currentIdentity,
                                               List<AttributeMap> attrMap, ScriptIntegration se,
                                               String managedSysId, String domainId,
                                               Map<String, Object> bindingMap,
                                               String createdBy) {

        ExtensibleUser extUser = new ExtensibleUser();


        if (attrMap != null) {

            log.debug("buildModifyFromRules: attrMap IS NOT null");


            for (AttributeMap attr : attrMap) {

                if ("IN-ACTIVE".equalsIgnoreCase(attr.getStatus())) {
                    continue;
                }

                Policy policy = attr.getAttributePolicy();
                String url = policy.getRuleSrcUrl();
                if (url != null) {
                    Object output = se.execute(bindingMap, url);
                    if (output != null) {
                        String objectType = attr.getMapForObjectType();
                        if (objectType != null) {

                            log.debug("buildModifyFromRules: OBJECTTYPE=" + objectType + " SCRIPT OUTPUT=" + output + " attribute name=" + attr.getAttributeName());

                            if (objectType.equalsIgnoreCase("USER") || objectType.equalsIgnoreCase("PASSWORD")) {

                                ExtensibleAttribute newAttr;
                                if (output instanceof String) {

                                    // if its memberOf object than dont add it to the list
                                    // the connectors can detect a delete if an attribute is not in the list

                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), (String) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);
                                    extUser.getAttributes().add(newAttr);


                                } else if (output instanceof Date) {
                                    // date
                                    Date d = (Date) output;
                                    String DATE_FORMAT = "MM/dd/yyyy";
                                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), sdf.format(d), 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);

                                    extUser.getAttributes().add(newAttr);
                                } else if (output instanceof BaseAttributeContainer) {
                                   // process a complex object which can be passed to the connector
                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), (BaseAttributeContainer) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);
                                    extUser.getAttributes().add(newAttr);

                                } else {
                                    // process a list - multi-valued object
                                    newAttr = new ExtensibleAttribute(attr.getAttributeName(), (List) output, 1, attr.getDataType());
                                    newAttr.setObjectType(objectType);

                                    extUser.getAttributes().add(newAttr);

                                    log.debug("buildModifyFromRules: added attribute to extUser:" + attr.getAttributeName());
                                }

                            } else if (objectType.equalsIgnoreCase("PRINCIPAL")) {

                                extUser.setPrincipalFieldName(attr.getAttributeName());
                                extUser.setPrincipalFieldDataType(attr.getDataType());

                            }
                        }
                    }
                }
            }

            if (pUser.getPrincipalList() == null) {
                List<Login> principalList = new ArrayList<Login>();
                principalList.add(currentIdentity);
                pUser.setPrincipalList(principalList);
            } else {
                pUser.getPrincipalList().add(currentIdentity);
            }

        }


        return extUser;


    }

}
