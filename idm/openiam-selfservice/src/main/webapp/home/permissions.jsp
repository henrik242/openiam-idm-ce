<!-- permissions -->


<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*, org.openiam.idm.srvc.menu.dto.Menu;" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<table>
<%
System.out.println("permissions.jsp");
  String userId = (String)session.getAttribute("userId");
  String token = (String)session.getAttribute("token");
  String login = (String)session.getAttribute("login");

  List menuList = (List) session.getAttribute("permissions");

  if (menuList != null && !menuList.isEmpty() ) {
  	int size = menuList.size();
  	int counter = 0;
  	Iterator it = menuList.iterator();
%>
  <tr>
  	<td class="bodytext">
<%

    while (it.hasNext()) {
      Menu md = (Menu)it.next();
      String url = md.getUrl();
      if (url != null) {
        if (url.indexOf("?") == -1) {
           url = url + "?menuid=" + md.getId().getMenuId() + "&l=p";
        } else {    
           url = url + "&menuid=" + md.getId().getMenuId() + "&l=p";
        }  
       }
      
%>

        <a href="<%= request.getContextPath() %><%=url%>"><%=md.getMenuName()%></a>

<%
          if (size >= ++counter) {
            out.print(" | ");
          }
		 
      }
%>
<a href="<%= request.getContextPath() %>/logout.do"> Logout</a>
<%
   }    
 %>     

            
  
        </td>
      </tr>
      
</table>


