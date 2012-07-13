var currentButtons = [  "setPopularity0", "setPopularity1", "setPopularity2", "setPopularity3", "setPopularity4" ];
var allButtons = [ "prev", "play", "stop", "pause", "next", "playRandomAlbums", "volumedown", "changemute", "volumeup", "shutdown" ].concat( currentButtons);


function ButtonAction(name) {
	this.name = name;
	this.action = function() {
		event.preventDefault();
		if (name == "shutdown") {
			var answer = confirm("Realy shutdown?");
			if (!answer) {
				return;
			}
		}
		$.get("music/" + name);
		setTimeout(function() {
			$("#currentWrapper").load("music/currentHTML", function(){
				setActions(currentButtons);
			});
		}, 500);
	};
}

function setActions(buttons){
	for ( var i = 0; i < buttons.length; i++) {
		var button = buttons[i];
		var ba = new ButtonAction(button)
		$("#" + button + " > a").click(ba.action);
	}
}

$(document).ready( function() {
	setActions(allButtons);
});
