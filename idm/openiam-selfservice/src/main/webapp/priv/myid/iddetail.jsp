<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<h4>User Identity List</h4>

<form:form commandName="idListDetailCmd">
<form:hidden path="userId" />
<fieldset>


					<div class="block">
						<div class="wrap alt">
							<div class="col-1">



                                <div class="row">
                                    <label for="t-1">Managed System:</label>
                                    <form:select path="principal.id.managedSysId" multiple="false">
                                        <form:option value="-" label="-Please Select-"/>
                                        <c:forEach items="${idListDetailCmd.managedSysAry}" var="msys">
                                            <form:option value="${msys.managedSysId}" label="${msys.name}" />
                                        </c:forEach>
                                    </form:select>
                                </div>

                                <div class="row">
									<label for="t-1">Identity:</label>
                                        <form:input path="principal.id.login" size="60" maxlength="120"  />
                                </div>
                                <div class="row">
                                    <label for="t-1">Target System Attribute Name:</label>
                                    <form:input path="principal.loginAttrInTargetSystem" size="60" maxlength="120"  />
                                </div>

                                <div class="row">
                                    <c:if test="${idListDetailCmd.principal.password == null}" >
                                        <label for="t-1">Password:</label>
                                        <form:password path="principal.password" size="60" maxlength="120"  />
                                    </c:if>
                                    <c:if test="${idListDetailCmd.principal.password != null}" >
                                        <form:hidden path="principal.password"   />
                                    </c:if>

                                    <form:hidden path="newRecord" />
                                    <form:hidden path="principal.userId" />
                                    <form:hidden path="principal.status" />
                                    <form:hidden path="principal.pwdChanged" />
                                    <form:hidden path="principal.pwdExp" />
                                    <form:hidden path="principal.firstTimeLogin" />
                                    <form:hidden path="principal.isLocked" />
                                    <form:hidden path="principal.gracePeriod" />
                                    <form:hidden path="principal.createDate" />
                                    <form:hidden path="principal.currentLoginHost" />
                                    <form:hidden path="principal.authFailCount" />
                                    <form:hidden path="principal.lastAuthAttempt" />
                                    <form:hidden path="principal.lastLogin" />
                                    <form:hidden path="principal.passwordChangeCount" />
                                    <form:hidden path="principal.resetPassword" />
                                    <form:hidden path="principal.origPrincipalName" />
                              </div>



							</div>

							
							<div class="col-1">

                            </div>
						</div>
                    </div>
        </div>




    <div class="button">
        <input type="submit" name="btn" value="Submit" />
    </div>
    <div class="button">
        <input type="submit" name="btn" value="Delete" />
    </div>
    <div class="button">
        <input type="submit" name="_cancel" value="Cancel" />
    </div>


</fieldset>
								


</form:form>

