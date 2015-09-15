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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FleetLocationServiceApplication.class)
@WebAppConfiguration
public class DemoApplicationTests {

	@Autowired
	LocationRepository repository;

	@Test
	public void saveLocation() {
		this.repository.save(new Location(new UnitInfo("VIN0")));
	}

	@Test
	public void addFault() {
		Location location = new Location(new UnitInfo("VIN1"));
		location.setUnitFault(new UnitFault(location.getVin()));
		this.repository.save(location);
	}

	@Test
	public void queryByServiceType() {
		Location location = new Location(new UnitInfo("VIN1"));
		location.setServiceType("foo");
		this.repository.save(location);
		assertTrue(this.repository.findByServiceType("foo", new PageRequest(0, 20))
				.getSize() > 0);
	}

}
