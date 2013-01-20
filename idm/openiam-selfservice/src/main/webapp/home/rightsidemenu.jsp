<%@ page import="java.util.ResourceBundle" %><%

    ResourceBundle res = ResourceBundle.getBundle("securityconf");
    String UNLOCK_ACCOUNT_URL =  res.getString("UNLOCK_ACCOUNT_URL");

    String APP_BASE_URL =  res.getString("APP_BASE_URL");
    String SELFSERVICE_EXT_CONTEXT =  res.getString("SELFSERVICE_EXT_CONTEXT");
    String SELFSERVICE_CONTEXT =  res.getString("SELFSERVICE_CONTEXT");

%>


<div id="sidebar">
	<div class="head">
		Self - Service Center
	</div>
	
	<ul class="menu">
		<li><a href="<%= APP_BASE_URL %>/<%= SELFSERVICE_CONTEXT %>/pub/directory.do?method=view">Directory Lookup</a></li>
		<li><a href="<%= APP_BASE_URL %>/<%= SELFSERVICE_CONTEXT %><%=UNLOCK_ACCOUNT_URL%>">Forgot Password</a></li>
		<li><a href="<%= APP_BASE_URL %>/<%= SELFSERVICE_EXT_CONTEXT %>/pub/registration/edit.jsp">Self Registration</a></li>

	</ul>
</div>




