EventDispatcher = Class.extend({
	
	init: function() {
		this.listeners = {};
	},
	
	addEventListener: function(event, handler) {
		if (this.listeners[event] == undefined) {
			this.listeners[event] = Array();
		}
		this.listeners[event].push(handler);
	},
	
	dispatchEvent: function(event, e) {
		if (this.listeners[event] != undefined) {
			var handlers = this.listeners[event];
			for(var i=0;i<handlers.length;i++) {
				handlers[i].call(this, e);
			}
		}
	},
	
	removeEventListener: function(event, handler) {
		if (this.listeners[event] != undefined) {
			var index = this.listeners[event].indexOf(handler);
			if (index != -1)
				this.listeners[event].splice(index, 1);
		}
	}
});

/**
 * Base displayable object
 */
DisplayObject = EventDispatcher.extend({
	
	init: function(config) {
		this._super();
		this.domEventBound = {};
		if (config == undefined) config = {};
		this.config = config;
		this.setupBase(config);
		this.setupDisplay(config);
		this.setupDomObject(config);
		
		var objMgr = SingletonFactory.getInstance(Application).getObjectManager();
		objMgr.register(this);
	},
	
	addEventListener: function(event, handler) {
		var _self = this;
		if (this.domEventBound[event] != true) {
			this.access().bind(event, function(e) {
				_self.dispatchEvent(event, e);
			});
			this.domEventBound[event] = true;
		}
		this._super(event, handler);
	},
	
	setupBase: function(config) {
		this.className = this.className || "DisplayObject";
		this.type = this.type || this.className.toLowerCase();
		this.id = this.id || generateId(this.type);
		this._parent = undefined;
	},
	
	setupDisplay: function(config) {
		this.scaleX = this.scaleY = 1;
		this.rotation = 0;
	},
	
	setupDomObject: function(config) {
		this.domObject = $(this.toHtml());
		this.setAttribute('id', this.id);
		this.access().addClass('joo-'+this.type);
		this.access().addClass('joo-uicomponent');	//for base styles, e.g: all DisplayObject has 'position: absolute'
		
		var x = config.x || 0;
		var y = config.y || 0;
		this.setLocation(x, y);
		if (config.backgroundColor != undefined)
			this.setStyle('background-color', config.backgroundColor);
	},
	
	getLocation: function() {
		return {x: this.getX(), y: this.getY()};
	},
	
	setLocation: function(x, y) {
		this.setX(x);
		this.setY(y);
	},
	
	getX: function() {
		return this.getStyle('left');
	},
	
	setX: function(x) {
		this.setStyle('left', x);
	},
	
	getY: function() {
		return this.getStyle('top');
	},
	
	setY: function(y) {
		this.setStyle('top', y);
	},
	
	getRotation: function() {
		return this.rotation;
	},
	
	setRotation: function(r) {
		this.rotation = r;
		this.setCSS3Style('transform', 'rotate('+r+'deg)');
	},
	
	setAttribute: function(attrName, value) {
		this.access().attr(attrName, value);
	},
	
	getAttribute: function(attrName) {
		return this.access().attr(attrName);
	},
	
	setStyle: function(k, v) {
		this.access().css(k, v);
	},
	
	getStyle: function(k) {
		return this.access().css(k);
	},
	
	setCSS3Style: function(k, v) {
		this.setStyle(k, v);
		this.setStyle('-ms-'+k, v);
		this.setStyle('-webkit-'+k, v);
		this.setStyle('-moz-'+k, v);
		this.setStyle('-o-'+k, v);
	},
	
	getScale: function() {
		return { scaleX: this.scaleX, scaleY:this.scaleY };
	},
	
	setScaleX: function(x, time) {
		if (time == undefined) time = 0;
		this.scaleX = x;
		this.access().effect('scale', { percent: x*100, direction: 'horizontal' }, time);
	},
	
	setScaleY: function(y, time) {
		if (time == undefined) time = 0;
		this.scaleY = y;
		this.access().effect('scale', { percent: y*100, direction: 'vertical' }, time);
	},
	
	setScale: function(s, time) {
		if (time == undefined) time = 0;
		this.access().effect('scale', { percent: s*100, direction: 'both' }, time);
	},
	
	getId: function() {
		return this.id;
	},
	
	access: function() {
		return this.domObject;
	},
	
	toHtml: function() {
		return "";
	},
	
	dispose: function() {
		this.access().dispose();
		var objMgr = SingletonFactory.getInstance(Application).getObjectManager();
		objMgr.remove(this);
	}
});

