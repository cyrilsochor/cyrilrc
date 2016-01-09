package cz.skymia.cyrilrc.server.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class Application {
	
	Logger log =  LoggerFactory.getLogger(this.class)
	
	ConfigObject config
	
	static List configLocations = [
		Application.class.classLoader.loadClass('ConfigDefault'),
		new File("c:/windows/cyrilrc/server-config.groovy"),
		new File("/etc/cyrilrc/server-config.groovy"),
		new File(System.getProperty("user.home"), ".cyrilrc/server-config.groovy"),
	]
	
	def Application(){
		log.info "Initializing Music Application"

		config = loadConfiguration();
		log.info "Configuration: ${config}"
	}
	
	ConfigObject loadConfiguration(){
		def ret = null
		
		configLocations.each {
			if( it ){
				def url = null
				if( it instanceof File ){
					if( it.isFile() ){
						url = it.toURI().toURL()
					} else {
						log.info "No configuration found on location ${it.absolutePath}"
					}
				} else if( it instanceof URL) {
					url = it;
				} else if( it instanceof Class) {
					url = it;
				} else {
					log.error "Unknown config location type ${it.class.name}"
				}
				
				if( url ) {
					log.info "Loading configuration ${url}"
					def partConfig = new ConfigSlurper().parse(url);
					log.info "Loaded partial configuration ${partConfig}"
					ret = ret == null ? partConfig : ret.merge(partConfig)
				}
			}
		}
		
		
		return ret
	}
	
	public ConfigObject getConfig(){
		return config;
	}
	
}
