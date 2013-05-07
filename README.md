## CORS Filter

CORS (Cross Origin Resource Sharing) is a mechanism supported by W3C to enable cross domain requests, in web-browsers. CORS requires support from both browser and server, to work.

### How to use CORS ?
Include cors-filter jar in your classpath. And, add filter configuration to your web.xml. Example:
```xml
<filter>
    <filter-name>CORS Filter</filter-name>
    <filter-class>org.apache.catalina.filters.CORSFilter</filter-class>
    <init-param>
      <description>A comma separated list of allowed origins. Note: An '*' cannot be used for an allowed origin when using credentials.</description>
      <param-name>cors.allowed.origins</param-name>
      <param-value>http://localhost:8080,http://localhost.ebay.com:8080</param-value>
    </init-param>
    <init-param>
      <description>A comma separated list of HTTP verbs, using which a CORS request can be made.</description>
      <param-name>cors.allowed.methods</param-name>
      <param-value>GET,POST,HEAD,OPTIONS,PUT</param-value>
    </init-param>
    <init-param>
      <description>A comma separated list of allowed headers when making a non simple CORS request.</description>
      <param-name>cors.allowed.headers</param-name>
      <param-value>Content-Type,X-Requested-With</param-value>
    </init-param>
    <init-param>
      <description>A comma separated list non-standard response headers that will be exposed to XHR2 object.</description>
      <param-name>cors.exposed.headers</param-name>
      <param-value></param-value>
    </init-param>
    <init-param>
      <description>A flag that suggests if CORS is supported with cookies</description>
      <param-name>cors.support.credentials</param-name>
      <param-value>true</param-value>
    </init-param>
    <init-param>
      <description>Indicates how long (in seconds) the results of a preflight request can be cached in a preflight result cache.</description>
      <param-name>cors.preflight.maxage</param-name>
      <param-value>10</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CORS Filter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

### Background
[Same origin policy](http://en.wikipedia.org/wiki/Same_origin_policy) in browsers prevents XMLHttpRequest to make a request to a resource, on a URL that's different from the origin URL.

###  How does CORS work ?
CORS requires support from both browsers and servers to work. On a very high level:
* According to CORS specification, a browser that supports CORS, sends a 'Origin' header along, that helps resource server to determine if a request is a CORS request.
* A server, if it supports CORS requests, sends an 'Access-Control-Allow-Origin' header, in response to a CORS request.
* 'Access-Control-Allow-Origin' header helps browser runtime, to make a decision to allow a cross-domain request to complete.

#### CORS and Browsers
* [Browsers that supports CORS](http://caniuse.com/cors), do it with either an [XMLHttpRequest2](http://www.w3.org/TR/XMLHttpRequest/) object (for Firefox, Chrome, Safari, Opera, etc) or an [XDomainRequest](http://msdn.microsoft.com/en-us/library/ie/cc288060.aspx) object for IE family (partially supported for IE8 and IE9; [fully supported in IE10](http://blogs.msdn.com/b/ie/archive/2012/02/09/cors-for-xhr-in-ie10.aspx)). 
* Client side JS can determine browser's CORS support by determining the presence of either XMLHttpRequest2 object or XDomainRequest.
* By default, browser will prevent an XHR2 request to send cookies with the request. To send cookies with XHR2 request, set 'withCredentials' flag to true. Ex: **xhr.withCredentials = true**;

#### IE and CORS (Yes, IE gets a special section)
* XDomainRequest on IE8 and IE9, doesn't allow cookies to be sent, along with requests.
* For authenticated CORS requests, use session IDs.
* For older IE browsers, please fallback to JSONP or iframes or (some custom rain-dance that worked once for someone)
* [More IE restrictions, limitations and workarounds](http://blogs.msdn.com/b/ieinternals/archive/2010/05/13/xdomainrequest-restrictions-limitations-and-workarounds.aspx).
* Please see [Client side CORS](https://github.scm.corp.ebay.com/mosoni/cors/wiki/Client-Side-CORS) for a fallback mechanism that works on IE.

#### CORS and Servers
* A CORS request can be identified by a server that supports CORS, by checking for the presence of 'Origin' header.
* A server that supports CORS, is required to reply with a 'Access-Control-Allow-Origin' header. Ex: 
  * Access-Control-Allow-Origin: * 
  * Access-Control-Allow-Origin: *.ebay.com
  * Access-Control-Allow-Origin: deals.ebay.com
* For cookies to work with CORS, server must respond with 'Access-Control-Allow-Credentials' header. Ex: Access-Control-Allow-Credentials: true. 
  * Note: For cookies to work holistically, Access-Control-Allow-Credentials header is required alongwith XHR2's withCredentials property
  * This header shouldn't be present in the response, if the cookies are not required.

#### CORS and Pre-flight request
To support CORS requests other than simple requests, a pre-flight request is initiated by browser, to query target server for it's support of not-simple-requests. A pre-flight request is sent as an HTTP OPTIONS request.

#### Request Filter
A CORS requests filter is now available. The artifact is package with a Feature and a FeatureActiviator. It is injected in inbound filter chain. Allowed cross-domain origins will be loaded from the config file, along with other configuration.              
![CORS Flowchart](https://github.scm.corp.ebay.com/mosoni/cors/wiki/images/cors_filter_flowchart.png)

Here's what a CORS request filter will do:

1. Checks the type of request (can be simple, pre-flight, not-cors, or an invalid cors)
1. Decorate HttpServletRequest object with CORS attributes
1. If the request is a Simple CORS request
   1. Add CORS response headers
      1. Access-Control-Allow-Origin: <origin-from-which-request-originates>
      1. Access-Control-Allow-Credentials: true
      1. Access-Control-Expose-Headers: <if-any>
   1. Set request attributes
   1. Forward request to filter chain
1. If it's a preflight CORS request:
   1. Reply back with pre-flight headers and return
      1. Access-Control-Allow-Origin: <origin-from-which-request-originates>
      1. Access-Control-Allow-Methods: GET, POST, OPTIONS, HEAD
      1. Access-Control-Allow-Credentials: true
      1. Access-Control-Allow-Headers: <if-any>
1. If not a CORS request, 
   1. forward request to filter chain
1. If an invalid CORS request
   1. Set attributes
   1. throw ServletException
   1. Request will be redirected to AbortServlet

### CORS Configuration
CORS filter can be configured from an application via following properties:

```python
# CORS Configuration
# A comma separated list of allowed origins. Note: An '*' cannot be used for an allowed origin when using credentials.
cors.allowed.origins=https://localhost.ebay.com:8443,https://deals.ebay.com:8443

