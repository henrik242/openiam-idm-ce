package org.openiam.idm.srvc.report.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportDto", propOrder = {
        "reportId",
        "reportName",
        "reportDataSource",
        "reportUrl"
})
public class ReportDto {
    private String reportId;
    private String reportName;
    private String reportDataSource;
    private String reportUrl;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportDataSource() {
        return reportDataSource;
    }

    public void setReportDataSource(String reportDataSource) {
        this.reportDataSource = reportDataSource;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }
}

