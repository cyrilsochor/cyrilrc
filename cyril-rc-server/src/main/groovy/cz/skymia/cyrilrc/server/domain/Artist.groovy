package cz.skymia.cyrilrc.server.domain

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

class Artist {

	private File dir
	private String name
	def ConcurrentSkipListMap<String,Album> albumsMap = new ConcurrentSkipListMap<String,Album>()
	
	static java.util.logging.Logger log =  java.util.logging.Logger.getLogger(Artist.class.name)
	
    static constraints = {
    }
	
	public Artist (File dir){
		this.dir = dir
		this.name = dir.name;
	}
	
	public String getName(){
		return name;
	}
	 
	public void setName(String name){
		this.name = name;
	}
	
	public Collection<Album> getAlbums(){
		return albumsMap.values();
	}

	public addAlbum(Album album){
		this.albumsMap.put(album.name, album)
	}
	
}
