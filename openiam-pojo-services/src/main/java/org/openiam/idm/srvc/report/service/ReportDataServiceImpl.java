package org.openiam.idm.srvc.report.service;

import java.io.IOException;
import java.util.HashMap;
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
    public ReportDataDto getReportData(final String reportName, final Map<String, String> reportParams)
            throws IOException, ClassNotFoundException, ScriptEngineException {


        /*  ---- Enable after this is integrated with BIRT Report writer
        ReportInfo reportInfo = reportDao.findByName(reportName);
        if (reportInfo == null) {
            throw new IllegalArgumentException("Invalid parameter list: report with name="+reportName + " was not found in DataBase");
        }

        if(!validateParams(reportInfo, reportParams)) {
           throw new IllegalArgumentException("Invalid parameter list: required="+reportInfo.getRequiredParams());
        }
        Map<String, Object> objectMap = new HashMap<String, Object>();
        if (reportParams != null) {
            objectMap.putAll(reportParams);
        }

        ScriptIntegration se = ScriptFactory.createModule(this.scriptEngine);
        ReportDataDto reportData = (ReportDataDto) se.execute(objectMap, reportInfo.getGroovyScriptPath());

        */

        ScriptIntegration se = ScriptFactory.createModule(scriptEngine);
        ReportQueryScript rptScript =  (ReportQueryScript)se.instantiateClass(null, reportName);

        ReportDataDto data = rptScript.getReportData(reportParams);


        return data;


    }

    private static boolean validateParams(ReportInfo reportQuery, Map<String, String> queryParams) {
        for(String requiredParam : reportQuery.getRequiredParamsList()) {
            if(!queryParams.containsKey(requiredParam)) {
               return false;
            }
        }
        return true;
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
}
