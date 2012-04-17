var simpleButtons = ["prev", "play", "stop", "pause", "next", "playRandomAlbums", "volumedown", "changemute", "volumeup"];

function ButtonAction(name) {
	  this.name = name;
	  this.action = function () { 
			event.preventDefault();
			$.get("music/"+name);
			setTimeout(function() {$("#currentWrapper").load("music/currentHTML")} , 500);
	  };
}

$(document).ready(function(){
	
	for(var i=0; i<simpleButtons.length; i++){
		button = simpleButtons[i];
		ba = new ButtonAction(button)
		$("#"+button+" > a").click(ba.action);
	}

});



