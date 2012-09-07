<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="formName" required="true" rtexprvalue="true" description="no need to add a '#'"%>
<%@ attribute name="processedPostUrl" required="true" rtexprvalue="true" %>
<%@ attribute name="callback" required="false" rtexprvalue="true" %>
<%@ attribute name="serializator" required="false" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<spring:url value="${processedPostUrl}" var="postUrl" />
<script type="text/javascript">


	$(document).ready(function() {
		var $form = $('#${formName}');		$form.on('submit', function(event) {
		    var callback=null;
		    <c:if test="${callback!=null && callback!=''}" >
		    	callback = ${callback};
			</c:if>
			var serializator = null;
			<c:if test="${serializator!=null && serializator!=''}" >
			  serializator = ${serializator};
			</c:if> 
			
			
			
		  	if(!isBusy()){
		  		cleanMessages('#${formName}');
				// Ajax validation
				var data = null;
				
				if(serializator)
				  data = callFunction(serializator);
				else    
				  data =serializeObject('#${formName}');
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