/*
DisplayObject = Class.extend	({
	init: function()	{
		var app = SingletonFactory.getInstance(Application);
		this.id=0;
		this.type = "default";
		this.htmlClasses = new Array();
		this.interfaces = new Array();
		var objMgr = app.getObjectManager();
		this.className = "DisplayObject";
		objMgr.register(this);
	},
	
	addEventHandler: function(eventHandler)	{
		new EventBinder().bindEvent(this, eventHandler);
	},
	
	deserializeStyle: function(style)	{
		this.deserializedStyle = style;
	},
	
	serializeStyle: function()	{
		return this.getAttribute('style');
	},
	
	serialize: function()	{
		var obj = {};
		obj.className = this.className;
		return obj;
	},
	
	getPosition: function()	{
		return this.access().offset();
	},
	
	moveTo: function(top, left)	{
		this.setStyle('left', left+"px");
		this.setStyle('top', top+"px");
	},
	
	center: function()	{
		this.access().hAlign();
		this.access().vAlign();
	},
	
	moveToBR: function(bottom, right)	{
		this.setStyle('bottom', bottom+"px");
		this.setStyle('right', right+"px");
	},

	getType: function()	{
		return "ij-"+this.type;
	},

	toHtml: function()	{
		return "";
	},
	
	getParent: function()	{
		return this.parent;
	},
	
	setParent: function(parent)	{
		if (this.parent != undefined)	{
			this.parent.detachObject(this);
		}
		this.parent = parent;
	},
	
	getId: function()	{
		return this.id;
	},
	
	access: function()	{
		return $('#'+this.id);
	},
	
	resizeTo: function(width, height)	{
		this.setStyle('width', width+"px");
		this.setStyle('height', height+"px");
	},
	
	getDimension: function()	{
		var dim = {};
		dim.width = this.access().width();
		dim.height = this.access().height();
		return dim;
	},
	
	setStyle: function(styleName, value)	{
		this.setStyle(styleName, value);
	},
	
	setAttribute: function(attrName, value)	{
		this.access().attr(attrName, value);
	},
	
	getAttribute: function(attrName)	{
		return this.access().attr(attrName);
	},
	
	implement: function(implementor)	{
		if($("#"+this.getId()).length > 0){
			implementor.implement(this);
			implementor.state = true;
		}
		this.interfaces.push(implementor);
		this.updateInterfaces();
	},
	
	addClass: function(className)	{
		this.htmlClasses.push(className);
		this.updateClasses();
	},
	
	getHtmlClasses: function()	{
		return this.htmlClasses;
	},
	
	show: function()	{
		this.access().show();
	},
	
	hide: function()	{
		this.access().hide();
	},
	
	dispose: function()	{
		var app = SingletonFactory.getInstance(Application);
		this.access().remove();
		app.getObjectManager().remove(this);
	},
	
	updateClasses: function()	{
		var classes = this.getHtmlClasses();
		var classNames = this.getType();
		for(var i=0;i<classes.length;i++)	{
			classNames+= " "+classes[i];
		}
		this.access().attr('class', classNames);
	},
	
	updateInterfaces: function()	{
		var interfaces = this.interfaces;
		for(var i=0;i<interfaces.length;i++)	{
			var implementor = interfaces[i];
			if (implementor != null && !implementor.state && $("#"+this.getId()).length > 0)	{
				implementor.implement(this);
				implementor.state = true;
			}
		}
	},
	
	afterLoad: function()	{
		this.setAttribute('style', this.deserializedStyle);
	},
	
	update: function()	{
		this.updateClasses();
		this.updateInterfaces();
	},
	
	afterCreate: function()	{
		this.update();
		new EventBinder().fireEvent(this.access(), 'object_created');
	}
});
*/

/**
 * Container class. A container is an object which 'contains' other objects
 */
