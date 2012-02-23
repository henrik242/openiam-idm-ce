<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*, org.openiam.selfsrvc.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>


<%
      	String userId = (String)session.getAttribute("userId");
   		String bodypage = request.getParameter("bodyjsp");
				if (bodypage == null) {
 				bodypage = (String)request.getAttribute("bodyjsp");
 			}
 			System.out.println("body page =" + bodypage);
 			
			  String login = (String)session.getAttribute("login");
			  String pageTitle = (String)session.getAttribute("pageTitle");
			  if (pageTitle == null) {
			  	pageTitle= "";
			  }

				String title = (String)session.getAttribute("title");
				String url = (String)session.getAttribute("logoUrl");
				String welcomePageUrl = (String)session.getAttribute("welcomePageUrl");

				if (url == null) {
					url = (String)request.getAttribute("logoUrl");
					title = (String)request.getAttribute("title");	
				}


%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/all.css" type="text/css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script src="<%= request.getContextPath() %>/js/cufon-yui.js" type="text/javascript"></script>
<script src="<%= request.getContextPath() %>/js/myriad-pro.js" type="text/javascript"></script>
<script src="<%= request.getContextPath() %>/js/calibri.js" type="text/javascript"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.main.js"></script>
<title>OpenIAM Identity Manager v2.1</title>
</head>

<html:base/>

<body>
<div id="wrapper">
	<% if (userId == null) { 
			 if (bodypage != null && bodypage.contains("login")) {

	%>

		<div id="header">&nbsp;</div>
	<%
		} else {
	%>
		<div id="header">
			<p><%=pageTitle%></p>
			<h1 class="logo"><a href="<%=request.getContextPath()%>/"><img src="<%=url%>">OpenIAM</a></h1>
		</div>

	<% } %>
	<% }else { %>
		<div id="header">
			<p><%=pageTitle%></p>
			<h1 class="logo"><a href="<%=request.getContextPath()%>/"><img src="<%=url%>">OpenIAM</a></h1>
		</div>
	<% } %>

	<div id="main">
		<% 
			if (userId == null) { 
		%>
			<tiles:insert attribute='rightsidemenu'/>
	<%
		} else {
	%>
		<tiles:insert attribute='menubar'/>
	<% } %>	

	<div id="content">

			<tiles:insert attribute='body'/>


		</div>
	</div>
</div>
</body>
</html>