package org.openiam.idm.srvc.batch;

import java.util.*;

import java.net.ConnectException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.module.client.MuleClient;
import org.openiam.base.id.UUIDGen;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.ws.LoginDataWebService;
import org.openiam.idm.srvc.batch.dto.BatchTask;
import org.openiam.idm.srvc.batch.service.BatchDataService;
import org.openiam.idm.srvc.policy.service.PolicyDataService;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Scheduled task that is called at set intervals by the schedular
 * @author suneet
 *
 */
public class IntervalTask  implements ApplicationContextAware, MuleContextAware {

	private static final Log log = LogFactory.getLog(IntervalTask.class);

    /*
     * The flags for the running tasks are handled by this Thread-Safe Set.
     * It stores the taskIds of the currently executing tasks.
     * This is faster and as reliable as storing the flags in the database,
     * if the tasks are only launched from ONE host in a clustered environment.
     * It is unique for each class-loader, which means unique per war-deployment.
     */
    private static Set<String> runningTask = Collections.newSetFromMap(new ConcurrentHashMap());
	protected BatchDataService batchService;
	
	protected LoginDataWebService loginManager;
	protected PolicyDataService policyDataService;
	protected String scriptEngine;
	protected AuditHelper auditHelper;

    protected MuleContext muleContext;

    static protected ResourceBundle res = ResourceBundle.getBundle("datasource");

    static boolean isPrimary = Boolean.parseBoolean(res.getString("IS_PRIMARY"));

    static String serviceHost = res.getString("PRIMARY_HOST");
    static String serviceContext = res.getString("openiam.idm.ws.path");

	
	public static ApplicationContext ac;
	
	public IntervalTask() {
		super();
	}

	public void execute(String frequencyMeasure) 
	{
        if (!isPrimary) {
            log.debug("Scheduler: Not primary instance");

            if (isAlive()) {
                log.debug("Primary is alive.");
                return;
            }
        }


	
		ScriptIntegration se = null;
		Map<String, Object> bindingMap = new HashMap<String, Object>();
		bindingMap.put("context", ac);	
		
		try {
			se = ScriptFactory.createModule(this.scriptEngine); 
		}catch(Exception e) {
			log.error(e);
			return;

		}

		// get the list of domains
		List<BatchTask> taskList = batchService.getAllTasksByFrequency(frequencyMeasure);

		if (taskList != null) {
			for (BatchTask task : taskList) {
				String requestId = UUIDGen.getUUID();
				try {
					if (task.getEnabled() != 0) {
                        // This needs to be synchronized, because the check for the taskId and the insertion need to
                        // happen atomically. It is possible for two threads, started by Quartz, to reach this point at
                        // the same time for the same task.
                        synchronized (runningTask) {
                            if(runningTask.contains(task.getTaskId())) {
                                log.debug("Task " + task.getTaskName() + " already running");
                                continue;
                            }
                            runningTask.add(task.getTaskId());
                        }

						auditHelper.addLog(task.getTaskName(), null,	null,
								"IDM BATCH TASK", null, "0", "BATCH", task.getTaskId(), 
								null,   "TASK STARTED", null,  null, 
								task.getParam1(), requestId, null, null, null);
						
						log.debug("Executing task:" + task.getTaskName());

						if (task.getLastExecTime() == null) {
							task.setLastExecTime(new Date(System.currentTimeMillis()));
						}
						bindingMap.put("taskObj", task);
						bindingMap.put("lastExecTime", task.getLastExecTime());
						bindingMap.put("parentRequestId", requestId);
						
						Integer output = (Integer)se.execute(bindingMap, task.getTaskUrl());
						if (output.intValue() == 0 ) {
							 auditHelper.addLog(task.getTaskName(), null,	null,
									"IDM BATCH TASK", null, "0", "BATCH", task.getTaskId(), 
									null,   "FAIL", null,  null, 
									task.getParam1(), requestId, null, null, null);
						}else {
							auditHelper.addLog(task.getTaskName(), null,	null,
									"IDM BATCH TASK", null, "0", "BATCH", task.getTaskId(), 
									null,   "SUCCESS", null,  "USER_STATUS", 
									task.getParam1(), requestId, null, null, null);
						}
					}

				}catch(Exception e) {
					log.error(e);
				}finally {
					if (task.getEnabled() != 0) {
                        // this point can only be reached by the thread, which put the taskId into the map
                        runningTask.remove(task.getTaskId());
                        // Get the updated status of the task
                        task = batchService.getBatchTask(task.getTaskId());
						task.setLastExecTime(new Date(System.currentTimeMillis()));
						batchService.updateTask(task);
					}
				}
				
			}
		}

	
	}

    private boolean isAlive() {
        Map<String, String> msgPropMap = new HashMap<String, String>();
        msgPropMap.put("SERVICE_HOST", serviceHost);
        msgPropMap.put("SERVICE_CONTEXT", serviceContext);


        //Create the client with the context
        try {

            MuleClient client = new MuleClient(muleContext);
            client.send("vm://heartBeatIsAlive", null, msgPropMap);

        }catch(Exception  ce) {
            log.error(ce.toString());

            if (ce instanceof ConnectException) {
                return false;
            }

        }
        return true;
    }



	public BatchDataService getBatchService() {
		return batchService;
	}

	public void setBatchService(BatchDataService batchService) {
		this.batchService = batchService;
	}

	public LoginDataWebService getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(LoginDataWebService loginManager) {
		this.loginManager = loginManager;
	}

	public PolicyDataService getPolicyDataService() {
		return policyDataService;
	}

	public void setPolicyDataService(PolicyDataService policyDataService) {
		this.policyDataService = policyDataService;
	}

	public String getScriptEngine() {
		return scriptEngine;
	}

	public void setScriptEngine(String scriptEngine) {
		this.scriptEngine = scriptEngine;
	}

	public AuditHelper getAuditHelper() {
		return auditHelper;
	}

	public void setAuditHelper(AuditHelper auditHelper) {
		this.auditHelper = auditHelper;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ac = applicationContext;
		
	}

    public void setMuleContext(MuleContext ctx) {

        log.debug("** Setting MuleContext for IntervalTask **");

        muleContext = ctx;
    }


}
