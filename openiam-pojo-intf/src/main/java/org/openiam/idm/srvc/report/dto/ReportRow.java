package org.openiam.idm.srvc.report.dto;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportRow", propOrder = {
        "column"
})
public class ReportRow {
    protected List<ReportColumn> column;

    public List<ReportColumn> getColumn() {
        if(column == null) {
          column = new LinkedList<ReportColumn>();
        }
        return column;
    }

    public void setColumn(List<ReportColumn> column) {
        this.column = column;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class ReportColumn {
        @XmlElement
        protected String value;
        @XmlAttribute
        protected String name;

        public ReportColumn() {
        }

        public ReportColumn(String name, String value) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
