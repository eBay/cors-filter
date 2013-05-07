package com.ebay.web.cors;

import java.util.Set;

import javax.servlet.ServletException;

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
    public void testWithFilterConfig() throws ServletException {
        CORSConfiguration corsConfiguration = CORSConfiguration
                .loadFromFilterConfig(TestConfigs.getDefaultFilterConfig());
        Assert.assertTrue(corsConfiguration.getAllowedHttpHeaders().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 4);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 0);
        Assert.assertTrue(corsConfiguration.isAnyOriginAllowed());
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 0);
        Assert.assertFalse(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 1800);
    }

    @Test
    public void testWithFilterConfigNull() throws ServletException {
        CORSConfiguration corsConfiguration = CORSConfiguration
                .loadFromFilterConfig(null);
        Assert.assertNull(corsConfiguration);
    }

    @Test(expected = ServletException.class)
    public void testWithFilterConfigInvalidPreflightAge()
            throws ServletException {
        CORSConfiguration.loadFromFilterConfig(TestConfigs
                .getFilterConfigInvalidMaxPreflightAge());
    }

    @Test
    public void testWithStringParser() throws ServletException {
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
    public void testWithStringParserEmpty() throws ServletException {
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
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 0);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 0);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 0);
        Assert.assertFalse(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 0);
    }

    @Test
    public void testWithStringParserNull() throws ServletException {
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
        Assert.assertTrue(corsConfiguration.getAllowedHttpMethods().size() == 0);
        Assert.assertTrue(corsConfiguration.getAllowedOrigins().size() == 0);
        Assert.assertTrue(corsConfiguration.getExposedHeaders().size() == 0);
        Assert.assertFalse(corsConfiguration.isSupportsCredentials());
        Assert.assertTrue(corsConfiguration.getPreflightMaxAge() == 0);
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
}
