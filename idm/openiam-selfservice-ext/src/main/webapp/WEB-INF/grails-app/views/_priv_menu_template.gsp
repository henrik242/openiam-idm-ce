<%@ page import="org.openiam.idm.srvc.res.dto.Resource; org.openiam.idm.srvc.menu.dto.Menu; java.util.ResourceBundle" %>
<%

    ResourceBundle res = ResourceBundle.getBundle("securityconf");

    String SELFSERVICE_BASE_URL =  res.getString("SELFSERVICE_BASE_URL");
    String SELFSERVICE_EXT_BASE_URL =  res.getString("SELFSERVICE_EXT_BASE_URL");
    String SELFSERVICE_EXT_CONTEXT =  res.getString("SELFSERVICE_EXT_CONTEXT");
    String SELFSERVICE_CONTEXT =  res.getString("SELFSERVICE_CONTEXT");

    String selfserviceExt =  SELFSERVICE_EXT_BASE_URL + "/" + SELFSERVICE_EXT_CONTEXT;
    String selfservice =  SELFSERVICE_BASE_URL + "/" + SELFSERVICE_CONTEXT;

    String rMenu = (String)request.getSession().getAttribute("hideRMenu");
    if (rMenu == null ||  !rMenu.equals("1")) {

        System.out.println("menubar.jsp");
        String userId = (String)session.getAttribute("userId");
        String token = (String)session.getAttribute("token");


        List<Menu> privRightMenuList1 = (List<Menu>)session.getAttribute("privateRightMenuGroup1");
        List<Menu> privRightMenuList2 = (List<Menu>)session.getAttribute("privateRightMenuGroup2");
        String queryString = null;

        if (userId != null) {
            if (token != null) {
                queryString =  "tk=" + token ;
            }
        }


%>

<div id="sidebar">
    <%
            if (userId != null && privRightMenuList1 != null ) {
    %>
    <div class="head">
    Access Management Center
    </div>

    <ul class="menu">
        <%
                for (Menu m: privRightMenuList1) {
                    if (m.getSelected()) {
                        String url = m.getUrl();
                        if (url != null) {
                            if (url.indexOf("?") == -1) {
                                url = url + "?" + queryString + "&menuid=" + m.getId().getMenuId() + "&l=p";
                            } else {
                                url = url + "&"  + queryString + "&menuid=" + m.getId().getMenuId() + "&l=p";
                            }

                            url = url.replace("{SELFSERVICE_EXT}", selfserviceExt);
                            url = url.replace("{SELFSERVICE}", selfservice);
                        }
        %>


        <li><a <g:if test="${selectedMenuItem == m.getId().getMenuId()}">class='active'</g:if> href="<%=url %>"><%=m.getMenuName() %></a></li>

        <%

        }
        }
        %>

    </ul>

<%
        if (userId != null && privRightMenuList2 != null ) {
%>
<div class="head">
    Self - Service Center
</div>
<ul class="menu">

    <%
            for (Menu m: privRightMenuList2) {
                if (m.getSelected()) {
                    String url = m.getUrl();
                    if (url != null) {
                        if (url.indexOf("?") == -1) {
                            url = url + "?" + queryString + "&menuid=" + m.getId().getMenuId() + "&l=p";
                        } else {
                            url = url + "&"  + queryString + "&menuid=" + m.getId().getMenuId() + "&l=p";
                        }
                        url = url.replace("{SELFSERVICE_EXT}", selfserviceExt);
                        url = url.replace("{SELFSERVICE}", selfservice);
                    }
              %>
    <li><a  <g:if test="${selectedMenuItem == m.getId().getMenuId()}">class='active'</g:if> href="<%=url %>"><%=m.getMenuName() %></a></li>

    <% 				}
    }
    %>
</ul>
<%
        }

%>


<%
        }
    }

%>
<div class="head">
    <a href="<%= selfservice %>/logout.do">Logout</a>
</div>
</div>