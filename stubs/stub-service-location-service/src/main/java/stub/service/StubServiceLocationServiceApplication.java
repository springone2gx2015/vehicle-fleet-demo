package stub.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
@RequestMapping("/serviceLocations")
public class StubServiceLocationServiceApplication {

	@RequestMapping("")
	public String home() {
		return "forward:/stubs/locations.json";
	}

	public static void main(String[] args) {
		SpringApplication.run(StubServiceLocationServiceApplication.class, args);
	}
}
