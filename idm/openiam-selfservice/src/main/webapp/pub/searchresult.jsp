<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*, org.openiam.idm.srvc.user.dto.*,org.openiam.webadmin.busdel.base.*" %>
<%@ page import="org.openiam.idm.srvc.continfo.dto.EmailAddress,org.openiam.idm.srvc.continfo.dto.Phone" %>


<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>


<%
 	int recordCount = 0;
	int maxResultSize = 100;
		
 	Integer resultSize = (Integer)request.getAttribute("resultSize");
 	Integer maxSize = (Integer)request.getAttribute("maxResultSize");
 	if (maxSize != null) {
 		maxResultSize = maxSize.intValue();
 	}
 	String msg = (String)request.getAttribute("msg");
 	
 	List userList = (List)request.getAttribute("userList");
	if (userList == null){
		userList = new ArrayList();
	}
	if (resultSize != null) {
		recordCount = resultSize.intValue();
	}
	// only show the max. number of records.
	if (recordCount > maxResultSize) {
		recordCount = maxResultSize;
	}
%>


<% if (msg != null ) { %>
<p>
    <font color="red"><%=msg %></font>
</p>
<% }  %>

<% if (recordCount > 0 ) { %>
<h4 class="alignment">Search Results - <%=recordCount%> Found.</h4>

<table class="resource alt">
	<tbody>
		<tr class="caption">
			<th>Last Name</th>
			<th>First Name</th>
			<th>Initial</th>
			<th>Dept</th>
			<th>Phone</th>
			<th>E-mail</th>
			<th></th>
		</tr>
  <% 
  
      if( userList != null && userList.size() > 0 ) {
	    	
	    	int x=0;
	    	int size = userList.size();
	    	
	    	// Only show the maxium # of records as defined in the maxResultSize parameter.
	    	if (size > maxResultSize) {
	    		size = maxResultSize;
	    	}
    		for (int i=0; i < recordCount; i++ ) {
    			User ud = (User)userList.get(i);
	
  %>		
  	<tr>
  		  <td >
     	  <% if ( ud.getLastName() != null) { %>
        <a href="viewUser.selfserve?personId=<%=ud.getUserId()%>"><%=ud.getLastName()%></a>
        <% } %>
     	</td>
     <td >
        <% if (ud.getFirstName() != null ) { %>
            <%=ud.getFirstName()%>
        <% } %>&nbsp;
    </td>
     <td >
        <% if (ud.getMiddleInit() != null ) { %>
            <%=ud.getMiddleInit()%>
        <% } %>&nbsp;
    </td>
    <td >
      <% if (ud.getDeptCd() != null) { %>
         <%=ud.getDeptCd()%>
      <% } %>&nbsp;
    </td>
 
    <td> 
    	<% if (ud.getPhoneNbr() != null ) { %>
    	(<%=ud.getAreaCd() %>)<%=ud.getPhoneNbr() %>
    	<% } %>
    </td>
    <td >
     <% if (ud.getEmail() != null ) { %>
    		<%=ud.getEmail() %>
    	<% } %>  
    </td>
      
  	</tr>
  	<%   
       }
       }
  	%>
	</tbody>
</table>

<%   

   }
%>  

  
 