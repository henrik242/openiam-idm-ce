<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<div class="block">
				<div class="wrap">
					
					<div class="text-wrap">
						<p>Set new password</p>
					</div>
					<form:form commandName="unlockUserCmd">
						<fieldset>
							<p><form:errors path="domainId" /> </p>

								
							<label for="t-1">New Password<span>*</span></label>
								<form:password path="password" size="30"  maxlength="30"   />
    					 <p><form:errors path="password" /></p>
							<label for="t-1">Confirm New Password<span>*</span></label>
							  <form:password path="confPassword" size="30"  maxlength="30"   />
							  <p><form:errors path="confPassword" /></p>
        	

							<div class="button">
								<input type="submit" name="_finish" value="Finish" tabindex="1" />    
							</div>
							<div class="button">
								<input type="submit" name="_target2" value="Previous"/>    
							</div>
                            <div class="button">
                                <input type="submit" name="_cancel" value="Cancel" />
                            </div>

						</fieldset>
					</form:form>
				</div>
			</div>

 
