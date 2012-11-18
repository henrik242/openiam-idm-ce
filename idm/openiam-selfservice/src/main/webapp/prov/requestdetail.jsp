<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<form:form commandName="requestDetailCmd" cssClass="user-info">
				<fieldset>
					<div class="block">
						<div class="wrap alt">

							<!-- column 1  -->	
							<div class="col-1">
								<div class="row">
									<label for="t-1">Request Id</label>
									<form:input path="request.requestId" size="32" maxlength="32" readonly="true" />
								</div>
                                <div class="row">
                                    <label for="t-1">Title</label>
                                        ${requestDetailCmd.request.requestTitle}
                                </div>
                                <div class="row">
                                    <label for="t-1">Request Status</label>
                                    <form:input path="request.status" size="32" maxlength="32" readonly="true" />
                                </div>
                                <div class="row">
                                    <label for="t-1">Reason for Request:</label>
                                        ${requestDetailCmd.request.requestReason}
                                </div>

                                <div class="row alt">
                                    <label for="t-1"></label>
                                    <b>Requestor Information:</b>
                                </div>
                                <div class="row alt">
                                    <label for="t-1">Requestor Name</label>
                                        ${requestDetailCmd.request.requestorFirstName}  ${requestDetailCmd.request.requestorLastName}
                                </div>
                                <c:if test="${requestDetailCmd.requestor != null}" >

                                    <div class="row alt">
                                        <label for="t-1">Title</label>
                                            ${requestDetailCmd.requestor.title}
                                    </div>
                                    <div class="row alt">
                                        <label for="t-1">Phone</label>
                                            ${requestDetailCmd.requestor.areaCd} - ${requestDetailCmd.requestor.phoneNbr}
                                    </div>
                                    <div class="row alt">
                                        <label for="t-1">Email Address</label>
                                            ${requestDetailCmd.requestor.email}
                                    </div>
                                </c:if>

                                <div class="row alt">
                                    <label for="t-1"></label>
                                    <b>Request Details:</b>
                                </div>

                                <div class="row alt">
                                    <label for="t-1">Requestor Name</label>
                                        ${requestDetailCmd.userDetail.firstName}  ${requestDetailCmd.userDetail.lastName}
                                </div>

								<div class="row alt">
									<label for="t-1">Title</label>
									${requestDetailCmd.userDetail.title}
								</div>																
								<div class="row alt">
									<label for="t-1">Organization</label>
									${requestDetailCmd.orgName}
								</div>	
								<div class="row alt">
									<label for="t-1">Phone</label>
									${requestDetailCmd.userDetail.areaCd}-${requestDetailCmd.userDetail.phoneNbr}
								</div>
								<div class="row">
									<label for="t-1">Email</label>
									${requestDetailCmd.userDetail.email}
								</div>

                                <div class="row">
                                    <label for="t-1">Membership to Role(s)</label>
                                    <c:forEach items="${requestDetailCmd.roleList}" var="role" >
                                        ${role.roleName}<br>
                                    </c:forEach>
                                </div>
                                <div class="row">
                                    <label for="t-1">Membership to Group(s)</label>
                                    <c:forEach items="${requestDetailCmd.groupList}" var="group" >
                                        ${group.grpName}</br>
                                    </c:forEach>
                                </div>

                                <div class="row">
									<label for="t-1">Comment</label>
									<form:textarea path="comment" rows="5" cols="60" />
								</div>
																
							</div>
							<!-- column 2  -->	
							<div class="col-1">
								<div class="row">
									<label for="t-1">Request Date</label>
									<form:input path="request.requestDate" size="32" maxlength="32" readonly="true" />
								</div>
								<div class="row">
									<label for="t-1">Status Change Date</label>
									<form:input path="request.statusDate" size="32" maxlength="32" readonly="true"/>
								</div>								
							</div>
						</div>
					</div>
					<div class="button">
                        <input type="submit" name="btn" value="Approve">
                    </div>
                    <div class="button">
                        <input type="submit" name="btn" value="Reject">
                    </div>
              
				</fieldset>	
</table>

</form:form>
