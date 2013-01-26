<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'all.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <g:javascript library="jquery-1.7.1.min" />
    <g:javascript library="jquery.main" />
    <g:javascript library="cufon-yui.js" />
    <g:javascript library="myriad-pro.js" />
    <g:javascript library="calibri.js" />


    <title><g:layoutTitle default="OpenIAM Identity Manager Selfservice Ext v2.3.0" /></title>
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">
    <g:layoutHead />
    <r:layoutResources />
</head>
<body>
<%
    ResourceBundle res = ResourceBundle.getBundle("securityconf");
    String SELFSERVICE_BASE_URL =  res.getString("SELFSERVICE_BASE_URL");
    String SELFSERVICE_CONTEXT =  res.getString("SELFSERVICE_CONTEXT");
%>
<div id="wrapper">
    <div id="header">
        <p>OpenIAM Selfservice Application</p>
        <h1 class="logo"><a href="<%= SELFSERVICE_BASE_URL %>/<%= SELFSERVICE_CONTEXT %>">OpenIAM</a></h1>
    </div>
    <div id="main">
        <g:layoutBody />
    </div>
</div>
</body>
</html>