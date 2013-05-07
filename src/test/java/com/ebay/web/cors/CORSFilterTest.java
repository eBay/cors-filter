package com.ebay.web.cors;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class CORSFilterTest {

    @Test
    public void testDoFilterSimple() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTPS_WWW_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getSpecificOriginFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
        corsFilter.destroy();
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test
    public void testDoFilterSimpleWithCredentials() throws IOException,
            ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTPS_WWW_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        response.addHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS,
                "true");
        EasyMock.expectLastCall();
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getSecureFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
        corsFilter.destroy();
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test
    public void testDoFilterPreflight() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTPS_WWW_APACHE_ORG).anyTimes();

        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("Content-Type").anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.PRE_FLIGHT.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getSpecificOriginFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test
    public void testDoFilterNotCORS() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(null).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                false);
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test(expected = ServletException.class)
    public void testDoFilterInvalidCORSOriginNotAllowed() throws IOException,
            ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("www.google.com").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.INVALID_CORS.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        CORSConfiguration corsConfiguration = CORSConfiguration
                .loadFromFilterConfig(TestConfigs
                        .getSpecificOriginFilterConfig());
        CORSFilter corsFilter = new CORSFilter(corsConfiguration);
        corsFilter.doFilter(request, response, filterChain);
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test(expected = ServletException.class)
    public void testDoFilterNullRequestNullResponse() throws IOException,
            ServletException {
        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(null, null, filterChain);
    }

    @Test(expected = ServletException.class)
    public void testDoFilterNullRequestResponse() throws IOException,
            ServletException {
        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        HttpServletResponse response = EasyMock
                .createMock(HttpServletResponse.class);
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(null, response, filterChain);
    }

    @Test(expected = ServletException.class)
    public void testDoFilterRequestNullResponse() throws IOException,
            ServletException {
        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, null, filterChain);
    }

    @Test
    public void testDoFilterSimpleCustomHandlers() throws IOException,
            ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTPS_WWW_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());

        corsFilter.doFilter(request, response, filterChain);
        corsFilter.destroy();
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    /**
     * Should load the default config and configure default handlers. And, not
     * throw IOException.
     * 
     * @throws IOException
     */
    public void testDefaultConstructor() throws IOException {
        new CORSFilter();
    }

    @Test
    public void testInit() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTPS_WWW_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getSpecificOriginFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
        corsFilter.destroy();
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test
    public void testInitDefaultFilterConfig() throws IOException,
            ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTPS_WWW_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(null);
        corsFilter.doFilter(request, response, filterChain);
        corsFilter.destroy();
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test(expected = ServletException.class)
    public void testInitInvalidFilterConfig() throws IOException,
            ServletException {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getFilterConfigInvalidMaxPreflightAge());
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    @Test
    public void testSimpleCORSRequest() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        filterChain.doFilter(request, response);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
    }

    /**
     * Tests if a non-simple request is given to simple request handler.
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotSimple() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("Content-Type").anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.PRE_FLIGHT.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.handleSimpleCORS(request, response, filterChain);
    }

    /**
     * When a non-preflight request is given to a pre-flight requets handler.
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotPreflight() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.handlePreflightCORS(request, response, filterChain);
    }

    @Test
    public void testPreFlightHandler() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD))
                .andReturn("OPTIONS").anyTimes();
        EasyMock.expect(
                request.getHeader(CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS))
                .andReturn("Content-Type").anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.PRE_FLIGHT.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.handlePreflightCORS(request, response, filterChain);
    }

    @Test
    public void testHandleNonCORSHandler() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        filterChain.doFilter(request, response);
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        EasyMock.replay(response);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.handleNonCORS(request, response, filterChain);
    }

    @Test(expected = ServletException.class)
    public void testHandleInvalidCORS() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn("www.google.com").anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();

        EasyMock.replay(request);
        EasyMock.replay(response);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter();

        corsFilter.handleInvalidCORS(request, response, filterChain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecorateCORSPropertiesNullRequestNullCORSRequestType() {
        CORSFilter.decorateCORSProperties(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecorateCORSPropertiesNullRequestValidCORSRequestType() {
        CORSFilter.decorateCORSProperties(null, CORSRequestType.SIMPLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecorateCORSPropertiesValidRequestNullRequestType() {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        EasyMock.replay(request);
        CORSFilter.decorateCORSProperties(request, null);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeSimple() {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        CORSFilter.decorateCORSProperties(request, CORSRequestType.SIMPLE);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypePreFlight() {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(TestConfigs.HTTP_TOMCAT_APACHE_ORG).anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                TestConfigs.HTTP_TOMCAT_APACHE_ORG);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.PRE_FLIGHT.getType());
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        CORSFilter.decorateCORSProperties(request, CORSRequestType.PRE_FLIGHT);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeNotCORS() {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                false);
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        CORSFilter.decorateCORSProperties(request, CORSRequestType.NOT_CORS);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeInvalidCORS() {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);
        EasyMock.replay(request);
        CORSFilter
                .decorateCORSProperties(request, CORSRequestType.INVALID_CORS);
    }

    @Test
    public void testJoin() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = ",";
        elements.add("world");
        elements.add("peace");
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue("world,peace".equals(join));
    }

    @Test
    public void testJoinSingleElement() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = ",";
        elements.add("world");
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue("world".equals(join));
    }

    @Test
    public void testJoinSepNull() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = null;
        elements.add("world");
        elements.add("peace");
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue("world,peace".equals(join));
    }

    @Test
    public void testJoinElementsNull() {
        Set<String> elements = null;
        String separator = ",";
        String join = CORSFilter.join(elements, separator);

        Assert.assertNull(join);
    }

    @Test
    public void testJoinOneNullElement() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = ",";
        elements.add(null);
        elements.add("peace");
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue(",peace".equals(join));
    }

    @Test
    public void testJoinAllNullElements() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = ",";
        elements.add(null);
        elements.add(null);
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue("".equals(join));
    }

    @Test
    public void testJoinAllEmptyElements() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = ",";
        elements.add("");
        elements.add("");
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue("".equals(join));
    }

    @Test
    public void testJoinPipeSeparator() {
        Set<String> elements = new LinkedHashSet<String>();
        String separator = "|";
        elements.add("world");
        elements.add("peace");
        String join = CORSFilter.join(elements, separator);
        Assert.assertTrue("world|peace".equals(join));
    }

    @Test
    public void testDestroy() {
        // Nothing to test.
        // NO-OP
    }
}
