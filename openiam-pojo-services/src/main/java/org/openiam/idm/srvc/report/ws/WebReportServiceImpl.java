package org.openiam.idm.srvc.report.ws;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.core.domain.ReportInfo;
import org.openiam.idm.srvc.report.dto.ReportDataDto;
import org.openiam.idm.srvc.report.dto.ReportDto;
import org.openiam.idm.srvc.report.dto.ReportParameterDto;
import org.openiam.idm.srvc.report.service.ReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

@Service("reportWS")
@WebService(endpointInterface = "org.openiam.idm.srvc.report.ws.WebReportService",
        targetNamespace = "urn:idm.openiam.org/idm/srvc/report/ws",
        portName = "ReportServicePort",
        serviceName = "ReportService")
public class WebReportServiceImpl implements WebReportService {
    protected final Log LOG = LogFactory.getLog(WebReportServiceImpl.class);

    @Autowired
    private ReportDataService reportDataService;

    @Override
    public GetReportDataResponse executeQuery(final String reportName, Map<String, String> queryParams) {
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
        for (ReportInfo reportQuery : reports) {
            ReportDto reportDto = new ReportDto();
            reportDto.setReportName(reportQuery.getReportName());
            reportDto.setReportUrl(reportQuery.getReportFilePath());
            reportDto.setParams(reportQuery.getParamsList());
            reportDto.setRequiredParams(reportQuery.getRequiredParamsList());
            reportDtos.add(reportDto);
        }
        reportsResponse.setReports(reportDtos);
        return reportsResponse;
    }

    @Override
    public GetReportParametersResponse getParametersByReport(@WebParam(name = "reportName", targetNamespace = "") String reportName) {
        ReportInfo reportQuery = reportDataService.getReportByName(reportName);
        GetReportParametersResponse getReportParametersResponse = new GetReportParametersResponse();
        List<String> params = reportQuery.getParamsList();
        List<String> requiredParams = reportQuery.getRequiredParamsList();
        List<ReportParameterDto> parameterDtoList = new LinkedList<ReportParameterDto>();
        for (String param : params) {
            ReportParameterDto parameterDto = new ReportParameterDto();
            parameterDto.setName(param);
            parameterDto.setLabel(param);
            parameterDto.setRequired(requiredParams.contains(param));
            parameterDtoList.add(parameterDto);
        }
        getReportParametersResponse.setParameters(parameterDtoList);
        return getReportParametersResponse;
    }
}
