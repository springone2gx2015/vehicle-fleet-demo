package demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestApi {
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	@Autowired
	private ClientConfig clientConfig;

    @RequestMapping("/clientConfig")
    @ResponseBody
    public Map<String, String> config() throws Exception {
    	Map<String, String> data = new HashMap<String, String>();
    	try {
    		if (clientConfig != null && clientConfig.getServices() != null) {
    			data.putAll(clientConfig.getServices());
    			for (Map.Entry<String, String> serviceEntry : data.entrySet()) {
    				String serviceUrl = getServiceUrl(serviceEntry.getKey());
    				if (serviceUrl != null) {
    					serviceEntry.setValue(serviceUrl);
    				}
    			}
    		}
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    	return data;
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
