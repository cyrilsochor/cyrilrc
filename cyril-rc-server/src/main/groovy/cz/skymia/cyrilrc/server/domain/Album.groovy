package cz.skymia.cyrilrc.server.domain

class Album {

	private File dir
	private String name

	static constraints = {
	}

	public Album (File dir){
		this.dir = dir;
		this.name = dir.name;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name= name;
	}
	
	public File getDir(){
		return dir;
	}
	
	public String toString(){
		"Album[name=${name}]"
	}
}
