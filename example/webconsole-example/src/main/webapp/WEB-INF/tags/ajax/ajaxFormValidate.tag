<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="formName" required="true" rtexprvalue="true" description="no need to add a '#'"%>
<%@ attribute name="processedValidateUrl" required="true" rtexprvalue="true" %>
<%@ attribute name="serializator" required="false" rtexprvalue="true" %>

<spring:url value="${processedValidateUrl}" var="postUrl" />
<script type="text/javascript">
	$(document).ready(function() {
		var $form = $('#${formName}');
		$form.on('submit', function(event) {
			// Ajax validation
			var serializator = ${serializator};
			var data = null;
			
			if(serializator)
			  data = callFunction(serializator);
			else    
			  data =serializeObject('#${formName}');
			
			if(!isBusy()){
				var data = serializeObject('#${formName}');
				cleanMessages('#${formName}');
				// client form validation 
				if(validate('#${formName}')){
					$.postJSON('${postUrl}', data, function(response) {
					  if (response && !hasError(response.notifications)) {
						$form.off('submit');
						$form.submit();
					  }
					});
				}
				resetProcessingEvent();
		  	}
			return stopEventPropagation(event);
		});
	});
</script>