<!DOCTYPE html>
<%@page pageEncoding="UTF-8" session="true"%>

<html>
<head>
<title>WebConsole Example</title>

<%@include file="../include/header.jsp"%>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>
<body>
	<%@include file="../include/header-body.jsp"%>
	<div id="openiam-container" class="container-fluid">
		<div class="container-inner">
			<%@include file="../include/page-header.jsp"%>
		
			<div class="row-fluid">
				<form:form id="loginForm" modelAttribute="loginModel" class="form-inline span7" action="login">
					<fieldset>
						<form:hidden path="clientIP"/>
						
						<html:inputField name="principal" label="label.user.principal" errorMsg="error.principal.required" requiredField="true" cssClass="span4"/>
						<html:passwordField name="password" label="label.password" errorMsg="error.password.required" requiredField="true" cssClass="span4"/>
                     </fieldset>
                     <div class="form-actions">
						<button type="submit" class="btn btn-success pull-right">Login</button>
					 </div>
				</form:form>
			 	<ajax:ajaxFormValidate processedValidateUrl="validateLogin" formName="loginForm"/>
			</div>
		</div>
		<%@include file="../include/footer.jsp"%>
	</div>
</body>
</html>