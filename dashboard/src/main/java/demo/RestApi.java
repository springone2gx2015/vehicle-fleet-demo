package demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestApi {
	
	private static final String SERVICE_LOCATION_UPDATER = "service-location-updater";
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	@Value("${stomp.url:}")
	private String stompUrl;
	
    @RequestMapping("/clientConfig")
    @ResponseBody
    public String config() throws Exception {
    	String url = null;
    	try {
    		url = getServiceUrl(SERVICE_LOCATION_UPDATER);
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    	return url == null || url.isEmpty() ? stompUrl : url + "/stomp";
    }
    
    private String getServiceUrl(String service) {
		if (discoveryClient != null) {
        	List<ServiceInstance> instances = discoveryClient.getInstances(service);
        	if (instances != null && !instances.isEmpty()) {
            	return instances.get(0).getUri().toString();    			
        	}
		}
		return null;
    }

}