# A comma separated list of HTTP verbs, using which a CORS request can be made.
cors.allowed.methods=GET,POST,HEAD,OPTIONS

# A comma separated list of allowed headers when making a non simple CORS request.
cors.allowed.headers=Content-Type
 
# A comma separated list non-standard response headers that will be exposed to XHR2 object.
cors.exposed.headers=

# A flag that suggests if CORS is supported with cookies
cors.support.credentials=true

# Indicates how long the results of a preflight request can be cached in a preflight result cache.
cors.preflight.maxage=100
```

### Adding information about the CORS in HttpServletRequest
CORS filter adds information about a CORS request, in the HttpServletRequest object, for consumption downstream. Following attributes are set:

* **cors.isCorsRequest**: Flag to determine if a request is a CORS request
* **cors.origin**: Origin URL
* **cors.requestType**: simple or preflight or not_cors or invalid_cors

### Notes
* For authenticated CORS request, we cannot use * in the Allowed Origin header. A valid URL for an authenticated CORS resource should not contain '*'.
* When specifying an allowed origin, for authenticated resources, specifying protocol (http / https) is must, while returning an "Access-Control-Allow-Origin" header.
* In CORS configuration's allowed origin list, filter all the URLs that contains '*' and log at WARN that an invalid URL was requested to be added to allowed origin list - TODO
* When cookies are sent with request for a resource that requires authentication, returning a "Access-Control-Allow-Credentials" header is a must. Otherwise, browser will drop the request.

## CORS Class Diagram
![CORS Class Diagram](https://github.scm.corp.ebay.com/mosoni/cors/wiki/images/cors_filter_class_diagram.png)

## References
Here's a list of good resources to start with CORS:

* [HTML5 Rocks CORS Tutorial](http://www.html5rocks.com/en/tutorials/cors/)
* [W3C's CORS Spec](http://www.w3.org/TR/cors/)
* [Enable CORS](http://enable-cors.org)
* [List of Response headers supports by CORS Spec](http://www.w3.org/TR/cors/#syntax)
* [Other implementations](http://software.dzhuvinov.com/cors-filter.html)