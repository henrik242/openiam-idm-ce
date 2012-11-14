<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%
  String userId = (String)session.getAttribute("userId");
  String token = (String)session.getAttribute("token");
  String login = (String)session.getAttribute("login");
  String queryString = null;


  if (userId != null) {
      queryString = "&userId=" + userId + "&lg="+login + "&tk=" + token;

  }

%>
			<h4>Pending Requests</h4>
			<table class="resource alt">
				<tbody>
					<tr class="caption">
						<th>Create Date</th>
						<th>Requestor</th>
						<th>Status</th>
						<th>Reason</th>
					</tr>
					
<c:if test="${reqList != null}" >
<c:forEach items="${reqList}" var="provisionRequest">
    		<tr>
        		<td><a href="requestDetail.selfserve?requestId=${provisionRequest.requestId}<%=queryString%>"> ${provisionRequest.requestDate}</a></td>
        		<td> ${provisionRequest.requestorId}</td>
        		<td> ${provisionRequest.status}</td>
        		<td> ${provisionRequest.requestTitle}</td>
    		</tr>
</c:forEach>
</c:if>
                    <c:if test="${reqList == null}" >
                        <tr>
                            <td colspan="4">0 Requests are pending review.</td>
                        </tr
                    </c:if>

                </tbody>
			</table>
		</div>
	</div>

