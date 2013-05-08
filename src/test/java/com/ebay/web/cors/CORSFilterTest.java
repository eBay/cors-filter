package com.ebay.web.cors;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CORSFilterTest {
    private FilterChain filterChain = new MockFilterChain();

    @Before
    public void setup() {
    }

    /**
     * Tests if a GET request is treated as simple request.
     * 
     * @See http://www.w3.org/TR/cors/#simple-method
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testDoFilterSimpleGET() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("GET");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                "*"));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    /**
     * Tests if a POST request is treated as simple request.
     * 
     * @See http://www.w3.org/TR/cors/#simple-method
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testDoFilterSimplePOST() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("POST");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                "*"));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    /**
     * Tests if a HEAD request is treated as simple request.
     * 
     * @See http://www.w3.org/TR/cors/#simple-method
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testDoFilterSimpleHEAD() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("HEAD");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                "*"));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    @Test
    public void testDoFilterSimpleSpecificHeader() throws IOException,
            ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("POST");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getSpecificOriginFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    @Test
    public void testDoFilterSimpleAnyOriginAndSupportsCredentials()
            throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("GET");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs
                .getFilterConfigAnyOriginAndSupportsCredentials());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .equals(
                        "true"));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    @Test
    public void testDoFilterSimpleWithExposedHeaders() throws IOException,
            ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("POST");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs
                .getFilterConfigWithExposedHeaders());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                "*"));
        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS)
                .equals(TestConfigs.EXPOSED_HEADERS));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    @Test
    public void testDoFilterPreflight() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD, "PUT");
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                "Content-Type");
        request.setMethod("OPTIONS");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs
                .getSpecificOriginFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.PRE_FLIGHT.getType()));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_HEADERS).equals(
                "Content-Type"));
    }

    @Test
    public void testDoFilterPreflightWithCredentials() throws IOException,
            ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD, "PUT");
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                "Content-Type");
        request.setMethod("OPTIONS");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs
                .getSecureFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .equals("true"));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.PRE_FLIGHT.getType()));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_HEADERS).equals(
                "Content-Type"));
    }

    @Test
    public void testDoFilterNotCORS() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                null);
        request.setMethod("POST");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertFalse((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
    }

    @Test(expected = ServletException.class)
    public void testDoFilterInvalidCORSOriginNotAllowed() throws IOException,
            ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                "www.google.com");
        request.setMethod("POST");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getSpecificOriginFilterConfig());
        corsFilter.doFilter(request, response, filterChain);
    }

    @Test(expected = ServletException.class)
    public void testDoFilterNullRequestNullResponse() throws IOException,
            ServletException {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(null, null, filterChain);
    }

    @Test(expected = ServletException.class)
    public void testDoFilterNullRequestResponse() throws IOException,
            ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(null, response, filterChain);
    }

    @Test(expected = ServletException.class)
    public void testDoFilterRequestNullResponse() throws IOException,
            ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.doFilter(request, null, filterChain);
    }

    @Test
    public void testInitDefaultFilterConfig() throws IOException,
            ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("GET");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(null);
        corsFilter.doFilter(request, response, filterChain);

        Assert.assertTrue(response.getHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
                "*"));
        Assert.assertTrue((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
                TestConfigs.HTTPS_WWW_APACHE_ORG));
        Assert.assertTrue(request.getAttribute(
                CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
                CORSRequestType.SIMPLE.getType()));
    }

    @Test(expected = ServletException.class)
    public void testInitInvalidFilterConfig() throws IOException,
            ServletException {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getFilterConfigInvalidMaxPreflightAge());
        // If we don't get an exception at this point, then all mocked objects
        // worked as expected.
    }

    /**
     * Tests if a non-simple request is given to simple request handler.
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotSimple() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD, "PUT");
        request.setHeader(
                CORSFilter.REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                "Content-Type");
        request.setMethod("OPTIONS");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs
                .getDefaultFilterConfig());
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeader(CORSFilter.REQUEST_HEADER_ORIGIN,
                TestConfigs.HTTPS_WWW_APACHE_ORG);
        request.setMethod("GET");
        MockHttpServletResponse response = new MockHttpServletResponse();

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(TestConfigs.getDefaultFilterConfig());
        corsFilter.handlePreflightCORS(request, response, filterChain);
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        CORSFilter.decorateCORSProperties(request, null);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeNotCORS() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CORSFilter.decorateCORSProperties(request, CORSRequestType.NOT_CORS);
        Assert.assertFalse((Boolean) request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeInvalidCORS() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CORSFilter.decorateCORSProperties(request, CORSRequestType.INVALID_CORS);
        Assert.assertNull(request
                .getAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
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
