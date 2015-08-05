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
package demo.support;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.GpsSimulatorApplication;
import demo.model.DirectionInput;
import demo.model.Point;
import demo.service.PathService;
import demo.support.NavUtils;

/**
 * @author Gunnar Hillert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@SpringApplicationConfiguration(classes = {GpsSimulatorApplication.class})
public class NavUtilTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(NavUtilTests.class);

	@Autowired
	private PathService pathService;

	@Test
	@IfProfileValue(name="use-google-directions", value="true")
	public void testTotalDistance() throws NumberFormatException, JAXBException {

		DirectionInput directionInput = new DirectionInput();
		directionInput.setFrom("73-2020 Kaloko Dr, Kailua-Kona, HI 96740");
		directionInput.setTo("73-1249 Kaloko Dr, Kailua-Kona, HI 96740");

		List<Point> points = pathService.getCoordinatesFromGoogle(directionInput);

		double totalDistance = NavUtils.getTotalDistance(points);

		LOGGER.info("totalDistance: " + totalDistance);
	}

}
