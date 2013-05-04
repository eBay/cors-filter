package com.ebay.web.cors;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests {@link CORSRequestType} for positive and negative scenarios.
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * 
 */
public class CORSRequestTypeTest {

	/**
	 * The allowed origin for this test.
	 */
	private static final String HTTPS_LOCALHOST_EBAY_COM_8443 = "https://localhost.ebay.com:8443";

	/**
	 * The allowed origin for this test.
	 */
	private static final String HTTPS_HISTORY_EBAY_COM_8443 = "https://history.ebay.com:8443";

	/**
	 * Any origin
	 */
	private static final String ANY_ORIGIN = "*";

	private CORSConfiguration corsConfiguration;

	private CORSConfiguration corsConfigurationAnyOrigin;

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
		allowedOrigins.add(HTTPS_HISTORY_EBAY_COM_8443);
		corsConfiguration.setAllowedOrigins(allowedOrigins);

		Set<String> exposedHeaders = new HashSet<String>();
		corsConfiguration.setExposedHeaders(exposedHeaders);
		corsConfiguration.setSupportsCredentials(true);

		corsConfigurationAnyOrigin = generateAnyOriginConfig();
	}

	private CORSConfiguration generateAnyOriginConfig() {
		CORSConfiguration corsConfiguration = new CORSConfiguration();
		Set<String> allowedHttpHeaders = new HashSet<String>();
		corsConfiguration.setAllowedHttpHeaders(allowedHttpHeaders);

		Set<String> allowedOrigins = new HashSet<String>();
		allowedOrigins.add(ANY_ORIGIN);
		corsConfiguration.setAllowedOrigins(allowedOrigins);

		Set<String> exposedHeaders = new HashSet<String>();
		corsConfiguration.setExposedHeaders(exposedHeaders);
		corsConfiguration.setSupportsCredentials(false);

		return corsConfiguration;
	}

	/**
	 * Happy path test, when a valid CORS Simple request arrives.
	 */
	@Test
	public void testCheckSimpleRequestTypeAnyOrigin() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(ANY_ORIGIN).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfigurationAnyOrigin);

		Assert.assertEquals(CORSRequestType.SIMPLE, requestType);
	}

	/**
	 * Happy path test, when a valid CORS Simple request arrives.
	 */
	@Test
	public void testCheckSimpleRequestType() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.SIMPLE, requestType);
	}

	/**
	 * Happy path test, when a valid CORS Pre-flight request arrives.
	 */
	@Test
	public void testCheckPreFlightRequestType() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn("OPTIONS").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
				.andReturn("Content-Type").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
	}

	/**
	 * when a valid CORS Pre-flight request arrives, with no
	 * Access-Control-Request-Method
	 */
	@Test
	public void testCheckPreFlightRequestTypeNoACRM() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn(null).anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
				.andReturn("Content-Type").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
	}

	/**
	 * when a valid CORS Pre-flight request arrives, with empty
	 * Access-Control-Request-Method
	 */
	@Test
	public void testCheckPreFlightRequestTypeEmptyACRM() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn("").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
				.andReturn("Content-Type").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
	}

	/**
	 * Happy path test, when a valid CORS Pre-flight request arrives.
	 */
	@Test
	public void testCheckPreFlightRequestTypeNoHeaders() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn("OPTIONS").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
				.andReturn(null).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
	}

	/**
	 * Happy path test, when a valid CORS Pre-flight request arrives.
	 */
	@Test
	public void testCheckPreFlightRequestTypeEmptyHeaders() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);

		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(HTTPS_LOCALHOST_EBAY_COM_8443).anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_METHOD))
				.andReturn("OPTIONS").anyTimes();
		EasyMock.expect(
				request.getHeader(CORSRequestHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
				.andReturn("").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("OPTIONS").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.PRE_FLIGHT, requestType);
	}

	/**
	 * Negative test, when a CORS request arrives, with an empty origin.
	 */
	@Test
	public void testCheckNotCORSRequestTypeEmptyOrigin() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.NOT_CORS, requestType);
	}

	/**
	 * Negative test, when a CORS request arrives, with a null origin.
	 */
	@Test
	public void testCheckNotCORSRequestTypeNullOrigin() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn(null).anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.NOT_CORS, requestType);
	}

	/**
	 * Tests for failure, when a different domain is used, that's not in the
	 * allowed list of origins.
	 */
	@Test
	public void testCheckInvalidOrigin() {

		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("www.google.com").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSConfiguration corsConfiguration = CORSConfiguration.loadFromFilterConfig(TestFilterConfigs.getFilterConfig());
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
	}

	/**
	 * Tests for failure, when a different sub-domain is used, that's not in the
	 * allowed list of origins.
	 */
	@Test
	public void testCheckInvalidOriginNotAllowedSubdomain() {

		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("https://foo.ebay.com:8443").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSConfiguration corsConfiguration = CORSConfiguration.loadFromFilterConfig(TestFilterConfigs.getFilterConfig());
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
	}

	/**
	 * PUT is not an allowed request method.
	 */
	@Test
	public void testCheckInvalidRequestMethod() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("https://localhost.ebay.com:8443").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("PUT").anyTimes();
		EasyMock.replay(request);
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
	}

	/**
	 * "https://localhost.ebay.com:8443" is an allowed origin and
	 * "http://localhost.ebay.com:8443" is not, because protocol doesn't match
	 */
	@Test
	public void testCheckForProtocolVariance() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("http://localhost.ebay.com:8443").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSConfiguration corsConfiguration = CORSConfiguration.loadFromFilterConfig(TestFilterConfigs.getFilterConfig());
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
	}

	/**
	 * "https://localhost.ebay.com:8443" is an allowed origin and
	 * "https://localhost.ebay.com:8080" is not, because ports doesn't match
	 */
	@Test
	public void testCheckForPortVariance() {
		HttpServletRequest request = EasyMock
				.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getHeader(CORSRequestHeaders.ORIGIN))
				.andReturn("https://localhost.ebay.com:8080").anyTimes();
		EasyMock.expect(request.getMethod()).andReturn("POST").anyTimes();
		EasyMock.replay(request);
		CORSConfiguration corsConfiguration = CORSConfiguration.loadFromFilterConfig(TestFilterConfigs.getFilterConfig());
		CORSRequestType requestType = CORSRequestType.checkRequestType(request,
				corsConfiguration);

		Assert.assertEquals(CORSRequestType.INVALID_CORS, requestType);
	}

	/**
	 * Tests for failure, when an invalid {@link HttpServletRequest} is
	 * encountered.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCheckRequestTypeNull() {
		HttpServletRequest request = null;
		CORSRequestType.checkRequestType(request, corsConfiguration);
	}
}
