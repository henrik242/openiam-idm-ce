package org.openiam.idm.srvc.health;


import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * WebService interface that clients will access to determine if the service layer is up and running.
 * @author Suneet Shah
 * @version 2.2
 */
@WebService(targetNamespace = "urn:idm.openiam.org/srvc/health/service", name = "HeartBeatService")
public interface HeartBeatWebService {

    /**
     * If the service is, the operation will respond
     * @return
     */
	@WebMethod
	public String isAlive();


}