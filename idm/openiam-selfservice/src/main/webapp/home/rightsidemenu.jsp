<%@ page import="java.util.ResourceBundle" %><%

    ResourceBundle res = ResourceBundle.getBundle("securityconf");
    String UNLOCK_ACCOUNT_URL =  res.getString("UNLOCK_ACCOUNT_URL");
%>

<div id="sidebar">
	<div class="head">
		Self - Service Center
	</div>
	
	<ul class="menu">
		<li><a href="<%= request.getContextPath() %>/pub/directory.do?method=view">Directory Lookup</a></li>
		<li><a href="<%= request.getContextPath() %><%=UNLOCK_ACCOUNT_URL%>">Forgot Password</a></li>
		<li><a href="<%= request.getContextPath() %>/pub/selfRegister.selfserve">Self Registration</a></li>

	</ul>
</div>


