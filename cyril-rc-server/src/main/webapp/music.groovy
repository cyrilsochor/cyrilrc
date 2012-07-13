import org.slf4j.Logger
import org.slf4j.LoggerFactory
import grails.converters.*
import groovy.xml.*
import cz.skymia.cyrilrc.server.domain.*
import cz.skymia.cyrilrc.server.service.*
import groovy.transform.Field

@Field Logger log =  LoggerFactory.getLogger("music")
@Field MusicService musicService = MusicService.instance
@Field def config = Application.instance.config

def action = request.getAttribute("action")
if( !action ) action = "all"
log.info "Music action ${action}"
this."$action"()

def all(){
	renderObject musicService.artists
}

def randomArtist(){
	renderObject musicService.randomArtist
}

def randomAlbum(){
	renderObject musicService.randomAlbum
}

def playRandomAlbums(){
	renderObject musicService.playRandomAlbums(params["hearer"])
}

def setPopularity(popularity){
	renderObject musicService.setPopularity(params["hearer"], popularity)
}

def setPopularity0(){
	setPopularity(0)
}

def setPopularity1(){
	setPopularity(1)
}

def setPopularity2(){
	setPopularity(2)
}

def setPopularity3(){
	setPopularity(3)
}

def setPopularity4(){
	setPopularity(4)
}

def play(){
	renderObject musicService.execute(config.player.play)
}

def stop(){
	renderObject musicService.execute(config.player.stop)
}

def pause(){
	renderObject musicService.execute(config.player.pause)
}

def prev(){
	renderObject musicService.execute(config.player.prev)
}

def next(){
	renderObject musicService.execute(config.player.next)
}

def volumeup(){
	renderObject musicService.execute(config.player.volumeup)
}

def volumedown(){
	renderObject musicService.execute(config.player.volumedown)
}

def changemute(){
	renderObject musicService.execute(config.player.changemute)
}

def shutdown(){
	renderObject musicService.execute(config.player.shutdown)
}

def currentHTML(){
	musicService.writePlayingHTML(html)
}

def renderObject(it){
	response.setContentType('text/xml')
	if( it == null ){
		out.print "<nothing></nothing>";
	} else {
		def xml = new MarkupBuilder(out)
		write(it, xml)
	}
}

void write(Collection<Artist> l, MarkupBuilder xml){
	xml.artists {
		l.each { a ->
			write(a, xml)
		}
	}
}

void write( Artist a, MarkupBuilder xml ){
	xml.artist(name: a.name){
		albums {
			a.albums.each{ t ->
				write(t, xml)
			}
		}
	}
}

void write( Album a, MarkupBuilder xml){
	xml.album(name: a.name) {
		if( "true" == params["displayDirectory"] ){
			directory(a.dir.absolutePath)
		}
	}
}

void write( String s, MarkupBuilder xml ){
	xml.result(s)
}

