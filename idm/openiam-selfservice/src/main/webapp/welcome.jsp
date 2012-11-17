<%@ page language="java" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="java.util.*"  %>
<%@ page import="org.openiam.idm.srvc.user.dto.User"  %>
<%@ page import="org.openiam.idm.srvc.user.dto.UserAttribute"  %>

<%

  String token = (String)session.getAttribute("token");
  String userId = (String)session.getAttribute("userId");
  String login = (String)session.getAttribute("login");
 %>
			<div class="block">
				<div class="wrap">
					<div class="box">
						<h5>Welcome ${user.firstName}</h5>
						<div class="wrap-box">
							<dl>
								<c:if test="${groupList != null}" >
										<dt>Member of Groups:</dt>
										<c:forEach items="${groupList}" var="group">
											<dd>${group.grpName}</dd>
							      </c:forEach>
								</c:if>
								<c:if test="${roleList != null}" >
										<dt>Member of Roles:</dt>							  
										<c:forEach items="${roleList}" var="role">
							       			<dd>${role.roleName}</dd>
						       		</c:forEach>
       						</c:if>
       					        <c:if test="${user.title != null}" >
									<dt>Title:</dt>
									<dd>${user.title}</dd>
							    </c:if>
								
								<c:if test="${supervisor != null}" >
									<dt>Supervisor:</dt>
									<dd>${supervisor}</dd>
								 </c:if>
								
								<c:if test="${dept != null}" >
								    <dt>Department:</dt>
									<dd>${dept}</dd>
								 </c:if>
								
								<c:if test="${user.email != null}" >
								    <dt>E-mail:</dt>
									<dd>${user.email}</dd>
								
								</c:if>
									
								
								<c:if test="${primaryIdentity.pwdChanged != null}" >									
									<dt>Password Changed:</dt>
										<dd>${primaryIdentity.pwdChanged}</dd>
								</c:if>
								
								<c:if test="${daysToExp != null}" >
									<dt>Days to Password Exp:</dt>
										<dd>${daysToExp}</dd>
								</c:if>
								
								<dt>Current Login:</dt>
									<dd>${primaryIdentity.lastLogin} from ${primaryIdentity.lastLoginIP}</dd>
								
								<dt>Previous Login:</dt>
									<dd>${primaryIdentity.prevLogin} from ${primaryIdentity.prevLoginIP}</dd>
							   <c:if test="${pendingReq != null}" >
							  	<dt>Pending Requests:</dt>
							  	<dd><a href="myPendingRequest.selfserve?userId=<%=userId%>&lg=<%=login%>&tk=<%=token%>" >${pendingReq} Request(s)</a></dd>
							  </c:if>
							</dl>
							<c:if test="${pendingReq != null}" >
								<p><font color="red">Warning: Before you continue, please complete the "Challenge Response" from the Self-Service section
        on the right. This will enable your password self-service options.</font></p>
							</c:if>
							
						</div>
					</div>
				</div>
			</div>
