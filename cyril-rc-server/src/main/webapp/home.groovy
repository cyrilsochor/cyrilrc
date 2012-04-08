import cz.skymia.cyrilrc.server.service.*

def playing = MusicService.instance.playingInfo

def soe(it){
	it?it:""
}

html.html {
  head {
      title("Cyril Remote Control Server")
	  link( rel:"stylesheet", type:"text/css", href:"${request.contextPath}/css/main.css" )
  }
  body {
	h1("Cyril Remote Control Server")
    div(class:"summary"){
		p{
			table(class:"info"){
				caption("Current playing")
				tr {
					td() 
					th( "File" ) 
					th( "id3v1" ) 
					th( "id3v2" ) 
				}
				tr { 
					th( "Artist" ) 
					td( "${soe(playing?.pathTag?.artist)}" )
					td( "${soe(playing?.id3v1Tag?.artist)}" )
					td( "${soe(playing?.id3v2Tag?.artist)}" )
				}
				tr { 
					th( "Album" ) 
					td( "${soe(playing?.pathTag?.album)}" )
					td( "${soe(playing?.id3v1Tag?.album)}" )
					td( "${soe(playing?.id3v2Tag?.album)}" )
				}
				tr { 
					th( "Title" )
					td( "${soe(playing?.pathTag?.title)}" )
					td( "${soe(playing?.id3v1Tag?.title)}" )
					td( "${soe(playing?.id3v2Tag?.title)}" )
				}
			}
		}
		p(
			"There are ${MusicService.instance.artists?.size()} artists."
		)
		table {
			title("Actions")
			tr{ td{	a( href:"music/all", "List of all albums") } }
			tr{ td{	a( href:"music/randomArtist", "Display random artist") }}
			tr{ td{	a( href:"music/randomAlbum?displayDirectory=true", "Display random album with directory") }}
			tr{ td{	a( href:"music/playRandomAlbums?displayDirectory=true", "Play ${Application.instance.config.player.loadCount} random albums") }}
			tr{ td{	a( href:"music/iHateIt?displayDirectory=true", "I hate it - add current album to blacklist and play another") }}
			tr{ td{	a( href:"music/stop", "Stop") }}
			tr{ td{	a( href:"music/play", "Play") }}
			tr{ td{	a( href:"music/prev", "Previous") }}
			tr{ td{	a( href:"music/next", "Next") }}
			tr{ td{	a( href:"music/volumeup", "Volume UP") }}
			tr{ td{	a( href:"music/volumedown", "Volume DOWN") }}
			tr{ td{	a( href:"music/changemute", "Change mute") }}
		}
	}
  }
}