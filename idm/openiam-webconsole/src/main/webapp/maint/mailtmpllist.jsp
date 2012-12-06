<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--@elvariable id="mailTmplListCmd" type="org.openiam.webadmin.admin.mailtmpl.MailTemplateListCommand"--%>

<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>
<script type="text/javascript">
    var listAction = function(selTmplId) {
        $('#selectedTmplId').val(selTmplId);
        return true;
    };
</script>

<table width="800pt">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="pageTitle" width="70%">
                        <h2 class="contentheading">Mail Templates</h2>
                    </td>
                </tr>
            </table>
        </td>
    <tr>
        <td>
            <form:form id="mailTmplListForm" commandName="mailTmplListCmd">
                <input id="selectedTmplId" type="hidden" name="selectedTemplate.tmplId" value="" />

                <table width="800pt" class="bodyTable" height="100%">
                    <tr>
                        <td>
                            <fieldset class="userformSearch">
                                <legend>Template List</legend>
                                <table class="fieldsetTable" width="100%" height="200pt">
                                    <tr>
                                        <th>Name</th>
                                        <th>Subject</th>
                                        <th>Body Type</th>
                                        <th>Attachment File</th>
                                        <th>Actions</th>
                                    </tr>
                                    <c:choose>
                                        <c:when test="${mailTmplListCmd.templateList!=null}">
                                            <c:forEach var="item" items="${mailTmplListCmd.templateList}">
                                                <tr>
                                                    <td>${item.name}</td>
                                                    <td>${item.subject}</td>
                                                    <td>${item.type}</td>
                                                    <td>${item.attachmentFilePath}</td>
                                                    <td><input type="submit" name="edit_btn" value="Edit" onclick="return listAction('${item.tmplId}','edit');"></td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr><td colspan="4">No templates</td></tr>
                                        </c:otherwise>
                                    </c:choose>
                                    <tr>
                                        <td colspan="4" align="left">
                                            <input type="submit" name="add_btn" value="New Mail Template">
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
