package com.ebay.web.cors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests {@link CORSRequestType} for positive and negative scenarios.
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * 
 */
public class CORSRequestTypeTest {

    /**
     * Happy path test, when a valid CORS Simple request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckSimpleRequestTypeAnyOrigin() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.ANY_ORIGIN);
        request.setMethod("GET");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.SIMPLE, requestType);
    }

    /**
     * Happy path test, when a valid CORS Simple request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckSimpleRequestType() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        request.setMethod("POST");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.SIMPLE, requestType);
    }

    /**
     * Happy path test, when a valid CORS Pre-flight request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestType() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                "PUT");
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                "Content-Type");
        request.setMethod("OPTIONS");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
    }

    /**
     * when a valid CORS Pre-flight request arrives, with no
     * Access-Control-Request-Method
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeNoACRM() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);

        request.setMethod("OPTIONS");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * when a valid CORS Pre-flight request arrives, with empty
     * Access-Control-Request-Method
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeEmptyACRM()
            throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                "");
        request.setMethod("OPTIONS");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * Happy path test, when a valid CORS Pre-flight request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeNoHeaders()
            throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                "PUT");
        request.setMethod("OPTIONS");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
    }
    
    @Test
    public void testCheckPreFlightRequestTypeInvalidRequestMethod()
            throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                "POLITE");
        request.setMethod("OPTIONS");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * Happy path test, when a valid CORS Pre-flight request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeEmptyHeaders()
            throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                "PUT");
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                "");
        request.setMethod("OPTIONS");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
    }

    /**
     * Negative test, when a CORS request arrives, with an empty origin.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckNotCORSRequestTypeEmptyOrigin()
            throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "");
        request.setMethod("GET");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.NOT_CORS, requestType);
    }

    /**
     * Negative test, when a CORS request arrives, with a null origin.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckNotCORSRequestTypeNullOrigin() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                null);
        request.setMethod("GET");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.NOT_CORS, requestType);
    }

    /**
     * Tests for failure, when a different domain is used, that's not in the
     * allowed list of origins.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckInvalidOrigin() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "www.example.com");
        request.setMethod("GET");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * Tests for failure, when a different sub-domain is used, that's not in the
     * allowed list of origins.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckInvalidOriginNotAllowedSubdomain()
            throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "http://commons.apache.org");
        request.setMethod("GET");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }
    
    /**
     * PUT is not an allowed request method.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckInvalidRequestMethod() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "http://tomcat.apache.org");
        request.setMethod("PUT");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * When requestMethod is null
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckNullRequestMethod() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "http://tomcat.apache.org");
        request.setMethod(null);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * "http://tomcat.apache.org" is an allowed origin and
     * "https://tomcat.apache.org" is not, because scheme doesn't match
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckForSchemeVariance() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "https://tomcat.apache.org");
        request.setMethod("POST");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * "http://tomcat.apache.org" is an allowed origin and
     * "http://tomcat.apache.org:8080" is not, because ports doesn't match
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckForPortVariance() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "http://tomcat.apache.org:8080");
        request.setMethod("GET");
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);
        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
    }

    /**
     * Tests for failure, when an invalid {@link HttpServletRequest} is
     * encountered.
     * 
     * @throws ServletException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckRequestTypeNull() throws ServletException {
        HttpServletRequest request = null;
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType.checkRequestType(request, corsConfigurationAnyOrigin);
    }
}
