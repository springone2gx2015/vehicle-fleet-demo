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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Dave Syer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FleetLocationServiceApplication.class)
@WebAppConfiguration
@TestPropertySource(properties = "spring.jpa.showSql=true")
public class FleetLocationTests {

	@Autowired
	ObjectMapper mapper;

	@Autowired
	LocationRepository repository;

	@Before
	public void setup() throws Exception {
		saveJson();
	}

	@Test
	public void findAll() throws Exception {
		Iterable<Location> vehicles = this.repository.findAll();
		assertEquals(4, getList(vehicles).size());
	}

	@Test
	public void findByVin() throws Exception {
		Page<Location> vehicles = this.repository.findByUnitInfoUnitVin(
				"1FUJGBDV20LBZ2345", new PageRequest(0, 20));
		assertEquals(1, getList(vehicles).size());
	}

	private void saveJson() throws IOException, JsonParseException, JsonMappingException {
		saveJson(new ClassPathResource("fleet.json"));
	}

	private void saveJson(Resource resource) throws IOException, JsonParseException,
	JsonMappingException {
		if (this.repository.count() == 0) {
			List<Location> value = this.mapper.readValue(resource.getInputStream(),
					new TypeReference<List<Location>>() {
			});
			assertEquals(4, value.size());
			this.repository.save(value);
		}
	}

	private List<Location> getList(Iterable<Location> vehicles) {
		ArrayList<Location> list = new ArrayList<Location>();
		for (Location location : vehicles) {
			list.add(location);
		}
		return list;
	}

}
