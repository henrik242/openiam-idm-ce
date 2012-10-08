package org.openiam.idm.srvc.report.ws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openiam.idm.srvc.report.dto.ReportParameterDto;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "parameters"
})
public class GetReportParametersResponse {
    List<ReportParameterDto> parameters;

    public List<ReportParameterDto> getParameters() {
        return parameters;
    }

    public void setParameters(List<ReportParameterDto> parameters) {
        this.parameters = parameters;
    }
}
