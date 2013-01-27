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

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'all.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <g:javascript library="jquery-1.7.1.min" />
    <g:javascript library="jquery.main" />
    <g:javascript library="cufon-yui.js" />
    <g:javascript library="myriad-pro.js" />
    <g:javascript library="calibri.js" />

    <r:layoutResources />
</head>

<% List<User> userList = (List<User>)session.getAttribute("supervisorList");
String msg = (String)session.getAttribute("msg"); %>

<script type="text/javascript">
    function selUser(userId, name)
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
<div id="wrapper">
    <div id="content">
        <form action="<%= SELFSERVICE_EXT_BASE_URL %>/<%= SELFSERVICE_EXT_CONTEXT %>/priv/newhire/seluser.jsp?<%=queryString%>" method="post" target="_self">
            <fieldset>
                <div class="block">
                    <div class="wrap alt">
                        <div class="col-1">
                            <div class="row alt">
                                <label for="t-1">First Name</label>
                                <input id="t-1" type="text" name="firstName" size="30" maxlength="30"/>
                                <p class="info">'%' for wildcard searches</p>
                            </div>
                        </div>

                        <div class="col-1">
                            <div class="row alt">
                                <label for="t-2">Last Name</label>
                                <input id="t-2" type="text" name="lastName" size="30" maxlength="30">
                            </div>
                        </div>
                        <div class="button">
                            <input type="submit" value="Search"/>

                        </div>
                    </div>
                </div>
            </fieldset>

        </form>
        <h4 class="alignment">Search Results</h4>

        <%	if (session.getAttribute("result") != null) { %>
        <table class="resource alt" width="600">
            <tbody>
            <tr class="caption">
                <th>Name</th>
                <th>Title</th>
                <th>E-mail</th>
                <th>Department</th>
            </tr>

            <% if (msg != null) { %>
            <tr class="error">
                <td colspan="2"><%=msg %></td>
            </tr>
            <% } %>


            <%
                    if (userList != null) {
                        for (User user : userList) { %>


            <tr>

                <td><a href="javascript:selUser('<%=user.getUserId()%>','<%=user.getFirstName() %> <%=user.getLastName() %>' );window.close();">
                    <%=user.getFirstName() %> <%=user.getLastName() %></a></td>
                <td> <%= user.getTitle() %> </td>
                <td> <%= user.getEmail() %> </td>
                <td> <%= user.getDeptCd() %> </td>
            </tr>
            <% 	}
            }
            %>
            </tbody>
        </table>

        <% }  %>
    </div>
</div>
</body>