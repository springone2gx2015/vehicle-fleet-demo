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
package demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import demo.model.DirectionInput;
import demo.model.Point;
import demo.model.SimulatorFixture;
import demo.service.PathService;

/**
 *
 * @author Gunnar Hillert
 *
 */
@Service
public class DefaultPathService implements PathService {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment environment;

	@Autowired
	private Unmarshaller unmarshaller;

	public DefaultPathService() {
		super();
	}

	/* (non-Javadoc)
	 * @see frk.gpssimulator.service.impl.PathServiceInterface#loadDirections()
	 */
	@Override
	public List<DirectionInput> loadDirectionInput() {
		final InputStream is = this.getClass().getResourceAsStream("/directions.json");

		try {
			return objectMapper.readValue(is, new TypeReference<List<DirectionInput>>() {
				//Just make Jackson happy
			});
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/* (non-Javadoc)
	 * @see frk.gpssimulator.service.impl.PathServiceInterface#loadDirections()
	 */
	@Override
	public SimulatorFixture loadSimulatorFixture() {
		final InputStream is = this.getClass().getResourceAsStream("/fixture.json");

		try {
			return objectMapper.readValue(is, SimulatorFixture.class);
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getCoordinatesFromGoogleAsPolyline(DirectionInput directionInput) {
		final GeoApiContext context = new GeoApiContext().setApiKey(environment.getRequiredProperty("gpsSimmulator.googleApiKey"));
		final DirectionsApiRequest request =  DirectionsApi.getDirections(
			context,
			directionInput.getFrom(),
			directionInput.getTo());

		try {
			DirectionsRoute[] routes = request.await();
			return routes[0].overviewPolyline.getEncodedPath();
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/* (non-Javadoc)
	 * @see frk.gpssimulator.service.impl.PathServiceInterface#getCoordinatesFromGoogle(frk.gpssimulator.model.DirectionInput)
	 */
	@Override
	public List<Point> getCoordinatesFromGoogle(DirectionInput directionInput) {

		final GeoApiContext context = new GeoApiContext().setApiKey(environment.getRequiredProperty("gpsSimmulator.googleApiKey"));
		final DirectionsApiRequest request =  DirectionsApi.getDirections(
			context,
			directionInput.getFrom(),
			directionInput.getTo());
		List<LatLng> latlongList = null;

		try {
			DirectionsRoute[] routes = request.await();

			for (DirectionsRoute route : routes) {
				latlongList = route.overviewPolyline.decodePath();
			}
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}

		final List<Point> points = new ArrayList<>(latlongList.size());

		for (LatLng latLng : latlongList) {
			points.add(new Point(latLng.lat, latLng.lng));
		}

		return points;
	}

	/* (non-Javadoc)
	 * @see frk.gpssimulator.service.KmlService#getCoordinates(java.io.File)
	 */
	@Override
	public final List<Point> getCoordinatesFromKmlFile(File kmlFile) {

		final Kml kml;
		try {
			kml = (Kml) unmarshaller.unmarshal(new StreamSource(kmlFile));
		}
		catch (XmlMappingException | IOException e) {
			throw new IllegalStateException(e);
		}

		final Document doc = (Document) kml.getFeature();
		List<Feature> features = doc.getFeature();
		List<Point> pointsToReturn = new ArrayList<Point>();

		for (Feature feature : features) {
			if (feature instanceof Placemark) {
				final Placemark placemark = (Placemark) feature;
				if (placemark.getGeometry() instanceof LineString) {
					final LineString lineString = (LineString) placemark.getGeometry();
					List<Coordinate> coordinates = lineString.getCoordinates();
					for(Coordinate coord : coordinates) {
						Point point2 = new Point(
								coord.getLatitude(),
								coord.getLongitude());
						pointsToReturn.add(point2);
					}
					break;
				}
			}

		}
		return pointsToReturn;

	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setGoogleApiKey(String googleApiKey) {
		Assert.hasText(googleApiKey, "The googleApiKey must not be empty.");

	}

}
