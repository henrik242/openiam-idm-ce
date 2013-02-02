package org.openiam.idm.srvc.report.ws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.report.dto.ReportInfoDto;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "reports"
})
public class GetAllReportsResponse extends Response {

    protected List<ReportInfoDto> reports;

    public List<ReportInfoDto> getReports() {
        return reports;
    }

    public void setReports(List<ReportInfoDto> reports) {
        this.reports = reports;
    }
}
