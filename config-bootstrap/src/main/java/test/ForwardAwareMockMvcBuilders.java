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

package test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Dave Syer
 *
 */
public class ForwardAwareMockMvcBuilders {

	public static DefaultMockMvcBuilder webAppContextSetup(
			WebApplicationContext context) {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context);
		ForwardAwareResultHandler forwarder = new ForwardAwareResultHandler();
		builder.alwaysDo(forwarder);
		forwarder.setMockMvc(builder.build());
		return builder;
	}

	private static class ForwardAwareResultHandler implements ResultHandler {

		private MockMvc mockMvc;

		@Override
		public void handle(MvcResult result) throws Exception {
			MockHttpServletRequest request = result.getRequest();
			String uri = request.getRequestURI();
			MockHttpServletResponse response = result.getResponse();
			String forward = response.getForwardedUrl();
			if (StringUtils.hasText(forward)) {
				request.setRequestURI(forward);
				response.setForwardedUrl(null);
				MvcResult forwarded = this.mockMvc.perform(servletContext -> request)
						.andReturn();
				// Hack response into result so it can be asserted as normal
				ReflectionTestUtils.setField(result, "mockResponse",
						forwarded.getResponse());
				// Reset request to original uri
				request.setRequestURI(uri);
			}
		}

		public void setMockMvc(MockMvc mockMvc) {
			this.mockMvc = mockMvc;
		}
	}

}
