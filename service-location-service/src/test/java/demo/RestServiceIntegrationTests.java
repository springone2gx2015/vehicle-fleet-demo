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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringApplicationConfiguration(classes = ServiceLocationServiceApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class RestServiceIntegrationTests {

	@Value("${local.server.port}")
	int port;

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@ClassRule
	public static MongoTestSupport mongo = new MongoTestSupport("mongoClient");

	@Rule
	public SpringMethodRule springMethod = new SpringMethodRule();

	@Autowired
	ServiceLocationRepository repository;

	@Autowired
	ObjectMapper objectMapper;

	@Before
	public void init() throws Exception {
		if (this.repository.count() == 0) {
			List<ServiceLocation> values = this.objectMapper.readValue(
					new ClassPathResource("locations.json").getInputStream(),
					new TypeReference<List<ServiceLocation>>() {
					});
			this.repository.save(values);
		}
	}

	@Test
	public void findAll() {
		TestRestTemplate template = new TestRestTemplate();
		ResponseEntity<Resource<List<ServiceLocation>>> result = template.exchange(
				"http://localhost:" + this.port + "/serviceLocations", HttpMethod.GET,
				new HttpEntity<Void>((Void) null),
				new ParameterizedTypeReference<Resource<List<ServiceLocation>>>() {
				});
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void findByLocation() {
		TestRestTemplate template = new TestRestTemplate();
		ResponseEntity<Resource<ServiceLocation>> result = template.exchange(
				"http://localhost:" + this.port
				+ "/serviceLocations/search/findFirstByLocationNear?location={lat},{long}",
				HttpMethod.GET, new HttpEntity<Void>((Void) null),
				new ParameterizedTypeReference<Resource<ServiceLocation>>() {
				}, 39, -84);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

}
