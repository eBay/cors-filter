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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * <p>
 * A configuration object for {@link CORSFilter}.
 * </p>
 * <ul>
 * <li>
 * <b>Allowed Origins:</b> A {@link Collection} of origins consisting of zero or
 * more origins that are allowed access to the resource. Init param name:
 * <code>cors.allowed.origins</code></li>
 * <li>
 * <b>Allowed HTTP Methods:</b> A {@link Collection} of methods consisting of
 * zero or more methods that are supported by the resource. Init param name:
 * <code>cors.allowed.methods</code></li>
 * <li>
 * <b>Allowed HTTP Headers:</b> A {@link Collection} of headers consisting of
 * zero or more header field names that are supported by the resource. Init
 * param name: <code>cors.allowed.headers</code></li>
 * <li>
 * <b>Exposed headers:</b> A {@link Collection} of exposed headers consisting of
 * zero or more header field names of headers other than the simple response
 * headers that the resource might use and can be exposed. Init param name:
 * <code>cors.exposed.headers</code></li>
 * <li>
 * <b>Pre-flight max age:</b> The amount of seconds the user agent is allowed to
 * cache the result of the pre-flight request. Init param name:
 * <code>cors.preflight.maxage</code></li>
 * <li>
 * <b>Supports credentials:</b> A supports credentials flag that indicates
 * whether the resource supports user credentials in the request. It is true
 * when the resource does and false otherwise. Init param name:
 * <code>cors.support.credentials</code></li>
 * </ul>
 * <p>
 * If {@link FilterConfig} is not provided. Following defaults are used:
 * </p>
 * <ul>
 * <li><b>Allowed origin:</b> *</li>
 * <li><b>Allowed HTTP methods:</b> [GET, POST, OPTIONS, HEAD]</li>
 * <li><b>Pre-flight max age:</b> 1800 seconds</li>
 * <li><b>Supports credentials:</b> false</li>
 * <li><b>Allowed HTTP headers:</b> Origin,Accept,X-Requested-With,Content-Type</li>
 * </ul>
 * 
 * @author Mohit Soni
 * 
 */
public final class CORSConfiguration {
    // -------------------------------------------------------------- Defaults
    /**
     * By default, all origins are allowed to make requests.
     */
    public static final String DEFAULT_ALLOWED_ORIGINS = "*";

    /**
     * By default, following methods are supported: GET, POST, HEAD and OPTIONS.
     */
    public static final String DEFAULT_ALLOWED_HTTP_METHODS =
            "GET,POST,HEAD,OPTIONS";

    /**
     * By default, time duration to cache pre-flight response is 30 mins.
     */
    public static final String DEFAULT_PREFLIGHT_MAXAGE = "1800";

    /**
     * By default, cookie based auth is turned off.
     */
    public static final String DEFAULT_SUPPORTS_CREDENTIALS = "false";

    /**
     * By default, following headers are supported:
     * Origin,Accept,X-Requested-With, and Content-Type.
     */
    public static final String DEFAULT_ALLOWED_HTTP_HEADERS =
            "Origin,Accept,X-Requested-With,Content-Type";

    /**
     * By default, none of the headers are exposed in response.
     */
    public static final String DEFAULT_EXPOSED_HEADERS = "";

    // ----------------------------------------------------- Init param-name(s)
    /**
     * Key to retrieve allowed origins from {@link FilterConfig}.
     */
    public static final String PARAM_CORS_ALLOWED_ORIGINS =
            "cors.allowed.origins";

    /**
     * Key to retrieve support credentials from {@link FilterConfig}.
     */
    public static final String PARAM_CORS_SUPPORT_CREDENTIALS =
            "cors.support.credentials";

    /**
     * Key to retrieve exposed headers from {@link FilterConfig}.
     */
    public static final String PARAM_CORS_EXPOSED_HEADERS =
            "cors.exposed.headers";

    /**
     * Key to retrieve allowed headers from {@link FilterConfig}.
     */
    public static final String PARAM_CORS_ALLOWED_HEADERS =
            "cors.allowed.headers";

    /**
     * Key to retrieve allowed methods from {@link FilterConfig}.
     */
    public static final String PARAM_CORS_ALLOWED_METHODS =
            "cors.allowed.methods";

    /**
     * Key to retrieve preflight max age from {@link FilterConfig}.
     */
    public static final String PARAM_CORS_PREFLIGHT_MAXAGE =
            "cors.preflight.maxage";

    // ----------------------------------------------------- Instance variables
    /**
     * A {@link Collection} of origins consisting of zero or more origins that
     * are allowed access to the resource.
     */
    private final Set<String> allowedOrigins;

    /**
     * Determines if any origin is allowed to make request.
     */
    private boolean anyOriginAllowed;

    /**
     * A {@link Collection} of methods consisting of zero or more methods that
     * are supported by the resource.
     */
    private final Set<String> allowedHttpMethods;

    /**
     * A {@link Collection} of headers consisting of zero or more header field
     * names that are supported by the resource.
     */
    private final Set<String> allowedHttpHeaders;

