/**
 * Copyright 2012-2013 eBay Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ebay.web.cors.handlers;

import java.io.IOException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.ebay.web.cors.CORSConfiguration;
import com.ebay.web.cors.CORSFilter;
import com.ebay.web.cors.CORSRequestHeaders;
import com.ebay.web.cors.CORSRequestType;

/**
 * Handles a CORS request of type {@link CORSRequestType}.SIMPLE.
 * 
 * @see <a href="http://www.w3.org/TR/cors/#resource-requests">Simple
 *      Cross-Origin Request, Actual Request, and Redirects</a>
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * 
 */
public class DefaultSimpleCORSHandler implements CORSHandler {

	private CORSConfiguration corsConfiguration;

	public DefaultSimpleCORSHandler(CORSConfiguration corsConfiguration) {
		super();
		this.corsConfiguration = corsConfiguration;
	}

	public void handle(final HttpServletRequest request,
			final HttpServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		if (CORSRequestType.checkRequestType(request, corsConfiguration) != CORSRequestType.SIMPLE) {
			String message = "Expects a HttpServletRequest object of type "
					+ CORSRequestType.SIMPLE.getType();
			throw new IllegalArgumentException(message);
		}

		String origin = request.getHeader(CORSRequestHeaders.ORIGIN);

		final CORSConfiguration corsConfig = corsConfiguration;
		final Set<String> exposedHeaders = corsConfig.getExposedHeaders();

		// Must be returned, in order for browser runtime to accept the
		// response.
		response.addHeader(CORSFilter.ACCESS_CONTROL_ALLOW_ORIGIN,
				origin);

		// Must be returned, in order for browser to accept the response, as
		// this request was made with cookies.
		if (corsConfig.isSupportsCredentials()) {
			response.addHeader(
					CORSFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS,
					"true");
		}

		// Expose headers if any.
		if ((exposedHeaders != null) && (exposedHeaders.size() > 0)) {
			String exposedHeadersString = StringUtils.join(exposedHeaders, ",");
			response.addHeader(
					CORSFilter.ACCESS_CONTROL_EXPOSE_HEADERS,
					exposedHeadersString);
		}

		// Forward the request down the filter chain.
		filterChain.doFilter(request, response);
	}

}
