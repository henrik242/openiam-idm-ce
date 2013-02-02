package org.openiam.idm.srvc.report.dto;

import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.idm.srvc.report.domain.ReportInfoEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * This DTO used in reporting system to transferring Report information to WS clients
 *
 * @author vitaly.yakunin
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportInfoDto", propOrder = {
        "reportId",
        "reportName",
        "reportDataSource",
        "reportUrl"
})
@DozerDTOCorrespondence(ReportInfoEntity.class)
public class ReportInfoDto {
    private String reportId;
    private String reportName;
    private String reportDataSource;
    private String reportUrl;

    public ReportInfoDto() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportInfoDto reportDto = (ReportInfoDto) o;

        if (reportDataSource != null ? !reportDataSource.equals(reportDto.reportDataSource) : reportDto.reportDataSource != null)
            return false;
        if (reportId != null ? !reportId.equals(reportDto.reportId) : reportDto.reportId != null) return false;
        if (reportName != null ? !reportName.equals(reportDto.reportName) : reportDto.reportName != null) return false;
        if (reportUrl != null ? !reportUrl.equals(reportDto.reportUrl) : reportDto.reportUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reportId != null ? reportId.hashCode() : 0;
        result = 31 * result + (reportName != null ? reportName.hashCode() : 0);
        result = 31 * result + (reportDataSource != null ? reportDataSource.hashCode() : 0);
        result = 31 * result + (reportUrl != null ? reportUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportDto{" +
                "reportId='" + reportId + '\'' +
                ", reportName='" + reportName + '\'' +
                ", reportDataSource='" + reportDataSource + '\'' +
                ", reportUrl='" + reportUrl + '\'' +
                '}';
    }
}

