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
	<div class="container">
		<div class="container-inner">
			<%@include file="../include/page-header.jsp"%>
		
			<div class="row">
				<div id="loginForm" class="form-inline">
					<fieldset>
						<div class="control-group">
	                       <label for="inpLogin" class="control-label">Login</label>
	                       <div class="controls">
	                         <input type="text" errormsg="Please input login" class="input-xlarge" id="inpLogin">
	                       </div>
	                     </div>
	                     <div class="control-group">
	                       <label for="inpLogin" class="control-label">Password</label>
	                       <div class="controls">
	                         <input type="text" errormsg="Please input login" class="input-xlarge" id="inpPassword">
	                       </div>
	                     </div>
                     </fieldset>
                     <button class="btn btn-success flt-rght" id="btnLogin">Login</button>
				</div>
			</div>
		</div>
		<%@include file="../include/footer.jsp"%>
	</div>
</body>
</html>