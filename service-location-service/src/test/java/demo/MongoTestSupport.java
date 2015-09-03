/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import static org.junit.Assert.fail;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;


/**
 * Abstract base class for JUnit {@link Rule}s that detect the presence of some external resource. If the resource is
 * indeed present, it will be available during the test lifecycle through {@link #getResource()}. If it is not, tests
 * will either fail or be skipped, depending on the value of system property {@value #EXTERNAL_SERVERS_REQUIRED}.
 *
 * @author Eric Bottard
 * @author Gary Russell
 */
public class MongoTestSupport implements TestRule {

	public static final String EXTERNAL_SERVERS_REQUIRED = "EXTERNAL_SERVERS_REQUIRED";

	protected MongoClient resource;

	private String resourceDescription;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected MongoTestSupport(String resourceDescription) {
		Assert.hasText(resourceDescription, "resourceDescription is required");
		this.resourceDescription = resourceDescription;
	}

	@Override
	public Statement apply(final Statement base, Description description) {
		try {
			obtainResource();
		}
		catch (Exception e) {
			maybeCleanup();

			return failOrSkip(e);
		}

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
				}
				finally {
					try {
						cleanupResource();
					}
					catch (Exception ignored) {
						MongoTestSupport.this.logger.warn("Exception while trying to cleanup proper resource", ignored);
					}
				}
			}

		};
	}

	private Statement failOrSkip(final Exception e) {
		String serversRequired = System.getenv(EXTERNAL_SERVERS_REQUIRED);
		if ("true".equalsIgnoreCase(serversRequired)) {
			this.logger.error(this.resourceDescription + " IS REQUIRED BUT NOT AVAILABLE", e);
			fail(this.resourceDescription + " IS NOT AVAILABLE");
			// Never reached, here to satisfy method signature
			return null;
		}
		else {
			this.logger.error(this.resourceDescription + " IS NOT AVAILABLE, SKIPPING TESTS", e);
			return new Statement() {

				@Override
				public void evaluate() throws Throwable {
					Assume.assumeTrue("Skipping test due to " + MongoTestSupport.this.resourceDescription + " not being available " + e, false);
				}
			};
		}
	}

	private void maybeCleanup() {
		if (this.resource != null) {
			try {
				cleanupResource();
			}
			catch (Exception ignored) {
				this.logger.warn("Exception while trying to cleanup failed resource", ignored);
			}
		}
	}

	public MongoClient getResource() {
		return this.resource;
	}

	/**
	 * Perform cleanup of the {@link #resource} field, which is guaranteed to be non null.
	 *
	 * @throws Exception any exception thrown by this method will be logged and swallowed
	 */
	protected void cleanupResource() throws Exception {
		this.resource.close();
	}

	/**
	 * Try to obtain and validate a resource. Implementors should either set the {@link #resource} field with a valid
	 * resource and return normally, or throw an exception.
	 */
	protected void obtainResource() throws Exception {
		this.resource = new MongoClient("localhost", MongoClientOptions.builder().connectTimeout(300).build());
		this.resource.getDatabaseNames();
	}

}
