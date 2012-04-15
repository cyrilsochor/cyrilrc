package cz.skymia.cyrilrc.server.service

import cz.skymia.cyrilrc.server.domain.*

import java.util.logging.*;
import java.util.regex.*;
import java.util.concurrent.*
import org.joda.time.format.*
import org.joda.time.*

@Singleton
class MusicService {
	
	def ConcurrentSkipListMap<String,Artist> artistsMap = new ConcurrentSkipListMap<String,Artist>()

	def Pattern songPattern
	def Random random = new Random()
	private static Pattern MESSAGE_VARIABLE_PATTERN = Pattern.compile("([^#]*)(#([^#]*)#)?");
	private static Pattern PROFILE_SECTION_PATTERN = Pattern.compile("\\[([^\\]]*)\\]");
	
	def config = Application.instance.config
	def File musicHome = new File( config.music.home ).absoluteFile
	def File dataHome = new File( config.data.home ).absoluteFile
	def File backupDir = config?.backup?.dir ? new File( dataHome, config.backup.dir ) : null
	def Map<String,HearerProfile> profiles = new ConcurrentHashMap()

	private static DateTimeFormatter BACKUP_DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	def SECTION_MUSIC_POPULARITY = "music-popularity"
		 
	def Logger log =  Logger.getLogger(this.class.name)
	
	def MusicService(){
		initSongPattern();
	}
	
	void init(){
		log.info "Initializing MusicService - home: ${musicHome}, song suffixes: ${config.song.suffixes}"
		
		if( backupDir ){
			backupDir.mkdirs()
		}
		
		loadMusic()
		log.info "Found ${artistsMap.size()} artists"
		if( config.player.startup.playRandomAlbums ){
			playRandomAlbums()
		}
	}

