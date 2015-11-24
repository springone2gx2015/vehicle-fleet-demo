/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
  * file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package demo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Gunnar Hillert
 */
@JsonPropertyOrder({ "numberOfGpsSimulatorRequests", "gpsSimulatorRequests" })
public class SimulatorFixture {

	private List<GpsSimulatorRequest> gpsSimulatorRequests = new ArrayList<>(0);

	public List<GpsSimulatorRequest> getGpsSimulatorRequests() {
		return gpsSimulatorRequests;
	}

	public int getNumberOfGpsSimulatorRequests() {
		return gpsSimulatorRequests.size();
	}

	public void setGpsSimulatorRequests(List<GpsSimulatorRequest> gpsSimulatorRequests) {
		Assert.notEmpty(gpsSimulatorRequests, "gpsSimulatorRequests must not be empty.");
		this.gpsSimulatorRequests = gpsSimulatorRequests;
	}

	public boolean usesKmlIntegration() {
		return gpsSimulatorRequests.stream()
				.anyMatch(request -> request.isExportPositionsToKml());
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SimulatorFixture that = (SimulatorFixture) o;

		return !(gpsSimulatorRequests != null ?
				!gpsSimulatorRequests.equals(that.gpsSimulatorRequests) :
				that.gpsSimulatorRequests != null);

	}

	@Override public int hashCode() {
		return gpsSimulatorRequests != null ? gpsSimulatorRequests.hashCode() : 0;
	}
}
