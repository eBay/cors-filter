package com.ebay.web.cors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
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
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.ANY_ORIGIN).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.SIMPLE, requestType);
        EasyMock.verify(request);
    }

    /**
     * Happy path test, when a valid CORS Simple request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckSimpleRequestType() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.SIMPLE, requestType);
        EasyMock.verify(request);
    }

    /**
     * Happy path test, when a valid CORS Pre-flight request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestType() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("Content-Type").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
        EasyMock.verify(request);
    }

    /**
     * when a valid CORS Pre-flight request arrives, with no
     * Access-Control-Request-Method
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeNoACRM() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn(null).anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("Content-Type").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
        EasyMock.verify(request);
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
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("Content-Type").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
        EasyMock.verify(request);
    }

    /**
     * Happy path test, when a valid CORS Pre-flight request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeNoHeaders()
            throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn(null).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
        EasyMock.verify(request);
    }

    /**
     * Happy path test, when a valid CORS Pre-flight request arrives.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckPreFlightRequestTypeEmptyHeaders()
            throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
        EasyMock.verify(request);
    }

    /**
     * Negative test, when a CORS request arrives, with an empty origin.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckNotCORSRequestTypeEmptyOrigin()
            throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.NOT_CORS, requestType);
        EasyMock.verify(request);
    }

    /**
     * Negative test, when a CORS request arrives, with a null origin.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckNotCORSRequestTypeNullOrigin() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(null).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.NOT_CORS, requestType);
        EasyMock.verify(request);
    }

    /**
     * Tests for failure, when a different domain is used, that's not in the
     * allowed list of origins.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckInvalidOrigin() throws ServletException {

        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("www.google.com").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfiguration =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request, corsConfiguration);

        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
        EasyMock.verify(request);
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

        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("https://foo.ebay.com:8443").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfiguration =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request, corsConfiguration);

        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
        EasyMock.verify(request);
    }

    /**
     * PUT is not an allowed request method.
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckInvalidRequestMethod() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("https://localhost.ebay.com:8443").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("PUT").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
        EasyMock.verify(request);
    }
    
    /**
     * When requestMethod is null
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckNullRequestMethod() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("https://localhost.ebay.com:8443").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn(null).anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfigurationAnyOrigin =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getDefaultFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request,
                        corsConfigurationAnyOrigin);

        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
        EasyMock.verify(request);
    }

    /**
     * "https://localhost.ebay.com:8443" is an allowed origin and
     * "http://localhost.ebay.com:8443" is not, because protocol doesn't match
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckForProtocolVariance() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("http://localhost.ebay.com:8443").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfiguration =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request, corsConfiguration);

        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
        EasyMock.verify(request);
    }

    /**
     * "https://localhost.ebay.com:8443" is an allowed origin and
     * "https://localhost.ebay.com:8080" is not, because ports doesn't match
     * 
     * @throws ServletException
     */
    @Test
    public void testCheckForPortVariance() throws ServletException {
        HttpServletRequest request =
                EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("https://localhost.ebay.com:8080").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
        EasyMock.replay(request);
        CORSConfiguration corsConfiguration =
                CORSConfiguration.loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSRequestType requestType =
                CORSRequestType.checkRequestType(request, corsConfiguration);

        Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
        EasyMock.verify(request);
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
