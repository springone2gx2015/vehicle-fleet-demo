package stub.fleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
public class StubFleetLocationServiceApplication {

	@RequestMapping("/")
	public String home() {
		return "forward:/stubs/home.json";
	}

	@RequestMapping("/locations")
	public String home(@RequestParam(required=false, defaultValue="0") int page) {
		if (page>0) {
			return "forward:/stubs/empty.json";
		}
		return "forward:/stubs/locations.json";
	}

	@RequestMapping("/locations/search/findByUnitInfoUnitVin")
	public String findByVin(@RequestParam String vin) {
		return "forward:/stubs/findByVin.json";
	}

	public static void main(String[] args) {
		SpringApplication.run(StubFleetLocationServiceApplication.class, args);
	}
}
