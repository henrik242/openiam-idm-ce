package org.openiam.idm.srvc.report.ws;

import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto;
import org.openiam.idm.srvc.report.service.ReportCriteriaParamDao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "parameters"
})
public class GetReportParametersResponse extends Response {

    protected List<ReportCriteriaParamDto> parameters;

    public List<ReportCriteriaParamDto> getParameters() {
        return parameters;
    }

    public void setParameters(List<ReportCriteriaParamDto> parameters) {
        this.parameters = parameters;
    }
}
