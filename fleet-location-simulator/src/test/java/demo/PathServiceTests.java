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

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import demo.model.GpsSimulatorRequest;
import demo.model.Point;
import demo.model.ServiceLocation;
import demo.model.SimulatorFixture;
import demo.service.KmlService;
import demo.service.PathService;

/**
 * @author Gunnar Hillert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@SpringApplicationConfiguration(classes = {GpsSimulatorApplication.class})
public class PathServiceTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(PathServiceTests.class);

	@Autowired
	private PathService pathService;

	@Autowired
	private KmlService kmlService;

	/**
	 * Test method for {@link demo.service.impl.DefaultKmlService#setupKmlIntegration(java.io.File)}.
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCreateGpsKml() throws FileNotFoundException, JAXBException {
		Set<Long> ids = new HashSet<>();
		ids.add(1L);
		ids.add(2L);
		ids.add(3L);

		kmlService.setupKmlIntegration(ids, new Point(123d, 123d), null);
		Assert.assertNotNull(kmlService.getKmlBootstrap());
	}

	@Test
	public void testPolyUtil() throws FileNotFoundException, JAXBException {
		EncodedPolyline encodedPolyline = new EncodedPolyline(
				"gdgwBnb_w\\VqBj@aBh@_AfBuBLOjC_DdAiABCNMPOfN_O`AwAn@u@v@iAZ}@F[Fi@@"
				+ "a@Aq@C[CSGWIYM_@IQMSMQWWYW{@g@sEy@uIuAeASgI{AaBYk@KsHqAYImAm@m@c@"
				+ "SWe@m@a@s@c@qAUaB?i@AE@c@Bc@Fk@Jm@Pk@Ti@^k@x@_AjCoCxC}CX[Z[V]rPoQ"
				+ "xFeGz@}@d@e@\\g@Vc@N_@Pi@Hm@D}@Aq@Ci@Ig@IUQi@Qe@S][e@i@a@YSe@Ug@Ss"
				+ "@Ms@Gs@CqACkKSyXi@on@gAcAMu@Wy@k@m@wAUk@Kw@Aw@Do@Pu@Pc@Tc@JKhA_AfAw@");
		List<LatLng> latlongs = encodedPolyline.decodePath();
		LOGGER.info("Number of coordinates: {}", latlongs.size());
		LOGGER.info("Coordinates: {}", latlongs);

		Assert.assertTrue(!latlongs.isEmpty());
	}

	@Test
	@IfProfileValue(name="use-google-directions", value="true")
	public void testDirections() throws FileNotFoundException, JAXBException {
		List<Point> points = pathService.getCoordinatesFromGoogle(pathService.loadDirectionInput().get(0));;

		LOGGER.info("Number of coordinates: {}", points.size());
		LOGGER.info("Coordinates: {}", points);

	}

	@Test
	@IfProfileValue(name="use-google-directions", value="true")
	public void testGetServiceStationCoordinates() {
		List<ServiceLocation> serviceLocations = pathService.getServiceStations();
		Assert.assertNotNull(serviceLocations);

		for (ServiceLocation serviceLocation : serviceLocations) {
			LOGGER.info("ServiceLocation: {}", serviceLocation);
		}
	}

	@Test
	public void testLoadSimulatorFixture() {
		final SimulatorFixture fixture = pathService.loadSimulatorFixture();
		Assert.assertNotNull(fixture);

		final List<GpsSimulatorRequest> gpsSimulatorRequests = fixture.getGpsSimulatorRequests();
		Assert.assertNotNull(gpsSimulatorRequests);
		Assert.assertEquals(Integer.valueOf(8), Integer.valueOf(gpsSimulatorRequests.size()));
		for (GpsSimulatorRequest gpsSimulatorRequest : gpsSimulatorRequests) {
			Assert.assertNotNull(gpsSimulatorRequest.getVin());
			Assert.assertNotNull(gpsSimulatorRequest.getPolyline());
			Assert.assertNotNull(gpsSimulatorRequest.getReportInterval());
			Assert.assertNotNull(gpsSimulatorRequest.getSecondsToError());
			Assert.assertNotNull(gpsSimulatorRequest.getSpeedInKph());
			Assert.assertNotNull(gpsSimulatorRequest.getVehicleStatus());
		}
	}

}
