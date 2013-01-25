<%@ page import="java.util.ResourceBundle" %>
<%

    ResourceBundle res = ResourceBundle.getBundle("securityconf");
    String UNLOCK_ACCOUNT_URL =  res.getString("UNLOCK_ACCOUNT_URL");

    String SELFSERVICE_BASE_URL =  res.getString("SELFSERVICE_BASE_URL");
    String SELFSERVICE_EXT_BASE_URL =  res.getString("SELFSERVICE_EXT_BASE_URL");
    String SELFSERVICE_EXT_CONTEXT =  res.getString("SELFSERVICE_EXT_CONTEXT");
    String SELFSERVICE_CONTEXT =  res.getString("SELFSERVICE_CONTEXT");

%>

<div id="sidebar">
    <div class="head">
        Self - Service Center
    </div>

    <ul class="menu">
        <%
            String userId = (String)session.getAttribute("userId");
            if (userId != null && userId.length() > 0 ) { %>
        <li> <a href="<%= request.getContextPath() %>/priv/profile/edit.jsp" <g:if test="${selectedMenuItem == 'profile'}">class='active'</g:if>>User Profile</a></li>
        <li> <a href="<%= request.getContextPath() %>/priv/newhire/edit.jsp" <g:if test="${selectedMenuItem == 'newhire'}">class='active'</g:if>>New Hire</a></li>

        <% }else {  %>

        <li><a href="<%= SELFSERVICE_BASE_URL %>/<%= SELFSERVICE_CONTEXT %>/pub/directory.do?method=view">Directory Lookup</a></li>
        <li><a href="<%= SELFSERVICE_BASE_URL %>/<%= SELFSERVICE_CONTEXT %><%=UNLOCK_ACCOUNT_URL%>">Forgot Password</a></li>
        <li><a href="<%= SELFSERVICE_EXT_BASE_URL %>/<%= SELFSERVICE_EXT_CONTEXT %>/pub/registration/edit.jsp">Self Registration</a></li>

        <% } %>
    </ul>
</div>


