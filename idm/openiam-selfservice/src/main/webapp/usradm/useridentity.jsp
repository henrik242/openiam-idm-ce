<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<h4>User Identity List</h4>

<form:form commandName="identityUserCmd">
<form:hidden path="perId" />
<fieldset>
	<c:forEach items="${identityUserCmd.principalList}" var="principalList" varStatus="principal">
					
					<h5>Managed System: ${principalList.id.managedSysId} - ${principalList.managedSysName} </h5>
					<div class="block">
						<div class="wrap alt">
							<div class="col-1">
								<div class="row">
									<label for="t-1">Identity:</label>
									<form:input path="principalList[${principal.index}].id.login" size="60"  />
										<form:hidden path="principalList[${principal.index}].id.domainId" />
										<form:hidden path="principalList[${principal.index}].userId" />							
								</div>
								<div class="row">
									<label for="t-1">Locked?</label>
									<c:if test="${principalList.isLocked == 0}" >
										NO
										</c:if>	 
 									 <c:if test="${principalList.isLocked != 0}" >
										YES
										</c:if>	 			
								</div>
                                <div class="row">
                                    <label for="t-1">Last Password Change</label>
                                    ${principalList.pwdChanged}
                                </div>
                                <div class="row">
                                    <label for="t-1">Password Expiration</label>
                                        ${principalList.pwdExp}
                                </div>
                                <div class="row">
                                    <label for="t-1">Password Grace Period</label>
                                        ${principalList.gracePeriod}
                                </div>

							</div>
							
							<div class="col-1">
								<div class="row">
									<label for="t-1">Delete Identity</label>
									<form:checkbox path="principalList[${principal.index}].selected" />						
								</div>

								<div class="row">                                                               
									<label for="t-1">Authn Fail Count</label>                                      								
									${principalList.authFailCount}
								</div>
                                <div class="row">
                                    <label for="t-1">Last Authn Attempt</label>
                                        ${principalList.lastAuthAttempt}
                                </div>
                                <div class="row">
                                    <label for="t-1">First Time Login</label>
                                    <c:if test="${principalList.firstTimeLogin == 0}" >
                                        NO
                                    </c:if>
                                    <c:if test="${principalList.firstTimeLogin!= 0}" >
                                        YES
                                    </c:if>
                                </div>

                                <div class="row">
                                    <label for="t-1">Identity Status</label>
                                        ${principalList.status}
                                </div>

                            </div>
						</div>
					</div>
	</c:forEach>

    <div class="button">
        <input type="submit" value="Save">
    </div>
    <div class="button">
        <input type="submit" value="Cancel">
    </div>

</fieldset>
								


</form:form>

