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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A servlet filter to support CORS (Cross-Origin Resource Sharing).
 * </p>
 * 
 * <p>
 * An incoming HTTP request is intercepted, and is inspected as per CORS spec,
 * and with help of {@link CORSConfiguration}. Appropriate, response headers (as
 * per spec) are added, if required.
 * </p>
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * @see <a href="http://www.w3.org/TR/cors/">CORS spec</a>
 * 
 */
public class CORSFilter implements Filter {
    /** Configuration object */
    private CORSConfiguration corsConfiguration;

    /**
     * Creates a CORS filter, and loads configuration from classpath, by default
     * from cors-configuration.properties.
     * 
     * @throws IOException
     */
    public CORSFilter() {
        super();
    }

    public void doFilter(final ServletRequest servletRequest,
            final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)
                || !(servletResponse instanceof HttpServletResponse)) {
            String message = "CORS doesn't support non-HTTP request or response.";
            throw new ServletException(message);
        }

        // Safe to downcast at this point.
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Determines the CORS request type.
        CORSRequestType requestType = CORSRequestType.checkRequestType(request,
                corsConfiguration);

        // Adds CORS specific attributes to request.
        CORSFilter.decorateCORSProperties(request, requestType);

        switch (requestType) {
        case SIMPLE:
            this.handleSimpleCORS(request, response, filterChain);
            break;
        case PRE_FLIGHT:
            this.handlePreflightCORS(request, response, filterChain);
            break;
        case NOT_CORS:
            this.handleNonCORS(request, response, filterChain);
            break;
        default:
            this.handleInvalidCORS(request, response, filterChain);
            break;
        }
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        if (filterConfig != null) {
            try {
                this.corsConfiguration = CORSConfiguration
                        .loadFromFilterConfig(filterConfig);
            } catch (Exception e) {
                throw new ServletException(
                        "Error loading configuration using filter init", e);
            }
        } else {
            // Initialize with default configuration.
            this.corsConfiguration = new CORSConfiguration();
        }
    }

    public void destroy() {

    }

    /**
     * Handles a CORS request of type {@link CORSRequestType}.SIMPLE.
     * 
     * @see <a href="http://www.w3.org/TR/cors/#resource-requests">Simple
     *      Cross-Origin Request, Actual Request, and Redirects</a>
     */
    public void handleSimpleCORS(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        if (CORSRequestType.checkRequestType(request, corsConfiguration) != CORSRequestType.SIMPLE) {
            String message = "Expects a HttpServletRequest object of type "
                    + CORSRequestType.SIMPLE.getType();
            throw new IllegalArgumentException(message);
        }

        String origin = request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN);

        final CORSConfiguration corsConfig = corsConfiguration;
        final Set<String> exposedHeaders = corsConfig.getExposedHeaders();

        // Must be returned, in order for browser runtime to accept the
        // response.
        if (corsConfig.isAnyOriginAllowed()
                && !corsConfig.isSupportsCredentials()) {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        } else {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                    origin);
        }

        // Must be returned, in order for browser to accept the response, as
        // this request was made with cookies.
        if (corsConfig.isSupportsCredentials()) {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    "true");
        }

        // Expose headers if any.
        if ((exposedHeaders != null) && (exposedHeaders.size() > 0)) {
            String exposedHeadersString = join(exposedHeaders, ",");
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
                    exposedHeadersString);
        }

        // Forward the request down the filter chain.
        filterChain.doFilter(request, response);
    }

    /**
     * Handles CORS pre-flight request.
     */
    public void handlePreflightCORS(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        if (CORSRequestType.checkRequestType(request, corsConfiguration) != CORSRequestType.PRE_FLIGHT) {
            throw new IllegalArgumentException(
                    "Expects a HttpServletRequest object of type "
                            + CORSRequestType.PRE_FLIGHT.getType());
        }

        String origin = request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN);

        final CORSConfiguration corsConfig = corsConfiguration;
        final Set<String> allowedHttpMethods = corsConfig
                .getAllowedHttpMethods();
        final Set<String> allowedHttpHeaders = corsConfig
                .getAllowedHttpHeaders();
        final long preflightMaxAge = corsConfig.getPreflightMaxAge();

        // Must be returned, in order for browser runtime to accept the
        // response.
        response.addHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin);

        // Must be returned, in order for browser to accept the response, as
        // this request was made with cookies.
        if (corsConfig.isSupportsCredentials()) {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    "true");
        }

        if ((allowedHttpMethods != null) && (allowedHttpMethods.size() > 0)) {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS,
                    join(allowedHttpMethods, ","));
        }

        if ((allowedHttpHeaders != null) && (allowedHttpHeaders.size() > 0)) {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS,
                    join(allowedHttpHeaders, ","));
        }

        if (preflightMaxAge > 0) {
            response.addHeader(
                    CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE,
                    String.valueOf(preflightMaxAge));
        }

        // Don't forward the request down the filter chain.
    }

    /**
     * Handles a request, that's not a CORS request, but is a valid request.
     * This implementation, just forwards the request down the filter chain.
     */
    public void handleNonCORS(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        // Let request pass.
        filterChain.doFilter(request, response);
    }

    /**
     * Will throw a {@link ServletException}, which ultimately, will lead the
     * request to a {@link AbortServlet}.
     */
    public void handleInvalidCORS(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        String origin = request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN);
        String method = request.getMethod();
        String message = "Encountered an invalid CORS request, from Origin: "
                + origin + " ; requested with method: " + method;
        throw new ServletException(message);
    }

    /**
     * Decorates the {@link HttpServletRequest}, with CORS attributes.
     * 
     * @param request
     *            The {@link HttpServletRequest} object.
     * @param corsRequestType
     *            The {@link CORSRequestType} object.
     */
    public static void decorateCORSProperties(final HttpServletRequest request,
            final CORSRequestType corsRequestType) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "HttpServletRequest object is null");
        }

        if (corsRequestType == null) {
            throw new IllegalArgumentException("CORSRequestType object is null");
        }

        switch (corsRequestType) {
        case SIMPLE:
            request.setAttribute(
                    CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, true);
            request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                    request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN));
            request.setAttribute(
                    CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                    corsRequestType.getType());
            break;
        case PRE_FLIGHT:
            request.setAttribute(
                    CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, true);
            request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                    request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN));
            request.setAttribute(
                    CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                    corsRequestType.getType());
            break;
        case NOT_CORS:
            request.setAttribute(
                    CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, false);
            break;
        default:
            // Don't set any attributes
            break;
        }
    }

    public static String join(final Set<String> elements,
            final String joinSeparator) {
        String separator = ",";
        if (elements == null) {
            return null;
        }
        if (joinSeparator != null) {
            separator = joinSeparator;
        }
        StringBuilder buffer = new StringBuilder();
        boolean isFirst = true;
        Iterator<String> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();

            if (!isFirst) {
                buffer.append(separator);
            } else {
                isFirst = false;
            }

            if (element != null) {
                buffer.append(element);
            }
        }

        return buffer.toString();
    }

    /**
     * The Access-Control-Allow-Origin header indicates whether a resource can
     * be shared based by returning the value of the Origin request header in
     * the response.
     */
    public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    /**
     * The Access-Control-Allow-Credentials header indicates whether the
     * response to request can be exposed when the omit credentials flag is
     * unset. When part of the response to a preflight request it indicates that
     * the actual request can include user credentials.
     */
    public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    /**
     * The Access-Control-Expose-Headers header indicates which headers are safe
     * to expose to the API of a CORS API specification
     */
    public static final String RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    /**
     * The Access-Control-Max-Age header indicates how long the results of a
     * preflight request can be cached in a preflight result cache.
     */
    public static final String RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    /**
     * The Access-Control-Allow-Methods header indicates, as part of the
     * response to a preflight request, which methods can be used during the
     * actual request.
     */
    public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    /**
     * The Access-Control-Allow-Headers header indicates, as part of the
     * response to a preflight request, which header field names can be used
     * during the actual request.
     */
    public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    /**
     * The Origin header indicates where the cross-origin request or preflight
     * request originates from.
     */
    public static final String REQUEST_HEADER_ORIGIN = "Origin";

    /**
     * The Access-Control-Request-Method header indicates which method will be
     * used in the actual request as part of the preflight request.
     */
    public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    /**
     * The Access-Control-Request-Headers header indicates which headers will be
     * used in the actual request as part of the preflight request.
     */
    public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    /**
     * The prefix to a CORS request attribute.
     */
    public static final String HTTP_REQUEST_ATTRIBUTE_PREFIX = "cors.";

    /**
     * Attribute that contains the origin of the request.
     */
    public static final String HTTP_REQUEST_ATTRIBUTE_ORIGIN = HTTP_REQUEST_ATTRIBUTE_PREFIX
            + "origin";

    /**
     * Boolean value, suggesting if the request is a CORS request or not.
     */
    public static final String HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST = HTTP_REQUEST_ATTRIBUTE_PREFIX
            + "isCorsRequest";

    /**
     * Type of CORS request, of type {@link CORSRequestType}.
     */
    public static final String HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE = HTTP_REQUEST_ATTRIBUTE_PREFIX
            + "requestType";

    /**
     * {@link Collection} of Simple HTTP methods. Case sensitive.
     * 
     * @see http://www.w3.org/TR/cors/#terminology
     */
    public static final Set<String> SIMPLE_HTTP_METHODS = new HashSet<String>(
            Arrays.asList("GET", "POST", "HEAD"));

    /**
     * {@link Collection} of Simple HTTP request headers. Case in-sensitive.
     * 
     * @see http://www.w3.org/TR/cors/#terminology
     */
    public static final Set<String> SIMPLE_HTTP_REQUEST_HEADERS = new HashSet<String>(
            Arrays.asList("Accept", "Accept-Language", "Content-Language"));

    /**
     * {@link Collection} of Simple HTTP request headers. Case in-sensitive.
     * 
     * @see http://www.w3.org/TR/cors/#terminology
     */
    public static final Set<String> SIMPLE_HTTP_RESPONSE_HEADERS = new HashSet<String>(
            Arrays.asList("Cache-Control", "Content-Language", "Content-Type",
                    "Expires", "Last-Modified", "Pragma"));

    /**
     * A Simple HTTP request header, if the header values matches
     * {@code SIMPLE_HTTP_REQUEST_CONTENT_TYPE_VALUES}. Case in-sensitive.
     * 
     * @see http://www.w3.org/TR/cors/#terminology
     */
    public static final String SIMPLE_REQUEST_HEADER_CONTENT_TYPE = "Content-Type";

    /**
     * {@link Collection} of Simple HTTP request headers. Case in-sensitive.
     * 
     * @see http://www.w3.org/TR/cors/#terminology
     */
    public static final Set<String> SIMPLE_HTTP_REQUEST_CONTENT_TYPE_VALUES = new HashSet<String>(
            Arrays.asList("application/x-www-form-urlencoded",
                    "multipart/form-data", "text/plain"));
}
