package org.openiam.idm.srvc.report.service;

import java.util.Map;
import org.openiam.idm.srvc.report.dto.ReportDataDto;

public interface ReportDataSetBuilder {
    ReportDataDto getReportData(Map<String, String> reportParams);
}
