package cz.skymia.cyrilrc.server.service

import javax.servlet.ServletException;
import javax.servlet.http.*


public class Startup extends HttpServlet {
	
	java.util.logging.Logger log =  java.util.logging.Logger.getLogger(this.class.name)
	
	public Startup(){
	}
	
	@Override
	public void init() throws ServletException {
		log.info "Starting Cyril RC"
		MusicService.instance.init()
	}	
}
