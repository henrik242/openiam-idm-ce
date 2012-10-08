package org.openiam.idm.srvc.report.service;

import org.openiam.idm.srvc.report.dto.ReportDataDto;

import java.util.Map;

/**
 * Interface that all Report Query Scripts must implement
 */
public interface ReportQueryScript {

    ReportDataDto getReportData(Map<String, String> reportParams);

}
