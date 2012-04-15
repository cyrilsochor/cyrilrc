<html>
<head>
 	<meta charset="utf-8" />
	<link rel="stylesheet" type="text/css" href="css/mobile.css" />
</head>
<%
def playing = cz.skymia.cyrilrc.server.service.MusicService.instance.playingInfo

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
		</div>
	</div>
    <div class="summary">
		<table class="info" id="currentPlaying">
			<caption>Current playing</caption>
			<tr class="header">
				<th></th> 
				<th>File</th> 
				<th>id3v1</th> 
				<th>id3v2</th> 
			</tr>
			<tr class="data artist">
				<th>Artist</th>
				<td class="pathTag"   ><% print soe(playing?.pathTag?.artist) %></td> 
				<td class="id3v1Tag"  ><% print soe(playing?.id3v1Tag?.artist) %></td>
				<td class="id3v2Tag"  ><% print soe(playing?.id3v2Tag?.artist) %></td>
			</tr>
			<tr class="data album">
				<th>Album</th> 
				<td class="pathTag"   ><% print soe(playing?.pathTag?.album) %></td>
				<td class="id3v1Tag"  ><% print soe(playing?.id3v1Tag?.album) %></td>
				<td class="id3v2Tag"  ><% print soe(playing?.id3v2Tag?.album) %></td>
			</tr>
			<tr class="data title">
				<th>Title</th>
				<td class="pathTag"   ><% print soe(playing?.pathTag?.title) %></td>
				<td class="id3v1Tag"  ><% print soe(playing?.id3v1Tag?.title) %></td>
				<td class="id3v2Tag"  ><% print soe(playing?.id3v2Tag?.title) %></td>
			</tr>
		</table>
	</div>
</body>
</html>
