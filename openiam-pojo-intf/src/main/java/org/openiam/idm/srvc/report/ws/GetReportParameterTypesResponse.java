package org.openiam.idm.srvc.report.ws;

import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.report.dto.ReportParamTypeDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "types"
})
public class GetReportParameterTypesResponse extends Response {
    private List<ReportParamTypeDto> types;

    public List<ReportParamTypeDto> getTypes() {
        return types;
    }

    public void setTypes(List<ReportParamTypeDto> types) {
        this.types = types;
    }
}
