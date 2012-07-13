package cz.skymia.cyrilrc.server.domain

import cz.skymia.cyrilrc.server.service.MusicService;
import cz.skymia.cyrilrc.server.util.*
import java.util.concurrent.*

class HearerProfile {

	String name
	ConcurrentSkipListMap<File, Integer> musicPopularity = new ConcurrentSkipListMap() 
	ListWithWeight<Album> albumsWeight = new ConcurrentSkipListMap();
	
	public HearerProfile(String name){
		this.name = name;
	}
	
	public HearerProfile(){
	}
		
	public int getPopularity(Album album){
		if( ! album?.dir ){
			return MusicService.POPULARITY_DEFAULT
		}
		
		Integer popularity = musicPopularity.get(album.dir)
		if( popularity==null ){
			return MusicService.POPULARITY_DEFAULT
		} else {
			return popularity
		}
	}
				
	public void setPopularity(Album album, int popularity){
		musicPopularity.put( album.dir.absoluteFile, popularity )
		ensure(album, popularity)
	}
	
	void ensure(Album album, popularity){
		albumsWeight.remove(album)
		def weight = popularity + 1
		if( weight > 0 ){
			albumsWeight.add(album,weight)
		}
	}
	
	public String toString(){
		"HearerProfile[name=${name}]"
	}

}
