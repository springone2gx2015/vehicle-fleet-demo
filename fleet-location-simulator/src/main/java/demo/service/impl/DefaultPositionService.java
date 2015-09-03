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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.model.CurrentPosition;
import demo.service.KmlService;
import demo.service.PositionService;

/**
 *
 * @author Gunnar Hillert
 *
 */
@Service
public class DefaultPositionService implements PositionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPositionService.class);

	@Autowired
	private KmlService kmlService;

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	public DefaultPositionService() {
		super();
	}

	@HystrixCommand(fallbackMethod = "processPositionInfoFallback")
	@Override
	public void processPositionInfo(long id, CurrentPosition currentPosition, boolean exportPositionsToKml,
			boolean sendPositionsToIngestionService) {

		if (exportPositionsToKml) {
			this.kmlService.updatePosition(id, currentPosition);
		}

		if (sendPositionsToIngestionService) {
			this.restTemplate.postForLocation("http://fleet-location-ingest/api/locations", currentPosition);
		}

	}

	public void processPositionInfoFallback(long id, CurrentPosition currentPosition, boolean exportPositionsToKml,
			boolean sendPositionsToIngestionService) {
		LOGGER.error("Hystrix Fallback Method. Unable to send message for ingestion.");
	}

}
