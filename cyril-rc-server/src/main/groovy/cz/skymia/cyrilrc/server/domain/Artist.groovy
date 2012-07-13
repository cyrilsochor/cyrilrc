package cz.skymia.cyrilrc.server.domain

import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Artist {

	private File dir
	private String name
	def ConcurrentSkipListMap<String,Album> albumsMap = new ConcurrentSkipListMap<String,Album>()
	
	static Logger log =  LoggerFactory.getLogger(Artist.class)
	
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
	
	public String toString(){
		"Artist[name=${name}]"
	}
}
