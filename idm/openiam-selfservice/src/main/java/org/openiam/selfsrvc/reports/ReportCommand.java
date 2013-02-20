package org.openiam.selfsrvc.reports;

import java.util.Collections;
import java.util.List;
import org.openiam.idm.srvc.report.dto.ReportDto;

public class ReportCommand {
    private List<ReportDto> reports = Collections.EMPTY_LIST;
    private ReportDto report = new ReportDto();

    public List<ReportDto> getReports() {
        return reports;
    }

    public void setRepors(List<ReportDto> reports) {
        this.reports = reports;
    }

    public ReportDto getReport() {
        return report;
    }

    public void setReport(ReportDto report) {
        this.report = report;
    }
}
