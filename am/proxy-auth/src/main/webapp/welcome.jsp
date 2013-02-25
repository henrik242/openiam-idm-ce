<%@ page language="java" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="java.util.*"  %>
<%@ page import="org.openiam.idm.srvc.user.dto.User"  %>
<%@ page import="org.openiam.idm.srvc.user.dto.UserAttribute"  %>

<%

  String token = (String)session.getAttribute("token");
  String userId = (String)session.getAttribute("userId");
  String login = (String)session.getAttribute("login");
 %>

    <table width="80%" border="0" cellspacing="1" cellpadding="1">
      <tr align="center" valign="top">
        <td colspan="2" class="normaltext" >Welcome ${user.firstName} ${user.lastName}</td>
      </tr>
      <tr align="center" valign="top">
        <td></td>
      </tr>
      <tr>
		 <td align="right" ><b>Member of Groups:</b></td>
         <td valign="center">
         <c:if test="${groupList != null}" >
         	<table>
			<c:forEach items="${groupList}" var="group">
	       		<tr>
	       			<td>${group.grpName}</td>
	       		</tr>
       		</c:forEach>
       		</table>
       	</c:if>
       	</td>
       </tr>
       <tr>
		<td align="right" ><b>Member of Roles:</b></td>
        <td >
        <c:if test="${roleList != null}" >
         	<table>
			<c:forEach items="${roleList}" var="role">
	       		<tr>
	       			<td>${role.roleName}</td>
	       		</tr>
       		</c:forEach>
       		</table>
       	</c:if>
		</td>
      </tr>
       <tr>
		 <td align="right"><b>Title:</b></td>
         <td valign="center">${user.title} </td>
       </tr>

         <tr>
		 <td align="right"><b>Last Login:</b> </td>
         <td valign="center">${primaryIdentity.lastLogin}</td>
       </tr>


      <c:if test="${challenge == false}" >

      <tr  valign="top" align="center">
        <td colspan="2"><font color="red"><b>NO REQUEST URL FOUND.</b></font></td>
         <td> </td>
      </tr>
      </c:if>


    </table>



