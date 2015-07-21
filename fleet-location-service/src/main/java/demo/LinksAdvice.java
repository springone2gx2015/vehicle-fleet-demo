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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.TypeUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Dave Syer
 *
 */
@ControllerAdvice
public class LinksAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		returnType.increaseNestingLevel();
		Type nestedType = returnType.getNestedGenericParameterType();
		returnType.decreaseNestingLevel();
		return ResourceSupport.class.isAssignableFrom(returnType.getParameterType())
				|| TypeUtils.isAssignable(ResourceSupport.class, nestedType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
					ServerHttpRequest request, ServerHttpResponse response) {
		if (request instanceof ServletServerHttpRequest) {
			beforeBodyWrite(body, (ServletServerHttpRequest) request);
		}
		return body;
	}

	private void beforeBodyWrite(Object body, ServletServerHttpRequest request) {
		Object pattern = request.getServletRequest().getAttribute(
				HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		if (pattern != null) {
			String path = pattern.toString();
			if (isHomePage(path)) {
				ResourceSupport resource = (ResourceSupport) body;
				String rel = "fleet";
				resource.add(linkTo(LinksAdvice.class).slash("/fleet").withRel(rel));
			}
		}
	}

	private boolean isHomePage(String path) {
		return "".equals(path) || "/".equals(path);
	}

}
