package com.ebay.web.cors;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.ebay.web.cors.handlers.DefaultInvalidCORSHandler;
import com.ebay.web.cors.handlers.DefaultNonCORSHandler;
import com.ebay.web.cors.handlers.DefaultPreflightCORSHandler;
import com.ebay.web.cors.handlers.DefaultSimpleCORSHandler;

public class CORSFilterTest {
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
		corsConfiguration.setAllowedHttpHeaders(allowedHttpHeaders);

		Set<String> allowedOrigins = new HashSet<String>();
		allowedOrigins.add(HTTPS_LOCALHOST_EBAY_COM_8443);
		corsConfiguration.setAllowedOrigins(allowedOrigins);

		Set<String> exposedHeaders = new HashSet<String>();
		corsConfiguration.setExposedHeaders(exposedHeaders);
		corsConfiguration.setSupportsCredentials(true);
	}

	@Test
	public void testDoFilterSimple() throws IOException, ServletException {
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

		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(request, response, filterChain);
		corsFilter.destroy();
		// If we don't get an exception at this point, then all mocked objects
		// worked as expected.
	}

	@Test
	public void testDoFilterPreflight() throws IOException, ServletException {
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

		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(request, response, filterChain);
		// If we don't get an exception at this point, then all mocked objects
		// worked as expected.
	}

	@Test
	public void testDoFilterNotCORS() throws IOException, ServletException {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(null).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();

		request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, false);
		EasyMock.expectLastCall();

		EasyMock.replay(request);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		EasyMock.replay(response);

		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(request, response, filterChain);
		// If we don't get an exception at this point, then all mocked objects
		// worked as expected.
	}

	@Test(expected = ServletException.class)
	public void testDoFilterInvalidCORSOriginNotAllowed() throws IOException,
			ServletException {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("www.google.com").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn("OPTIONS").anyTimes();

		request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, true);
		EasyMock.expectLastCall();
		request.setAttribute(CORSRequestProperties.ORIGIN,
				HTTPS_LOCALHOST_EBAY_COM_8443);
		EasyMock.expectLastCall();
		request.setAttribute(CORSRequestProperties.REQUEST_TYPE,
				CORSRequestType.INVALID_CORS.getType());
		EasyMock.expectLastCall();

		EasyMock.replay(request);

		HttpServletResponse response = EasyMock
				.createNiceMock(HttpServletResponse.class);
		EasyMock.replay(response);

		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(request, response, filterChain);
		// If we don't get an exception at this point, then all mocked objects
		// worked as expected.
	}

	@Test(expected = ServletException.class)
	public void testDoFilterNullRequestNullResponse() throws IOException,
			ServletException {
		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(null, null, filterChain);
	}

	@Test(expected = ServletException.class)
	public void testDoFilterNullRequestResponse() throws IOException,
			ServletException {
		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
		HttpServletResponse response = EasyMock
				.createMock(HttpServletResponse.class);
		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(null, response, filterChain);
	}

	@Test(expected = ServletException.class)
	public void testDoFilterRequestNullResponse() throws IOException,
			ServletException {
		FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		corsFilter.doFilter(request, null, filterChain);
	}

	@Test
	public void testDoFilterSimpleCustomHandlers() throws IOException,
			ServletException {
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

		CORSFilter corsFilter = new CORSFilter(corsConfiguration);
		DefaultInvalidCORSHandler defaultInvalidCORSHandler = new DefaultInvalidCORSHandler();
		DefaultNonCORSHandler defaultNonCORSHandler = new DefaultNonCORSHandler();
		DefaultPreflightCORSHandler defaultPreflightCORSHandler = new DefaultPreflightCORSHandler(
				corsConfiguration);
		DefaultSimpleCORSHandler defaultSimpleCORSHandler = new DefaultSimpleCORSHandler(
				corsConfiguration);

		corsFilter.setInvalidCORSRequestHandler(defaultInvalidCORSHandler);
		corsFilter.setNonCORSRequestHandler(defaultNonCORSHandler);
		corsFilter.setPreFlightRequestHandler(defaultPreflightCORSHandler);
		corsFilter.setSimpleRequestHandler(defaultSimpleCORSHandler);
		corsFilter.setCorsConfiguration(corsConfiguration);

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
		final String allowedHttpHeaders = "Content-Type";
		final String allowedHttpMethods = "GET,POST,HEAD,OPTIONS";
		final String allowedOrigins = "https://localhost.ebay.com:8443,https://deals.ebay.com";
		final String exposedHeaders = "Content-Encoding";
		final String supportCredentials = "true";
		final String preflightMaxAge = "1000";

		FilterConfig filterConfig = new FilterConfig() {

			public String getFilterName() {
				// TODO Auto-generated method stub
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

		CORSFilter corsFilter = new CORSFilter();
		corsFilter.init(filterConfig);
		corsFilter.doFilter(request, response, filterChain);
		corsFilter.destroy();
		// If we don't get an exception at this point, then all mocked objects
		// worked as expected.
	}

	@Test
	public void testDestroy() {
		// Nothing to test.
		// NO-OP
	}

}
