$(function(){

	Cufon.replace('#sidebar ul a', { fontFamily: 'Myriad Pro Regular', textShadow: "0 0 rgba(0, 0, 1, 0.5)", hover: true }); 
	Cufon.replace('#sidebar .head a', { fontFamily: 'Calibri', textShadow: " 0 0 rgba(0, 0, 1, 0.7)", hover: true }); 
	//menu
	(function($) {
		$.fn.collapsor = function(settings) {
		settings = $.extend({}, $.fn.collapsor.defaults, settings);
		var triggers = this;
		return this.each(function() {
		$(this).find('+ ' + settings.sublevelElement).hide();
		if($(this).hasClass(settings.openClass)){
		$(this).find('+ ' + settings.sublevelElement).show();
		}
		$(this).click(function() {
		$(triggers).not($(this)).removeClass(settings.openClass);
		if ($(this).next().is(settings.sublevelElement)){
		$(this).blur().toggleClass(settings.openClass);
		$(this).next().animate({height:'toggle', opacity:'toggle'}, settings.speed, settings.easing);
		$(this).parent().parent().find(settings.sublevelElement).not($(this).next()).animate({height:'hide', opacity:'hide'}, settings.speed, settings.easing);
		return false;
		}
		});
		});
	};
	$.fn.collapsor.defaults = {
	activeClass: 'active',
	openClass:'active',
	sublevelElement: 'ul',
	speed: 500,
	easing: 'swing'
	};
	})(jQuery);
	$('.menu a').collapsor();
})