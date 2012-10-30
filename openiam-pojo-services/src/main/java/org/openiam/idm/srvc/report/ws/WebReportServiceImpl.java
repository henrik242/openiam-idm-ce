package org.openiam.idm.srvc.report.ws;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.commons.lang.StringUtils;
import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.core.domain.ReportInfo;
import org.openiam.idm.srvc.report.dto.ReportDataDto;
import org.openiam.idm.srvc.report.dto.ReportDto;
import org.openiam.idm.srvc.report.service.ReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportWS")
@WebService(endpointInterface = "org.openiam.idm.srvc.report.ws.WebReportService",
        targetNamespace = "urn:idm.openiam.org/idm/srvc/report/ws",
        portName = "ReportServicePort",
        serviceName = "ReportService")
public class WebReportServiceImpl implements WebReportService {

    @Autowired
    private ReportDataService reportDataService;

    @Override
    public GetReportDataResponse executeQuery(final String reportName, final HashMap<String, String> queryParams) {
        GetReportDataResponse response = new GetReportDataResponse();
        if (!StringUtils.isEmpty(reportName)) {
            try {
                ReportDataDto reportDataDto = reportDataService.getReportData(reportName, queryParams);

                response.setReportDataDto(reportDataDto);
            } catch (Throwable ex) {
                response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
                response.setErrorText(ex.getMessage());
                response.setStatus(ResponseStatus.FAILURE);
            }
        } else {
            response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
            response.setErrorText("Invalid parameter list: reportName=" + reportName);
            response.setStatus(ResponseStatus.SUCCESS);
        }

        return response;
    }

    @Override
    public GetAllReportsResponse getReports() {
        List<ReportInfo> reports = reportDataService.getAllReports();
        GetAllReportsResponse reportsResponse = new GetAllReportsResponse();
        List<ReportDto> reportDtos = new LinkedList<ReportDto>();
        for (ReportInfo reportInfo : reports) {
            ReportDto reportDto = new ReportDto();
            reportDto.setReportId(reportInfo.getId());
            reportDto.setReportName(reportInfo.getReportName());
            reportDto.setReportDataSource(reportInfo.getDatasourceFilePath());
            reportDto.setReportUrl(reportInfo.getReportFilePath());
            reportDtos.add(reportDto);
        }
        reportsResponse.setReports(reportDtos);
        return reportsResponse;
    }

    @Override
    public Response createOrUpdateReportInfo(@WebParam(name = "reportName", targetNamespace = "") String reportName, @WebParam(name = "reportDataSource", targetNamespace = "") String reportDataSource, @WebParam(name = "reportUrl", targetNamespace = "") String reportUrl) {
        Response response = new Response();
        if (!StringUtils.isEmpty(reportName)) {
            try {
                reportDataService.createOrUpdateReportInfo(reportName, reportDataSource, reportUrl);
            } catch (Throwable t) {
                response.setStatus(ResponseStatus.FAILURE);
                response.setErrorCode(ResponseCode.SQL_EXCEPTION);
                response.setErrorText(t.getMessage());
                return response;
        }
            response.setStatus(ResponseStatus.SUCCESS);
        } else {
            response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
            response.setErrorText("Invalid parameter list: reportName=" + reportName);
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}
