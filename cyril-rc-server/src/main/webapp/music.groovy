import grails.converters.*
import groovy.xml.*
import groovy.util.logging.Log
import cz.skymia.cyrilrc.server.domain.*
import cz.skymia.cyrilrc.server.service.*
import groovy.transform.Field

@Field java.util.logging.Logger log =  java.util.logging.Logger.getLogger("music")
@Field MusicService musicService = MusicService.instance

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
	renderObject musicService.playRandomAlbums()
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

void write(List<Artist> l, MarkupBuilder xml){
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



