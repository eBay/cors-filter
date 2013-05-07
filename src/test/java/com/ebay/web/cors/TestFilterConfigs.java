package com.ebay.web.cors;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

public class TestFilterConfigs {
    public static FilterConfig getFilterConfig() {
        final String allowedHttpHeaders = "Content-Type";
        final String allowedHttpMethods = "GET,POST,HEAD,OPTIONS";
        final String allowedOrigins = "https://www.apache.org,http://tomcat.apache.com";
        final String exposedHeaders = "Content-Encoding";
        final String supportCredentials = "true";
        final String preflightMaxAge = "1000";

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge);
    }

    public static FilterConfig getAnyOriginFilterConfig() {
        final String allowedHttpHeaders = "Content-Type";
        final String allowedHttpMethods = "GET,POST,HEAD,OPTIONS";
        final String allowedOrigins = "*";
        final String exposedHeaders = "Content-Encoding";
        final String supportCredentials = "false";
        final String preflightMaxAge = "1000";

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
                return null;
            }

            public String getInitParameter(String name) {
                if (CORSConfiguration.CORS_ALLOWED_HEADERS
                        .equalsIgnoreCase(name)) {
                    return allowedHttpHeaders;
                } else if (CORSConfiguration.CORS_ALLOWED_METHODS
                        .equalsIgnoreCase(name)) {
                    return allowedHttpMethods;
                } else if (CORSConfiguration.CORS_ALLOWED_ORIGINS
                        .equalsIgnoreCase(name)) {
                    return allowedOrigins;
                } else if (CORSConfiguration.CORS_EXPOSED_HEADERS
                        .equalsIgnoreCase(name)) {
                    return exposedHeaders;
                } else if (CORSConfiguration.CORS_SUPPORT_CREDENTIALS
                        .equalsIgnoreCase(name)) {
                    return supportCredentials;
                } else if (CORSConfiguration.CORS_PREFLIGHT_MAXAGE
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
