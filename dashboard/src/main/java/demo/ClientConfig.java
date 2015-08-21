package demo;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="config", locations = "classpath:client.properties")
public class ClientConfig {

	private Map<String, String> services;
	
	public void setServices(Map<String, String> services) {
		this.services = services;
	}
	
	public Map<String, String> getServices() {
		return this.services;
	}
	
}
