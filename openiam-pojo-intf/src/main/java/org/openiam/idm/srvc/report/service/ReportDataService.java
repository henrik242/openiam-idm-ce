package org.openiam.idm.srvc.report.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.openiam.core.domain.ReportInfo;
import org.openiam.exception.ScriptEngineException;
import org.openiam.idm.srvc.report.dto.ReportDataDto;

public interface ReportDataService {
    ReportDataDto getReportData(final String reportName, final Map<String, String> reportParams) throws ClassNotFoundException, ScriptEngineException, IOException;
    List<ReportInfo> getAllReports();
    ReportInfo getReportByName(String name);
    void createOrUpdateReportInfo(String reportName, String reportDataSource, String reportUrl);
}