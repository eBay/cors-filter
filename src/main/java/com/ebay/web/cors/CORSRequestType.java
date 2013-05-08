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
 * Enumerates varies types of CORS requests. Also, provides utility methods to
 * determine the request type.
 * 
 * @author Mohit Soni
 * 
 */
public enum CORSRequestType {
    // ----------------------------------------------------------- Enumerations
    /**
     * Actual CORS request.
     */
    SIMPLE("simple"),
    /**
     * A pre-flight CORS request, to get meta information.
     */
    PRE_FLIGHT("pre_flight"),
    /**
     * Not a CORS request, but a normal request.
     */
    NOT_CORS("not_cors"),
    /**
     * An invalid CORS request.
     */
    INVALID_CORS("invalid_cors");

    // ----------------------------------------------------- Instance variables
    /**
     * The string representation of CORS request type.
     */
    private String type;

    // ---------------------------------------------------- Private constructor
    /**
     * Initializes the type property.
     * 
     * @param type
     */
    private CORSRequestType(final String type) {
        this.type = type;
    }

    // --------------------------------------------------------- Helper methods
    /**
     * Determines the type of CORS request.
     * 
     * @param request
     *            The {@link HttpServletRequest} object.
     * @return {@link CORSRequestType}
     */
    public static CORSRequestType
            checkRequestType(final HttpServletRequest request,
                    final CORSConfiguration corsConfig) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "HttpServletRequest object is null");
        }

        final Set<String> allowedHttpMethods =
                corsConfig.getAllowedHttpMethods();
        final String requestMethod = request.getMethod();

        // A valid CORS request must have an 'Origin' header.
        // Section 6.1.1 and 6.2.1
        if (!hasOriginHeader(request)) {
            return NOT_CORS;
        }

        // Did request originate from an allowed Origin ?
        // Section 6.1.2 and 6.2.2
        if (!isOriginAllowed(request, corsConfig)) {
            return INVALID_CORS;
        }

        // Does this CORS request, requests resource with an allowed HttpMethod
        // ?
        if (!allowedHttpMethods.contains(requestMethod)) {
            return INVALID_CORS;
        }

        // Checks if the request is a pre-flight request.
        if (requestMethod.equals("OPTIONS")) {
            // Section 6.2.3
            boolean hasAccessControlRequestMethodHeader =
                    hasAccessControlRequestMethodHeader(request);
            if (!hasAccessControlRequestMethodHeader) {
                return INVALID_CORS;
            }

            return PRE_FLIGHT;
        }

        return SIMPLE;
    }

    /**
     * Checks if the Origin is allowed to make a CORS request.
     * 
     * @param request
     *            The {@link HttpServletRequest} object.
     * @param corsConfig
     *            The {@link CORSConfiguration} object.
     * @return <code>true</code> if origin is allowed; <code>false</code>
     *         otherwise.
     */
    private static boolean isOriginAllowed(final HttpServletRequest request,
            final CORSConfiguration corsConfig) {
        if (corsConfig.isAnyOriginAllowed()) {
            return true;
        }
        final Set<String> allowedOrigins = corsConfig.getAllowedOrigins();
        final String origin =
                request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN);

        // If 'Origin' header is a case-sensitive match of any of allowed
        // origins, then return true, else return false.
        if (allowedOrigins.contains(origin)) {
            return true;
        }

        return false;
    }

    /**
     * Checks for the presence of 'Origin' header.
     * 
     * @param request
     *            The {@link HttpServletRequest} object.
     * @return <code>true</code> if request has 'Origin' header;
     *         <code>false</code> otherwise.
     */
    private static boolean hasOriginHeader(final HttpServletRequest request) {
        final String originHeader =
                request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN);
        return (originHeader != null) && (originHeader.length() > 0);
    }

    /**
     * Checks for the presence of 'Access-Control-Request-Method' header.
     * 
     * @param request
     *            The {@link HttpServletRequest} object.
     * @return <code>true</code> if request has 'Access-Control-Request-Method'
     *         header; <code>false</code> otherwise.
     */
    private static boolean hasAccessControlRequestMethodHeader(
            final HttpServletRequest request) {
        final String header =
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD);
        if (header != null) {
            if (CORSFilter.HTTP_METHODS.contains(header)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------- Accessors
    /**
     * Returns the {@link String} representation of the {@link CORSRequestType}.
     * 
     * @return The {@link String} representation.
     */
    public String getType() {
        return this.type;
    }

}
