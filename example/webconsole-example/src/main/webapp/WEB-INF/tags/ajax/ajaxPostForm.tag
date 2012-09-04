<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="formName" required="true" rtexprvalue="true" description="no need to add a '#'"%>
<%@ attribute name="processedPostUrl" required="true" rtexprvalue="true" %>
<%@ attribute name="callback" required="false" rtexprvalue="true" %>

<spring:url value="${processedPostUrl}" var="postUrl" />
<script type="text/javascript">
	$(document).ready(function() {
		var $form = $('#${formName}');
		var callback=${callback};
		$form.on('submit', function(event) {
		  	if(!isBusy()){
		  		cleanMessages('#${formName}');
				// Ajax validation
				var data = serializeObject('#${formName}');
				if(validate('#${formName}')){
				  	$.postJSON('${postUrl}', data, function(response) {
				  		if (response && !hasError(response.notifications)) {	
				  		  if(callback){
				  		  	callFunction(callback, response.value);
				  		  }
				  		  showGlobalSuccess("Request complite successfully");
				  		}
					});
				}
				resetProcessingEvent();
		  	}
			return stopEventPropagation(event);
		});
	});
</script>