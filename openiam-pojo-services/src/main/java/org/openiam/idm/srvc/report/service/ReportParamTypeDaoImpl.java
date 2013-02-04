package org.openiam.idm.srvc.report.service;

import org.openiam.core.dao.BaseDaoImpl;
import org.openiam.idm.srvc.report.domain.ReportParamTypeEntity;
import org.springframework.stereotype.Repository;

/**
 * DAO service for ReportParamTypeEntity implementation
 *
 * @author vitaly.yakunin
 */
@Repository
public class ReportParamTypeDaoImpl extends BaseDaoImpl<ReportParamTypeEntity, String> implements ReportParamTypeDao {

    @Override
    protected String getPKfieldName() {
        return "id";
    }
}
