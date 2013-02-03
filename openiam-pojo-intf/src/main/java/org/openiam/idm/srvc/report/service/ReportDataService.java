package org.openiam.idm.srvc.report.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openiam.idm.srvc.report.domain.ReportCriteriaParamEntity;
import org.openiam.idm.srvc.report.domain.ReportInfoEntity;
import org.openiam.exception.ScriptEngineException;
import org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto;
import org.openiam.idm.srvc.report.dto.ReportDataDto;

public interface ReportDataService {
    ReportDataDto getReportData(final String reportName, final Map<String, String> reportParams) throws ClassNotFoundException, ScriptEngineException, IOException;
    List<ReportInfoEntity> getAllReports();
    ReportInfoEntity getReportByName(String name);
    void createOrUpdateReportInfo(String reportName, String reportDataSource, String reportUrl);
    List<ReportCriteriaParamEntity> getReportParametersByReportId(String reportId);
    void updateReportParametersByReportName(String reportName, List<ReportCriteriaParamEntity> prameters);
}