<%@ page language="java" contentType="text/html;" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--@elvariable id="params" type="java.util.List<org.openiam.idm.srvc.report.dto.ReportParameterDto>"--%>

<div class="params">
    <c:if test="${params!=null}">
        <table>
        <c:forEach var="item" items="${params}" varStatus="status">
           <tr>
           <td>
           <label for="param_${status.count}">${item.name}</label>
           </td>
           <td>
               <input id="paramName_${status.count}" name="paramNames" value="${item.name}" type="hidden" />
               <input id="param_${status.count}" name="paramValues" type="text" />
           </td>
          </tr>
        </c:forEach>
        </table>
    </c:if>
</div>