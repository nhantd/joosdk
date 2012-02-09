JOOImage = UIComponent.extend	({
	
	setupBase: function(config) {
		this.className = this.className || "JOOImage";
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this._super(config);
		this.setAttribute('src', config.src);
	},
	
	toHtml: function()	{
		return "<img />";
	},
	
	getSrc: function()	{
		return this.getAttribute('src');
	},
	
	setSrc: function(src) {
		this.setAttribute('src', src);
	}
});

JOOInput = UIComponent.extend({
	
	setupDomObject: function(config) {
		this._super(config);
		this.access().val(config.value);
		this.setAttribute('name', config.name);
	},
	
	getValue: function()	{
		return this.access().val();
	},
	
	getName: function() {
		return this.getAttribute('name');
	},
	
	focus: function()	{
		this.access().focus();
	}
});

JOOTextArea = JOOInput.extend	({
	
	setupBase: function(config) {
		this.className = this.className || "JOOTextArea";
		this._super(config);
	},
	
	toHtml: function()	{
		return "<textarea></textarea>";
	},
	
	getText: function()	{
		return this.access().val();
	}
});

JOOLabel = UIComponent.extend	({
	
	setupBase: function(config)	{
		this.className = this.className || "JOOLabel";
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this._super(config);
		this.access().html(config.text);
	},
	
	toHtml: function()	{
		return "<label></label>";
	},
		
	getText: function()	{
		return this.access().html();
	},
	
	setText: function(txt)	{
		this.access().html(txt);
	}
});

JOOTextInput = JOOInput.extend({
	
	setupBase: function(config) {
		this.className = this.className || "JOOTextInput";
		this._super(config);
	},
	
	toHtml: function()	{
		return "<input type='text' />";
	}
});

JOOInputBox = JOOInput.extend({
	
	setupBase: function(config) {
		this.className = this.className || "JOOInputBox";
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this._super(config);
		this.label = config.labelObject || new JOOLabel(config.lbl);
		this.input = config.inputObject || new JOOTextInput(config.value);
		this.addChild(this.label);
		this.addChild(this.input);
	},
	
	getValue: function()	{
		return this.input.getValue();
	},
	
	getLabel: function() {
		return this.label.getValue();
	},
	
	getName: function() {
		return this.input.getName();
	},
	
	focus: function()	{
		this.input.focus();
	}
});

JOOButton = UIComponent.extend({
	
	setupBase: function(config)	{
		this.className = this.className || "Button";
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this._super(config);
		if (config.txt == undefined) {
			config.txt = this.id;
		}
		this.access().html(config.txt);
		this.addEventListener('click', this.onclick);
	},
	
	toHtml: function()	{
		return "<button></button>";
	},
	
	onClick: function(e) {}
});

JOOToggleButton = JOOButton.extend({
	
	setupBase: function(config) {
		this.state = config.state;
		this.className = "JOOToggleButton";
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this._super(config);
		if (!this.state)
			this.addClass('joo-toggle-down');
	},
	
	onclick: function(e) {
		this.access().toggleClass("joo-toggle-down");
		if(this.state) {
			this.state = false;
			this.ontoggledown();
		} else {
			this.state = true;
			this.ontoggleup();
		}
	},
	
	ontoggledown: function() {},
	
	ontoggleup: function() {}
});

SelectableInterface = InterfaceImplementor.extend({
	
	implement: function(obj) {
		obj.prototype._select = obj.prototype._select || function() {}
		
		obj.prototype._deselect = obj.prototype._deselect || function() {}
		
		obj.prototype.select = obj.prototype.select || function(isMultiSelect) {
			this.dispatchEvent('beforeselected');
			if (this.blockSelect != true) {
				this.stage.addSelectedObject(this, isMultiSelect);
				this.dispatchEvent('afterselected');
			}
		},
		
		obj.prototype.deselect = obj.prototype.deselect || function() {
			this.stage.removeSelectedObject(this);
		}
	}
});

DraggableInterface = InterfaceImplementor.extend({
	
	implement: function(obj){
		
		obj.prototype.draggable = obj.prototype.draggable || function(param) {
			this.access().draggable(param);
		};

		obj.prototype.startDrag = obj.prototype.startDrag || function() {
			this.access().draggable("enable");
		};
		
		obj.prototype.stopDrag = obj.prototype.stopDrag || function(){
			this.access().draggable("disable");
		};
	},
});

