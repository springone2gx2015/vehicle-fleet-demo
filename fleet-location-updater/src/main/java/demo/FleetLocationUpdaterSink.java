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
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import demo.model.CurrentPosition;
import demo.model.ServiceLocation;

/**
 * Spring Cloud Stream {@link Sink}, responsible for sending current position data to
 * connected Websocket clients.
 *
 * @author Gunnar Hillert
 *
 */
@EnableBinding(Sink.class)
public class FleetLocationUpdaterSink {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	@Bean
	@Transformer(inputChannel = Sink.INPUT, outputChannel = "addCurrentPositionChannel")
	public org.springframework.integration.transformer.Transformer JsonToObjectTransformer() {
		return new JsonToObjectTransformer(CurrentPosition.class);
	}

	@Bean
	public MessageChannel addCurrentPositionChannel() {
		return new DirectChannel();
	}

	@Transformer(inputChannel = "addCurrentPositionChannel", outputChannel = "sendToBroker")
	public CurrentPosition addServiceLocations(CurrentPosition payload) {

		switch (payload.getVehicleStatus()) {

		case SERVICE_NOW:
		case SERVICE_SOON:
			ResponseEntity<Resource<ServiceLocation>> result = this.restTemplate.exchange(
					"http://SERVICE-LOCATION-SERVICE/serviceLocations/search/findFirstByLocationNear?location={lat},{long}",
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
		return payload;
	}

	@Bean
	public MessageChannel sendToBroker() {
		return new DirectChannel();
	}

	@ServiceActivator(inputChannel = "sendToBroker")
	public void sendToStompClients(CurrentPosition payload) {
		this.template.convertAndSend("/queue/fleet.location.ingest.queue", payload);
	}

}
