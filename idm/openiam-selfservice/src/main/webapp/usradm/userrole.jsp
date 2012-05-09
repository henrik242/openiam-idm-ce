<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



<form:form commandName="userRoleCmd">

<form:hidden path="perId" />

<table class="resource alt"> 
	<tbody>
		<tr class="caption">
			<th>Role ID</th>
			<th>Role Name</th>
			<th>Parent Role</th>
			<th>Status</th>
		</tr>

	      <c:forEach items="${userRoleCmd.roleList}" var="roleList" varStatus="role">
		  
						<tr>
								<td>
									<form:checkbox path="roleList[${role.index}].selected" />
									${roleList.id.serviceId} - ${roleList.id.roleId}
									<form:hidden path="roleList[${role.index}].id.serviceId" />
									<form:hidden path="roleList[${role.index}].id.roleId" />
								</td>
								<td>${roleList.roleName}</td>
								<td>${roleList.parentRoleId}</td>
								<td>${roleList.status}</td>
						</tr>				
				</c:forEach>

   

  <tr>
    <td colspan="4" align="right"> 
          <input type="submit" value="Submit"/>   
    </td>
  </tr>
  


</form:form>
	<tbody>
</table>