DisplayObjectContainer = DisplayObject.extend({
	
	setupBase: function(config)	{
		this.className = this.className || "Container";
		this.children = new Array();
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this._super(config);
		if (config.layout == undefined)
			config.layout = 'flow';
		this.setLayout(config.layout);
	},
	
	addChildBeforePosition: function(obj, positionObj)	{
		this.objects.push(obj);
		if (obj._parent != undefined)
			obj._parent.detachObject(obj);
		obj.access().insertBefore(positionObj);
		obj._parent = this;
		obj.stage = this.stage;
	},
	
	addChild: function(obj)	{
		this.children.push(obj);
		if (obj._parent != undefined)
			obj._parent.detachObject(obj);
		obj.access().appendTo(this.access());
		obj._parent = this;
		obj.stage = this.stage;
	},
	
	removeObject: function(object)	{
		for(var i=0;i<this.children.length;i++)	{
			var obj = this.children[i];
			if (obj.getId() == object.getId())	{
				this.children.splice(i, 1);
				object.dispose();
			}
		}
	},
	
	detachObject: function(object)	{
		for(var i=0;i<this.children.length;i++)	{
			var obj = this.children[i];
			if (obj.getId() == object.getId())	{
				this.children.splice(i, 1);
				obj.access().detach();
			}
		}
	},
	
	getChildren: function()	{
		return this.children;
	},
	
	setLayout: function(layout) {
		if (this.layout != undefined)
			this.access().removeClass('joo-layout-'+this.layout);
		this.access().addClass('joo-layout-'+layout);
		this.layout = layout;
	}
});

Sketch = DisplayObjectContainer.extend({
	
	setupBase: function(config)	{
		this.className = this.className || "Sketch";
		this._super(config);
	},
	
	repaint: function(html) {
		this.access().html(html);
	},
	
	draw: function(html) {
		this.access().append(html);
	},
	
	clear: function(html) {
		this.access().html("");
	},

	toHtml: function()	{
		return "<div></div>";
	}
});

UIComponent = Sketch.extend({
	
	setupBase: function(config) {
		this.className = "UIComponent";
		this._super(config);
	},
	
	setupDisplay: function(config) {
		this._super(config);
		this.contextMenu = new JOOContextMenu(this);
	},
	
	getContextMenu: function() {
		return this.contextMenu;
	},
});

UIRenderInterface = InterfaceImplementor.extend({
	
	implement: function(obj) {
		obj.prototype.render = obj.prototype.render || function() {
			tmpl('UI-'+obj.className, obj.config);
		}
	}
});

Stage = DisplayObjectContainer.extend({

	setupBase: function(config)	{
		this.stage = this;
		this.id = config.id;
		this.allowMultiSelect = config.allowMultiSelect || true;
		this.selectedObjects = Array();
		this._super(config);
	},
	
	getSelectedObjects: function() {
		return this.selectedObjects;
	},
	
	removeSelectedObject: function(obj) {
		if (typeof obj['_deselect'] == 'undefined')
			throw 'Object '+obj+' is not deselectable';
		var index = this.selectedObjects.indexOf(obj);
		if (index != -1) {
			obj.selected = false;
			obj._deselect();
			this.selectedObjects.splice(index, 1);
/*			
			var subject = SingletonFactory.getInstance(Subject);
			subject.notifyEvent('ObjectDeselected', obj);*/
		}
	},
	
	deselectAllObjects: function() {
		for(var i=0;i<this.selectedObjects.length;i++) {
			this.selectedObjects[i]._deselect();
		}
		this.selectedObjects = Array();
		
/*		var subject = SingletonFactory.getInstance(Subject);
		subject.notifyEvent('AllObjectDeselected');*/
	},
	
	addSelectedObject: function(obj, isMultiSelect) {
		if (typeof obj['_select'] == 'undefined')
			throw 'Object '+obj+' is not selectable';
		if (isMultiSelect == undefined) isMultiSelect = false;
		if (!this.allowMultiSelect || !isMultiSelect) {
			this.deselectAllObjects();
		}
		obj.selected = true;
		obj._select();
		this.selectedObjects.push(obj);
		
		var subject = SingletonFactory.getInstance(Subject);
		subject.notifyEvent('ObjectDeselected', obj);
	},
	
	setupDomObject: function(config) {
		this.domObject = $('#'+this.id);
		this.access().addClass('joo-'+this.type);
		this.access().addClass('joo-uicomponent');

		if (config.layout == undefined)
			config.layout = 'flow';
		this.setLayout(config.layout);
	},
});

