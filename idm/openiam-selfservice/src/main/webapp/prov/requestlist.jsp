<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<div class="lookup-wrap">
				<h4>Request History</h4>
				<div class="block">
					<div class="wrap alt">
						
<form:form commandName="requestSearchCriteria" cssClass="lookup">
	<fieldset>
						<div class="row alt">
									<label>Status:</label>
										<form:select path="status">
	              			<form:option value="APPROVED" />
	              			<form:option value="REJECTED" />
          					</form:select>
						 </div>
						<div class="row alt">
									<label for="t-1">Date (MM/DD/YYYY):</label>
									<form:input path="startDate" size="10" cssClass="date" />
									<p class="sep">-</p>
									<form:input path="endDate" size="10"  cssClass="date" />
						</div>		
						<div class="row alt">
									<label>Requests By Direct Reports:</label>
									<form:select path="requestorId">
	            	  <form:option value="-" label="-Please Select-"/>
	                  <c:forEach items="${directReports}" var="direct">
		                <form:option value="${direct.employee.userId}" label="${direct.employee.firstName} ${direct.employee.lastName}" />
		              </c:forEach>
          		 </form:select>
								</div>
								<div class="row alt">
									<div class="button">
										<input type="submit" value="Search" />
									</div>
								</div>
																						
</fieldset>

</form:form>
					</div>
				</div>
			</div>

<h4>Search Results</h4>
<table class="resource alt">
    <tbody>
    <tr class="caption">
        <th>Create Date</th>
        <th>Requestor</th>
        <th>Status</th>
        <th>Reason</th>
    </tr>

    <c:if test="${resultList != null}" >
        <c:forEach items="${resultList}" var="provisionRequest">
            <tr>
                <td><a href="requestDetail.selfserve?requestId=${provisionRequest.requestId}"> ${provisionRequest.requestDate}</a></td>
                <td>
                    <c:if test="${provisionRequest.requestorFirstName != null}" >
                        ${provisionRequest.requestorFirstName}
                    </c:if>
                        ${provisionRequest.requestorLastName}
                </td>
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
