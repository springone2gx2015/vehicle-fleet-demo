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

package autoconfig;

import java.security.SecureRandom;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.cloud.sleuth.zipkin.ZipkinAutoConfiguration;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dave Syer
 *
 */
@Configuration
@ConditionalOnClass({ Trace.class, ZipkinSpanListener.class })
@AutoConfigureBefore({ TraceAutoConfiguration.class, ZipkinAutoConfiguration.class })
@ConfigurationProperties("fleet.zipkin")
public class DefaultSamplerAutoConfiguration {

	SecureRandom random = new SecureRandom();

	@Getter
	@Setter
	double sampleRate = 0.1;

	@Getter
	@Setter
	private boolean enabled;

	@Bean
	@ConditionalOnMissingBean(Sampler.class)
	public Sampler<?> defaultSampler() {
		return (Object context) -> {
			return this.random.nextFloat() < this.sampleRate;
		};
	}

	@Configuration
	@ConditionalOnClass(ZipkinSpanListener.class)
	protected static class ZipkinCollectorAutoConfiguration {

		// Use this for debugging (or if there is no Zipkin collector running on port
		// 9410)
		@Bean
		@ConditionalOnProperty(value = "fleet.zipkin.enabled", havingValue = "false", matchIfMissing = true)
		public SpanCollector spanCollector() {
			return new LoggingSpanCollector();
		}

	}

}
