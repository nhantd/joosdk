PlayState = TriggeredState.extend({
	
	onGameInitialization: function() {
		//TODO: Do game initialization here, such as register events
		//Below is an example of register for 'drag' event

		/* start registering events */
		this.helper.onGenericObjDragged(['success'], function(e) {
			//TODO: Handle drag success event here
		});
		this.helper.onGenericObjDragged(['fail'], function(e) {
			//TODO: Handle drag success fail here
		});
		/* end registering events */
		
		//start playing game
		this.play();
	},
	
	onWakeup: function() {
		//TODO: Do something when state is woken up
		this.play();
	},
	
	onPause: function() {
		//TODO: Do something when state is paused
	},
	
	play: function() {
		this.refreshScreen();
		
		//TODO: Do something on game-play
	}
});