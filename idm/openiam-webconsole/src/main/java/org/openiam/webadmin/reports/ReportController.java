package org.openiam.webadmin.reports;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto;
import org.openiam.idm.srvc.report.ws.WebReportService;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ReportController extends SimpleFormController {
    private static ResourceBundle res = ResourceBundle.getBundle("securityconf");

    private WebReportService reportService;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ReportCommand reportCommand = ((ReportCommand) command);
        if (request.getParameterMap().containsKey("save")) {
            MultipartFile dataSourceFile = reportCommand.getDataSourceScriptFile();
            MultipartFile designFile = reportCommand.getReportDesignFile();
            String dataSourceFileName = "", designFileName = "";
            if (dataSourceFile.getSize() > 0) {
                dataSourceFileName = dataSourceFile.getOriginalFilename();
                String filePath = res.getString("scriptRoot") + "/reports/" + dataSourceFileName;
                File dest = new File(filePath);
                try {
                    dataSourceFile.transferTo(dest);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (designFile.getSize() > 0) {
                designFileName = designFile.getOriginalFilename();
                String filePath = res.getString("reportRoot") + "/" + designFileName;
                File dest = new File(filePath);
                try {
                    designFile.transferTo(dest);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(StringUtils.isNotEmpty(reportCommand.getReport().getReportName())) {
                List<ReportCriteriaParamDto> params = new LinkedList<ReportCriteriaParamDto>();
                reportService.createOrUpdateReportInfo(reportCommand.getReport().getReportName(), dataSourceFile.getSize() > 0 ? dataSourceFileName : reportCommand.getReport().getReportDataSource(), designFile.getSize() > 0 ? designFileName : reportCommand.getReport().getReportUrl(), params);
            }
        }
        return new ModelAndView(new RedirectView("birtReportList.cnt", true));
    }
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return super.formBackingObject(request);
    }
    @Override
    protected void initBinder (HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    public WebReportService getReportService() {
        return reportService;
    }

    public void setReportService(WebReportService reportService) {
        this.reportService = reportService;
    }
}
