var bootstrap = new Bootstrap();
var app = SingletonFactory.getInstance(Application);
app.getSystemProperties().set("host.root", ApplicationRoot);
app.getSystemProperties().set("page.default", "Home");
app.setBootstrap(bootstrap);
Request.setProactive(true);

$(window).bind("hashchange", function(){
    if (Request.getProactive != true) {
        var subject = SingletonFactory.getInstance(Subject);
        subject.notifyEvent('NeedAssembleRequest');
    }
    Request.setProactive(false);
});

$(document).ready(function()	{
	$.get(ApplicationRoot+'/static/microtemplating/all.default.txt', {}, function(ret)	{
		var useragent = navigator.userAgent;
		if (useragent.indexOf('MSIE') != -1)	{
			$('#Application-Main').html(ret);
		} else {
			document.getElementById('Application-Main').innerHTML = ret;
		}
		app.begin();
	});
});