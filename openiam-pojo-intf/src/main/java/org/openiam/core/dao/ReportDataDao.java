package org.openiam.core.dao;

import org.openiam.core.domain.ReportInfo;

public interface ReportDataDao extends BaseDao<ReportInfo> {
    ReportInfo findByName(String name);
}
