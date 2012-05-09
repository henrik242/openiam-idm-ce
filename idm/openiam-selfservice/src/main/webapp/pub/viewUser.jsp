<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*,org.apache.struts.validator.*, org.openiam.webadmin.busdel.base.*" %>
<%@ page import="org.openiam.idm.srvc.user.dto.*" %>
<%@ page import="org.openiam.idm.srvc.continfo.dto.*" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<form class="user-info" action="#">
		<fieldset>
					<div class="block">
						<div class="wrap alt">
							<div class="col-1">
								<div class="row">
									<label for="t-1">Name</label>
									${userData.firstName} ${userData.middleInit} ${userData.lastName}
								</div>
								<div class="row">
									<label for="t-1">Title</label>
									${userData.title}
								</div>
								<div class="row">
									<label for="t-1">Organization Name</label>
									${org.organizationName}
								</div>
								<div class="row">
									<label for="t-1">Division</label>
									${userData.division}
								</div>																
								<div class="row">
									<label for="t-1">Department</label>
									${userData.deptName}
								</div>
								<div class="row">
									<label for="t-1">Email Address</label>
									${userData.email}
								</div>	
								<div class="row">
									<label for="t-1">Phone Number</label>
									${userData.areaCd}-${userData.phoneNbr}
								</div>
							</div>
							<div class="col-1">
								<div class="row">
									<label for="t-1">Manager</label>
										<table>
							          <c:if test="${supervisor != null}" >
							          <c:forEach items="${supervisor}" var="manager">		
												<tr>
												 	<td> 
												 		<a href="viewUser.selfserve?personId=${manager.supervisor.userId}">${manager.supervisor.firstName } ${manager.supervisor.lastName }</a>		
													</td>					
												</tr>
												</c:forEach>
												</c:if>
									</table>
								</div>
								<div class="row">
									<label for="t-1">Job Function</label>
									${userData.jobCode}
								</div>
								<div class="row">
									<label for="t-1">Cost Center</label>
									${userData.costCenter}
								</div>

								<div class="row-alt">
									<label for="t-1">Address</label>
									${userData.bldgNum} ${userData.address1} ${userData.streetDirection}<br>
									${userData.address2}							
								</div>								
								<div class="row-alt">
									<label for="t-1">&nbsp;</label>
									${userData.city} ${userData.state} ${userData.postalCd} <br>								
								</div>
								<div class="row-alt">
									<label for="t-1">&nbsp;</label>
									${userData.countryCd}									
								</div>								
								
							</div>						
						</div>
					</div>
				</fieldset>							
</form>

<h4 class="alignment">Direct Reports</h4>
<table class="resource alt">   
	<tbody>	
		<tr class="caption">
			<th>Name</th>
			<th>Phone</th>
			<th>E-mail</th>
		</tr>

     <c:if test="${directReports != null}" >
          <c:forEach items="${directReports}" var="emp">		
			<tr>
				 <td> 
				 		<a href="viewUser.selfserve?personId=${emp.employee.userId}">${emp.employee.firstName } ${emp.employee.lastName }</a>	
				</td>			
				<td>
					<c:if test="${emp.employee.areaCd != null}" >
						(${emp.employee.areaCd})-${emp.employee.phoneNbr} ext: ${emp.employee.phoneExt}
					</c:if>
				</td>
				<td>${emp.employee.email}</td>
			</tr>
		</c:forEach>
		</c:if>
	    <c:if test="${directReports == null}" >
		<tr>
			<td colspan="3"> No Direct Reports Found. </td>
		</tr>
		</c:if>


    
 	</tbody>
</table>
   

</html:form>




