package org.openiam.webadmin.reports;

import java.util.Collections;
import java.util.List;
import org.openiam.idm.srvc.report.dto.ReportInfoDto;

public class ReportListCommand {
    private List<ReportInfoDto> reports = Collections.EMPTY_LIST;
    private ReportInfoDto report = new ReportInfoDto();

    public List<ReportInfoDto> getReports() {
        return reports;
    }

    public void setRepors(List<ReportInfoDto> reports) {
        this.reports = reports;
    }

    public ReportInfoDto getReport() {
        return report;
    }

    public void setReport(ReportInfoDto report) {
        this.report = report;
    }
}
