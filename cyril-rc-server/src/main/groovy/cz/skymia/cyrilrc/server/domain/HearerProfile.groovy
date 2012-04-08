package cz.skymia.cyrilrc.server.domain

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
		Integer popularity = musicPopularity.get(album.dir)
		if( !popularity ){
			return 0
		} else {
			return popularity
		}
	}
				
	void ensure(Album album, popularity){
		albumsWeight.remove(album)
		def weight = popularity + 1
		if( weight > 0 ){
			albumsWeight.add(album,weight)
		}
	}
}
