package org.openiam.idm.srvc.msg.service;

import org.openiam.base.ws.PropertyMap;
import org.openiam.base.ws.PropertyMapAdapter;
import org.openiam.base.ws.Response;

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
    Response sendNotification(@WebParam(name = "notificationName", targetNamespace = "") String notificationName, @WebParam(name = "mailParams", targetNamespace = "") PropertyMap mailParams);

    @WebMethod
    void send(String from, String to, String subject, String msg, boolean isHtmlFormat);

}
