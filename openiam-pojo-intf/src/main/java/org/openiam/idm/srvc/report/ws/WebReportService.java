package org.openiam.idm.srvc.report.ws;

import org.openiam.base.ws.PropertyMapAdapter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

@WebService(targetNamespace = "urn:idm.openiam.org/idm/srvc/report/ws/service", name = "ReportService")
public interface WebReportService {

    @WebMethod
    GetReportDataResponse executeQuery(
            @WebParam(name = "reportName", targetNamespace = "") String reportName,
            @WebParam(name = "queryParams", targetNamespace = "") @XmlJavaTypeAdapter(PropertyMapAdapter.class) Map<String, String> queryParams);

    @WebMethod
    GetAllReportsResponse getReports();

    @WebMethod
    GetReportParametersResponse getParametersByReport(@WebParam(name = "reportName", targetNamespace = "") String reportName);
}
