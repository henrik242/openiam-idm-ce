import org.openiam.idm.groovy.helper.ServiceHelper;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.*;

import org.openiam.idm.srvc.audit.ws.*;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openiam.idm.srvc.audit.dto.*;


import org.openiam.util.WebServiceHelper;


println("publishAuditEvent.groovy")

def PublishAuditEventWebService publishAuditEvent = publishEventService();

System.out.println("service client =" + publishAuditEvent);

def auditService = context.getBean("auditWS")

System.out.println("audit service  =" + auditService);

//if (publishAuditEvent.isAlive()) {

def SearchAudit search = new SearchAudit();
search.customAttrname2 = "PUBLISHED";
search.customAttrValue2= "0";
def List<IdmAuditLog> eventList = auditService.search(search).logList;

if (eventList != null) {
	System.out.println("Audit records found=" + eventList.size());
	for (IdmAuditLog l : eventList) {
	
		if (l.objectTypeId != "BATCH") {
			System.out.println("Exporting event");
			publishAuditEvent.publishEvent(l);
		}
		l.customAttrvalue2 = "1";
		auditService.updateLog(l);
		
	}
}else {
	System.out.println("No unpublished events found.");
}
//}else{
//	System.out.println("Unable to connect with ATNA - No events published.");
//}


output=1

	static PublishAuditEventWebService publishEventService() {
		 String BASE_URL= "http://localhost:8080/openiam-idm-esb/idmsrvc";

		String serviceUrl = BASE_URL + "/PublishAuditEventService"
		String port ="PublishAuditEventWebServicePort"
		String nameSpace = "urn:idm.openiam.org/srvc/audit/service"
		
		Service service = Service.create(QName.valueOf(serviceUrl))
			
		service.addPort(new QName(nameSpace,port),
				SOAPBinding.SOAP11HTTP_BINDING,	serviceUrl)
		
		return service.getPort(new QName(nameSpace,	port),
				PublishAuditEventWebService.class);
	}
