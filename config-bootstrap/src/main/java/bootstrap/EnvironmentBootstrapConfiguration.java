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

package bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.ClassUtils;

import lombok.extern.apachecommons.CommonsLog;

/**
 * @author Dave Syer
 *
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@CommonsLog
public class EnvironmentBootstrapConfiguration implements EnvironmentPostProcessor {

	private static final String CONFIG_SERVER_BOOTSTRAP = "configServerBootstrap";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment,
			SpringApplication application) {
		if (!environment.getPropertySources().contains(CONFIG_SERVER_BOOTSTRAP)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("spring.cloud.config.uri",
					"${CONFIG_SERVER_URI:${vcap.services.${PREFIX:}configserver.credentials.uri:http://localhost:8888}}");
			if (ClassUtils.isPresent("org.springframework.cloud.sleuth.zipkin.ZipkinProperties", null)) {
				map.put("spring.zipkin.host",
						"${ZIPKIN_HOST:${vcap.services.${PREFIX:}zipkin.credentials.host:localhost}}");
				map.put("logging.pattern.console",
						"%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID:- }){magenta} %clr(---){faint} %clr([trace=%X{X-Trace-Id:-},span=%X{X-Span-Id:-}]){yellow} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wex");
				String zipkinHost = environment.resolvePlaceholders("${ZIPKIN_HOST:${vcap.services.${PREFIX:}zipkin.credentials.host:}}");
				if (!"".equals(zipkinHost)) {
					map.put("fleet.zipkin.enabled", "true");
				}
			}
			String space = environment.resolvePlaceholders("${vcap.application.space_name:dev}");
			log.info("Spacename: " + space);
			if (space.startsWith("dev")) {
				environment.addActiveProfile("dev");
			}
			map.put("encrypt.failOnError", "false");
			map.put("endpoints.shutdown.enabled", "true");
			map.put("endpoints.restart.enabled", "true");
			environment.getPropertySources()
					.addLast(new MapPropertySource(CONFIG_SERVER_BOOTSTRAP, map));
		}
	}

}
