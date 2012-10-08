package org.openiam.core.domain;

import java.util.Arrays;
import java.util.List;

public class ReportInfo {

    private String id;

    private String reportName;

    private String groovyScriptPath;

    private String reportFilePath;

    private String params;

    private String requiredParams;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getGroovyScriptPath() {
        return groovyScriptPath;
    }

    public void setGroovyScriptPath(String groovyScriptPath) {
        this.groovyScriptPath = groovyScriptPath;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRequiredParams() {
        return requiredParams;
    }

    public void setRequiredParams(String requiredParams) {
        this.requiredParams = requiredParams;
    }

    public List<String> getRequiredParamsList() {
        return Arrays.asList(this.requiredParams.split(","));
    }

    public List<String> getParamsList() {
        return Arrays.asList(this.params.split(","));
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }
}