package com.ebay.web.cors;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CORSConfigurationTest {
    private CORSConfiguration config;

    @Before
    public void setUp() throws Exception {
        this.config = new CORSConfiguration();
    }

    @Test
    public void testCORSConfigurationDefaultsAllowedHttpHeaders() {
        Set<String> allowedHttpHeaders = this.config.getAllowedHttpHeaders();
        Assert.assertNotNull(allowedHttpHeaders);
        Assert.assertTrue(allowedHttpHeaders.size() == 0);
    }

    @Test
    public void testCORSConfigurationDefaultsAllowedHttpMethods() {
        Set<String> allowedHttpMethods = this.config.getAllowedHttpMethods();
        Assert.assertNotNull(allowedHttpMethods);
        Assert.assertTrue(allowedHttpMethods.size() == 4);

        Assert.assertTrue(allowedHttpMethods.contains("GET"));
        Assert.assertTrue(allowedHttpMethods.contains("POST"));
        Assert.assertTrue(allowedHttpMethods.contains("OPTIONS"));
        Assert.assertTrue(allowedHttpMethods.contains("HEAD"));
    }

    @Test
    public void testCORSConfigurationDefaultsAllowedOrigins() {
        Set<String> allowedOrigins = this.config.getAllowedOrigins();
        Assert.assertNotNull(allowedOrigins);
        Assert.assertTrue(allowedOrigins.size() == 0);
    }

    @Test
    public void testCORSConfigurationDefaultsExposedMethods() {
        Set<String> exposedHeaders = this.config.getExposedHeaders();
        Assert.assertNotNull(exposedHeaders);
        Assert.assertTrue(exposedHeaders.size() == 0);
    }

    @Test
    public void testCORSConfigurationDefaultsSupportsCredentials() {
        boolean supportsCredentials = this.config.isSupportsCredentials();
        Assert.assertFalse(supportsCredentials);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedOrigins() {
        Set<String> allowedOrigins = this.config.getAllowedOrigins();
        allowedOrigins.add("sherlock holmes");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedHttpMethods() {
        Set<String> allowedHttpMethods = this.config.getAllowedHttpMethods();
        allowedHttpMethods.add("dr.watson");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetExposedHeaders() {
        Set<String> exposedHeaders = this.config.getExposedHeaders();
        exposedHeaders.add("Sheldon!");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedHttpHeaders() {
        Set<String> allowedHttpHeaders = this.config.getAllowedHttpHeaders();
        allowedHttpHeaders.add("Raj");
    }

    @Test
    public void testWithStringParser() {
        String allowedHttpHeaders = "Content-Type";
        String allowedHttpMethods = "GET,POST,HEAD,OPTIONS";
        String allowedOrigins = "https://www.ebay.com,https://deals.ebay.com";
        String exposedHeaders = "Content-Encoding";
        String supportCredentials = "true";
        String preflightMaxAge = "1000";
        CORSConfiguration corsConfiguration = new CORSConfiguration(
                allowedOrigins, allowedHttpMethods, allowedHttpHeaders,
                exposedHeaders, supportCredentials, preflightMaxAge);
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 1);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 2);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 1);
        Assert.assertTrue(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 1000);
    }

    @Test
    public void testWithStringParserEmpty() {
        String allowedHttpHeaders = "";
        String allowedHttpMethods = "";
        String allowedOrigins = "";
        String exposedHeaders = "";
        String supportCredentials = "";
        String preflightMaxAge = "";
        CORSConfiguration corsConfiguration = new CORSConfiguration(
                allowedOrigins, allowedHttpMethods, allowedHttpHeaders,
                exposedHeaders, supportCredentials, preflightMaxAge);
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 0);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 0);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 0);
        Assert.assertFalse(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 0);
    }

    @Test
    public void testWithStringParserNull() {
        String allowedHttpHeaders = null;
        String allowedHttpMethods = null;
        String allowedOrigins = null;
        String exposedHeaders = null;
        String supportCredentials = null;
        String preflightMaxAge = null;
        CORSConfiguration corsConfiguration = new CORSConfiguration(
                allowedOrigins, allowedHttpMethods, allowedHttpHeaders,
                exposedHeaders, supportCredentials, preflightMaxAge);
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 0);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 0);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 0);
        Assert.assertFalse(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 0);
    }

    @Test
    public void testWithProperties() {
        String allowedHttpHeaders = "Content-Type";
        String allowedHttpMethods = "GET,POST,HEAD,OPTIONS";
        String allowedOrigins = "https://www.ebay.com,https://deals.ebay.com";
        String exposedHeaders = "Content-Encoding";
        String supportCredentials = "true";

        Properties properties = new Properties();
        properties.setProperty(CORSConfiguration.CORS_ALLOWED_HEADERS,
                allowedHttpHeaders);
        properties.setProperty(CORSConfiguration.CORS_ALLOWED_METHODS,
                allowedHttpMethods);
        properties.setProperty(CORSConfiguration.CORS_ALLOWED_ORIGINS,
                allowedOrigins);
        properties.setProperty(CORSConfiguration.CORS_EXPOSED_HEADERS,
                exposedHeaders);
        properties.setProperty(CORSConfiguration.CORS_SUPPORT_CREDENTIALS,
                supportCredentials);

        CORSConfiguration corsConfiguration = new CORSConfiguration(properties);
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 1);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 2);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 1);
        Assert.assertTrue(corsConfiguration.isSupportsCredentials());
    }

    @Test
    public void testLoadDefault() throws Exception {
        CORSConfiguration corsConfiguration = CORSConfiguration.loadDefault();
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 2);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 2);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 0);
        Assert.assertTrue(corsConfiguration.isSupportsCredentials());
    }

    @Test
    public void testWithFilterConfig() {
        final String allowedHttpHeaders = "Content-Type";
        final String allowedHttpMethods = "GET,POST,HEAD,OPTIONS";
        final String allowedOrigins = "https://www.ebay.com,https://deals.ebay.com";
        final String exposedHeaders = "Content-Encoding";
        final String supportCredentials = "true";
        final String preflightMaxAge = "1000";

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
        CORSConfiguration corsConfiguration = CORSConfiguration
                .loadFromFilterConfig(filterConfig);
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 1);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 2);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 1);
        Assert.assertTrue(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 1000);
    }

}
