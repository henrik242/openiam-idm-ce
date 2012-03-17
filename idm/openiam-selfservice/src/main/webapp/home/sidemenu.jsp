
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,org.openiam.idm.srvc.menu.dto.Menu,org.openiam.idm.srvc.user.dto.User, org.openiam.idm.srvc.user.dto.UserStatusEnum" %>


<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>



<%

    System.out.println("sidemenu.jsp called.");

    String sideMenuGroup = (String)request.getAttribute("menuGroup");
    List<Menu> menuL3 = (List<Menu>)request.getAttribute("menuL3");
    String objId = (String)request.getAttribute("objId");


    String userId = (String)session.getAttribute("userId");
    String token = (String)session.getAttribute("token");
    String login = (String)session.getAttribute("login");
    String queryString = null;

    System.out.println("user=" + userId);

    if (userId != null) {
        queryString =  "&tk=" + token;

    }

    User personData = (User)request.getAttribute("personData");
    List menuList = (List) session.getAttribute("sideMenus");
    String personId = (String)request.getAttribute("personId");

    UserStatusEnum status = null;
    if (personData != null) {
        status = personData.getStatus();
    }

%>
<%
    if ( menuL3 != null ) {
%>

<% for (Menu menu : menuL3) {
    String url = null;
    if ( menu.getUrl().contains("?") ) {
        url = menu.getUrl() + "&" +  "menuid=" + menu.getId() + "&menugrp=" + menu.getMenuGroup() + "&objId=" + objId + queryString;
    }else {
        url = menu.getUrl() + "?" +  "menuid=" + menu.getId().getMenuId() + "&menugrp=" + menu.getMenuGroup() + "&objId=" + objId + queryString;
    }
    if (personId != null) {
        url = url + "&personId=" + personId + queryString;
    }

%>
<li><a href="<%=url %>"><%=menu.getMenuName() %></a></li>
<%
    }
%>

<%
    }

%>
