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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterConfig;

/**
 * The configuration for {@link CORSFilter}.
 * 
 * <ul>
 * <li>
 * <b>Allowed Origins:</b> A white list of origins, which can send a CORS
 * request.</li>
 * <li>
 * <b>Allowed HTTP Methods:</b> HTTP verbs that can be used to send a CORS
 * request.</li>
 * <li>
 * <b>Allowed HTTP Headers:</b> A list of headers that can be sent with a CORS
 * request.</li>
 * <li>
 * <b>Exposed headers:</b> A list of response headers that can be exposed to JS
 * API.</li>
 * <li>
 * <b>Pre-flight max age:</b> Time duration, in seconds that a client can cache
 * pre-flight request.</li>
 * </ul>
 * <p>
 * Defaults:
 * </p>
 * <ul>
 * <li>Allowed HTTP methods: [GET, POST, OPTIONS, HEAD]</li>
 * <li>Supports credentials: true</li>
 * </ul>
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * 
 */
public final class CORSConfiguration {
    private static final int DEFAULT_PREFLIGHT_MAXAGE = 1800;

    public static final String CORS_SUPPORT_CREDENTIALS = "cors.support.credentials";

    public static final String CORS_EXPOSED_HEADERS = "cors.exposed.headers";

    public static final String CORS_ALLOWED_HEADERS = "cors.allowed.headers";

    public static final String CORS_ALLOWED_METHODS = "cors.allowed.methods";

    public static final String CORS_ALLOWED_ORIGINS = "cors.allowed.origins";

    public static final String CORS_PREFLIGHT_MAXAGE = "cors.preflight.maxage";

    /**
     * Set of origin URL that are allowed to make a CORS request.
     */
    private final Set<String> allowedOrigins;

    /**
     * Determines if any origin is allowed to make request.
     */
    private boolean anyOriginAllowed;

    /**
     * Set of allowed HTTP methods, which the CORS filter support.
     */
    private final Set<String> allowedHttpMethods;

    /**
     * Set of allowed headers, which the CORS filter support.
     */
    private final Set<String> allowedHttpHeaders;

    /**
     * Set of exposed headers.
     */
    private Set<String> exposedHeaders;

    /**
     * Does filter supports credentials ?
     */
    private boolean supportsCredentials;

    /**
     * Indicates (in seconds) how long the results of a pre-flight request can
     * be cached in a pre-flight result cache.
     */
    private long preflightMaxAge;

    /**
     * Initialize defaults.
     */
    public CORSConfiguration() {
        this.allowedOrigins = new HashSet<String>();
        this.allowedHttpMethods = new HashSet<String>();
        this.allowedHttpHeaders = new HashSet<String>();
        this.exposedHeaders = new HashSet<String>();
        this.anyOriginAllowed = false;
        initDefaults();
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
            final String preflightMaxAge) {
        this();

        parseAndStore(allowedOrigins, allowedHttpMethods, allowedHttpHeaders,
                exposedHeaders, supportsCredentials, preflightMaxAge);
    }

    private void parseAndStore(final String allowedOrigins,
            final String allowedHttpMethods, final String allowedHttpHeaders,
            final String exposedHeaders, final String supportsCredentials,
            final String preflightMaxAge) {
        if (allowedOrigins != null) {
            if (allowedOrigins.trim().equals("*")) {
                this.anyOriginAllowed = true;
            } else {
                this.anyOriginAllowed = false;
                Set<String> setAllowedOrigins = parseStringToSet(allowedOrigins);
                setAllowedOrigins(setAllowedOrigins);
            }
        }

        Set<String> setAllowedHttpMethods = parseStringToSet(allowedHttpMethods);
        setAllowedHttpMethods(setAllowedHttpMethods);

        Set<String> setAllowedHttpHeaders = parseStringToSet(allowedHttpHeaders);
        setAllowedHttpHeaders(setAllowedHttpHeaders);

        Set<String> setExposedHeaders = parseStringToSet(exposedHeaders);
        setExposedHeaders(setExposedHeaders);

        boolean isSupportsCredentials = Boolean
                .parseBoolean(supportsCredentials);
        this.supportsCredentials = isSupportsCredentials;

        this.preflightMaxAge = (preflightMaxAge != null && preflightMaxAge
                .isEmpty() == false) ? Long.parseLong(preflightMaxAge) : 0;
    }

    private void initDefaults() {
        this.supportsCredentials = false;
        this.anyOriginAllowed = true;
        this.preflightMaxAge = DEFAULT_PREFLIGHT_MAXAGE;
        initDefaultAllowedHttpMethods();
    }

    private void initDefaultAllowedHttpMethods() {
        this.allowedHttpMethods.add("GET");
        this.allowedHttpMethods.add("POST");
        this.allowedHttpMethods.add("OPTIONS");
        this.allowedHttpMethods.add("HEAD");
    }

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
     * Is cookie based aut supported ?
     * 
     * @return boolean
     */
    public boolean isSupportsCredentials() {
        return this.supportsCredentials;
    }

    /**
     * Sets the supportCredentials property that is used to determine
     * authentication support in a CORS request.
     * 
     * @param supportsCredentials
     */
    public void setSupportsCredentials(final boolean supportsCredentials) {
        this.supportsCredentials = supportsCredentials;
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

    /**
     * Gets pre-flight response max cache time.
     * 
     * @return long
     */
    public long getPreflightMaxAge() {
        return preflightMaxAge;
    }

    /**
     * Sets pre-flight response max cache time.
     * 
     * @param preflightMaxAge
     *            The value of max cache time.
     */
    public void setPreflightMaxAge(long preflightMaxAge) {
        this.preflightMaxAge = preflightMaxAge;
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
            FilterConfig filterConfig) {
        CORSConfiguration corsConfiguration = null;

        if (filterConfig != null) {
            String allowedOrigins = filterConfig
                    .getInitParameter(CORS_ALLOWED_ORIGINS);
            String allowedHttpMethods = filterConfig
                    .getInitParameter(CORS_ALLOWED_METHODS);
            String allowedHttpHeaders = filterConfig
                    .getInitParameter(CORS_ALLOWED_HEADERS);
            String exposedHeaders = filterConfig
                    .getInitParameter(CORS_EXPOSED_HEADERS);
            String supportsCredentials = filterConfig
                    .getInitParameter(CORS_SUPPORT_CREDENTIALS);
            String preflightMaxAge = filterConfig
                    .getInitParameter(CORS_PREFLIGHT_MAXAGE);
            corsConfiguration = new CORSConfiguration(allowedOrigins,
                    allowedHttpMethods, allowedHttpHeaders, exposedHeaders,
                    supportsCredentials, preflightMaxAge);
        }

        return corsConfiguration;
    }
}
