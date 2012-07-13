package cz.skymia.cyrilrc.server.service

import javax.servlet.ServletException;
import javax.servlet.http.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory


public class Startup extends HttpServlet {
	
	Logger log =  LoggerFactory.getLogger(this.class)
	
	public Startup(){
	}
	
	@Override
	public void init() throws ServletException {
		log.info "Starting Cyril RC"
		MusicService.instance.init()
	}	
}
