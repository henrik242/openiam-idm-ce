<%@ page language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.*,javax.servlet.http.*, org.openiam.idm.srvc.user.dto.User, org.openiam.webadmin.admin.JSPUtil" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<jsp:include page="/idman/search.jsp" flush="true" />

<%
		int size = 0;	// # of pages of data
 		int recordCount = 0;
 		int curPage = 0;
 		
 	 	String msg = (String)request.getAttribute("msg");
        String searchmsg = (String)request.getAttribute("searchmsg");
 		
 		Integer resultSize = (Integer)request.getAttribute("resultSize");

 		
 		if (resultSize != null) {
 			recordCount = resultSize.intValue();
 		}
	List<User> userList = (List)request.getAttribute("userList");
	if (userList == null){
		userList = new ArrayList();
	}
%>



<table  width="1200pt">
    <tr>
        <td align="center" height="100%">
            <fieldset class="userform" >
                <legend>SEARCH RESULTS - <%=recordCount%> Records Found</legend>


                <table class="resourceTable" cellspacing="2" cellpadding="2"  width="1200pt" >
                    <%
                        if (searchmsg != null) {
                    %>
                    <tr>
                        <td colspan="7"><font color="rea"><%=searchmsg%></font></td>
                    </tr>

                    <% } %>


                    <tr class="header">
                        <th width="30%">Name</th>
                        <th width="30%">E-mail</th>
                        <th width="15%">Phone</th>
                        <th width="10%">User Status</th>
                        <th width="10%">Acct State</th>
                        <th width="5%"></th>
                    </tr>

                    <%

                        if( userList != null && userList.size() > 0 ) {
                            for (User ud : userList) {
                                StringBuilder ph = new StringBuilder();
                                if (ud.getAreaCd() != null && !ud.getAreaCd().isEmpty()) {
                                    ph.append("(");
                                    ph.append(ud.getAreaCd());
                                    ph.append(")");
                                }
                                if (ud.getPhoneNbr() != null && !ud.getPhoneNbr().isEmpty()) {
                                    ph.append(ud.getPhoneNbr());
                                }
                                if ( ud.getPhoneExt() != null && !ud.getPhoneExt().isEmpty()) {
                                    ph.append(" x");
                                    ph.append(ud.getPhoneExt());
                                }


                    %>


                    <tr>


                        <td width="30%"  class="tableEntry" valign="top">
                            <% if (ud.getFirstName() != null || ud.getLastName() != null) { 
                                String name = ud.getFirstName() + " " + ud.getLastName();

                            %>
                            <a href="<%=request.getContextPath()%>/editUser.cnt?personId=<%=ud.getUserId()%>&menugrp=QUERYUSER"><%= JSPUtil.display(name)%></a>
                            <% } %>
                        </td>
                        <td width="30%"  class="tableEntry"  valign="top"><%= JSPUtil.display(ud.getEmail() )%> </td>
                        <td width="15%" class="tableEntry" valign="top"><%= JSPUtil.display(ph.toString())%> </td>
                        <td width="10%" class="tableEntry"  valign="top">
                            <% if (ud.getStatus() != null) { %>
                            <%=JSPUtil.display(ud.getStatus())%>
                            <% } %>
                        </td>
                        <td width="10%" class="tableEntry"  valign="top">
                            <% if (ud.getSecondaryStatus() != null) { %>
                            <%=JSPUtil.display(ud.getSecondaryStatus())%>
                            <% } %>
                        </td>


                        <td width="5%" class="tableEntry"  valign="top">
                            <a href="<%=request.getContextPath()%>/userChangeStatus.cnt?personId=<%=ud.getUserId()%>&status=DELETED">DELETE</a>
                        </td>


                    </tr>
                    <%
                            }
                        }
                    %>

                </table>

        </td>
    </tr>
</table>
