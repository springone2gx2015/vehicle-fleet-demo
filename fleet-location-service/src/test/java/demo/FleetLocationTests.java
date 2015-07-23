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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Dave Syer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FleetLocationServiceApplication.class)
public class FleetLocationTests {

	@Autowired
	ObjectMapper mapper;

	@Autowired
	LocationRepository repository;

	@Test
	public void json() throws Exception {
		FleetLocation value = this.mapper.readValue(this.mapper.writeValueAsString(new FleetLocation()), FleetLocation.class);
		assertEquals(0, value.getStoppedTrucks());
	}

	@Test
	public void stub() throws Exception {
		FleetLocation value = this.mapper.readValue(new ClassPathResource("fleet.json").getInputStream(), FleetLocation.class);
		assertEquals(2, value.getStoppedTrucks());
		assertEquals(3, value.getTrucks().size());
		this.repository.save(value.getTrucks());
		Location location = this.repository.findOne(1L);
		System.err.println(this.mapper.writeValueAsString(location));
	}

}
