<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<% 
System.out.println("unlockuser.jsp");
%>
			<div class="block">
				<div class="wrap">
					<div class="text-wrap">
						<h3>Unlock Account</h3>
					</div>
					<form:form commandName="unlockUserCmd">
						<fieldset>
							<label for="t-1">Enter Login ID: <span>*</span></label>
							<form:input path="principal" size="40" maxlength="40" />
								<p><form:errors path="principal"/></p>

                            <div class="button">
                                <input type="submit" name="_target1" value="Next"/>
                            </div>

							<div class="button">
                                <input type="submit" name="_cancel" value="Cancel" />
                            </div>

						</fieldset>
					</form:form>
				</div>
			</div>
			 

