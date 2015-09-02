/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Dave Syer
 *
 */
@Configuration
@EnableAutoConfiguration
public class FleetLocationNarrower implements CommandLineRunner {

	@Value("${fleet.json:classpath:/fleet.json}")
	private Resource fleet;

	@Value("${fleet.longitude:-77.0164}")
	private double longitude;

	@Value("${fleet.radius:1}")
	private double radius;

	@Value("${fleet.laitude:38.9047}")
	private double latitude;

	@Autowired
	ObjectMapper mapper;

	@Override
	public void run(String... args) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> location : loadJson(this.fleet)) {
			if (closeEnough(location)) {
				list.add(location);
			}
		}
		this.mapper.writeValue(new FileSystemResource("new_fleet.json").getOutputStream(),
				list);
	}

	private boolean closeEnough(Map<String, Object> location) {
		if (location.get("longitude")==null || location.get("latitude")==null) {
			return false;
		}
		Double locLong = Double.valueOf((String) location.get("longitude"));
		Double locLat = Double.valueOf((String) location.get("latitude"));
		if (locLat!=null && locLong!=null) {
			if (this.latitude+5>locLat && this.latitude-this.radius<locLat && this.longitude+this.radius>locLong && this.longitude-this.radius<locLong) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(FleetLocationNarrower.class).web(false)
		.properties("spring.jackson.serialization.INDENT_OUTPUT=true").run(args);
	}

	private List<Map<String, Object>> loadJson(Resource resource)
			throws IOException, JsonParseException, JsonMappingException {
		return this.mapper.readValue(resource.getInputStream(),
				new TypeReference<List<Map<String, Object>>>() {
		});
	}

}
