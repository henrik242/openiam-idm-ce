/*
 * COMMON JAVASCRIPT FOR ALL UTILITIES
 *
 */
$('.message').bind('click', function() {
  hideSystemMessage();
});

$(document).ready(function() {
  if (!notification) {
    hideSystemMessage();
  } else {
    $('#alertArea').appendAlert(notification);
  }
  
  $("span.formError").each(function(index) {
    var ctrlSelector = $(this).attr("id").split(".")[0];
    var msg = $(this).text();
    $(this).empty();
    if(ctrlSelector){
    	showNotification({type : "error",
    										message : msg,
          							elementSelector : getElementSelector(ctrlSelector)});
    }
  });
});
function hideSystemMessage() {
  if ($('.message p').length > 0) {
    $('.message').animate({top:-$('.message').outerHeight()}, 500).delay(200).empty();
  }
}
/*
  var alertOptions = {
    type : alertType,
    message : message,
    title : title,
    elementSelector : elementSelector,
    delay : delay ? delay : 0,
    closable : isClosable ? isClosable : true,
    global : true,
    callback : callback
  };
*/
function showNotification(alertOptions) {
  alertOptions.global = !alertOptions.elementSelector;
  if (!alertOptions.global) {
    var validAlertId = "validAlert" + alertOptions.elementSelector.substring(1);
    $("body").append("<div id='" + validAlertId + "'></div>");
    $("#" + validAlertId).css(
        {
          'top' : $(alertOptions.elementSelector).offset().top - 5,
          'left' : $(alertOptions.elementSelector).offset().left
              + $(alertOptions.elementSelector).width() + 35,
          'position' : 'fixed',
          'z-index' : 10000000
        });
    $("#" + validAlertId).appendAlert(alertOptions);
    getParent(alertOptions.elementSelector,'.control-group').addClass(alertOptions.type);
    var elementSelector = alertOptions.elementSelector;
    $(elementSelector).blur(function() {
      clearMessage(elementSelector);
    });
  } else {
    $('#alertArea').appendAlert(alertOptions);
  }
}

function showGlobalError(message,delay,callback) {
  showGlobalNotification('error',message,delay,callback);
}

function showGlobalSuccess(message,delay,callback) {
  showGlobalNotification('success',message,delay,callback);
}

function showGlobalNotification(type,message,delay,callback) {
	if(!delay)
    delay=5000;
  showNotification({
      type: type,
      message: message,
      delay: delay,
      callback: callback,
      global: true
    });
}
function getElementSelector(elementSelector){
	if (!elementSelector.startsWith("#") && !elementSelector.startsWith("."))
          elementSelector = "#" + elementSelector;
  return elementSelector;      
}
function clearMessage(elementSelector) {
  if (!elementSelector)
    hideSystemMessage();
  else {
    getParent(elementSelector,'.control-group').removeClass("error");
    $(elementSelector).next().addClass('hide');
    $("#" + "validAlert" + elementSelector.substring(1)).remove();
  }
}

function cleanMessages(elementSelector) {
  $(elementSelector + " input, " + elementSelector + " select, "
          + elementSelector + " textarea").each(function(index) {
    $(this).blur();
  });
  $(".error-icon-holder").addClass('hide');
  hideSystemMessage();
}
function resetForm(elementSelector) {
  cleanMessages(elementSelector);
  $(elementSelector + " input:not(:checkbox,:radio), " + elementSelector + " select, "
          + elementSelector + " textarea").val('');
  $(elementSelector + " input[type=radio]:checked,"+elementSelector+"  input[type=checkbox]:checked").attr("checked", false);
}

function bindNumericKeyPressChecking(){
  $(".input-numeric").keypress(function(e) {
    var k = e.keyCode ? e.keyCode : e.charCode;
    if (k == 8 || k == 46 || k == 37 || k == 39 || k == 9)
      return true;
    if (String.fromCharCode(k).match(/[^0-9]/g))
      return false;
  });
}
bindNumericKeyPressChecking();


if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function(char) {
    return this.lastIndexOf(char, 0) === 0;
  };
}

function getContent(linkUrl, sourceElementId, targetElementSelector) {
  var toLoad = linkUrl;
  if (sourceElementId) {
    toLoad = toLoad + ' #' + sourceElementId;
  }
  $(targetElementSelector).empty().load(linkUrl,
      function(response, status, xhr) {
        if (status == "error") {
          errorHandler(xhr, xhr.statusText);
        }
      });
  return false;
}

function callFunction(fn, data) {
  if (fn) {
    if (fn.substring) {
      if(fn.indexOf("()") != -1)
        fn = fn.substring(0, fn.indexOf("()"));
      if(data)
      	return window[fn](data);
      return window[fn]();
    } else{
     if(data)
      	return fn(data);
      return fn();
    }  
  }
  return true;

}
function getParent(elementSelector,parentSelector) {
  var parent = $(elementSelector).parentsUntil(".control-group").parent();
  if(!parent || parent.length == 0)
    parent = $(elementSelector).parent();
  return parent;
}

function stopEventPropagation(event){
  if (event.stopPropagation) {
      event.stopPropagation();   // W3C model
  } else {
      event.cancelBubble = true; // IE model
  }
  return false;
}