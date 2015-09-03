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
package demo.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Gunnar Hillert
 */
public class ServiceLocationTests {

	@Test
	public void testServiceLocationDeserialization() throws JsonParseException, JsonMappingException, IOException {
		final InputStream is = ServiceLocationTests.class.getResourceAsStream("/service-location.json");
		final ObjectMapper objectMapper = new ObjectMapper();
		final ServiceLocation serviceLocation = objectMapper.readValue(is, ServiceLocation.class);
		Assert.assertNotNull(serviceLocation);
		Assert.assertEquals(Double.valueOf(38.907773), serviceLocation.getLocation().getLatitude());
		Assert.assertEquals(Double.valueOf(-77.023735), serviceLocation.getLocation().getLongitude());

		Assert.assertEquals("55e521c430044aedf761fa52", serviceLocation.getId());
		Assert.assertEquals("1317 9th St NW", serviceLocation.getAddress1());
		Assert.assertEquals("Washington", serviceLocation.getCity());
		Assert.assertEquals("DC", serviceLocation.getState());
		Assert.assertEquals("20001", serviceLocation.getZip());
		Assert.assertEquals("Service", serviceLocation.getType());
	}

	@Test
	public void testServiceLocationSerialization() throws JsonParseException, JsonMappingException, IOException {
		final ServiceLocation serviceLocation = new ServiceLocation(Double.valueOf(38.907773), Double.valueOf(-77.023735));
		serviceLocation.setId("55e521c430044aedf761fa52");
		serviceLocation.setAddress1("1317 9th St NW");
		serviceLocation.setCity("Washington");
		serviceLocation.setState("DC");
		serviceLocation.setZip("20001");
		serviceLocation.setType("Service");

		final InputStream is = ServiceLocationTests.class.getResourceAsStream("/service-location.json");
		final String serviceLocationAsString = IOUtils.toString(is);


		final ObjectMapper objectMapper = new ObjectMapper();
		final String json = objectMapper.writeValueAsString(serviceLocation);

		Assert.assertEquals(serviceLocationAsString, json);
	}
}
