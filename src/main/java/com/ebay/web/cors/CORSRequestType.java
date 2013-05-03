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
package com.ebay.web.cors;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Types of CORS request, as per specification.
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * 
 */
public enum CORSRequestType {
	/** A valid CORS request. */
	SIMPLE("simple"),
	/** A valid pre-flight CORS request. */
	PRE_FLIGHT("pre_flight"),
	/** Not a CORS request, but a normal request. */
	NOT_CORS("not_cors"),
	/** An invalid CORS request. */
	INVALID_CORS("invalid_cors");

	/**
	 * The string representation of CORS request type.
	 */
	private String type;

	/**
	 * Initializes the type property.
	 * 
	 * @param type
	 */
	private CORSRequestType(final String type) {
		this.type = type;
	}

	/**
	 * Determines the type of CORS request.
	 * 
	 * @param request
	 *            The {@link HttpServletRequest} object.
	 * @return {@link CORSRequestType}
	 */
	public static CORSRequestType checkRequestType(
			final HttpServletRequest request, final CORSConfiguration corsConfig) {
		if (request == null) {
			throw new IllegalArgumentException(
					"HttpServletRequest object is null");
		}

		final Set<String> allowedHttpMethods = corsConfig
				.getAllowedHttpMethods();
		String requestMethod = request.getMethod();

		// A valid CORS request must have an 'Origin' header.
		if (!hasOriginHeader(request)) {
			return NOT_CORS;
		}

		// Did request originated from an allowed Origin ?
		if (!isOriginAllowed(request, corsConfig)) {
			return INVALID_CORS;
		}

		// Does this CORS request requests resource with an allowed HttpMethod ?
		if (!allowedHttpMethods.contains(requestMethod)) {
			return INVALID_CORS;
		}

		// Check if the request is a Pre-flight request.
		if (isPreflight(request)) {
			return PRE_FLIGHT;
		}

		return SIMPLE;
	}

	private static boolean isOriginAllowed(final HttpServletRequest request,
			final CORSConfiguration corsConfig) {
		if (corsConfig.isAnyOriginAllowed()) {
			return true;
		}
		final Set<String> allowedOrigins = corsConfig.getAllowedOrigins();
		String origin = request.getHeader(CORSRequestHeaders.ORIGIN);

		// If 'Origin' header is a case-sensitive match of any of allowed
		// origins, then return true, else return false.
		if (allowedOrigins.contains(origin)) {
			return true;
		}

		return false;
	}

	/**
	 * Checks for the presence of Origin header.
	 * 
	 * @param request
	 * @return
	 */
	private static boolean hasOriginHeader(final HttpServletRequest request) {
		String originHeader = request.getHeader(CORSRequestHeaders.ORIGIN);
		return (originHeader != null) && (originHeader.length() > 0);
	}

	/**
	 * Checks for the presence of
	 * CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD header.
	 * 
	 * @param request
	 * @return
	 */
	private static boolean hasAccessControlRequestMethodHeader(
			final HttpServletRequest request) {
		String header = request
				.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD);
		return (header != null) && (header.length() > 0);
	}

	/**
	 * Checks for the presence of
	 * CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS header.
	 * 
	 * @param request
	 * @return
	 */
	private static boolean hasAccessControlRequestHeadersHeader(
			final HttpServletRequest request) {
		String header = request
				.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
		return (header != null) && (header.length() > 0);
	}

	/**
	 * Checks if the request is a CORS pre-flight request.
	 * 
	 * @param request
	 * @return
	 */
	private static boolean isPreflight(final HttpServletRequest request) {

		// A CORS pre-flight request is sent as a HTTP preflight request.
		String requestMethod = request.getMethod();
		if ((requestMethod != null) && requestMethod.equals("OPTIONS")) {
			boolean hasAccessControlRequestMethodHeader = hasAccessControlRequestMethodHeader(request);
			boolean hasAccessControlRequestHeadersHeader = hasAccessControlRequestHeadersHeader(request);
			if (hasAccessControlRequestMethodHeader
					|| hasAccessControlRequestHeadersHeader) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the {@link String} representation of the {@link CORSRequestType}.
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}

}
