package com.ebay.web.cors;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

public class TestConfigs {
    public static final String HTTPS_WWW_APACHE_ORG = "https://www.apache.org";
    public static final String HTTP_TOMCAT_APACHE_ORG =
            "http://tomcat.apache.org";
    public static final String EXPOSED_HEADERS = "X-CUSTOM-HEADER";
    /**
     * Any origin
     */
    public static final String ANY_ORIGIN = "*";

    public static final MockServletContext mockServletContext =
            new MockServletContext();

    public static FilterConfig getDefaultFilterConfig() {
        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = CORSFilter.DEFAULT_ALLOWED_ORIGINS;
        final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials =
                CORSFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getFilterConfigAnyOriginAndSupportsCredentials() {
        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
        final String allowedOrigins = CORSFilter.DEFAULT_ALLOWED_ORIGINS;
        final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials = "true";
        final String preflightMaxAge =
                CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig
            getFilterConfigAnyOriginAndSupportsCredentialsDisabled() {
        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
        final String allowedOrigins = CORSFilter.DEFAULT_ALLOWED_ORIGINS;
        final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials = "false";
        final String preflightMaxAge =
                CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getFilterConfigWithExposedHeaders() {
        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = CORSFilter.DEFAULT_ALLOWED_ORIGINS;
        final String exposedHeaders = EXPOSED_HEADERS;
        final String supportCredentials =
                CORSFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getSecureFilterConfig() {
        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
        final String allowedOrigins = HTTPS_WWW_APACHE_ORG;
        final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials = "true";
        final String preflightMaxAge =
                CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getNullFilterConfig() {
        return generateFilterConfig(null, null, null, null, null, null);
    }

    public static FilterConfig getSpecificOriginFilterConfig() {
        final String allowedOrigins =
                HTTPS_WWW_APACHE_ORG + "," + HTTP_TOMCAT_APACHE_ORG;

        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
        final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials =
                CORSFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getFilterConfigInvalidMaxPreflightAge() {
        final String allowedHttpHeaders =
                CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = CORSFilter.DEFAULT_ALLOWED_ORIGINS;
        final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials =
                CORSFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge = "abc";

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getEmptyFilterConfig() {
        final String allowedHttpHeaders = "";
        final String allowedHttpMethods = "";
        final String allowedOrigins = "";
        final String exposedHeaders = "";
        final String supportCredentials = "";
        final String preflightMaxAge = "";

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    private static FilterConfig generateFilterConfig(
            final String allowedHttpHeaders, final String allowedHttpMethods,
            final String allowedOrigins, final String exposedHeaders,
            final String supportCredentials, final String preflightMaxAge) {
        FilterConfig filterConfig = new FilterConfig() {

            public String getFilterName() {
                return "cors-filter";
            }

            public ServletContext getServletContext() {
                return mockServletContext;
            }

            public String getInitParameter(String name) {
                if (CORSFilter.PARAM_CORS_ALLOWED_HEADERS
                        .equalsIgnoreCase(name)) {
                    return allowedHttpHeaders;
                } else if (CORSFilter.PARAM_CORS_ALLOWED_METHODS
                        .equalsIgnoreCase(name)) {
                    return allowedHttpMethods;
                } else if (CORSFilter.PARAM_CORS_ALLOWED_ORIGINS
                        .equalsIgnoreCase(name)) {
                    return allowedOrigins;
                } else if (CORSFilter.PARAM_CORS_EXPOSED_HEADERS
                        .equalsIgnoreCase(name)) {
                    return exposedHeaders;
                } else if (CORSFilter.PARAM_CORS_SUPPORT_CREDENTIALS
                        .equalsIgnoreCase(name)) {
                    return supportCredentials;
                } else if (CORSFilter.PARAM_CORS_PREFLIGHT_MAXAGE
                        .equalsIgnoreCase(name)) {
                    return preflightMaxAge;
                }
                return null;
            }

            @SuppressWarnings("rawtypes")
            public Enumeration getInitParameterNames() {
                return null;
            }
        };

        return filterConfig;
    }
}
