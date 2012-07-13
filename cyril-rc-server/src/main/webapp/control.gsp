<html>
<head>
 	<meta charset="utf-8" />
	<link rel="stylesheet" type="text/css" href="css/mobile.css" />
</head>
<%
def musicService = cz.skymia.cyrilrc.server.service.MusicService.instance;
def playing = musicService.playingInfo

def soe(it){
	it?it:""
}
%>	
<body>
	<script src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
	<script src="js/main.js"></script>
	<div id="controlWrapper">
		<div id="playWrapper">
			<div id="prev"><a href="#"><img src="icon/prev.png" /></a></div>
			<div id="play"><a href="#"><img src="icon/play.png" /></a></div>
			<div id="pause"><a href="#"><img src="icon/pause.png" /></a></div>
			<div id="next"><a href="#"><img src="icon/next.png" /></a></div>
			<div id="playRandomAlbums"><a href="#"><img src="icon/playRandomAlbums.png" /></a></div>
		</div>
		<div id="volumeWrapper">
			<div id="volumedown"><a href="#"><img src="icon/volumedown.png" /></a></div>
			<div id="changemute"><a href="#"><img src="icon/changemute.png" /></a></div>
			<div id="volumeup"><a href="#"><img src="icon/volumeup.png" /></a></div>
			<div id="shutdown"><a href="#"><img src="icon/shutdown.png" /></a></div>
		</div>
	</div>
	<%  print musicService.writePlayingHTML(playing) %>
</body>
</html>
