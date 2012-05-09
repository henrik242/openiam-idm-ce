<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*, org.openiam.idm.srvc.user.dto.User, org.openiam.selfsrvc.JSPUtil" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<jsp:include page="/usradm/search.jsp" flush="true" />

<%


  String userId = (String)session.getAttribute("userId");
  String token = (String)session.getAttribute("token");
  String login = (String)session.getAttribute("login");
  
  String queryString = "&userId=" + userId + "&lg="+login + "&tk=" + token ;
  
		int size = 0;	// # of pages of data
 		int recordCount = 0;
 		int curPage = 0;
 		
 	 	String msg = (String)request.getAttribute("msg");
 		
 		Integer resultSize = (Integer)request.getAttribute("resultSize");

 		
 		if (resultSize != null) {
 			recordCount = resultSize.intValue();
 		}
	List<User> userList = (List)request.getAttribute("userList");
	if (userList == null){
		userList = new ArrayList();
	}
%>




<h4 class="alignment">Search Results - <%=recordCount%> Found.</h4>
<table class="resource alt">
	<tbody>
		<tr class="caption">
			<th>Name</th>
			<th>E-mail</th>
			<th>Status</th>
			<th>Secondary Status</th>
			<th>&nbsp;</th>
		</tr>
	  
	<% 

	      if( userList != null && userList.size() > 0 ) {
	    	int x=0;
	    	for (User ud : userList) {

	  %>
	
		<tr>
			<td>
		        <% if (ud.getFirstName() != null || ud.getLastName() != null) { %>
		           <a href="<%= request.getContextPath() %>/editUser.selfserve?personId=<%=ud.getUserId()%>&menugrp=SELF_QUERYUSER<%=queryString%>"><%= JSPUtil.display(ud.getFirstName())%> <%=JSPUtil.display(ud.getLastName())%> </a>
		        <% } %>
		    </td>
		    <td><%= JSPUtil.display(ud.getEmail() )%> </td>
		    <td>
		      <% if (ud.getStatus() != null) { %>     
		          <%=JSPUtil.display(ud.getStatus())%>
		      <% } %>
		    </td>
		    <td>
		      <% if (ud.getSecondaryStatus() != null) { %>     
		          <%=JSPUtil.display(ud.getSecondaryStatus())%>
		      <% } %>
		    </td>
		    
			<td>
		         <a href="userChangeStatus.selfserve?personId=<%=ud.getUserId()%>&status=DELETED">DELETE</a>
		    </td>
		    


		 </tr>
		  <%   x++;
		       }
		       }
		  %>
	
		
	</tbody>
</table>
