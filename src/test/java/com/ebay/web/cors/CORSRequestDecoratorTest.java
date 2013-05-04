package com.ebay.web.cors;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

public class CORSRequestDecoratorTest {
    /**
     * The allowed origin for this test.
     */
    private static final String HTTPS_LOCALHOST_EBAY_COM_8443 = "https://localhost.ebay.com:8443";

    @Test(expected = IllegalArgumentException.class)
    public void testDecorateCORSPropertiesNullRequestNullCORSRequestType() {
        CORSRequestDecorator.decorateCORSProperties(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecorateCORSPropertiesNullRequestValidCORSRequestType() {
        CORSRequestDecorator.decorateCORSProperties(null, CORSRequestType.SIMPLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecorateCORSPropertiesValidRequestNullRequestType() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.replay(request);
        CORSRequestDecorator.decorateCORSProperties(request, null);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeSimple() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN)).andReturn(HTTPS_LOCALHOST_EBAY_COM_8443)
                .anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN, HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE, CORSRequestType.SIMPLE.getType());
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        CORSRequestDecorator.decorateCORSProperties(request, CORSRequestType.SIMPLE);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypePreFlight() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN)).andReturn(HTTPS_LOCALHOST_EBAY_COM_8443)
                .anyTimes();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, true);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN, HTTPS_LOCALHOST_EBAY_COM_8443);
        EasyMock.expectLastCall();
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE, CORSRequestType.PRE_FLIGHT.getType());
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        CORSRequestDecorator.decorateCORSProperties(request, CORSRequestType.PRE_FLIGHT);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeNotCORS() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        request.setAttribute(CORSFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST, false);
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        CORSRequestDecorator.decorateCORSProperties(request, CORSRequestType.NOT_CORS);
    }

    @Test
    public void testDecorateCORSPropertiesCORSRequestTypeInvalidCORS() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.replay(request);
        CORSRequestDecorator.decorateCORSProperties(request, CORSRequestType.INVALID_CORS);
    }
}
