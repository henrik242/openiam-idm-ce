<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/all.css" type="text/css"/>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
    <script src="<%= request.getContextPath() %>/js/cufon-yui.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath() %>/js/myriad-pro.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath() %>/js/calibri.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.main.js"></script>
    <title>GTA West DI-r</title>

    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">
</head>

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%@page import="java.util.*, org.openiam.idm.srvc.menu.dto.Menu, org.openiam.idm.srvc.user.dto.User,org.openiam.idm.srvc.user.dto.UserStatusEnum"%>

<%
        ResourceBundle res = ResourceBundle.getBundle("datasource");
    String loginLinks = res.getString("LOGIN_LINKS");
%>
<body bgcolor="white">
<div id="wrapper">

    <div id="header">
        <h2 class="west">GTA DiR</h2>
        <h1 class="logo">OpenIAM</h1>
    </div>
</div>


        <table border="0" width="40%" align="center">
        <tr>
            <td><h3>You have successfully logged out</h3></td>
        </tr>
        <tr>
            <td><h3>For your security we recommend you close your browser.</h3></td>
        </tr>
                <tr>
            <td><h3>Return to<a href='<%=loginLinks %>'> main page </a> .</h3></td>
        </tr>
    </table>
</body>
