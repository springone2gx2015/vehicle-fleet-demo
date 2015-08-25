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

import java.util.Set;

import demo.model.CurrentPosition;
import demo.model.Point;

/**
 *
 * @author Gunnar Hillert
 *
 */
public interface KmlService {

	/**
	 * Creates a kml object containing a network link and writes it a file,
	 * This file is read by google earth. The file points to another file 'pos.kml' which has
	 * the latest position and is read periodically by google earth.
	 * @param kmlUrl
	 */
	void setupKmlIntegration(Set<Long> intanceIds, Point lookAtPoint, String kmlUrl);

	void updatePosition(Long id, CurrentPosition position);

	byte[] getKmlInstance(Long instanceId);

	byte[] getKmlBootstrap();

	void clearKmlInstances();

}