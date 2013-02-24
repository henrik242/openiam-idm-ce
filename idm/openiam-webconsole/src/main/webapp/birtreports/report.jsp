<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--@elvariable id="reportCommand" type="org.openiam.webadmin.reports.ReportCommand"--%>
<%--@elvariable id="reportParameters" type="java.util.List<org.openiam.idm.srvc.report.dto.ReportCriteriaParamDto>"--%>
<%--@elvariable id="reportTypes" type="java.util.List<org.openiam.idm.srvc.report.dto.ReportParamTypeDto>"--%>

<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>
<script type="text/javascript">
    $(function() {
        $('#sourceFileInpId').change(function(e) {
            $('#fakeReportDataSourceFileInput').val($(this).val());
        });
        $('#designFileInpId').change(function(e) {
            $('#fakeReportDesignFileInput').val($(this).val());
        });
    });
    var selectDataSourceFile = function() {
        $('#sourceFileInpId').click();
        return false;
    };
    var selectDesignFile = function() {
        $('#designFileInpId').click();
        return false;
    };
    var addParameter = function() {
        var newrow = '<tr><td><span>Name: </span><input type="text" name="paramName" value=""/></td><td><span>Type: </span><select name="paramTypeId" id="paramTypeId"><option value="0">-Select a value</option><c:forEach items="${reportTypes}" var="paramType" varStatus="sts"><option value="${paramType.id}" label="${paramType.description}">${paramType.description}</option></c:forEach></select><span><a href="#" onclick="return remove(this);">remove</a></span></td></tr>';
        $('#paramTable').append(newrow);
        return false;
    };
    var remove = function(obj) {
        $(obj).closest('tr').remove();
        return false;
    };
</script>
<table width="800pt">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="pageTitle" width="70%">
                        <h2 class="contentheading"><c:choose><c:when test="${reportCommand.report.reportName!=null}">Edit Report ${reportCommand.report.reportName}</c:when><c:otherwise>New Report</c:otherwise></c:choose></h2>
                    </td>
                </tr>
            </table>
        </td>
    <tr>
        <td>
            <form:form method="POST" id="reportCommand" action="birtReport.cnt" commandName="reportCommand" enctype="multipart/form-data">
                <input type="hidden" name="report.reportId" value="${reportCommand.report.reportId}" />
                <c:if test="${reportCommand.report.reportName!=null}">
                    <input type="hidden" name="report.reportName" value="${reportCommand.report.reportName}" />
                </c:if>
                <input type="hidden" name="report.reportDataSource" value="${reportCommand.report.reportDataSource}" />
                <input type="hidden" name="report.reportUrl" value="${reportCommand.report.reportUrl}" />
                <table width="650pt" class="bodyTable" height="100%">
                    <tr>
                        <td>
                            <fieldset class="userformSearch">
                                <legend>Report settings</legend>

                                <table class="fieldsetTable" width="100%" height="200pt">
                                    <c:if test="${reportCommand.report.reportName==null}">
                                       <tr valign="top">
                                        <td class="tddark" width="200pt">
                                            <label class="control-label" for="reportName">
                                                Please enter report name
                                            </label>
                                        </td>
                                        <td class="msg">
                                            <input type="text" id="reportName"
                                                   name="report.reportName"
                                                   value="${reportCommand.report.reportName}"  size="60" />
                                        </td>
                                    </tr>
                                    </c:if>
                                    <tr valign="top">
                                        <td class="tddark" width="200pt">
                                            <label class="control-label" for="sourceFileInpId">
                                                Please upload report datasource script:
                                            </label>
                                        </td>
                                        <td class="msg">
                                            <input type="text" id="fakeReportDataSourceFileInput" readonly="true"
                                                   value="${reportCommand.report.reportDataSource}"  size="60" />
                                            <input type="button" value="Select" style="font-size:0.8em" onclick="selectDataSourceFile();"/>
                                            <input id="sourceFileInpId" type="file" style="display:none"
                                                   name="dataSourceScriptFile" value="${reportCommand.report.reportDataSource}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="tddark" width="200pt">
                                            <label class="control-label" for="designFileInpId">
                                                Please upload report design file:
                                            </label>
                                        </td>
                                        <td class="msg">
                                            <input type="text" id="fakeReportDesignFileInput" readonly="true"
                                                   value="${reportCommand.report.reportUrl}"  size="60"/>
                                            <input type="button" value="Select" style="font-size:0.8em" onclick="selectDesignFile();"/>
                                            <input id="designFileInpId" type="file" style="display:none"
                                                   name="reportDesignFile"
                                                   value="${reportCommand.report.reportUrl}">
                                        </td>
                                    </tr>
                                    <tr><td colspan="2">Parameters</td></tr>
                                    <tr>
                                    <td colspan="2">
                                     <table id="paramTable">
                                            <c:choose>
                                            <c:when test="${not empty reportParameters}">
                                            <c:forEach items="${reportParameters}" var="rep" varStatus="status">
                                            <tr>
                                                <td>
                                                    <span>Name: </span>
                                                    <input type="text"
                                                           name="paramName"
                                                           value="${rep.name}"/>
                                                </td>
                                                <td>
                                                    <span>Type: </span>
                                                    <select name="paramTypeId">
                                                        <option value="0">-Select a value</option>
                                                        <c:forEach items="${reportTypes}" var="paramType" varStatus="sts">
                                                            <option <c:if test="${paramType.id == rep.typeId}"> selected="selected" </c:if> value="${paramType.id}" label="${paramType.description}">${paramType.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <c:if test="${status.index > 0}"><span><a href="#" onclick="return remove(this);">remove</a></span> </c:if>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td >
                                                        <span>Name: </span>
                                                        <input type="text"
                                                               name="paramName"
                                                               value=""/>
                                                    </td>
                                                    <td>
                                                        <span>Type: </span>
                                                        <form:select path="paramTypeId" multiple="false">
                                                            <form:option value="0"  label="-Select a value" />
                                                            <form:options items="${reportTypes}" itemValue="id" itemLabel="description"  />
                                                        </form:select>
                                                        <c:if test="${status.index > 0}"><span><a href="#" onclick="return remove(this);">remove</a></span> </c:if>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                            </c:choose>
                                       </table>
                                    </td>
                                    </tr>
                                    <tr><td colspan="2"><a href="#" onclick="return addParameter();">Add</a></td></tr>
                                    <tr>
                                        <td colspan="2" align="right">
                                           <%-- <input type="submit" name="delete" value="Delete">--%>
                                            <input type="submit" name="cancel" value="Cancel">
                                            <input type="submit" name="save" value="Save">
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>
