package demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestApi {

	private static final String SERVICE_LOCATION_UPDATER = "fleet-location-updater";

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
		} catch (Exception t) {
		}
		return url == null || url.isEmpty() ? this.stompUrl : url + "/stomp";
	}

	//TODO: Is this necessary?
	private String getServiceUrl(String service) {
		if (this.discoveryClient != null) {
			List<ServiceInstance> instances = this.discoveryClient.getInstances(service);
			if (!CollectionUtils.isEmpty(instances)) {
				ServiceInstance instance = instances.get(0);
				String host = instance.getHost();
				return host+(instance.getPort()==80 ? "" : ":" + instance.getPort());
			}
		}
		return null;
	}

}
