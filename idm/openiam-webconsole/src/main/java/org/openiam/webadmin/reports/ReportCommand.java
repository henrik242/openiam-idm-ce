package org.openiam.webadmin.reports;

import org.openiam.idm.srvc.report.dto.ReportInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReportCommand {
    private MultipartFile dataSourceScriptFile;
    private MultipartFile reportDesignFile;
    private ReportInfoDto report = new ReportInfoDto();
    private String[] paramName;
    private String[] paramValue;

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

    public String[] getParamName() {
        return paramName;
    }

    public void setParamName(String[] paramName) {
        this.paramName = paramName;
    }

    public String[] getParamValue() {
        return paramValue;
    }

    public void setParamValue(String[] paramValue) {
        this.paramValue = paramValue;
    }
}
