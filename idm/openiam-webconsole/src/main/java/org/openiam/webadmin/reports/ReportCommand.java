package org.openiam.webadmin.reports;

import org.openiam.idm.srvc.report.dto.ReportDto;
import org.springframework.web.multipart.MultipartFile;

public class ReportCommand {
    private MultipartFile dataSourceScriptFile;
    private MultipartFile reportDesignFile;
    private ReportDto report = new ReportDto();

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

    public ReportDto getReport() {
        return report;
    }

    public void setReport(ReportDto report) {
        this.report = report;
    }
}
