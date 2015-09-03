package stub.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
@RequestMapping("/serviceLocations")
public class StubServiceLocationServiceApplication {

	@RequestMapping("")
	public String home() {
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
