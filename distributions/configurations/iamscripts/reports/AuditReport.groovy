
import groovy.sql.*
import org.openiam.idm.srvc.report.dto.ReportRow
import org.openiam.idm.srvc.report.dto.ReportRow.ReportColumn
import org.openiam.base.id.UUIDGen
import java.util.ResourceBundle;

import org.openiam.idm.srvc.report.dto.ReportTable;
import org.openiam.idm.srvc.report.service.*;
import org.openiam.idm.srvc.report.dto.ReportDataDto;
import java.util.Map;
import java.text.*;



public class AuditReport implements ReportQueryScript { 

public Sql connect() {
	ResourceBundle resDS = ResourceBundle.getBundle("datasource");
	
	def db	=	resDS.getString("openiam.driver_url")  				
	def user		=	resDS.getString("openiam.username")   					
	def password	=	resDS.getString("openiam.password")			
	def driver		= resDS.getString("openiam.driver_classname")
	
	return Sql.newInstance(db,user, password, driver);
	
}

public ReportDataDto getReportData(Map<String, String> reportParams) {
	
	def String action;
	def String startDate;
	def String endDate;
	def String status;	
	def String requestorId;
	def String targetId;
	def String orgId 
	
	def paramList = [];
	
	def sql = connect();
	
	def SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
	java.util.Date eDate;
	java.util.Date sDate;
	

	ReportTable reportTable = new ReportTable();
	reportTable.setName("AuditReport");
	
	
	// get the report parameters
if (reportParams != null) {
	
	action = reportParams.get("ACTION");
	startDate = reportParams.get("START_DATE");
	endDate = reportParams.get("END_DATE");
	status = reportParams.get("STATUS");	
	requestorId = reportParams.get("REQUESTOR_ID");
	targetId = reportParams.get("TARGET_ID");
	orgId = reportParams.get("ORG_ID");
				
}
	

	def String query =  "SELECT DISTINCT ACTION_DATETIME, ACTION_ID, ACTION_STATUS, RESOURCE_NAME, REASON, LOGIN_ID AS TARGET_LOGIN, uv1.FIRST_NAME, uv1.LAST_NAME, c.COMPANY_NAME, " +
            "           CUSTOM_ATTRVALUE3 AS REQUESTOR_LOGIN, uv2.FIRST_NAME AS REQUESTOR_FNAME, uv2.LAST_NAME AS REQUESTOR_LNAME  " +
            "           FROM IDM_AUDIT_LOG a " +
            "           LEFT JOIN USER_IDENTITY_VW uv1 on (a.LOGIN_ID = uv1.LOGIN) " +
            "           LEFT JOIN USER_IDENTITY_VW uv2 on (a.CUSTOM_ATTRVALUE3 = uv2.LOGIN) " +
            "           LEFT JOIN COMPANY c ON (uv1.COMPANY_ID = c.COMPANY_ID)";

	def StringBuffer where = new StringBuffer();


    if (orgId != null && orgId.length() > 0) {
        if (where.length() > 1) {
            where.append(" and ");
        }

        where.append( " c.COMPANY_ID = ? ");
        paramList.add(orgId)

    }
    if (status != null && status.length() > 0) {
        if (where.length() > 1) {
            where.append(" and ");
        }

        where.append( "  a.ACTION_STATUS = ? ");
        paramList.add(status)
    }

    if (action != null && action.length() > 0) {
        if (where.length() > 1) {
            where.append(" and ");
        }

        where.append( "  a.ACTION_ID = ? ");
        paramList.add(action)

    }

    if (requestorId != null && requestorId.length() > 0) {
        if (where.length() > 1) {
            where.append(" and ");
        }

        where.append( " uv2.USER_ID = ? ");
        paramList.add(requestorId)

    }
    if (targetId != null && targetId.length() > 0) {
        if (where.length() > 1) {
            where.append(" and ");
        }

        where.append( " uv1.USER_ID = ? ");
        paramList.add(targetId)

    }

    if (startDate != null ) {
        if (where.length() > 1) {
            where.append(" and ");
        }
        
        // create a date object
        

        sDate = dateFormat.parse(startDate);
        

        where.append( " ACTION_DATETIME >= ? ");
        paramList.add(sDate)

    }

    if (endDate != null ) {
        if (where.length() > 1) {
            where.append(" and ");
            parameters.append(",");
        }

		    eDate = dateFormat.parse(endDate);

        where.append( " ACTION_DATETIME <= ? ");
        paramList.add(eDate)

    }
    if (where.length() > 1) {
        query = query + " WHERE " + where.toString();

    }
    
    query = query + " ORDER BY ACTION_DATETIME";

 

	sql.eachRow(query, paramList) { a ->
      
      
      ReportRow row = new ReportRow();
      row.getColumn().add(new ReportColumn('ACTION_DATETIME',a.ACTION_DATETIME.toString()));
      row.getColumn().add(new ReportColumn('ACTION_ID',a.ACTION_ID));
      row.getColumn().add(new ReportColumn('ACTION_STATUS',a.ACTION_STATUS));
      row.getColumn().add(new ReportColumn('RESOURCE_NAME',a.RESOURCE_NAME));
      row.getColumn().add(new ReportColumn('REASON',a.REASON));
      row.getColumn().add(new ReportColumn('TARGET_LOGIN',a.TARGET_LOGIN));
      row.getColumn().add(new ReportColumn('FIRST_NAME',a.FIRST_NAME));
      row.getColumn().add(new ReportColumn('LAST_NAME',a.LAST_NAME));
      row.getColumn().add(new ReportColumn('COMPANY_NAME',a.COMPANY_NAME));
      row.getColumn().add(new ReportColumn('REQUESTOR_LOGIN',a.REQUESTOR_LOGIN));
      row.getColumn().add(new ReportColumn('REQUESTOR_FNAME',a.REQUESTOR_FNAME));
      row.getColumn().add(new ReportColumn('REQUESTOR_LNAME',a.REQUESTOR_LNAME));
      			
           
      reportTable.getRow().add(row);
}


	ReportDataDto reportDataDto = new ReportDataDto();
	List<ReportTable> reportTables = new ArrayList<ReportTable>();
	reportTables.add(reportTable);
	reportDataDto.setTables(reportTables);
	return reportDataDto;

	
}

	  




}



