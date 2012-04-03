package cz.skymia.cyrilrc.server.service

import java.util.regex.*;
import cz.skymia.cyrilrc.server.domain.*

@Singleton
class MusicService {
	
	def List<Artist> artists = new LinkedList<Artist>()

	def Pattern songPattern
	def Random random = new Random()
	def Pattern MESSAGE_VARIABLE_PATTERN = Pattern.compile("([^#]*)(#([^#]*)#)?");

	def config = Application.instance.config
	java.util.logging.Logger log =  java.util.logging.Logger.getLogger(this.class.name)
	
	def MusicService(){
		log.info "Initializing MusicService - home: ${config.music.home}, song suffixes: ${config.song.suffixes}"
		initSongPattern();
	}
	
	void init(){
		loadMusic()
		if( config.player.startup.playRandomAlbums ){
			playRandomAlbums()
		}
	}

	private loadMusic() {
		new File( config.music.home ).eachDir { artistDir ->
			log.fine "Found artist directory ${artistDir.absolutePath}"
			def artist = new Artist( artistDir )

			checkAlbum(artist, artistDir)
			artistDir.eachDirRecurse { albumDir ->
				checkAlbum(artist, albumDir)
			}

			if( artist.albums ){
				artists.add(artist)
			}
		}
	}

	private initSongPattern() {
		StringBuilder pattern = new StringBuilder()
		pattern.append(".*(")
		config.song.suffixes.eachWithIndex { it, i ->
			if( i!=0 ){
				pattern.append("|")
			}
			pattern.append(it)
		}
		pattern.append(")\$")
		log.info "Song pattern: ${pattern}"
		songPattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE)
	}

	def getArtists() {
		return artists;
	}

	def getRandomArtist(){
		if( !artists ){
			return null
		}
		else {
			return artists[random.nextInt(artists.size())]
		}
	}
	
	def getRandomAlbum(){
		Artist artist = getRandomArtist()
		if( !artist || !artist.albums ){
			return null
		} else {
			return artist.albums[random.nextInt(artist.albums.size())]
		}
	}
	
	def playRandomAlbums(){
		def ret = []
		for( int i=0; i < config.player.loadCount; i++ ){
			def album = getRandomAlbum()
			if( album != null ){
				ret += album
				
				log.info "Playing ${album.dir.absolutePath}"
				def params = [file:album.dir.absolutePath]
				if( i == 0 ){
					execute(config.player.loadplay, params)
				} else {
					execute(config.player.load, params)
				}
			}
		}
		
		return ret
	}
	
	
	
	def checkAlbum(Artist artist, File albumDir ){

		boolean containsSong = false
		albumDir.eachFile {
			if( it.name.matches(songPattern)){
				containsSong = true;
			}
		}

		if( containsSong ){
			def album = new Album( albumDir )
			artist.addAlbum(album)
		}
	}
	

	def execute(cmd){
		if( cmd ){
			log.info "Executing command: " + cmd
			def initialSize = 4096
			def outStream = new ByteArrayOutputStream(initialSize)
			def errStream = new ByteArrayOutputStream(initialSize)
			def process = cmd.execute()
			process.consumeProcessOutput(outStream, errStream)
			def cmdExitCode = process.waitFor()
			if( outStream.toString() )	log.info 'out:\n' + outStream
			if( errStream.toString() ) log.info 'err:\n' + errStream
			log.info "Command exit code ${cmdExitCode}"
		}
	}
	
	def execute( ConfigObject cfg, Map params ){
		log.info "Executing ${cfg} with parameters ${params}"
		
		for(int urlIndex=0; ; urlIndex++){
			def action = cfg."url${urlIndex}"
			if( !action ) {
				break;
			}

			log.fine "Executing action ${action}"
			
			def url = new URL(config.player.url + replaceVariables(action, params))
			log.info "Executing HTTP GET request ${url}"
			def responseText = url.text
			log.info "Response ${responseText}"
		}
	}
	
	
	String replaceVariables(String text, Map params){
		final StringBuilder ret = new StringBuilder(text.length() + 10);

		final Matcher matcher = MESSAGE_VARIABLE_PATTERN.matcher(text);
		while (matcher.find()) {
			ret.append(matcher.group(1));
			final String paramKey = matcher.group(3);
			if (paramKey != null) {
				final String paramValue = params[paramKey];
				if( paramValue ){
					ret.append(java.net.URLEncoder.encode(paramValue,"ISO-8859-1").replaceAll("\\+", "%20"));
				}
			}
		}
		return ret.toString();
	}

		
}