    /**
     * A {@link Collection} of exposed headers consisting of zero or more header
     * field names of headers other than the simple response headers that the
     * resource might use and can be exposed.
     */
    private Set<String> exposedHeaders;

    /**
     * A supports credentials flag that indicates whether the resource supports
     * user credentials in the request. It is true when the resource does and
     * false otherwise.
     */
    private boolean supportsCredentials;

    /**
     * Indicates (in seconds) how long the results of a pre-flight request can
     * be cached in a pre-flight result cache.
     */
    private long preflightMaxAge;

    // ----------------------------------------------------------- Constructors
    /**
     * Initialize defaults.
     */
    public CORSConfiguration() throws ServletException {
        this.allowedOrigins = new HashSet<String>();
        this.allowedHttpMethods = new HashSet<String>();
        this.allowedHttpHeaders = new HashSet<String>();
        this.exposedHeaders = new HashSet<String>();

        parseAndStore(DEFAULT_ALLOWED_ORIGINS, DEFAULT_ALLOWED_HTTP_METHODS,
                DEFAULT_ALLOWED_HTTP_HEADERS, DEFAULT_EXPOSED_HEADERS,
                DEFAULT_SUPPORTS_CREDENTIALS, DEFAULT_PREFLIGHT_MAXAGE);
    }

    /**
     * Initialize CORS configuration, by passing in configuration attributes
     * from application.
     * 
     * @param allowedOrigins
     *            A comma separated list of allowed origin for CORS.
     * @param allowedHttpMethods
     *            A comma separated list of allowed HTTP methods.
     * @param allowedHttpHeaders
     *            A comma separated list of allowed HTTP headers.
     * @param exposedHeaders
     *            A comma separated list of HTTP headers in response that we are
     *            XHR2 object can access.
     * @param supportsCredentials
     *            Is auth required ? Response will contain an allow credentials
     *            header.
     */
    public CORSConfiguration(final String allowedOrigins,
            final String allowedHttpMethods, final String allowedHttpHeaders,
            final String exposedHeaders, final String supportsCredentials,
            final String preflightMaxAge) throws ServletException {
        this.allowedOrigins = new HashSet<String>();
        this.allowedHttpMethods = new HashSet<String>();
        this.allowedHttpHeaders = new HashSet<String>();
        this.exposedHeaders = new HashSet<String>();

        parseAndStore(allowedOrigins, allowedHttpMethods, allowedHttpHeaders,
                exposedHeaders, supportsCredentials, preflightMaxAge);
    }