	private loadMusic() {
		musicHome.eachDir { artistDir ->
			log.fine "Found artist directory ${artistDir.absolutePath}"
			def artist = new Artist( artistDir )

			loadAlbum(artist, artistDir)
			artistDir.eachDirRecurse { albumDir ->
				loadAlbum(artist, albumDir)
			}

			log.finer "Artist ${artist.name} has ${artist.albums?.size()} albums"
			if( artist.albums ){
				artistsMap.put(artist.name, artist)
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
		return artistsMap.values();
	}

	def getRandomArtist(){
		def a = artists.toArray()
		return a[random.nextInt(a.length)]
	}
	
	def getRandomAlbum(){
		Artist artist = getRandomArtist()
		if( !artist || !artist.albums ){
			return null
		} else {
			def a = artist.albums.toArray()
			return a[random.nextInt(a.length)]
		}
	}
	
	def playRandomAlbums( String hearer=null ){
		playProfileRandomAlbums( getHearerProfile(hearer) )
	}
	
	def playProfileRandomAlbums(HearerProfile profile){
		def ret = []
		boolean first = true
		profile.albumsWeight.randomIterator(config.player.loadCount).count { Album album ->
			log.info "Playing ${album.dir.absolutePath}"
			def params = [file:album.dir.absolutePath]
			if( first ){
				execute(config.player.loadplay, params)
				first = false
			} else {
				execute(config.player.load, params)
			}
			ret += album
		}
		
		return ret
	}
	
	def iHateIt(hearer){
		HearerProfile profile = getHearerProfile(hearer)
		Album album = getCurrentAlbum()
		
		if( album ){
			int oldPopularity = profile.getPopularity(album)
			int popularity = oldPopularity-1
			profile.musicPopularity.put( album.dir.absoluteFile, popularity )
			profile.ensure(album, popularity)
			storeProfile(profile)
		}
		
		playProfileRandomAlbums(profile)
	}
	
	HearerProfile getHearerProfile(String hearer=null){
		if( !hearer ) {
			hearer = "default"
		}
		
		def ret = profiles.get(hearer)
		if( !ret ){
			ret = new HearerProfile(hearer)
			loadProfile(ret)
			profiles.put(ret.name, ret)
		}
		
		log.info "Using profile ${ret}"
		return ret
	}

	static class PlayingInfo {
		File songPath
		
		Map pathTag  = [:]
		Map id3v1Tag = [:]
		Map id3v2Tag = [:]
		
		String[] pathArray
		String[] id3v1TagArray
		String[] id3v2TagArray
	}

	PlayingInfo getPlayingInfo() {
		try {
			PlayingInfo ret = new PlayingInfo()
			def currentFileName=execute(config.player.currentfile)
			log.info "Current file: ${currentFileName}"
			ret.songPath = new File(currentFileName).absoluteFile
			
			ret.pathArray = stringSuffix(musicHome.path, ret.songPath.path).split(Pattern.quote(File.separator))
			log.fine "Current file parts ${ret.pathArray}"
			
			if( ret.pathArray.length < 3 ){
				throw new IllegalStateException("At least 3 path parts required, current ${parts}")
			} else if( ret.pathArray.length == 3 ){
				ret.pathTag.album = ret.pathArray[1];
			} else {
				ret.pathTag.album = ret.pathArray[2];
			}
			ret.pathTag.artist = ret.pathArray[1]
			
			def filename = ret.pathArray[ret.pathArray.length-1]
			def lastdot = filename.lastIndexOf('.')
			ret.pathTag.title = 1 > lastdot ? filename : filename.substring(0, lastdot)
			
			ret.id3v1TagArray = execute(config.player.currentId3v1tag)?.split(";", -1)
			if( ret.id3v1TagArray?.length >= 4 ){
				ret.id3v1Tag.artist = ret.id3v1TagArray[1]
				ret.id3v1Tag.album = ret.id3v1TagArray[4]
				ret.id3v1Tag.title = ret.id3v1TagArray[0]
			}
			
			ret.id3v2TagArray = execute(config.player.currentId3v2tag)?.split(";", -1)
			if( ret.id3v2TagArray?.length >= 4 ){
				ret.id3v2Tag.artist = ret.id3v2TagArray[1]
				ret.id3v2Tag.album = ret.id3v2TagArray[4]
				ret.id3v2Tag.title = ret.id3v2TagArray[0]
			}
	
			log.info "Current authorName:${ret.pathTag.artist}, albumName:${ret.pathTag.album}"
			return ret
		} catch (Throwable e){
			log.log Level.SEVERE, "Error retrieve playing info", e 
			return null;
		}
	}
			
	Album getCurrentAlbum(){
		PlayingInfo playing = getPlayingInfo()
			
		Artist artist = artistsMap.get(playing?.pathTag.artist)
		if( !artist ){
			log.warning "Artist with name '${playing?.pathTag.artist}' not found"
			return null
		}
		
		Album album = artist.albumsMap.get(playing?.pathTag.album)
		if( !album ){
			log.warning "Album with name '${playing?.pathTag.album}' not found for artist ${artist}"
		}

		log.fine "Current album: ${album}"
		return album;
	}
	
	def File getProfileFile(profile){
		return new File(dataHome, "profile-"+profile.name + ".csv")
	}
	
	def storeProfile(HearerProfile profile){
		def file = getProfileFile(profile)
		
		if( file.exists() && backupDir ){
			def backupFile = new File(backupDir, file.name + "." + new DateTime().toString(BACKUP_DATETIME_FORMAT) )
			log.info "Moving ${file.absolutePath} to ${backupFile}"
			file.renameTo(backupFile)
		}
		
		file.withWriter { out ->
			out.println "["+SECTION_MUSIC_POPULARITY+"]"
			profile.musicPopularity.entrySet().each {
				out.println "${stringSuffix(musicHome.path, it.key.path)}:${it.value}"
			}
		}
	}
	
	
	def loadProfile(HearerProfile profile){
		def file = getProfileFile(profile)
		if( file.exists() ){
			file.withReader{ input ->
				String section;
				input.eachLine { line ->
					if( line.trim().length() > 0 ){
						log.fine "Profile line:${line}"
						def sectionM = line =~ PROFILE_SECTION_PATTERN;
						if( sectionM.matches() ){
							section = sectionM.group(1);
						} else {
							switch(section){
								case SECTION_MUSIC_POPULARITY:
									def fields = line.trim().split(":")
									switch (fields.length) {
									case 1:
										profile.musicPopularity.put(new File(musicHome, fields[0]), -1)
										break;
									case 2:
										profile.musicPopularity.put(new File(musicHome, fields[0]), Integer.parseInt(fields[1]))
										break;
									default:
										log.warning "Invalid line in section ${SECTION_MUSIC_POPULARITY}:  ${fields} must be array of length 1 or 2"
									}
									break;
								default:
									log.warning "Unknown section ${section}"
							}
						}
					}
				}
			}
		}
		
		artists.each { Artist artist ->
			artist.albums.each { Album album ->
				int popularity = profile.getPopularity(album)
				profile.ensure( album, popularity )
			}
		}
	}

	def loadAlbum(Artist artist, File albumDir ){

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
	
	def execute( ConfigObject cfg, Map params = null){
		log.info "Executing ${cfg} with parameters ${params}"
		def ret
				
		for(int urlIndex=0; ; urlIndex++){
			def action = cfg."url${urlIndex}"
			if( !action ) {
				break;
			}

			log.fine "Executing action ${action}"
			def url = new URL(config.player.url + replaceVariables(action, params))
			log.info "Executing HTTP GET request ${url}"
			ret = url.text
			log.info "Response ${ret}"
		}

		for(int cmdIndex=0; ; cmdIndex++){
			def action = cfg."cmd${cmdIndex}"
			if( !action ) {
				break;
			}

			log.info "Executing action ${action}"
			def process = action.execute()
			def cmdExitCode = process.waitFor()
			log.info "Command exit code ${cmdExitCode}"
			ret = process.text
			log.info "Response ${ret}"
		}

		return ret
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

	String stringSuffix(String base, String s){
		if(s.length() < base.length() ){
			throw new IllegalArgumentException("String '${s}' is shorter then base string '${base}'")
		}
		if( !s.startsWith(base) ){
			throw new IllegalArgumentException("String '${s}' doesn't start with '${base}'")
		}
		
		return s.substring(base.length())
	}
}
