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
import org.openiam.dozer.converter.ReportCriteriaParamDozerConverter;
import org.openiam.dozer.converter.ReportInfoDozerConverter;
import org.openiam.dozer.converter.ReportParamTypeDozerConverter;
import org.openiam.idm.srvc.report.domain.ReportCriteriaParamEntity;
import org.openiam.idm.srvc.report.domain.ReportInfoEntity;
import org.openiam.idm.srvc.report.domain.ReportParamTypeEntity;
import org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto;
import org.openiam.idm.srvc.report.dto.ReportDataDto;
import org.openiam.idm.srvc.report.dto.ReportInfoDto;
import org.openiam.idm.srvc.report.dto.ReportParamTypeDto;
import org.openiam.idm.srvc.report.service.ReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WS for report system
 *
 * @author vitaly.yakunin
 */
@Service("reportWS")
@WebService(endpointInterface = "org.openiam.idm.srvc.report.ws.WebReportService",
        targetNamespace = "urn:idm.openiam.org/idm/srvc/report/ws",
        portName = "ReportServicePort",
        serviceName = "ReportService")
public class WebReportServiceImpl implements WebReportService {
    @Autowired
    private ReportInfoDozerConverter reportInfoDozerConverter;
    @Autowired
    private ReportCriteriaParamDozerConverter criteriaParamDozerConverter;
    @Autowired
    private ReportParamTypeDozerConverter paramTypeDozerConverter;
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
        List<ReportInfoEntity> reports = reportDataService.getAllReports();
        GetAllReportsResponse reportsResponse = new GetAllReportsResponse();
        List<ReportInfoDto> reportDtos = new LinkedList<ReportInfoDto>();
        if(reports != null) {
            reportDtos = reportInfoDozerConverter.convertToDTOList(reports, false);
        }
        reportsResponse.setReports(reportDtos);
        return reportsResponse;
    }

    @Override
    public Response createOrUpdateReportInfo(@WebParam(name = "reportName", targetNamespace = "") String reportName, @WebParam(name = "reportDataSource", targetNamespace = "") String reportDataSource, @WebParam(name = "reportUrl", targetNamespace = "") String reportUrl, @WebParam(name = "parameters", targetNamespace = "") List<ReportCriteriaParamDto> parameters) {
        Response response = new Response();
        if (!StringUtils.isEmpty(reportName)) {
            try {
                reportDataService.createOrUpdateReportInfo(reportName, reportDataSource, reportUrl);
                reportDataService.updateReportParametersByReportName(reportName, criteriaParamDozerConverter.convertToEntityList(parameters, false));
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

    @Override
    public GetReportParametersResponse getReportParametersByReportId(@WebParam(name = "reportId", targetNamespace = "") String reportId) {
        GetReportParametersResponse response = new GetReportParametersResponse();
        if (!StringUtils.isEmpty(reportId)) {
            List<ReportCriteriaParamEntity> params = reportDataService.getReportParametersByReportId(reportId);
            List<ReportCriteriaParamDto> paramsDtos = new LinkedList<ReportCriteriaParamDto>();
            if(params != null) {
               paramsDtos = criteriaParamDozerConverter.convertToDTOList(params, false);
            }
            response.setParameters(paramsDtos);
            response.setStatus(ResponseStatus.SUCCESS);
        } else {
            response.setErrorCode(ResponseCode.INVALID_ARGUMENTS);
            response.setErrorText("Invalid parameter list: reportId=" + reportId);
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }

    @Override
    public GetReportParameterTypesResponse getReportParameterTypes() {
        GetReportParameterTypesResponse response = new GetReportParameterTypesResponse();
        List<ReportParamTypeEntity> paramTypeEntities = reportDataService.getReportParameterTypes();
        List<ReportParamTypeDto> reportParamTypeDtos = new LinkedList<ReportParamTypeDto>();
        if(paramTypeEntities != null && paramTypeEntities.size() > 0) {
           reportParamTypeDtos = paramTypeDozerConverter.convertToDTOList(paramTypeEntities, false);
        }
        response.setTypes(reportParamTypeDtos);
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }
}
