
<div id="sidebar">
    <div class="head">
        Self - Service Center
    </div>

    <ul class="menu">
        <%
            String userId = (String)session.getAttribute("userId");
            if (userId != null && userId.length() > 0 ) { %>
        <li> <a href="<%= request.getContextPath() %>/priv/profile/edit.jsp" <g:if test="${selectedMenuItem == 'profile'}">class='active'</g:if>>User Profile</a></li>
        <li> <a href="<%= request.getContextPath() %>/priv/newhire/edit.jsp" <g:if test="${selectedMenuItem == 'newhire'}">class='active'</g:if>>New Hire</a></li>
        <% }  %>
            <li> <a href="<%= request.getContextPath() %>/pub/registration/edit.jsp" <g:if test="${selectedMenuItem == 'selfRegistration'}">class='active'</g:if>>Self Registration</a></li>

    </ul>
</div>


