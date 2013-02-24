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
                                    ${requestDetailCmd.request.requestId}
									<form:hidden path="request.requestId" />
								</div>
                                <div class="row">
                                    <label for="t-1">Workflow Name</label>
                                        ${requestDetailCmd.request.workflowName}
                                </div>
                                <div class="row">
                                    <label for="t-1">Title</label>
                                        ${requestDetailCmd.request.requestTitle}
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
                                            (${requestDetailCmd.requestor.areaCd})${requestDetailCmd.requestor.phoneNbr}
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
                                    <label for="t-1">User Name:</label>
                                        ${requestDetailCmd.userDetail.firstName}  ${requestDetailCmd.userDetail.lastName}
                                </div>

                                <c:if test="${requestDetailCmd.userDetail.title != null}" >
                                    <div class="row alt">
                                        <label for="t-1">Title:</label>
                                        ${requestDetailCmd.userDetail.title}
                                    </div>
                                </c:if>

                                <c:if test="${requestDetailCmd.orgName != null}" >
                                    <div class="row alt">
                                        <label for="t-1">Organization:</label>
                                        ${requestDetailCmd.orgName}
                                    </div>
                                </c:if>

                                <c:if test="${requestDetailCmd.userDetail.phoneNbr != null}" >
                                    <div class="row alt">
                                        <label for="t-1">Phone:</label>
                                        (${requestDetailCmd.userDetail.areaCd})${requestDetailCmd.userDetail.phoneNbr}
                                    </div>
                                </c:if>
                                <c:if test="${requestDetailCmd.userDetail.email != null}" >
                                    <div class="row">
                                        <label for="t-1">Email:</label>
                                        ${requestDetailCmd.userDetail.email}
                                    </div>
                                </c:if>

                                <div class="row">
                                    <label for="t-1">Change Request:</label>
                                        ${requestDetailCmd.changeDescription}
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
                                        ${requestDetailCmd.request.requestDate}
								</div>
                                <div class="row">
                                    <label for="t-1">Request Status</label>
                                        ${requestDetailCmd.request.status}
                                </div>
                               <!-- <div class="row">
									<label for="t-1">Status Change Date</label>
                                    ${requestDetailCmd.request.statusDate}
								</div -->

                                <div class="row alt">
                                    <label for="t-1"></label>
                                    <b>Approval / Rejection History</b>
                                </div>
                                <div class="row">
                                    <label for="t-1">Date</label>
                                    ${requestDetailCmd.reqApprover.actionDate}
                                </div>
                                <div class="row">
                                    <label for="t-1">Action</label>
                                    ${requestDetailCmd.reqApprover.action}
                                </div>
                                <div class="row">
                                    <label for="t-1">Approver</label>
                                        ${requestDetailCmd.approverUser.firstName} ${requestDetailCmd.approverUser.lastName}
                                </div>
                                <div class="row">
                                    <label for="t-1">Comment</label>
                                        ${requestDetailCmd.reqApprover.comment}
                                </div>

							</div>
						</div>
					</div>
                    <c:if test="${requestDetailCmd.request.status == 'PENDING'}" >
                        <div class="button">
                            <input type="submit" name="btn" value="Approve">
                        </div>
                        <div class="button">
                            <input type="submit" name="btn" value="Reject">
                        </div>
                    </c:if>

                </fieldset>
</table>




</form:form>
