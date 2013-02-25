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

    <% if (SELFSERVICE_CONTEXT == null || SELFSERVICE_CONTEXT.isEmpty()) {%>

    <ul class="menu">

        <li><a <g:if test="${selectedMenuItem == 'DIRECTORY'}">class='active'</g:if> href="<%= SELFSERVICE_BASE_URL %>/pub/directory.do?method=view">Directory Lookup</a></li>
        <li><a <g:if test="${selectedMenuItem == 'UNLOCK_ACCOUNT_URL'}">class='active'</g:if> href="<%= SELFSERVICE_BASE_URL %><%=UNLOCK_ACCOUNT_URL%>">Forgot Password</a></li>
        <li><a <g:if test="${selectedMenuItem == 'REGISTRATION'}">class='active'</g:if> href="<%= SELFSERVICE_EXT_BASE_URL %>/<%= SELFSERVICE_EXT_CONTEXT %>/pub/registration/edit.jsp">Self Registration</a></li>

    </ul>

    <% }else { %>

    <ul class="menu">

        <li><a <g:if test="${selectedMenuItem == 'DIRECTORY'}">class='active'</g:if> href="<%= SELFSERVICE_BASE_URL %>/<%= SELFSERVICE_CONTEXT %>pub/directory.do?method=view">Directory Lookup</a></li>
        <li><a <g:if test="${selectedMenuItem == 'UNLOCK_ACCOUNT_URL'}">class='active'</g:if> href="<%= SELFSERVICE_BASE_URL %>/<%= SELFSERVICE_CONTEXT %><%=UNLOCK_ACCOUNT_URL%>">Forgot Password</a></li>
        <li><a <g:if test="${selectedMenuItem == 'REGISTRATION'}">class='active'</g:if> href="<%= SELFSERVICE_EXT_BASE_URL %>/<%= SELFSERVICE_EXT_CONTEXT %>pub/registration/edit.jsp">Self Registration</a></li>

    </ul>

    <% } %>

</div>


