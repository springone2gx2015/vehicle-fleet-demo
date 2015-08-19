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
package demo.service;

import java.io.File;
import java.util.List;

import demo.model.DirectionInput;
import demo.model.Point;
import demo.model.ServiceLocation;
import demo.model.SimulatorFixture;

/**
 *
 * @author Gunnar Hillert
 *
 */
public interface PathService {

	/**
	 *
	 * @return
	 */
	List<DirectionInput> loadDirectionInput();

	SimulatorFixture loadSimulatorFixture();

	/**
	 *
	 * @param directionInput
	 * @return
	 */
	List<Point> getCoordinatesFromGoogle(DirectionInput directionInput);
	String getCoordinatesFromGoogleAsPolyline(DirectionInput directionInput);

	/**
	 * Returns list of points contained in the path kml file.
	 * @param kmlFile path kml file
	 * @return
	 */
	List<Point> getCoordinatesFromKmlFile(File kmlFile);

	List<ServiceLocation> getServiceStations();
}