    // -------------------------------------------------------- Private methods
    /**
     * Parses each param-value and populates configuration variables.
     * 
     * @param allowedOrigins
     *            A {@link String} of comma separated origins.
     * @param allowedHttpMethods
     *            A {@link String} of comma separated HTTP methods.
     * @param allowedHttpHeaders
     *            A {@link String} of comma separated HTTP headers.
     * @param exposedHeaders
     *            A {@link String} of comma separated headers that needs to be
     *            exposed.
     * @param supportsCredentials
     *            "true" if support credentials needs to be enabled.
     * @param preflightMaxAge
     *            The amount of seconds the user agent is allowed to cache the
     *            result of the pre-flight request.
     * @throws ServletException
     */
    private void parseAndStore(final String allowedOrigins,
            final String allowedHttpMethods, final String allowedHttpHeaders,
            final String exposedHeaders, final String supportsCredentials,
            final String preflightMaxAge) throws ServletException {
        if (allowedOrigins != null) {
            if (allowedOrigins.trim().equals("*")) {
                this.anyOriginAllowed = true;
            } else {
                this.anyOriginAllowed = false;
                Set<String> setAllowedOrigins =
                        parseStringToSet(allowedOrigins);
                setAllowedOrigins(setAllowedOrigins);
            }
        }

        Set<String> setAllowedHttpMethods =
                parseStringToSet(allowedHttpMethods);
        setAllowedHttpMethods(setAllowedHttpMethods);

        Set<String> setAllowedHttpHeaders =
                parseStringToSet(allowedHttpHeaders);
        Set<String> lowerCaseHeaders = new HashSet<String>();
        for (String header : setAllowedHttpHeaders) {
            String lowerCase = header.toLowerCase();
            lowerCaseHeaders.add(lowerCase);
        }
        setAllowedHttpHeaders(lowerCaseHeaders);

        Set<String> setExposedHeaders = parseStringToSet(exposedHeaders);
        setExposedHeaders(setExposedHeaders);

        // For any value other then 'true' this will be false.
        boolean isSupportsCredentials =
                Boolean.parseBoolean(supportsCredentials);
        this.supportsCredentials = isSupportsCredentials;

        try {
            if (preflightMaxAge != null && preflightMaxAge.isEmpty() == false) {
                this.preflightMaxAge = Long.parseLong(preflightMaxAge);
            } else {
                this.preflightMaxAge = 0L;
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Unable to parse preflightMaxAge", e);
        }
    }

    /**
     * Takes a comma separated list and returns a Set<String>.
     * 
     * @param data
     *            A comma separated list of strings.
     * @return Set<String>
     */
    private Set<String> parseStringToSet(final String data) {
        String[] splits = null;

        if (data != null && data.length() > 0) {
            splits = data.split(",");
        } else {
            splits = new String[] {};
        }

        Set<String> set = new HashSet<String>();
        if (splits.length > 0) {
            for (String split : splits) {
                set.add(split.trim());
            }
        }

        return set;
    }

    // ------------------------------------------------- Accessors and Mutators
    /**
     * Returns an unmodifiable set of allowedOrigins.
     * 
     * @return Set<String>
     */
    public Set<String> getAllowedOrigins() {
        return Collections.unmodifiableSet(this.allowedOrigins);
    }

    /**
     * Set the allowedOrigins, from which a CORS request can be made.
     * 
     * @param allowedOrigins
     *            The set of allowed origins.
     */
    public void setAllowedOrigins(final Set<String> allowedOrigins) {
        this.allowedOrigins.addAll(allowedOrigins);
    }

    /**
     * Returns an unmodifiable set of allowedHttpMethods.
     * 
     * @return Set<String>
     */
    public Set<String> getAllowedHttpMethods() {
        return Collections.unmodifiableSet(this.allowedHttpMethods);
    }

    /**
     * Set the allowedHttpMethods, with which a CORS request can be made.
     * 
     * @param allowedHttpMethods
     *            The Set<String> of allowed HTTP methods.
     */
    public void setAllowedHttpMethods(final Set<String> allowedHttpMethods) {
        this.allowedHttpMethods.addAll(allowedHttpMethods);
    }

    /**
     * Returns an unmodifiable set of exposedHeaders.
     * 
     * @return Set<String>
     */
    public Set<String> getExposedHeaders() {
        return Collections.unmodifiableSet(this.exposedHeaders);
    }

    /**
     * Set the exposedHeaders, which a CORS response can expose to CORS API.
     * 
     * @param exposedHeaders
     *            The Set<String> of exposed HTTP headers.
     */
    public void setExposedHeaders(final Set<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    /**
     * Returns an unmodifiable set of allowedHttpHeaders.
     * 
     * @return Set<String>
     */
    public Set<String> getAllowedHttpHeaders() {
        return Collections.unmodifiableSet(this.allowedHttpHeaders);
    }

    /**
     * Set the allowedHttpHeaders, which a simple CORS request can include.
     * 
     * @param allowedHttpHeaders
     *            The Set<String> of allowed HTTP headers.
     */
    public void setAllowedHttpHeaders(final Set<String> allowedHttpHeaders) {
        this.allowedHttpHeaders.addAll(allowedHttpHeaders);
    }

    /**
     * Is cookie based auth supported ?
     * 
     * @return boolean
     */
    public boolean isSupportsCredentials() {
        return this.supportsCredentials;
    }

    /**
     * Gets pre-flight response max cache time.
     * 
     * @return long
     */
    public long getPreflightMaxAge() {
        return preflightMaxAge;
    }

    /**
     * Returns true if any origin is allowed to make CORS request as per
     * configuration.
     * 
     * @return <code>true</code> if any origin is allowed; <code>false</code>
     *         otherwise.
     */
    public boolean isAnyOriginAllowed() {
        return anyOriginAllowed;
    }

    // --------------------------------------------------------- Static methods
    /**
     * Creates an instance of {@link CORSConfiguration} from the
     * {@link FilterConfig} object.
     * 
     * @param filterConfig
     *            The {@link FilterConfig} object containing filter init
     *            parameters.
     * @return {@link CORSConfiguration} The configuration object.
     */
    public static CORSConfiguration loadFromFilterConfig(
            FilterConfig filterConfig) throws ServletException {
        CORSConfiguration corsConfiguration = null;

        if (filterConfig != null) {
            String allowedOrigins =
                    filterConfig.getInitParameter(PARAM_CORS_ALLOWED_ORIGINS);
            String allowedHttpMethods =
                    filterConfig.getInitParameter(PARAM_CORS_ALLOWED_METHODS);
            String allowedHttpHeaders =
                    filterConfig.getInitParameter(PARAM_CORS_ALLOWED_HEADERS);
            String exposedHeaders =
                    filterConfig.getInitParameter(PARAM_CORS_EXPOSED_HEADERS);
            String supportsCredentials =
                    filterConfig
                            .getInitParameter(PARAM_CORS_SUPPORT_CREDENTIALS);
            String preflightMaxAge =
                    filterConfig.getInitParameter(PARAM_CORS_PREFLIGHT_MAXAGE);
            corsConfiguration =
                    new CORSConfiguration(allowedOrigins, allowedHttpMethods,
                            allowedHttpHeaders, exposedHeaders,
                            supportsCredentials, preflightMaxAge);
        }

        return corsConfiguration;
    }
}
