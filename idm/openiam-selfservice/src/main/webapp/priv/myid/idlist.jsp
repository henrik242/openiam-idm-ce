<%@ page language="java" contentType="text/html; charset=utf-8"     pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 


<h4>User Identity List</h4>

<form:form commandName="idListCmd">
<form:hidden path="userId" />
<fieldset>
	<c:forEach items="${idListCmd.principalList}" var="principalList" varStatus="principal">
					
					<h5>Managed System: ${principalList.id.managedSysId} - ${principalList.managedSysName} </h5>
					<div class="block">
						<div class="wrap alt">
							<div class="col-1">
								<div class="row">
									<label for="t-1">Identity:</label>
                                        <a href="myIdentityDetail.selfserve?login=${principalList.id.login}&domain=${principalList.id.domainId}&msys=${principalList.id.managedSysId}">${principalList.id.login}</a>
								</div>

                                <div class="row">
                                    <label for="t-1">Identity Status</label>
                                        ${principalList.status}
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

							</div>
							
							<div class="col-1">
                                <div class="row">
                                    <label for="t-1">Last Password Change</label>
                                        ${principalList.pwdChanged}
                                </div>
                                <div class="row">
                                    <label for="t-1">Password Expiration</label>
                                        ${principalList.pwdExp}
                                </div>
                            </div>
						</div>
                    </div>
	</c:forEach>
        </div>



    <div class="button">
        <input type="submit" name="_cancel" value="Cancel" />
    </div>
    <div class="button">
        <a href="myIdentityDetail.selfserve">Add Identity</a>
    </div>
</fieldset>
								


</form:form>

