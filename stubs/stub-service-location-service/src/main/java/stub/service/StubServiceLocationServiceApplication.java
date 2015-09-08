package stub.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
@RequestMapping("/serviceLocations")
public class StubServiceLocationServiceApplication {

	@RequestMapping("")
	public String home(@RequestParam(required=false, defaultValue="0") int page) {
		if (page>0) {
			return "forward:/stubs/empty.json";
		}
		return "forward:/stubs/locations.json";
	}

	@RequestMapping("/search/findFirstByLocationNear")
	public String findByLocation(@RequestParam String location) {
		Assert.state(location.contains(","),
				"Location should be comma-separated lat,long");
		return "forward:/stubs/location.json";
	}

	public static void main(String[] args) {
		SpringApplication.run(StubServiceLocationServiceApplication.class, args);
	}
}
