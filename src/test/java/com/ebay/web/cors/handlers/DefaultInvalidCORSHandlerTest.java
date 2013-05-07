package com.ebay.web.cors.handlers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import com.ebay.web.cors.CORSFilter;

public class DefaultInvalidCORSHandlerTest {

    @Test(expected = ServletException.class)
    public void test() throws IOException, ServletException {
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
}
