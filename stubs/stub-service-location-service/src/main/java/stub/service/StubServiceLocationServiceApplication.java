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

	@RequestMapping("/search/findByLocationNear")
	public String findByLocation(@RequestParam String location,
			@RequestParam String distance,
			@RequestParam(required = false, defaultValue = "10") int size) {
		Assert.state(location.contains(","),
				"Location should be comma-separated lat,long");
		Assert.state(distance.endsWith("km"), "Distance should be in km");
		return "forward:/stubs/locations.json";
	}

	public static void main(String[] args) {
		SpringApplication.run(StubServiceLocationServiceApplication.class, args);
	}
}
