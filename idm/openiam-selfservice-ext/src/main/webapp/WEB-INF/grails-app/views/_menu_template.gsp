
<div id="sidebar">
    <div class="head">
        Self - Service Center
    </div>

    <ul class="menu">
        <%
            String userId = (String)session.getAttribute("userId");
            if (userId != null && userId.length() > 0 ) { %>
        <li> <a href="<%= request.getContextPath() %>/profile/edit.jsp" <g:if test="${selectedMenuItem == 'profile'}">class='active'</g:if>>User Profile</a></li>
        <% }  %>
            <li> <a href="<%= request.getContextPath() %>/registration/edit.jsp" <g:if test="${selectedMenuItem == 'selfRegistration'}">class='active'</g:if>>Self Registration</a></li>

    </ul>
</div>


