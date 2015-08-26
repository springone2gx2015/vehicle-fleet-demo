package stub.fleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
@RequestMapping("/locations")
public class StubFleetLocationServiceApplication {

	@RequestMapping("")
	public String home() {
		return "forward:/stubs/locations.json";
	}

	@RequestMapping("/search/findByUnitInfoUnitVin")
	public String findByVin(@RequestParam String vin) {
		return "forward:/stubs/findByVin.json";
	}

	public static void main(String[] args) {
		SpringApplication.run(StubFleetLocationServiceApplication.class, args);
	}
}
