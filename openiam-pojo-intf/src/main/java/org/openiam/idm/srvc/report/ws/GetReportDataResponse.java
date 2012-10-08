package org.openiam.idm.srvc.report.ws;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.report.dto.ReportDataDto;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "reportDataDto"
})
public class GetReportDataResponse extends Response {

    @XmlElement(name = "ReportData")
    protected ReportDataDto reportDataDto;

    public ReportDataDto getReportDataDto() {
        return reportDataDto;
    }

    public void setReportDataDto(ReportDataDto reportDataDto) {
        this.reportDataDto = reportDataDto;
    }
}
