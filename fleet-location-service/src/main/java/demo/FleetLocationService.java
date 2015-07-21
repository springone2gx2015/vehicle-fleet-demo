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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Dave Syer
 *
 */
@RestController
public class FleetLocationService {

	private final LocationRepository repository;

	@Autowired
	public FleetLocationService(LocationRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/fleet", method = RequestMethod.GET)
	public FleetResource fleet() {
		FleetLocation fleet = new FleetLocation();
		fleet.setTrucks(this.repository.findAll());
		fleet.setId(123456L);
		int stopped = 0;
		int moving = 0;
		for (Location truck : fleet.getTrucks()) {
			if (truck.getVehicleMovementType().isMoving()) {
				moving++;
			}
			else {
				stopped++;
			}
		}
		fleet.setMovingTrucks(moving);
		fleet.setStoppedTrucks(stopped);
		FleetResource resource = new FleetResource(fleet);
		resource.add(linkTo(FleetLocationService.class).slash("/fleet").withRel("self"));
		return resource;
	}

	public static class FleetResource extends ResourceSupport {
		private FleetLocation fleet;

		public FleetResource(FleetLocation fleet) {
			this.fleet = fleet;
		}

		@JsonUnwrapped
		public FleetLocation getFleet() {
			return this.fleet;
		}
	}
}
