package org.openiam.webadmin.reports;

import org.openiam.idm.srvc.report.dto.ReportInfoDto;
import org.springframework.web.multipart.MultipartFile;

public class ReportCommand {
    private MultipartFile dataSourceScriptFile;
    private MultipartFile reportDesignFile;
    private ReportInfoDto report = new ReportInfoDto();

    public MultipartFile getDataSourceScriptFile() {
        return dataSourceScriptFile;
    }

    public void setDataSourceScriptFile(MultipartFile dataSourceScriptFile) {
        this.dataSourceScriptFile = dataSourceScriptFile;
    }

    public MultipartFile getReportDesignFile() {
        return reportDesignFile;
    }

    public void setReportDesignFile(MultipartFile reportDesignFile) {
        this.reportDesignFile = reportDesignFile;
    }

    public ReportInfoDto getReport() {
        return report;
    }

    public void setReport(ReportInfoDto report) {
        this.report = report;
    }
}
