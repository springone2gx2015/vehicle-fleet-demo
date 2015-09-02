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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableModule;
import org.springframework.cloud.stream.annotation.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
 * Spring Cloud Stream {@link Sink}, responsible for sending current position data to connected
 * Websocket clients.
 *
 * @author Gunnar Hillert
 *
 */
@EnableModule(Sink.class)
public class FleetLocationUpdaterSink {

	@Autowired
	MessageChannel input;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	@Transformer(inputChannel="input", outputChannel="addCurrentPositionChannel")
	public org.springframework.integration.transformer.Transformer JsonToObjectTransformer( ) {
		return new JsonToObjectTransformer( CurrentPosition.class );
	}

	@Bean
	public MessageChannel addCurrentPositionChannel() {
		return new DirectChannel();
	}

	@Transformer(inputChannel="addCurrentPositionChannel", outputChannel="sendToBroker")
	public CurrentPosition addServiceLocations(CurrentPosition payload) {

		switch(payload.getVehicleStatus()) {

		case SERVICE_NOW:
		case SERVICE_SOON:
			ResponseEntity<Resource<List<ServiceLocation>>> result = this.restTemplate.exchange(
					"http://SERVICE-LOCATION-SERVICE/serviceLocations/search/findByLocationNear?location={lat},{long}&distance={radius}km&pageSize={size}", HttpMethod.GET,
					new HttpEntity<Void>((Void) null),
					new ParameterizedTypeReference<Resource<List<ServiceLocation>>>() {
					}, payload.getPoint().getLatitude(), payload.getPoint().getLongitude(), 10, 1);
			if (!result.getBody().getContent().isEmpty()) {
				payload.setServiceLocation(result.getBody().getContent().get(0));
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

	@ServiceActivator(inputChannel="sendToBroker")
	public void sendToStompClients(CurrentPosition payload) {
		this.template.convertAndSend("/queue/fleet.location.ingest.queue", payload);
	}
}
