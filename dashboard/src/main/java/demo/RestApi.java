package demo;

import java.util.Arrays;
import java.util.Collection;
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
	
	private static final Collection<String> SERVICES = Arrays.asList(new String[] {
			"service-location-updater"
	});
	
	@Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/clientConfig")
    @ResponseBody
    public Map<String, String> config() throws Exception {
    	Map<String, String> data = new HashMap<String, String>();
    	for (String service : SERVICES) {
        	List<ServiceInstance> instances = discoveryClient.getInstances(service);
        	if (instances != null && !instances.isEmpty()) {
            	data.put(service, instances.get(0).getUri().toString());    			
        	}
    	}
    	return data;
    }
    
    

}
