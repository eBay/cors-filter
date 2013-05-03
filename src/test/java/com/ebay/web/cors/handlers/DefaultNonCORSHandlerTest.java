package com.ebay.web.cors.handlers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import com.ebay.web.cors.handlers.DefaultNonCORSHandler;

public class DefaultNonCORSHandlerTest {

    @Test
    public void test() throws IOException, ServletException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
        FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);
        filterChain.doFilter(request, response);
        EasyMock.expectLastCall();
        EasyMock.replay(request);
        EasyMock.replay(response);
        EasyMock.replay(filterChain);

        DefaultNonCORSHandler handler = new DefaultNonCORSHandler();
        handler.handle(request, response, filterChain);
    }
}
