package com.ebay.web.cors.handlers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.ebay.web.cors.CORSConfiguration;
import com.ebay.web.cors.CORSFilter;
import com.ebay.web.cors.CORSRequestType;

public class DefaultSimpleCORSHandlerTest {

    /**
     * The allowed origin for this test.
     */
    private static final String HTTPS_LOCALHOST_EBAY_COM_8443 = "https://localhost.ebay.com:8443";

    private static final String EXPOSED_HEADERS = "Content-Type";

    private CORSConfiguration corsConfiguration;

    /**
     * Setup the intial configuration mock.
     * 
     * @throws ServletException
     */
    @Before
    public void setup() throws ServletException {
        corsConfiguration = new CORSConfiguration();
        Set<String> allowedHttpHeaders = new HashSet<String>();
        corsConfiguration.setAllowedHttpHeaders(allowedHttpHeaders);

        Set<String> allowedOrigins = new HashSet<String>();
        allowedOrigins.add(HTTPS_LOCALHOST_EBAY_COM_8443);
        corsConfiguration.setAllowedOrigins(allowedOrigins);

        Set<String> exposedHeaders = new HashSet<String>();
        exposedHeaders.add(EXPOSED_HEADERS);
        corsConfiguration.setExposedHeaders(exposedHeaders);
        corsConfiguration.setSupportsCredentials(true);
    }

    @Test
    public void test() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                HTTPS_LOCALHOST_EBAY_COM_8443);
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

        CORSFilter corsFilter = new CORSFilter(corsConfiguration);
        corsFilter.handleSimpleCORS(request, response, filterChain);
    }

    @Test
    public void testNotSupportCredentials() throws IOException,
            ServletException {
        corsConfiguration.setSupportsCredentials(false);

        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createMock(HttpServletResponse.class);
        response.addHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        response.addHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
                EXPOSED_HEADERS);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        filterChain.doFilter(request, response);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter(corsConfiguration);
        corsFilter.handleSimpleCORS(request, response, filterChain);
    }

    @Test
    public void testEmptyExposeHeaders() throws IOException, ServletException {
        corsConfiguration.setExposedHeaders(new HashSet<String>());

        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST,
                true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN,
                HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createMock(HttpServletResponse.class);
        response.addHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        response.addHeader(
                CORSFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS,
                "true");
        EasyMock.expectLastCall();

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        filterChain.doFilter(request, response);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter(corsConfiguration);
        corsFilter.handleSimpleCORS(request, response, filterChain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotSimple() throws IOException, ServletException {
        HttpServletRequest request = EasyMock
                .createMock(HttpServletRequest.class);

        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN))
                .andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
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
                HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE,
                CORSRequestType.PRE_FLIGHT.getType());
        EasyMock.expectLastCall();

        EasyMock.replay(request);

        HttpServletResponse response = EasyMock
                .createNiceMock(HttpServletResponse.class);
        EasyMock.replay(response);

        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        filterChain.doFilter(request, response);
        EasyMock.replay(filterChain);

        CORSFilter corsFilter = new CORSFilter(corsConfiguration);
        corsFilter.handleSimpleCORS(request, response, filterChain);
    }

}
