package org.openiam.idm.srvc.report.service;

import org.hibernate.Criteria;

import org.hibernate.criterion.Restrictions;
import org.openiam.core.dao.BaseDaoImpl;
import org.openiam.idm.srvc.report.domain.ReportInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;


@Repository
public class ReportInfoDaoImpl extends BaseDaoImpl<ReportInfoEntity, String> implements ReportInfoDao {

    private static final Logger LOG = LoggerFactory.getLogger(ReportInfoDaoImpl.class);

    @Override
    public ReportInfoEntity findByName(String name) {
        Criteria criteria = getSession().createCriteria(ReportInfoEntity.class).add(Restrictions.eq("reportName", name));
        return (ReportInfoEntity) criteria.uniqueResult();
    }

    @Override
    protected String getPKfieldName() {
        return "id";
    }

    @Override
    public void createOrUpdateReportInfo(String reportName, String reportDataSource, String reportUrl) {
        ReportInfoEntity reportInfo = findByName(reportName);
        if(reportInfo == null) {
           reportInfo = new ReportInfoEntity();
           reportInfo.setReportName(reportName);
        }
        reportInfo.setDatasourceFilePath(reportDataSource);
        reportInfo.setReportFilePath(reportUrl);
        getSession().saveOrUpdate(reportInfo);
    }
}
