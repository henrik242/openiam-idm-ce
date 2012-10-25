package org.openiam.idm.srvc.audit.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.mule.module.client.MuleClient;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;


import javax.jws.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@WebService(endpointInterface = "org.openiam.idm.srvc.audit.ws.AsynchIdmAuditLogWebService",
		targetNamespace = "urn:idm.openiam.org/srvc/audit/service",
		portName = "AsynchAuditDataServicePort",
		serviceName = "AsynchAuditDataService")
public class AsynchIdmAuditLogWebServiceImpl implements AsynchIdmAuditLogWebService, MuleContextAware {

    protected MuleContext muleContext;
    protected static final Log l = LogFactory.getLog(AsynchIdmAuditLogWebServiceImpl.class);

    static protected ResourceBundle res = ResourceBundle.getBundle("datasource");

    static String serviceHost = res.getString("openiam.service_base");
    static String serviceContext = res.getString("openiam.idm.ws.path");


    public void createLog(IdmAuditLog log) {

         try {


            l.debug("MuleContext = " + muleContext);


            Map<String, String> msgPropMap = new HashMap<String, String>();
            msgPropMap.put("SERVICE_HOST", serviceHost);
            msgPropMap.put("SERVICE_CONTEXT", serviceContext);


            //Create the client with the context
            MuleClient client = new MuleClient(muleContext);
            client.sendAsync("vm://logAuditEvent", (IdmAuditLog) log, msgPropMap);




        } catch (Exception e) {
            l.debug("EXCEPTION:AsynchIdmAuditLogWebServiceImpl");
            l.error(e);

        }
    }



    public void createLinkedLogs( List<IdmAuditLog> logList) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setMuleContext(MuleContext ctx) {

        l.debug("** setMuleContext called. **");

        muleContext = ctx;
    }

}
