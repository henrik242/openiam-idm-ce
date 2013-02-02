package org.openiam.webadmin.reports;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openiam.idm.srvc.report.dto.ReportDto;
import org.openiam.idm.srvc.report.ws.GetAllReportsResponse;
import org.openiam.idm.srvc.report.ws.WebReportService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class ReportListController extends SimpleFormController {

    private WebReportService reportService;

    public ReportListController() {
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ReportListCommand reportListCommand = new ReportListCommand();
        if(!request.getParameterMap().containsKey("report.reportId")) {
            GetAllReportsResponse allReportsResponse = reportService.getReports();
            List<ReportDto> reports = (allReportsResponse != null && allReportsResponse.getReports() != null) ? allReportsResponse.getReports() : Collections.EMPTY_LIST;
            reportListCommand.setRepors(reports);
        }
        return reportListCommand;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ReportListCommand reportListCommand = (ReportListCommand) command;
        ReportDto selectedReport = reportListCommand.getReport();
        String reportLocation = selectedReport.getReportUrl();
        if (request.getParameterMap().containsKey("open_btn")) {
            StringBuilder reportViewerLink = new StringBuilder();
            String requestURL = request.getRequestURL().toString();
            String reportViewerUrl = requestURL.substring(0,requestURL.indexOf(request.getContextPath()));
            reportViewerLink.append(reportViewerUrl).append("/reportviewer");
            reportViewerLink.append("/frameset?");
            reportViewerLink.append("__report=").append(reportLocation);
            reportViewerLink.append("&").append("http://localhost:9090/idmsrvc/ReportService?wsdl");

            response.sendRedirect(reportViewerLink.toString());
            return null;
        } else if (request.getParameterMap().containsKey("edit_btn")) {
            ReportCommand reportCommand = new ReportCommand();
            reportCommand.setReport(selectedReport);
            return new ModelAndView(getSuccessView(), "reportCommand", reportCommand);
        } else if (request.getParameterMap().containsKey("add_btn")) {
            ReportCommand reportCommand = new ReportCommand();
            reportCommand.setReport(new ReportDto());
            return new ModelAndView(getSuccessView(), "reportCommand", reportCommand);
        }
        return null;
    }

    public WebReportService getReportService() {
        return reportService;
    }

    public void setReportService(WebReportService reportService) {
        this.reportService = reportService;
    }
}