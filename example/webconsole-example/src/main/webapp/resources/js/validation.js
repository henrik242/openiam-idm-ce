function validate(formSelector, requiredFields) {
  var alertOption = {
    message : "",
    type : 'error',
    delay : null
  };
  // alert(4);
  var result = true;
  $(
      formSelector + " input, " + formSelector + " select, " + formSelector
          + " textarea")
      .each(
          function(index) {
            alertOption.message="Required fields should not be empty.";
            if ($(this).attr("type")
                && ($(this).attr("type") == "checkbox" || $(this).attr("type") == "radio")) {
              $(formSelector + " input:" + $(this).attr("type") + "[name=\""
                      + $(this).attr("name") + "\"]").each(function(index1) {
                result = callFunction($(this).attr("validator")) && result;
              });
            } else {
              if (!$(this).attr("id"))
                return;
              if (!$(this).val()) {
                if ($.inArray("#" + $(this).attr("id"), requiredFields) != -1) {
                  if ($(this).attr("validator"))
                    result = callFunction($(this).attr("validator")) && result;
                  else {
                    if ($(this).attr("errorMsg")) {
                      alertOption.message = $(this).attr("errorMsg");

                    }
                    alertOption.elementSelector = "#" + $(this).attr("id");
                    showNotification(alertOption);
                    result = false;
                  }
                } else {
                  result = callFunction($(this).attr("validator")) && result;
                }
              } else {
                result = callFunction($(this).attr("validator")) && result;
              }
            }
          });
  return result;
}