ResizableInterface = InterfaceImplementor.extend({
	
	implement: function(obj) {
		obj.prototype.updateArea = obj.prototype.updateArea || function(newW, newH) {};
		
		obj.prototype.showResizeControl = obj.prototype.showResizeControl || function(){
			this.closeBtn.access().show();
			this.topR.access().show();
			this.bttR.access().show();
			this.bttL.access().show();
		},
		
		obj.prototype.hideResizeControl = obj.prototype.hideResizeControl || function(){
			this.closeBtn.access().hide();
			this.topR.access().hide();
			this.bttR.access().hide();
			this.bttL.access().hide();		
		},
		
		obj.prototype.onDeleteHandler = obj.prototype.onDeleteHandler || function(e) {};
		
		obj.prototype.onMouseUpHandler = obj.prototype.onMouseUpHandler || function(e) {};
		
		obj.prototype.onMouseDownHandler = obj.prototype.onMouseDownHandler || function(e) {};
		
		obj.prototype.beginEditable = obj.prototype.beginEditable || function() {
			//Define child components
			this.topR = new Sketch();
			this.bttL = new Sketch();
			this.bttR = new Sketch();
			this.closeBtn = new JOOImage({src: "static/images/close.png"});
			
			this.closeBtn.access().addClass('joo-close-btn');
			this.closeBtn.setLocation(-5, -5);
			
			this.access().addClass("joo-editable-component");
			
			//Initialize resize buttons
			this.ctrlBtnOffset = -4;
			this.topR.setStyle("cursor", "e-resize");
			this.bttR.setStyle("cursor", "se-resize");
			this.bttL.setStyle("cursor", "s-resize");
			this.topR.access().addClass("unselectable");
			this.bttR.access().addClass("unselectable");
			this.bttL.access().addClass("unselectable");
			this.topR.access().addClass("joo-move-icon");
			this.bttL.access().addClass("joo-move-icon");
			this.bttR.access().addClass("joo-move-icon");
			this.topRDragging = false;
			this.bttRDragging = false;
			this.bttLDragging = false;
			
			var width = Math.abs(this.ctrlBtnOffset * 2);
			var height = Math.abs(this.ctrlBtnOffset * 2);
			this.topR.setStyle("width", width + "px");
			this.bttR.setStyle("width", width + "px");
			this.bttL.setStyle("width", width + "px");
			this.topR.setStyle("height", height + "px");
			this.bttR.setStyle("height", height + "px");
			this.bttL.setStyle("height", height + "px");

			this.minW = 0;
			this.maxW = Number.MAX_VALUE;
			this.minH = 0;
			this.maxH = Number.MAX_VALUE;
			
			var thisObject = this;
			var mouseMoveHandlerTopR = function(e){
				var posX = e.pageX - thisObject.access().offset().left;
				var newW = posX;
				thisObject.topRDragging = true;
				if(posX > thisObject.minW && posX < thisObject.maxW){
					thisObject.updateArea(newW);
				}
			};

			var mouseMoveHandlerBttL = function(e){
				var posY = e.pageY - thisObject.access().offset().top;
				var newH = posY;
				thisObject.bttLDragging = true;
				if(posY > thisObject.minH && posY < thisObject.maxH){
					thisObject.updateArea(undefined, newH);
				}		
			};
			
			var mouseMoveHandlerBttR = function(e){
				var posX = e.pageX - thisObject.access().offset().left;
				var posY = e.pageY - thisObject.access().offset().top;
				var newW = posX;
				var newH = posY;
				thisObject.bttRDragging = true;
				if(posX > thisObject.minW && posY > thisObject.minH
					&& posX < thisObject.maxW && posY < thisObject.maxH ){
					thisObject.updateArea(newW, newH);
				}
			};
			
			$(document).bind("mouseup",function(){
				if(thisObject.topRDragging){
					$(window).unbind("mousemove",mouseMoveHandlerTopR);
					thisObject.topR.removeEventListener("mousemove", mouseMoveHandlerTopR);
					thisObject.onMouseUpHandler();
					thisObject.topRDragging = false;
				}
				if(thisObject.bttLDragging){
					$(window).unbind("mousemove", mouseMoveHandlerBttL);
					thisObject.bttL.removeEventListener("mousemove", mouseMoveHandlerBttL);
					thisObject.onMouseUpHandler();
					thisObject.bttLDragging = false;
				}
				if(thisObject.bttRDragging){
					$(window).unbind("mousemove", mouseMoveHandlerBttR);
					thisObject.bttR.removeEventListener("mousemove", mouseMoveHandlerBttR);
					thisObject.onMouseUpHandler();
					thisObject.bttRDragging = false;
				}
			});	

			this.closeBtn.addEventListener('click', this.onDeleteHandler);

			this.topR.addEventListener('mousedown', function() {
				$(window).bind("mousemove", mouseMoveHandlerTopR);
				this.addEventListener("mousemove", mouseMoveHandlerTopR);
				thisObject.onMouseDownHandler();
			});
			
			this.bttL.addEventListener('mousedown', function() {
				$(window).bind("mousemove", mouseMoveHandlerBttL);
				this.addEventListener("mousemove", mouseMoveHandlerBttL);
				thisObject.onMouseDownHandler();
			});
			this.bttR.addEventListener('mousedown', function() {
				$(window).bind("mousemove", mouseMoveHandlerBttR);
				this.addEventListener("mousemove", mouseMoveHandlerBttR);
				thisObject.onMouseDownHandler();
			});

			this.addChild(this.closeBtn);
			this.addChild(this.topR);
			this.addChild(this.bttR);
			this.addChild(this.bttL);
			this.updateArea();
		};
	}
});

