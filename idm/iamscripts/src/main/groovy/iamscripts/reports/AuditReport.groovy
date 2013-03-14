package reports

import org.openiam.idm.srvc.report.service.ReportDataSetBuilder
import org.openiam.idm.srvc.report.dto.ReportDataDto
import groovy.sql.Sql
import java.text.SimpleDateFormat
import org.openiam.idm.srvc.report.dto.ReportTable
import org.openiam.idm.srvc.report.dto.ReportRow
import org.openiam.idm.srvc.report.dto.ReportRow.ReportColumn

class AuditReport implements ReportDataSetBuilder {
    public Sql connect() {
        ResourceBundle resDS = ResourceBundle.getBundle("datasource");

        def db = resDS.getString("openiam.driver_url")
        def user = resDS.getString("openiam.username")
        def password = resDS.getString("openiam.password")
        def driver = resDS.getString("openiam.driver_classname")

        return Sql.newInstance(db, user, password, driver);
    }

    @Override
    ReportDataDto getReportData(Map<String, String> reportParams) {
        def String action;
        def String startDate;
        def String endDate;

        def paramList = [];

        def sql = connect();

        def SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date eDate;
        java.util.Date sDate;

        ReportTable reportTable = new ReportTable();
        reportTable.setName("AuditReportTable1");

        // get the report parameters
        if (reportParams != null) {
            action = reportParams.get("ACTION_ID");
            startDate = reportParams.get("ACTION_DATETIME_START");
            endDate = reportParams.get("ACTION_DATETIME_END");
        }

        def String query = "SELECT IDM_AUDIT_LOG.LOG_ID, IDM_AUDIT_LOG.OBJECT_TYPE_ID, IDM_AUDIT_LOG.OBJECT_ID," +
                "IDM_AUDIT_LOG.ACTION_ID, IDM_AUDIT_LOG.ACTION_STATUS, IDM_AUDIT_LOG.REASON," +
                "IDM_AUDIT_LOG.REASON_DETAIL, IDM_AUDIT_LOG.ACTION_DATETIME," +
                "IDM_AUDIT_LOG.OBJECT_NAME, IDM_AUDIT_LOG.RESOURCE_NAME, IDM_AUDIT_LOG.USER_ID," +
                "IDM_AUDIT_LOG.SERVICE_ID, IDM_AUDIT_LOG.LOGIN_ID, IDM_AUDIT_LOG.HOST," +
                "IDM_AUDIT_LOG.CLIENT_ID, IDM_AUDIT_LOG.REQ_URL, IDM_AUDIT_LOG.LINKED_LOG_ID," +
                "IDM_AUDIT_LOG.LINK_SEQUENCE, IDM_AUDIT_LOG.ORIG_OBJECT_STATE," +
                "IDM_AUDIT_LOG.NEW_OBJECT_STATE, IDM_AUDIT_LOG.SRC_SYSTEM_ID," +
                "IDM_AUDIT_LOG.TARGET_SYSTEM_ID, IDM_AUDIT_LOG.REQUEST_ID, IDM_AUDIT_LOG.SESSION_ID," +
                "IDM_AUDIT_LOG.CUSTOM_ATTRNAME1, IDM_AUDIT_LOG.CUSTOM_ATTRNAME2," +
                "USERS.FIRST_NAME, USERS.MIDDLE_INIT, USERS.LAST_NAME, USERS.DEPT_CD," +
                "USERS.DEPT_NAME FROM IDM_AUDIT_LOG, USERS";

        def StringBuffer where = new StringBuffer();
        where.append(" IDM_AUDIT_LOG.USER_ID = USERS.USER_ID ");

        if (action != null && action.length() > 0) {
            if (where.length() > 1) {
                where.append(" and ");
            }

            where.append("  IDM_AUDIT_LOG.ACTION_ID = ? ");
            paramList.add(action)
        }
        if (startDate != null) {
            if (where.length() > 1) {
                where.append(" and ");
            }
            // create a date object

            sDate = dateFormat.parse(startDate);

            where.append(" IDM_AUDIT_LOG.ACTION_DATETIME >= ? ");
            paramList.add(sDate)
        }

        if (endDate != null) {
            if (where.length() > 1) {
                where.append(" and ");
            }

            eDate = dateFormat.parse(endDate);

            where.append(" IDM_AUDIT_LOG.ACTION_DATETIME <= ? ");
            paramList.add(eDate)

        }
        if (where.length() > 1) {
            query = query + " WHERE " + where.toString();
        }

        sql.eachRow(query, paramList) { a ->
            ReportRow row = new ReportRow();
            row.getColumn().add(new ReportColumn('LOG_ID', a.LOG_ID));
            row.getColumn().add(new ReportColumn('OBJECT_TYPE_ID', a.OBJECT_TYPE_ID));
            row.getColumn().add(new ReportColumn('OBJECT_ID', a.OBJECT_ID));
            row.getColumn().add(new ReportColumn('ACTION_ID', a.ACTION_ID));
            row.getColumn().add(new ReportColumn('ACTION_STATUS', a.ACTION_STATUS));
            row.getColumn().add(new ReportColumn('REASON', a.REASON));
            row.getColumn().add(new ReportColumn('REASON_DETAIL', a.REASON_DETAIL));
            row.getColumn().add(new ReportColumn('ACTION_DATETIME', a.ACTION_DATETIME.toString()));
            row.getColumn().add(new ReportColumn('OBJECT_NAME', a.OBJECT_NAME));
            row.getColumn().add(new ReportColumn('RESOURCE_NAME', a.RESOURCE_NAME));
            row.getColumn().add(new ReportColumn('USER_ID', a.USER_ID));
            row.getColumn().add(new ReportColumn('SERVICE_ID', a.SERVICE_ID));
            row.getColumn().add(new ReportColumn('LOGIN_ID', a.LOGIN_ID));
            row.getColumn().add(new ReportColumn('HOST', a.HOST));
            row.getColumn().add(new ReportColumn('CLIENT_ID', a.CLIENT_ID));
            row.getColumn().add(new ReportColumn('REQ_URL', a.REQ_URL));
            row.getColumn().add(new ReportColumn('LINKED_LOG_ID', a.LINKED_LOG_ID));
            row.getColumn().add(new ReportColumn('LINK_SEQUENCE', a.LINK_SEQUENCE.toString()));
            row.getColumn().add(new ReportColumn('ORIG_OBJECT_STATE', a.ORIG_OBJECT_STATE));
            row.getColumn().add(new ReportColumn('NEW_OBJECT_STATE', a.NEW_OBJECT_STATE));
            row.getColumn().add(new ReportColumn('SRC_SYSTEM_ID', a.SRC_SYSTEM_ID));
            row.getColumn().add(new ReportColumn('TARGET_SYSTEM_ID', a.TARGET_SYSTEM_ID));
            row.getColumn().add(new ReportColumn('REQUEST_ID', a.REQUEST_ID));
            row.getColumn().add(new ReportColumn('SESSION_ID', a.SESSION_ID));
            row.getColumn().add(new ReportColumn('CUSTOM_ATTRNAME1', a.CUSTOM_ATTRNAME1));
            row.getColumn().add(new ReportColumn('CUSTOM_ATTRNAME2', a.CUSTOM_ATTRNAME2));
            row.getColumn().add(new ReportColumn('FIRST_NAME', a.FIRST_NAME));
            row.getColumn().add(new ReportColumn('MIDDLE_INIT', a.MIDDLE_INIT));
            row.getColumn().add(new ReportColumn('LAST_NAME', a.LAST_NAME));
            row.getColumn().add(new ReportColumn('DEPT_CD', a.DEPT_CD));
            row.getColumn().add(new ReportColumn('DEPT_NAME', a.DEPT_NAME));
            reportTable.getRow().add(row);
        }

        ReportDataDto reportDataDto = new ReportDataDto();
        List<ReportTable> reportTables = new ArrayList<ReportTable>();
        reportTables.add(reportTable);
        reportDataDto.setTables(reportTables);
        return reportDataDto;
    }

}
