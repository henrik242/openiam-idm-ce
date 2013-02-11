package org.openiam.idm.srvc.msg.service;

import org.openiam.base.ws.PropertyMapAdapter;
import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.msg.ws.NotificationRequest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;

/**
 * Provides methods to be able to send emails.
 *
 * @author suneet
 */
@WebService(targetNamespace = "urn:idm.openiam.org/srvc/msg/service", name = "EmailWebService")
public interface MailService {

    @WebMethod
    void send(String from, String to, String subject, String msg, boolean isHtmlFormat);

    /**
     * Sends out a notification based on the information defined in the notification request.
     *
     * @param req
     */

    @WebMethod
    Response sendNotificationRequest(
            @WebParam(name = "req", targetNamespace = "")
            NotificationRequest req);
}
