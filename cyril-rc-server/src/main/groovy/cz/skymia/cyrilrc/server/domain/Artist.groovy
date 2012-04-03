package cz.skymia.cyrilrc.server.domain

import java.util.List;

class Artist {

	private File dir
	private String name
	private List<Album> albums = new LinkedList<Album>()
	private Album first
	
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
	
	public List<Album> getAlbums(){
		return albums;
	}

	public setAlbums(List<Album> albums){
		this.albums = albums;
	}
	
	public addAlbum(Album album){
		this.albums.add(album)
		first = album;
	}
	
	public Album getFirst(){return first}
	public void setFirst(Album a){first=a}

}
