<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



<form:form commandName="usergrpCmd">
<form:hidden path="perId" />
<table class="resource alt"> 
	<tbody>
		<tr class="caption">
			<th>Group Name</th>
			<th>Inherit From Parent</th>
			<th>Status</th>
		</tr>
	
    <c:forEach items="${usergrpCmd.groupList}" var="groupList" varStatus="group">
		  
				<tr>
								<td>
									<form:checkbox path="groupList[${group.index}].selected" />
									${groupList.grpName} 
									<form:hidden path="groupList[${group.index}].grpId" />
								</td>
								<td>${groupList.inheritFromParent}</td>
								<td>${groupList.status}</td>
				</tr>
				
			</c:forEach>


  <tr>
    <td colspan="4" align="right"> 
          <input type="submit" value="Submit"/>   
    </td>
  </tr>
  
</table>
</tbody>
</form:form>

