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
 * Defines CORS response header constants, as per CORS specification. The
 * description of headers is taken from CORS specification verbatim, to keep the
 * context intact.
 * 
 * @author <a href="mailto:mosoni@ebay.com">Mohit Soni</a>
 * 
 */
public interface CORSResponseHeaders {
	/**
	 * The Access-Control-Allow-Origin header indicates whether a resource can
	 * be shared based by returning the value of the Origin request header in
	 * the response.
	 */
	String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	/**
	 * The Access-Control-Allow-Credentials header indicates whether the
	 * response to request can be exposed when the omit credentials flag is
	 * unset. When part of the response to a preflight request it indicates that
	 * the actual request can include user credentials.
	 */
	String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

	/**
	 * The Access-Control-Expose-Headers header indicates which headers are safe
	 * to expose to the API of a CORS API specification
	 */
	String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	/**
	 * The Access-Control-Max-Age header indicates how long the results of a
	 * preflight request can be cached in a preflight result cache.
	 */
	String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

	/**
	 * The Access-Control-Allow-Methods header indicates, as part of the
	 * response to a preflight request, which methods can be used during the
	 * actual request.
	 */
	String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

	/**
	 * The Access-Control-Allow-Headers header indicates, as part of the
	 * response to a preflight request, which header field names can be used
	 * during the actual request.
	 */
	String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
}
