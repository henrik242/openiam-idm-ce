package org.openiam.core.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openiam.core.domain.ReportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDataDaoImpl extends BaseDaoImpl<ReportInfo> implements ReportDataDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger LOG = LoggerFactory.getLogger(ReportDataDaoImpl.class);

    @Override
    public ReportInfo findByName(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReportInfo.class).add(Restrictions.eq("reportName", name));
        return (ReportInfo) criteria.uniqueResult();
    }
}
