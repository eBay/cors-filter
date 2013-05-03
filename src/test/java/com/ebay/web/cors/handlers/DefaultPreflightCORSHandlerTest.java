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
import com.ebay.web.cors.CORSRequestHeaders;
import com.ebay.web.cors.CORSRequestProperties;
import com.ebay.web.cors.CORSRequestType;

public class DefaultPreflightCORSHandlerTest {

	/**
	 * The allowed origin for this test.
	 */
	private static final String HTTPS_LOCALHOST_EBAY_COM_8443 = "https://localhost.ebay.com:8443";

	private CORSConfiguration corsConfiguration;

	/**
	 * Setup the intial configuration mock.
	 */
	@Before
	public void setup() {
		corsConfiguration = new CORSConfiguration();
		Set<String> allowedHttpHeaders = new HashSet<String>();
		allowedHttpHeaders.add("Content-Type");

		corsConfiguration.setAllowedHttpHeaders(allowedHttpHeaders);

		Set<String> allowedOrigins = new HashSet<String>();
		allowedOrigins.add(HTTPS_LOCALHOST_EBAY_COM_8443);
		corsConfiguration.setAllowedOrigins(allowedOrigins);

		Set<String> exposedHeaders = new HashSet<String>();
		exposedHeaders.add("Content-Type");
		corsConfiguration.setExposedHeaders(exposedHeaders);
		corsConfiguration.setSupportsCredentials(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotPreflight() throws IOException, ServletException {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

		request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, true);
		EasyMock.expectLastCall();
		request.setAttribute(CORSRequestProperties.ORIGIN,
				HTTPS_LOCALHOST_EBAY_COM_8443);
		EasyMock.expectLastCall();
		request.setAttribute(CORSRequestProperties.REQUEST_TYPE,
				CORSRequestType.SIMPLE.getType());
		EasyMock.expectLastCall();

		EasyMock.replay(request);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		EasyMock.replay(response);

		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
		filterChain.doFilter(request, response);
		EasyMock.replay(filterChain);

		DefaultPreflightCORSHandler handler = new DefaultPreflightCORSHandler(
				corsConfiguration);
		handler.handle(request, response, filterChain);
	}

	@Test
	public void test() throws IOException, ServletException {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn("OPTIONS").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
				.andReturn("Content-Type").anyTimes();
		request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, true);
		EasyMock.expectLastCall();
		request.setAttribute(CORSRequestProperties.ORIGIN,
				HTTPS_LOCALHOST_EBAY_COM_8443);
		EasyMock.expectLastCall();
		request.setAttribute(CORSRequestProperties.REQUEST_TYPE,
				CORSRequestType.PRE_FLIGHT.getType());
		EasyMock.expectLastCall();

		EasyMock.replay(request);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		EasyMock.replay(response);

		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
		filterChain.doFilter(request, response);
		EasyMock.replay(filterChain);

		DefaultPreflightCORSHandler handler = new DefaultPreflightCORSHandler(
				corsConfiguration);
		handler.handle(request, response, filterChain);
	}

}
