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
	
	def String role;
	def String status;
	def String orgId;
	
	def paramList = [];
	
	def sql = connect();
	
	def SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
	java.util.Date eDate;
	java.util.Date sDate;
	

	ReportTable reportTable = new ReportTable();
	reportTable.setName("UserReport");
	
	
	// get the report parameters
if (reportParams != null) {


	
	role = reportParams.get("ROLE");	
	status = reportParams.get("STATUS");	
	orgId = reportParams.get("ORG_ID");
				
}
	
	
	 def String query  = "select FIRST_NAME, MIDDLE_INIT, LAST_NAME, TITLE, c.COMPANY_NAME, u.STATUS, u.SECONDARY_STATUS, EMPLOYEE_ID, EMAIL_ADDRESS, l.LOGIN ,l.LAST_LOGIN " +
                "				FROM USERS u JOIN LOGIN l ON (u.USER_ID = l.USER_ID) " +
                "   			LEFT JOIN COMPANY c ON (c.COMPANY_ID = u.COMPANY_ID )" +
                "   			LEFT JOIN USER_ROLE ur ON (ur.USER_ID = u.USER_ID) " +
                "   		where l.MANAGED_SYS_ID = '0' " ;

        if (orgId != null && orgId.length() > 0) {
            query = query + " and u.COMPANY_ID = '" + orgId + "' ";

        }
        if (status != null && status.length() > 0) {
						if ("DISABLE".equalsIgnoreCase(status)) {
							query = query + " and u.SECONDARY_STATUS = '" + status + "' ";
						}else {
						 	query = query + " and u.STATUS = '" + status + "' ";
          	}

        }

        if (role != null && role.length() > 0) {
            query = query + " and ur.ROLE_ID  = '" + role + "' ";
        }
        
		query = query + "ORDER BY LAST_NAME";
	

	sql.eachRow(query) { a ->
      
      
      ReportRow row = new ReportRow();
      row.getColumn().add(new ReportColumn('FIRST_NAME',a.FIRST_NAME));
      row.getColumn().add(new ReportColumn('MIDDLE_INIT',a.MIDDLE_INIT));
      row.getColumn().add(new ReportColumn('LAST_NAME',a.LAST_NAME));
      row.getColumn().add(new ReportColumn('TITLE',a.TITLE));
      row.getColumn().add(new ReportColumn('COMPANY_NAME',a.COMPANY_NAME));
      row.getColumn().add(new ReportColumn('STATUS',a.STATUS));
      row.getColumn().add(new ReportColumn('EMPLOYEE_ID',a.EMPLOYEE_ID));
      row.getColumn().add(new ReportColumn('EMAIL_ADDRESS',a.EMAIL_ADDRESS));
      row.getColumn().add(new ReportColumn('LAST_LOGIN',a.LAST_LOGIN.toString()));
      row.getColumn().add(new ReportColumn('LOGIN',a.LOGIN));
      			
           
      reportTable.getRow().add(row);
}


	ReportDataDto reportDataDto = new ReportDataDto();
	List<ReportTable> reportTables = new ArrayList<ReportTable>();
	reportTables.add(reportTable);
	reportDataDto.setTables(reportTables);
	return reportDataDto;

	
}

	  




}

