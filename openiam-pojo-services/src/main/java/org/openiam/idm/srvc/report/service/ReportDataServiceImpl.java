package org.openiam.idm.srvc.report.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.openiam.core.dao.ReportDataDao;
import org.openiam.core.domain.ReportInfo;
import org.openiam.exception.ScriptEngineException;
import org.openiam.idm.srvc.report.dto.ReportDataDto;
import org.openiam.script.ScriptFactory;
import org.openiam.script.ScriptIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportDataServiceImpl implements ReportDataService {

    private static final String scriptEngine = "org.openiam.script.GroovyScriptEngineIntegration";

    @Autowired
    private ReportDataDao reportDao;

    @Override
    @Transactional(readOnly = true)
    public ReportDataDto getReportData(final String reportName, final Map<String, String> reportParams) throws ClassNotFoundException, ScriptEngineException, IOException {
        ReportInfo reportInfo = reportDao.findByName(reportName);
        if (reportInfo == null) {
            throw new IllegalArgumentException("Invalid parameter list: report with name="+reportName + " was not found in Database");
        }

        ScriptIntegration se = ScriptFactory.createModule(scriptEngine);
        ReportDataSetBuilder dataSourceBuilder = (ReportDataSetBuilder) se.instantiateClass(Collections.EMPTY_MAP, "/reports/"+reportInfo.getDatasourceFilePath());

        return dataSourceBuilder.getReportData(reportParams);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportInfo> getAllReports() {
        return reportDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportInfo getReportByName(String name) {
        return reportDao.findByName(name);
    }

    @Override
    @Transactional
    public void createOrUpdateReportInfo(String reportName, String reportDataSource, String reportUrl) {
       reportDao.createOrUpdateReportInfo(reportName, reportDataSource, reportUrl);
    }
}
