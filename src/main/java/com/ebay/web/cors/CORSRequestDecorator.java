/**
 * Copyright 2012-2013 eBay Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ebay.web.cors;

import javax.servlet.http.HttpServletRequest;

/**
 * Decorates a {@link HttpServletRequest} with CORS specific attributes. The
 * request attributes added are listed in {@link CORSRequestProperties}.
 *
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 *
 */
public final class CORSRequestDecorator {
	
	private CORSRequestDecorator() {
		
	}
    /**
     * Decorates the {@link HttpServletRequest}, with CORS attributes.
     *
     * @param request
     *            The {@link HttpServletRequest} object.
     * @param corsRequestType
     *            The {@link CORSRequestType} object.
     */
    public static void decorateCORSProperties(final HttpServletRequest request, final CORSRequestType corsRequestType) {
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest object is null");
        }

        if (corsRequestType == null) {
            throw new IllegalArgumentException("CORSRequestType object is null");
        }

        switch (corsRequestType) {
            case SIMPLE:
                request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, true);
                request.setAttribute(CORSRequestProperties.ORIGIN, request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN));
                request.setAttribute(CORSRequestProperties.REQUEST_TYPE, corsRequestType.getType());
                break;
            case PRE_FLIGHT:
                request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, true);
                request.setAttribute(CORSRequestProperties.ORIGIN, request.getHeader(CORSFilter.REQUEST_HEADER_ORIGIN));
                request.setAttribute(CORSRequestProperties.REQUEST_TYPE, corsRequestType.getType());
                break;
            case NOT_CORS:
                request.setAttribute(CORSRequestProperties.IS_CORS_REQUEST, false);
                break;
            default:
                // Don't set any attributes
                break;
        }
    }
}
