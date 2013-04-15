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
package org.openiam.idm.srvc.synch.srcadapter;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.base.id.UUIDGen;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.service.MatchObjectRule;
import org.openiam.idm.srvc.synch.service.TransformScript;
import org.openiam.idm.srvc.synch.service.ValidationScript;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Reads a CSV file for use during the synchronization process
 *
 * @author suneet
 */
public class CSVAdapter extends AbstractSrcAdapter {

    private AuditHelper auditHelper;

    private String systemAccount;

    private MatchRuleFactory matchRuleFactory;

    private static final Log log = LogFactory.getLog(CSVAdapter.class);

    private static final int THREAD_COUNT = 5;

    public SyncResponse startSynch(final SynchConfig config) {

        log.debug("CSV startSynch CALLED.^^^^^^^^");


        Reader reader = null;

        final ProvisionService provService = (ProvisionService) ac.getBean("defaultProvision");

        String requestId = UUIDGen.getUUID();

        IdmAuditLog synchStartLog_ = new IdmAuditLog();
        synchStartLog_.setSynchAttributes("SYNCH_USER", config.getSynchConfigId(), "START", "SYSTEM", requestId);
        final IdmAuditLog synchStartLog = auditHelper.logEvent(synchStartLog_);

        try {
            File file = new File(config.getFileName());
            reader = new FileReader(file);
            CSVParser parser = new CSVParser(reader, CSVStrategy.EXCEL_STRATEGY);


            String[][] rows = parser.getAllValues();

            //initialization if validation script config exists
            final ValidationScript validationScript = StringUtils.isNotEmpty(config.getValidationRule()) ? SynchScriptFactory.createValidationScript(config.getValidationRule()) : null;
            //initialization if transformation script config exists
            final TransformScript transformScript = StringUtils.isNotEmpty(config.getTransformationRule()) ? SynchScriptFactory.createTransformationScript(config.getTransformationRule()) : null;
            //init match rules
            final MatchObjectRule matchRule = matchRuleFactory.create(config);
            //Get Header
            final LineObject rowHeader = populateTemplate(rows[0]);
            rows = Arrays.copyOfRange(rows,1,rows.length);
            // Multithreading
            int allRowsCount = rows.length;
            int threadCoount = THREAD_COUNT;
            int rowsInOneExecutors =  allRowsCount / threadCoount;
            int remains =allRowsCount % (rowsInOneExecutors * threadCoount);
            if (remains != 0) {
                threadCoount++;
            }
            log.debug("Thread count = " + threadCoount + "; Rows in one thread = " + rowsInOneExecutors + "; Remains rows = " + remains);

            List<Future> results = new LinkedList<Future>();
            ExecutorService service = Executors.newCachedThreadPool();
            for(int i = 0; i < threadCoount; i++) {
                final int startIndex = i*rowsInOneExecutors;
                int shiftIndex = i != threadCoount -1 ? rowsInOneExecutors : remains;

                final String[][] part = Arrays.copyOfRange(rows,startIndex,startIndex+shiftIndex);
                results.add(service.submit(new Runnable() {
                    @Override
                    public void run() {
                        proccess(config, provService, synchStartLog, part, validationScript, transformScript, matchRule, rowHeader, startIndex);
                    }
                }));
            }

            waitUntilWorkDone(results);

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();

            log.error(fe);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.FILE_EXCEPTION.toString(), fe.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FILE_EXCEPTION);
            return resp;

        } catch (ClassNotFoundException cnfe) {


            log.error(cnfe);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.CLASS_NOT_FOUND.toString(), cnfe.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            return resp;
        } catch (IOException io) {
            io.printStackTrace();

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.IO_EXCEPTION.toString(), io.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.IO_EXCEPTION);
            return resp;
        } catch (InterruptedException e) {
            log.error(e);

            synchStartLog.updateSynchAttributes("FAIL", ResponseCode.INTERRUPTED_EXCEPTION.toString(), e.toString());
            auditHelper.logEvent(synchStartLog);

            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.INTERRUPTED_EXCEPTION);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        log.debug("CSV SYNCHRONIZATION COMPLETE^^^^^^^^");

        return new SyncResponse(ResponseStatus.SUCCESS);
    }

    private void waitUntilWorkDone(List<Future> results) throws InterruptedException {
        boolean success = false;
        while(!success) {
            for(Future future : results) {
                if(!future.isDone()) {
                    success = false;
                    break;
                } else {
                    success = true;
                }
            }
            Thread.sleep(500);
        }
    }

    private void proccess(SynchConfig config, ProvisionService provService, IdmAuditLog synchStartLog, String[][] rows, ValidationScript validationScript, TransformScript transformScript, MatchObjectRule matchRule, LineObject rowHeader, int ctr) {
        for (String[] row : rows) {
            log.debug("*** Record counter: " + ctr);
            //populate the data object
            ProvisionUser pUser = new ProvisionUser();

            LineObject rowObj = rowHeader.copy();
            populateRowObject(rowObj, row);
            log.debug(" - Validation being called");

            // validate
            if (validationScript != null) {
                int retval = validationScript.isValid(rowObj);
                if (retval == ValidationScript.NOT_VALID) {
                    log.debug(" - Validation failed...transformation will not be called.");

                    continue;
                }
                if (retval == ValidationScript.SKIP) {
                    continue;
                }
            }

            log.debug(" - Getting column map...");

            // check if the user exists or not
            Map<String, Attribute> rowAttr = rowObj.getColumnMap();

            log.debug(" - Row Attr..." + rowAttr);
            //

            User usr = matchRule.lookup(config, rowAttr);

            //@todo - Update lookup so that an exception is thrown
            // when lookup fails due to bad matching.

            log.debug(" - Preparing transform script");

            // transform
            if (transformScript != null) {

                transformScript.init();

                // initialize the transform script
                if (usr != null) {
                    transformScript.setNewUser(false);
                    transformScript.setUser(userMgr.getUserWithDependent(usr.getUserId(), true));
                    transformScript.setPrincipalList(loginManager.getLoginByUser(usr.getUserId()));
                    transformScript.setUserRoleList(roleDataService.getUserRolesAsFlatList(usr.getUserId()));

                } else {
                    transformScript.setNewUser(true);
                }

                log.debug(" - Execute transform script");

                int retval = transformScript.execute(rowObj, pUser);

                log.debug(" - Execute complete transform script");


                pUser.setSessionId(synchStartLog.getSessionId());

                if (retval == TransformScript.DELETE && pUser.getUser() != null) {
                    provService.deleteByUserId(pUser, UserStatusEnum.DELETED, systemAccount);
                } else {
                    // call synch
                    if (retval != TransformScript.DELETE) {
                        if (usr != null) {
                            log.debug(" - Updating existing user");
                            pUser.setUserId(usr.getUserId());
                            provService.modifyUser(pUser);

                        } else {
                            log.debug(" - New user being provisioned");

                            pUser.setUserId(null);
                            provService.addUser(pUser);
                        }
                    }
                }
            }
            // show the user object
            ctr++;
        }

    }

    public Response testConnection(SynchConfig config) {
        File file = new File(config.getFileName());
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();

            log.error(fe);

            Response resp = new Response(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.FILE_EXCEPTION);
            resp.setErrorText(fe.getMessage());
            return resp;

        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    // This can safely be ignored. The file was opened successfully at this point.
                }
        }
        Response resp = new Response(ResponseStatus.SUCCESS);
        return resp;
    }

    private LineObject populateTemplate(String[] lineAry) {
        LineObject rowHeader = new LineObject();
        int ctr = 0;
        for (String s : lineAry) {
            Attribute a = new Attribute(s, null);
            a.setType("STRING");
            a.setColumnNbr(ctr);
            rowHeader.put(a.getName(), a);
            ctr++;
        }
        return rowHeader;
    }


    private void populateRowObject(LineObject rowObj, String[] lineAry) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Map<String, Attribute> attrMap = rowObj.getColumnMap();
        Set<String> keySet = attrMap.keySet();
        Iterator<String> it = keySet.iterator();

        while (it.hasNext()) {
            String key = it.next();
            Attribute attr = rowObj.get(key);
            int colNbr = attr.getColumnNbr();
            String colValue = lineAry[colNbr];


            attr.setValue(colValue);
        }

    }


    public static ApplicationContext getAc() {
        return ac;
    }

    public static void setAc(ApplicationContext ac) {
        CSVAdapter.ac = ac;
    }

    public LoginDataService getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginDataService loginManager) {
        this.loginManager = loginManager;
    }

    public RoleDataService getRoleDataService() {
        return roleDataService;
    }

    public void setRoleDataService(RoleDataService roleDataService) {
        this.roleDataService = roleDataService;
    }

    public UserDataService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataService userMgr) {
        this.userMgr = userMgr;
    }

    public String getSystemAccount() {
        return systemAccount;
    }

    public void setSystemAccount(String systemAccount) {
        this.systemAccount = systemAccount;
    }

    public MatchRuleFactory getMatchRuleFactory() {
        return matchRuleFactory;
    }

    public void setMatchRuleFactory(MatchRuleFactory matchRuleFactory) {
        this.matchRuleFactory = matchRuleFactory;
    }

    public AuditHelper getAuditHelper() {
        return auditHelper;
    }

    public void setAuditHelper(AuditHelper auditHelper) {
        this.auditHelper = auditHelper;
    }


}
