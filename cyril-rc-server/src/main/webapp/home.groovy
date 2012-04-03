import cz.skymia.cyrilrc.server.service.*

html.html {
  head {
      title("Cyril Remote Control Server")
  }
  body {
	h1("Cyril Remote Control Server")
    div(class:"summary"){
		p(
			"There are ${MusicService.instance.artists.size()} artists."
		)
		table{
			title("Actions")
			tr{ td{	a( href:"music/all", "List of all albums") } }
			tr{ td{	a( href:"music/randomArtist", "Display random artist") }}
			tr{ td{	a( href:"music/randomAlbum?displayDirectory=true", "Display random album with directory") }}
			tr{ td{	a( href:"music/playRandomAlbums?displayDirectory=true", "Play ${Application.instance.config.player.loadCount} random albums") }}
		}
	}
  }
}