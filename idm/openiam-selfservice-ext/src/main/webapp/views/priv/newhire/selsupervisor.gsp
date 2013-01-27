<%@ page import="org.openiam.idm.srvc.user.dto.User" %>

<%
    String userId = (String)session.getAttribute("userId");
    String token = (String)session.getAttribute("token");
    String login = (String)session.getAttribute("login");
    String queryString = "userId=" + userId + "&lg="+login + "&tk=" + token;

    ResourceBundle res = ResourceBundle.getBundle("securityconf");

    String SELFSERVICE_BASE_URL =  res.getString("SELFSERVICE_BASE_URL");
    String SELFSERVICE_EXT_BASE_URL =  res.getString("SELFSERVICE_EXT_BASE_URL");
    String SELFSERVICE_EXT_CONTEXT =  res.getString("SELFSERVICE_EXT_CONTEXT");
    String SELFSERVICE_CONTEXT =  res.getString("SELFSERVICE_CONTEXT");

    String selfserviceExt =  SELFSERVICE_EXT_BASE_URL + "/" + SELFSERVICE_EXT_CONTEXT;
    String selfservice =  SELFSERVICE_BASE_URL + "/" + SELFSERVICE_CONTEXT;
%>

<head>

    <base target="_self" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'diamelleapp.css')}" />

    <r:layoutResources />
</head>

<% List<User> userList = (List<User>)session.getAttribute("supervisorList"); %>

<script type="text/javascript">
    function selSupervisor(userId, name)
    {

        var o = new Object();
        o.id = userId;
        o.name = name;
        if(window.opener){
            window.opener.returnValue = o;
        }
        window.returnValue = o;
    }
</script>

<body>

<form action="<%= SELFSERVICE_EXT_BASE_URL %>/<%= SELFSERVICE_EXT_CONTEXT %>/priv/newhire/selsupervisor.jsp?<%=queryString%>" method="post" target="_self">
    <table width="620" border="0" cellspacing="2" cellpadding="1" align="center">
        <tr>
            <td colspan="3" class="title">
                <strong>Select User</strong>
            </td>
            <td class="text" align="right">
                <font size="1" color="red">*</font> Required
            </td>
        </tr>

        <tr>
            <td colspan="4" align="center" bgcolor="8397B4" >
                <font></font>
            </td>
        </tr>
        <tr>
            <td class="plaintext" align="right">First Name</td>
            <td class="plaintext" ><input type="text" name="firstName" size="30"></td>

            <td class="plaintext" align="right">Last Name</td>
            <td class="plaintext" ><input type="text" name="lastName" size="30"></td>
        </tr>



        <tr>
            <td class="plaintext"></td>
            <td class="plaintext">


            </td>

        </tr>

        <tr>
            <td colspan="4">&nbsp;</td>
        </tr>

        <tr>
            <td colspan="4" align="center" bgcolor="8397B4" >
                <font></font>
            </td>
        </tr>

        <tr>
            <td colspan="4" align="right">
                <input type="submit" value="Search"/>  <input type="submit" value="Close" onClick="window.close();"/>
            </td>
        </tr>

    </table>
</form>


<%
    if (userList != null) {
%>
<table width="620" border="0" cellspacing="2" cellpadding="1" align="center">
    <tr>
        <td colspan="4" class="title">
            <strong>Search Results</strong>
        </td>
    </tr>

    <tr>
        <td colspan="4" align="center" bgcolor="8397B4" >
            <font></font>
        </td>
    </tr>

    <tr class="plaintext">

        <td>Name </td>
        <td>Department</td>
        <td></td>
    </tr>
    <%	for (User user : userList) {
        String lastName = user.getLastName();
        lastName = lastName.replace("'"," ");
    %>

    <tr class="plaintext">

        <td> <%=user.getFirstName() %> <%=user.getLastName() %></td>
        <td> <%=user.getDeptCd()%> </td>
        <td> </td>
        <td>
            <a href="javascript:selSupervisor('<%=user.getUserId()%>','<%=user.getFirstName() %> <%=lastName %>' );window.close();">Select</a></td>
    </tr>
    <% 	}
    %>
</table>
<%
    }
%>
</body>