/*
 * COMMON BACKEND RELATED JAVASCRIPT 
 * 
 */
var timer = 0;
var timerInterval = null;
var needWait = false;
var timerCounter=<c:out value="${timerCounter}"/>;

$('#logout').click(function() {
  $.postJSON("auth/logout", {}, function(data) {
    if (data.response.status && data.response.status == 'successful') {
      window.location = "";
    } else
      $('#alertArea').appendAlert(data.response);
  });
});

function initCheckingProcess(checkUrl, callbackName) {
  counter = 0;
  timerInterval = setInterval(function() {
    waitPostResponse(checkUrl, callbackName);
  }, 1000);
  needWait = true;
}

function waitPostResponse(postUrl, callback) {
  if(timerCounter==0){
    showGlobalError("<fmt:message key='network.error' />");
    finalization(true);
  }
  else{
    timerCounter = timerCounter-1;
    $.postJSON(postUrl, null, function(data) {
      if (data && data.response)
        finalization(callback(hasError(data.response.notifications),
            data.response));
    }, function() {
      finalization(true);
    });
  }
}

function isBusy() {
  if (timerInterval)
    return true;
  else
    return false;
}

function finalization(stop) {
  timerCounter=<c:out value="${timerCounter}"/>;
  if (stop) {
    needWait = false;
    hideAjaxNotification();
    if (timerInterval) {
      window.clearInterval(timerInterval);
      timerInterval = null;
    }
  }
}
function hideAjaxNotification() {
  $("#ajaxLoading").hide();
}

function hasError(notifications, withoutNotifySuccess) {
  var error = notifications ? true : false;

  if (error)
    $.each(notifications, function() {
      error = this.type == 'error';
      var elementSelector = null;
      if (this.elementId != null) {
        elementSelector = this.elementId;
        // else
        // elementSelector = "#alertArea";
        if (!elementSelector.startsWith("#")
            && !elementSelector.startsWith("."))
          elementSelector = "#" + elementSelector; // assume id selector by
        // default;
      }
      if (!withoutNotifySuccess || error)
        showNotification({
          type: this.type,
          message: this.text, 
          elementSelector: elementSelector, 
          delay: this.delay
        });
    });
  return error;
}



$("#ajaxLoading").ajaxStart(function() {
  $(this).show();
}).ajaxComplete(function(e, XHR, options) {
  try {
    if (!needWait)
      $(this).hide();
  } catch (e) {
    return null;
  }
}).ajaxError(function(event, request, settings) {
  $(this).hide();
});

function errorHandler(jqXHR, textStatus, errorThrown) {
  if (jqXHR.status == 403)
    window.location = "<c:out value='${pageContext.request.contextPath}' />/";
  else if (jqXHR.status == 401)
    window.location = "<c:out value='${pageContext.request.contextPath}' />/expiredSession";
  else if (jqXHR.status == 500)
    showGlobalError("<fmt:message key='GENERIC_ERROR'/>");
  else
    showGlobalError("<fmt:message key='network.error' />");
}


/*window.onresize = function(event) {
  var i = 0;
  i++;
};


$('#signInBtn').click(function() {
  window.location = "login";
});*/
