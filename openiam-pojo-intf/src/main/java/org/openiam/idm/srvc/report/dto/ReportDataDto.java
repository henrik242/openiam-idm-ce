package org.openiam.idm.srvc.report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openiam.base.ws.PropertyMapAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportData", propOrder = {
        "parameters",
        "tables"
})
public class ReportDataDto {

    @XmlJavaTypeAdapter(PropertyMapAdapter.class)
    protected HashMap<String, String> parameters;

    @XmlAnyElement(lax = true)
    protected List<ReportTable> tables;

    public List<ReportTable> getTables() {
        if (tables == null) {
            tables = new ArrayList<ReportTable>();
        }
        return tables;
    }

    public void setTables(List<ReportTable> tables) {
        this.tables = tables;
    }

    public HashMap<String, String> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
}

