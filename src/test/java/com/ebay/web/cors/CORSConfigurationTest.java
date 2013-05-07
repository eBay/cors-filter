package com.ebay.web.cors;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Test;

public class CORSConfigurationTest {
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
    public void testWithStringParserEmpty() throws ServletException {
        CORSConfiguration corsConfiguration = CORSConfiguration
                .loadFromFilterConfig(TestConfigs.getEmptyFilterConfig());
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
}
