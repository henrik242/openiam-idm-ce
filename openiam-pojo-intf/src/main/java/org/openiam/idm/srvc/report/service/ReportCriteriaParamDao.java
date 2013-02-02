package org.openiam.idm.srvc.report.service;

import org.openiam.core.dao.BaseDao;
import org.openiam.idm.srvc.report.domain.ReportCriteriaParamEntity;

import java.util.List;
/**
 * DAO service for ReportCriteriaParamEntity
 *
 * @author vitaly.yakunin
 */
public interface ReportCriteriaParamDao extends BaseDao<ReportCriteriaParamEntity, String> {

    List<ReportCriteriaParamEntity> findByReportInfoId(String reportInfoId);


}
