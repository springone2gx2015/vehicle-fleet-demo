/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package demo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import demo.model.DirectionInput;
import demo.model.Point;
import demo.service.PathService;

/**
 * @author Gunnar Hillert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@SpringApplicationConfiguration(classes = {GpsSimulatorApplication.class})
public class DirectionTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirectionTests.class);

	@Autowired
	private PathService pathService;

	@Test
	public void testGetCoordinates() throws NumberFormatException, JAXBException, JsonParseException, JsonMappingException, IOException {

		final List<DirectionInput> objs = pathService.loadDirectionInput();
		Assert.assertTrue(objs.size() == 8);

	}

	/**
	 * Test method for {@link demo.service.impl.DefaultKmlService#getCoordinates(java.io.File)}.
	 * @throws JAXBException
	 * @throws NumberFormatException
	 */
	@Test
	public void testGetCoordinatesFromKmlFile() throws NumberFormatException, JAXBException {

		File file = new File("src/test/resources/test-route-1.kml");

		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isFile());

		List<Point> points = pathService.getCoordinatesFromKmlFile(file);

		Assert.assertTrue(points.size() > 0);

		for (Point point : points) {
			LOGGER.info(String.format("Lat/Lang: %s,%s",
					point.getLatitude(), point.getLongitude()));
		}

		Assert.assertEquals(Integer.valueOf(167), Integer.valueOf(points.size()));
	}

}
