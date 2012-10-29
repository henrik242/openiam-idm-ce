package org.openiam.provision.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.module.client.MuleClient;
import org.openiam.idm.srvc.msg.dto.NotificationRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The pre-processor allows us to define a set of rules for provisioning users that can be used regardless of how provisioning
 * is triggered. For example, if an attribute just as a job code is supposed to indicate Role Membership, then these rules
 * can be defined in the PreProcessor script. These rules would then be in place for user created through the webconsole, selfserivce,
 * and synchronization
 *
 * User: suneetshah
 * Date: 5/10/12
 * Time: 10:00 PM
 * @version 2.2
 */
public abstract class AbstractPreProcessor implements ProvisionServicePreProcessor {

    protected MuleContext muleContext;

    private static final Log log = LogFactory.getLog(AbstractPostProcessor.class);

    final static private ResourceBundle res = ResourceBundle.getBundle("datasource");
    final static private String serviceHost = res.getString("openiam.service_base");
    final static private String serviceContext = res.getString("openiam.idm.ws.path");

    public void setMuleContext(MuleContext ctx) {
        muleContext = ctx;
    }

    public void sendEmailNotification( NotificationRequest request) {
        try {


            MuleClient client = new MuleClient(muleContext);

            Map<String, String> msgPropMap = new HashMap<String, String>();
            msgPropMap.put("SERVICE_HOST", serviceHost);
            msgPropMap.put("SERVICE_CONTEXT", serviceContext);

            client.sendAsync("vm://notifyUserByEmailMessage", request, msgPropMap);

        } catch (MuleException me) {
            log.error(me.toString());
        }
    }
}
