<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--@elvariable id="sysMsgListCmd" type="org.openiam.webadmin.admin.sysmsg.SysMsgListCommand"--%>

<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.1.min.js'/>"></script>
<script type="text/javascript">
    var listAction = function(selMsgId) {
        $('#selectedMsgId').val(selMsgId);
        return true;
    };
</script>

<table width="800pt">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="pageTitle" width="70%">
                        <h2 class="contentheading">System Messages</h2>
                    </td>
                </tr>
            </table>
        </td>
    <tr>
        <td>
<form:form id="sysMsgListForm" commandName="sysMsgListCmd">
    <input id="selectedMsgId" type="hidden" name="selectedMsg.msgId" value="" />

              <table width="800pt" class="bodyTable" height="100%">
                    <tr>
                        <td>
                            <fieldset class="userformSearch">
                                <legend>Sys Message List</legend>
                                <table class="fieldsetTable" width="100%" height="200pt">
                                    <tr>
                                        <th>Name</th>
                                        <th>Mail Provider</th>
                                        <th>Mail Template</th>
                                        <th>Type</th>
                                        <th>Actions</th>
                                    </tr>
                                    <c:choose>
                                        <c:when test="${sysMsgListCmd.msgList!=null}">
                                            <c:forEach var="item" items="${sysMsgListCmd.msgList}">
                                                <tr>
                                                    <td>${item.name}</td>
                                                    <td>${item.providerScriptName}</td>
                                                    <td>${item.mailTemplate.name}</td>
                                                    <td>${item.type}</td>
                                                    <td><input type="submit" name="edit_btn" value="Edit" onclick="return listAction('${item.msgId}','edit');"></td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr><td colspan="4">No messages</td></tr>
                                        </c:otherwise>
                                    </c:choose>
                                    <tr>
                                        <td colspan="4" align="left">
                                            <input type="submit" name="add_btn" value="New System Message">
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
