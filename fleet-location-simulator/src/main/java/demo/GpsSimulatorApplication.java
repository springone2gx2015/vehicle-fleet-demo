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

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Main entry point for the GpsSimulator application.
 *
 * @author Gunnar Hillert
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableScheduling
public class GpsSimulatorApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GpsSimulatorApplication.class, args);
	}

	@Bean
	@ExportMetricWriter
	public InMemoryMetricRepository inMemoryMetricRepository() {
		return new InMemoryMetricRepository();
	}

	@Bean
	public Jaxb2Marshaller getMarshaller() {
		final Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Kml.class);

		final Map<String,Object> map = new HashMap<>();
		map.put("jaxb.formatted.output", true);

		jaxb2Marshaller.setMarshallerProperties(map);
		return jaxb2Marshaller;
	}
}
