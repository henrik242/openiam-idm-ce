/*
 * COMMON BACKEND RELATED JAVASCRIPT
 *
 */
function initCheckingProcess(checkUrl, callbackName) {
  counter = 0;
  timerInterval = setInterval(function() {
    waitPostResponse(checkUrl, callbackName);
  }, 1000);
  needWait = true;
}
function waitPostResponse(postUrl, callback) {
  if (timerCounter == 0) {
    showGlobalError(network_error);
    finalization(true);
  } else {
    timerCounter = timerCounter - 1;
    $.postJSON(postUrl, null, function(data) {
      if (data && data.response)
        finalization(callback(hasError(data.response.notifications), data.response));
    }, function() {
      finalization(true);
    });
  }
}

function isBusy() {
  if (!processingEvent) {
    processingEvent = true;
    return false;
  } else
    return true;
}

function finalization(stop) {
  timerCounter=timerCounterBk;
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
  $("#ajaxLoadingContainer").hide();
  resetProcessingEvent();
}

function resetProcessingEvent() {
  processingEvent = false;
}

function hasError(notifications, withoutNotifySuccess) {
  var error = notifications ? true : false;

  if (error)
    $.each(notifications, function() {
      error = this.type == 'error';
      var elementSelector = null;
      if (this.elementId != null) {
        elementSelector = this.elementId;
        if (!elementSelector.startsWith("#") && !elementSelector.startsWith("."))
          elementSelector = "#" + elementSelector;
      }
      if (!withoutNotifySuccess || error)
        showNotification({
          type : this.type,
          message : this.text,
          elementSelector : elementSelector,
          delay : this.delay
        });
    });
  return error;
}


$("#ajaxLoadingContainer").ajaxStart(function() {
  $(this).show();
}).ajaxComplete(function(e, XHR, options) {
  try {
    if (!needWait)
      hideAjaxNotification();
  } catch (e) {
    return null;
  }
}).ajaxError(function(event, request, settings) {
  hideAjaxNotification();
});

function errorHandler(jqXHR, textStatus, errorThrown) {
  if (jqXHR.status == 403)
    window.location = contextPath+"/";
  else if (jqXHR.status == 401)
    window.location = contextPath+"/expiredSession";
  else if (jqXHR.status == 500)
    showGlobalError(generic_error);
  else
    showGlobalError(network_error);
}