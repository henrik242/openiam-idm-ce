<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ page import="java.util.*,javax.servlet.http.*, org.openiam.idm.srvc.user.dto.User, org.openiam.selfsrvc.JSPUtil" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    String userId = (String)session.getAttribute("userId");
    String token = (String)session.getAttribute("token");
    String login = (String)session.getAttribute("login");
    String queryString = "userId=" + userId + "&lg="+login + "&tk=" + token;

%>


<head>

    <base target="_self" />

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/all.css" type="text/css"/>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
    <script src="<%= request.getContextPath() %>/js/cufon-yui.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath() %>/js/myriad-pro.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath() %>/js/calibri.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.main.js"></script>
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
        <form action="<%=request.getContextPath() %>/selectUser.selfserve?<%=queryString%>" method="post" target="_self">
            <fieldset>
                <div class="block">
                    <div class="wrap alt">
                        <div class="col-1">
                            <div class="row alt">
                                <label for="t-1">First Name</label>
                                <input type="text" name="firstName" size="30" maxlength="30"/>
                                <p class="info">'%' for wildcard searches</p>
                            </div>
                        </div>

                        <div class="col-1">
                            <div class="row alt">
                                <label for="t-2">Last Name</label>
                                <input type="text" name="lastName" size="30" maxlength="30">
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
                <td> <%=JSPUtil.display(user.getTitle())%> </td>
                <td> <%=JSPUtil.display(user.getEmail())%> </td>
                <td> <%=JSPUtil.display(user.getDeptCd())%> </td>
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
	

