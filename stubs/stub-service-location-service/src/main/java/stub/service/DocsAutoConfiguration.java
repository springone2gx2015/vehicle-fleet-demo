/*
 * Copyright 2014-2015 the original author or authors.
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

package stub.service;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dave Syer
 *
 */
@Component
public class DocsAutoConfiguration implements MvcEndpoint {

	@Override
	public String getPath() {
		return "/docs/service";
	}

	@Override
	public boolean isSensitive() {
		return false;
	}

	@Override
	public Class<? extends Endpoint> getEndpointType() {
		return Endpoint.class;
	}
	
	@RequestMapping
	public String docs() {
		return "forward:/api-docs/index.html";
	}
	

}