JOOContextMenu = Sketch.extend({
	
	setupBase: function()	{
		this.className = "JOOContextMenu";
		this.items = new Array();
		this._super();
	},
	
	addItem: function(item) {
		this.items.push(item);
		this.addObject(item);
	},
	
	getItems: function() {
		return this.items;
	},
	
	show: function(x, y) {
		this.setLocation(x, y);
		this.access().show();
	},
	
	hide: function() {
		this.access().hide();
	}
});

Canvas = DisplayObject.extend({
	
	setupBase: function(x, y, width, height)	{
		this.className = "Canvas";
		this.context = undefined;
		this._super();
	},
	
	getContext: function()	{
		if(this.context == undefined){
			this.context = new CanvasContext(this, "2d");
		}
		return this.context;
	},
	
	toHtml: function(){
		return "<canvas>Sorry, your browser does not support canvas</canvas>";
	}
});

/*
 * CanvasContext
 */
CanvasContext = Class.extend({
    init: function(canvas, dimension){
        if (canvas.access().length == 0) {
            throw Error("From CanvasContext constructor: cannot init canvas context");
        }
        this.canvas = canvas;
        if (dimension == undefined) {
            dimension = "2d";
        }
        this.dimension = dimension;
        this.context = document.getElementById(canvas.getId()).getContext(dimension);
    },
    
    /*
     * get&set fillStyle
     */
    setFillStyle: function(color){
        this.context.fillStyle = color;
    },
    
    getFillStyle: function(){
        return this.context.fillStyle;
    },
    
    /*
     * get&set strokeStyle
     */
    setStrokeStyle: function(color){
        this.context.strokeStyle = color;
    },
    
    getStrokeStyle: function(){
        return this.context.strokeStyle;
    },
    
    /*
     * get&set globalAlpha
     */
    setGlobalAlpha: function(alpha){
        this.context.globalAlpha = alpha;
    },
    
    getGlobalAlpha: function(){
        return this.context.globalAlpha;
    },
    /*
     * get&set lineWidth
     */
    setLineWidth: function(w){
        this.context.lineWidth = w;
    },
    
    getLineWidth: function(){
        return this.context.lineWidth;
    },
    
    /*
     * get&set lineCap
     */
    setLineCap: function(cap){
        this.context.lineCap = cap;
    },
    
    getLineCap: function(){
        return this.context.lineCap;
    },
    
    /*
     * get&set lineJoin
     */
    setLineJoin: function(j){
        this.context.lineJoin = j;
    },
    
    getLineJoin: function(){
        return this.context.lineJoin;
    },
    
    /*
     * get&set shadowOffsetX
     */
    setShadowOffsetX: function(x){
        this.context.shadowOffsetX = x;
    },
    
    getShadowOffsetX: function(){
        return this.context.shadowOffsetX;
    },
    
    /*
     * get&set shadowOffsetY
     */
    setShadowOffsetY: function(y){
        this.context.shadowOffsetY = y;
    },
    
    getShadowOffsetY: function(){
        return this.context.shadowOffsetY;
    },
    
    /*
     * get&set shadowBlur
     */
    setShadowBlur: function(blur){
        this.context.shadowBlur = blur;
    },
    
    getShadowBlur: function(){
        return this.context.shadowBlur;
    },
    
    /*
     * get&set shadowColor
     */
    setShadowColor: function(color){
        this.context.shadowColor = color;
    },
    
    getShadowColor: function(){
        return this.context.shadowColor;
    },
    
    /*
     * get&set globalCompositeOperation
     */
    setGlobalCompositeOperation: function(v){
        this.context.globalCompositeOperation = v;
    },
    
    getGlobalCompositeOperation: function(){
        return this.context.globalCompositeOperation;
    },
    
    clearRect: function(x,y,width,height){
    	this.context.clearRect(x,y,width,height);
    },
    
    fillRect: function(x, y, w, h){
        this.context.fillRect(x, y, w, h);
    },
    
    strokeRect: function(x, y, w, h){
        this.context.strokeRect(x, y, w, h);
    },
    
    beginPath: function(){
        this.context.beginPath();
    },
    
    closePath: function(){
        this.context.closePath();
    },
    
    stroke: function(){
        this.context.stroke();
    },
    
    fill: function(){
        this.context.fill();
    },
    
    getImageData: function(x,y,width,height){
    	return this.context.getImageData(x,y,width,height);
    },
    
    moveTo: function(x, y){
        this.context.moveTo(x, y);
    },
    
    lineTo: function(x, y){
        this.context.lineTo(x, y);
    },
    
    arc: function(x, y, radius, startAngle, endAngle, anticlockwise){
        this.context.arc(x, y, radius, startAngle, endAngle, anticlockwise);
    },
    
    quadraticCurveTo: function(cp1x, cp1y, x, y){
        this.context.quadraticCurveTo(cp1x, cp1y, x, y);
    },
    
    bezierCurveTo: function(cp1x, cp1y, cp2x, cp2y, x, y){
        this.context.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
    },
    
    drawRoundedRect: function(x, y, width, height, radius){
        var ctx = this.context;
        ctx.beginPath();
        ctx.moveTo(x, y + radius);
        ctx.lineTo(x, y + height - radius);
        ctx.quadraticCurveTo(x, y + height, x + radius, y + height);
        ctx.lineTo(x + width - radius, y + height);
        ctx.quadraticCurveTo(x + width, y + height, x + width, y + height - radius);
        ctx.lineTo(x + width, y + radius);
        ctx.quadraticCurveTo(x + width, y, x + width - radius, y);
        ctx.lineTo(x + radius, y);
        ctx.quadraticCurveTo(x, y, x, y + radius);
        ctx.stroke();
        ctx.closePath();
    },
	
	drawCircle: function(x, y, radius){
		var ctx = this.context;
		ctx.beginPath();
		ctx.arc(x,y,radius,0,Math.PI*2,true);
		if(ctx.fillStyle != undefined){
			ctx.fill();
		}
		if(ctx.strokeStyle != undefined){
			ctx.stroke();
		}
		ctx.closePath();
	},
    
    createLinearGradient: function(x1, y1, x2, y2){
        return this.context.createLinearGradient(x1, y1, x2, y2);
    },
    
    createRadialGradient: function(x1, y1, r1, x2, y2, r2){
        return this.context.createRadialGradient(x1, y1, r1, x2, y2, r2);
    },
    
    createPattern: function(image, type){
        return this.context.createPattern(image, type);
    },
    
    save: function(){
        this.context.save();
    },
    
    restore: function(){
        this.context.restore();
    },
    
    rotate: function(angle){
        this.context.rotate(angle);
    },
    
    scale: function(x, y){
        this.context.scale(x, y);
    },
    
    transform: function(m11, m12, m21, m22, dx, dy){
        this.context.transform(m11, m12, m21, m22, dx, dy);
    },
    
    setTransform: function(m11, m12, m21, m22, dx, dy){
        this.context.setTransform(m11, m12, m21, m22, dx, dy);
    },
    
    clip: function(){
        this.context.clip();
    },
    
    setFont: function(font){
    	var fstr = "";
    	if(font.getBold()){
    		fstr += "bold ";
    	}
    	if(font.getItalic()){
    		fstr += "italic ";
    	}
		this.context.fillStyle = font.getColor();
    	fstr += font.getFontSize()+" ";
    	fstr += font.getFontFamily();
    	this.context.font = fstr;
    },
    
    getFont: function(){
    	var font = new Font();
    	fstr = this.context.font;
    	if(fstr.indexOf("bold") != -1 || fstr.indexOf("Bold") != -1){
    		font.setBold(true);
    	}
    	if(fstr.indexOf("italic") != -1 || fstr.indexOf("Italic") != -1){
    		font.setItalic(true);
    	}
    	var fontSize = fstr.match(/\b\d+(px|pt|em)/g);
    	if(fontSize!=null && fontSize.length > 0){
    		font.setFontSize(fontSize[0]);
    	}
    	var fontFamily = fstr.match(/\b\w+,\s?[a-zA-Z-]+\b/g);
    	if(fontFamily!=null && fontFamily.length >0){
    		font.setFontFamily(fontFamily[0]);
    	}
    	return font;
    },
    
    getTextWidth: function(text){
    	return this.context.measureText(text).width;
    },
    
    getTextHeight: function(text){
    	//return this.context.measureText(text).width;
    	throw "not yet implemented";
    },
    
    fillText: function(text,x,y,maxWidth){
    	if(maxWidth != undefined){
	    	this.context.fillText(text,x,y,maxWidth);
		}else{
			this.context.fillText(text,x,y);
		}
    },
    
    strokeText: function(text,x,y,maxWidth){
		if(maxWidth != undefined){
			this.context.strokeText(text,x,y,maxWidth);
		}else{
			this.context.strokeText(text,x,y);
		}	
    }
});