JOOResizableCanvas = UIComponent.extend({
	
	setupBase: function(config) {
		this.className = "JOOResizableCanvas";
		this._super(config);
	},
	
	setupDomObject: function(config) {
		this.canvas = this.canvas || new Canvas();
		this.canvasW = 150;
		this.canvasH = 100;
		this.canvas.setAttribute("width", this.canvasW);
		this.canvas.setAttribute("height", this.canvasH);
		
		this.text = config.txt;
		this.textDisplay = new Sketch();
		this.textDisplay.access().addClass('joo-editable-text');
		this.textDisplay.repaint(this.text);
		
		this._super(config);
		this.beginEditable();
		
		this.addChild(this.canvas);
		this.addChild(this.textDisplay);
		
		this.addEventListener('dragstop', this.onDragStopHandler);
		this.addEventListener('click', this.onClick);
		this.addEventListener('drag', this.onDragHandler);
		this.addEventListener('mouseover', this.onMouseOverHandler);
		this.addEventListener('mouseout', this.onMouseOutHandler);
		
		this.hideResizeControl();
		
		this.draggable();
	},
	
	getCanvas: function() {
		return this.canvas;
	},
	
	onClick: function(e) {
		this.select();
	},

	_select: function() {
		this.showResizeControl();
	},
	
	_deselect: function() {
		this.hideResizeControl();
	},
	
	onDragHandler: function(e) {},
	
	onDragStopHandler: function(e) {},
	
	onMouseOverHandler: function(e) {},
	
	onMouseOutHandler: function(e) {},
	
	onMouseUpHandler: function(e) {
		this.startDrag();
	},
	
	onMouseDownHandler: function(e) {
		this.stopDrag();
	},

	updateArea: function(newW, newH) {
		this.canvasW = newW || this.canvasW;
		this.canvasH = newH || this.canvasH;
		this.canvas.setAttribute("width", this.canvasW);
		this.canvas.setAttribute("height", this.canvasH);
		
		this.textDisplay.access().html(this.text);
		this.textDisplay.setStyle("width", this.canvasW - 15 + "px");
		this.textDisplay.setStyle("height", this.canvasH - 6 + "px");
		
		this.topR.setLocation(this.canvasW + this.ctrlBtnOffset, this.ctrlBtnOffset);
		this.bttR.setLocation(this.canvasW + this.ctrlBtnOffset, this.canvasH + this.ctrlBtnOffset);
		this.bttL.setLocation(this.ctrlBtnOffset, this.canvasH + this.ctrlBtnOffset);
	}
}).implement(ResizableInterface).implement(DraggableInterface).implement(SelectableInterface);