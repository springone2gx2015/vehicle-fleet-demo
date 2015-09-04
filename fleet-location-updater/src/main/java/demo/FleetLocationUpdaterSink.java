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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.model.CurrentPosition;
import demo.model.ServiceLocation;

/**
 * Spring Cloud Stream {@link Sink}, responsible for sending current position data to
 * connected Websocket clients.
 *
 * @author Gunnar Hillert
 *
 */
@MessageEndpoint
@EnableBinding(Sink.class)
public class FleetLocationUpdaterSink {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@ServiceActivator(inputChannel = Sink.INPUT)
	public void addServiceLocations(String input) throws Exception {

		CurrentPosition payload = this.objectMapper.readValue(input, CurrentPosition.class);

		switch (payload.getVehicleStatus()) {

		case SERVICE_NOW:
		case SERVICE_SOON:
		case STOP_NOW:
			ResponseEntity<Resource<ServiceLocation>> result = this.restTemplate.exchange(
					"http://service-location-service/serviceLocations/search/findFirstByLocationNear?location={lat},{long}",
					HttpMethod.GET, new HttpEntity<Void>((Void) null),
					new ParameterizedTypeReference<Resource<ServiceLocation>>() {
					}, payload.getLocation().getLatitude(),
					payload.getLocation().getLongitude());
			if (result.getStatusCode() == HttpStatus.OK
					&& result.getBody().getContent() != null) {
				payload.setServiceLocation(result.getBody().getContent());
			}
			break;
		default:
		}
		this.template.convertAndSend("/queue/fleet.location.ingest.queue", payload);
	}

}
