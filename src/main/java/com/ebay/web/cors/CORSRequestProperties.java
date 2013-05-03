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

/**
 * The request attributes added to a CORS request.
 *
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 *
 */
public interface CORSRequestProperties {
    /** The prefix to a CORS request attribute. */
    String PREFIX = "cors.";

    /** Attribute that contains the origin of the request. */
    String ORIGIN = PREFIX + "origin";

    /** Boolean value, suggesting if the request is a CORS request or not. */
    String IS_CORS_REQUEST = PREFIX + "isCorsRequest";

    /** Type of CORS request, of type {@link CORSRequestType}. */
    String REQUEST_TYPE = PREFIX + "requestType";
}
