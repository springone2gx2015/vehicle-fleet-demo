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

/**
 * @author Dave Syer
 *
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class ConfigServerBootstrapConfiguration implements EnvironmentPostProcessor {

	private static final String CONFIG_SERVER_BOOTSTRAP = "configServerBootstrap";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment,
			SpringApplication application) {
		if (!environment.getPropertySources().contains(CONFIG_SERVER_BOOTSTRAP)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("spring.cloud.config.uri",
					"${CONFIG_SERVER_URI:${vcap.services.${PREFIX:}configserver.credentials.uri:http://localhost:8888}}");
			map.put("encrypt.failOnError", "false");
			environment.getPropertySources()
					.addLast(new MapPropertySource(CONFIG_SERVER_BOOTSTRAP, map));
		}
	}

}
