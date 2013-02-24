<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--@elvariable id="reports" type="java.util.List<org.openiam.core.dto.reports.ReportDto>"--%>
<%--@elvariable id="reportListCommand" type="org.openiam.webadmin.reports.ReportListCommand"--%>

<!-- OpenIAM Legacy style sheets -->


<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>
<script type="text/javascript">
    var selectReport = function(reportId, reportName, reportDataSource, reportUrl, actionName) {
        $('#selectedReportId').val(reportId);
        $('#selectedReportName').val(reportName);
        $('#selectedReportDataSource').val(reportDataSource);
        $('#selectedReportUrl').val(reportUrl);
        if(actionName == 'open') {
          $('#reportListForm').attr('target','_blank');
        } else {
           $('#reportListForm').removeAttr('target');
        }
        return true;
    };
</script>
<table width="800pt">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="pageTitle" width="70%">
                        <h2 class="contentheading">Birt Reports</h2>
                    </td>
                </tr>
            </table>
        </td>
    <tr>
        <td>
            <form:form id="reportListForm" commandName="reportListCommand">
                <input id="selectedReportId" type="hidden" name="report.reportId" value="" />
                <input id="selectedReportName" type="hidden" name="report.reportName" value="" />
                <input id="selectedReportDataSource" type="hidden" name="report.reportDataSource" value="" />
                <input id="selectedReportUrl" type="hidden" name="report.reportUrl" value="" />

                <table width="800pt" class="bodyTable" height="100%">
                    <tr>
                        <td>
                            <fieldset class="userformSearch">
                                <legend>REPORT PARAMETERS</legend>
                                <table class="fieldsetTable" width="100%" height="200pt">
                                    <tr>
                                        <th>Report Name</th>
                                        <th>Data Source Script</th>
                                        <th>Design File</th>
                                        <th>Actions</th>
                                    </tr>
                                    <c:choose>
                                    <c:when test="${reportListCommand.reports!=null}">
                                        <c:forEach var="item" items="${reportListCommand.reports}">
                                        <tr>
                                            <td>${item.reportName}</td>
                                            <td>${item.reportDataSource}</td>
                                            <td>${item.reportUrl}</td>
                                            <td>
                                                <input type="submit" name="edit_btn" value="Edit" onclick="return selectReport('${item.reportId}','${item.reportName}','${item.reportDataSource}','${item.reportUrl}','edit');">
                                                <c:if test="${item.reportUrl != ''}">
                                                    <input type="submit" name="open_btn" value="Open" onclick="return selectReport('${item.reportId}','${item.reportName}','${item.reportDataSource}','${item.reportUrl}','open');">
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                   </c:when>
                                   <c:otherwise>
                                        <tr><td colspan="4">No reports</td></tr>
                                   </c:otherwise>
                                   </c:choose>
                                    <tr><td colspan="4" align="left"><input type="submit" name="add_btn" value="New"></td></tr>
                                </table>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>

