SamplePortlet = Class.extend({
	init: function() {
		this.name = "SamplePortlet";
	},
	
	run: function() {
		this.getPortletPlaceholder().paintCanvas(this.render());
	}
}).implement(PortletInterface).implement(RenderInterface);