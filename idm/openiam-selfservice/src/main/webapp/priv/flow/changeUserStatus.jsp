<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



			<div class="block">
				<div class="wrap">
					<form:form commandName="changeUserStatus">

						<fieldset>
                            <p>${changeUserStatus.selectedUser.firstName} ${changeUserStatus.selectedUser.lastName}'s Current User Status is: ${changeUserStatus.selectedUser.status}  </p>

							<label for="t-1">Change User Status to:<span>*</span></label>
                            <form:select path="newStatus" multiple="false">
                                <form:option value="" label="-Please Select-"/>
                                <c:forEach items="${userStatusList}" var="userStatus">
                                    <form:option value="${userStatus}" label="${userStatus}" />
                                </c:forEach>
                            </form:select>

                            <div class="button">
                                <input type="submit" value="Save" name="btnSave">
                            </div>

                            <div class="button">
								<input type="submit" name="_cancel" value="Cancel" />
							</div>

						</fieldset>
					</form:form> 
				</div>
			</div>
