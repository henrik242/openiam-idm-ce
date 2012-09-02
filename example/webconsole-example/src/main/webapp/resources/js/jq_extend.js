/*
  alert options:
    message  - alert message or array of messages
    global   - flag to determine is the alert global or not
    title    - alert message title
    type     - alert type. error,info,or success
    delay    - alert delay. If >0, then alert message will hide automatically. If =0, user should click on alert to close it
    style    - custom alert style class
    callback - function, which will be called after all alert effects are finished.

*/
(function($) {
  $.fn.appendAlert = function(options) {
    return this
        .each(function() {

          var alertId = new Date().getTime();

          var message = null;
          if (!options.message)
            message = options;
          else if ($.isArray(options.message)) {
            if (options.message.length == 1)
              message = options.message[0];
            else {
              for ( var m in options.message)
                message += "<p>" + options.message[m] + "</p>";
            }
          } else
            message = options.message;
          if (options.global) {
            alertId='alertArea';
            $('#'+alertId).empty();
            if (options.title)
              $(this).append("<h3>" + options.title + "</h3>");
            if (!message.startsWith("<p>"))
              message = "<p>" + message + "</p>";
            $(this).append(message);
            $(this).attr('class','alert alert-default alert-'+options.type+' message');
            $(this).animate({top:'0'}, 500);
            if(options.delay && options.delay>0) {
              setTimeout(function() {hideSystemMessage();callFunction(options.callback)},options.delay);


            }
          } else {
            $(this)
                .append(
                    $("<div id='"
                        + alertId
                        + "' class='alert"
                        + (options.type ? ' alert-' + options.type : '')
                       // + " fade in alert-default hide"
                        + " fade in alert-default"
                        + (options.style ? options.style : '')
                        + "'>"
                        + (options.title ? '<h4 class="alert-heading">'
                            + options.title + '</h4>' : '') + message
                        + " </div>"));
               /*         
              if(options.elementSelector != null) {
                $(options.elementSelector).next().removeClass('hide');
                $(options.elementSelector).next().children(":first").off("mouseenter").off("mouseleave").on({
                    mouseenter : function() {
                      $('#'+alertId).toggleClass('hide');
                    },
                    mouseleave : function() {
                      $('#'+alertId).toggleClass('hide');
                    }
                });

              }
*/
          }
          if (options.delay && !options.global)
            $('#' + alertId).delay(options.delay).fadeOut("slow", function() {
              $(this).remove();
            });
        });
  };
})(jQuery);
