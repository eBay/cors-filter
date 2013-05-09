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

### Adding information about the CORS in HttpServletRequest
CORS filter adds information about a CORS request, in the HttpServletRequest object, for consumption downstream. Following attributes are set:

* **cors.isCorsRequest**: Flag to determine if a request is a CORS request
* **cors.origin**: Origin URL
* **cors.requestType**: simple or preflight or not_cors or invalid_cors
* **cors.request.headers**: Request headers sent as 'Access-Control-Request-Headers' header, for pre-flight request.

## References
Here's a list of good resources to start with CORS:

* [HTML5 Rocks CORS Tutorial](http://www.html5rocks.com/en/tutorials/cors/)
* [W3C's CORS Spec](http://www.w3.org/TR/cors/)
* [Enable CORS](http://enable-cors.org)
* [List of Response headers supports by CORS Spec](http://www.w3.org/TR/cors/#syntax)
* [Other implementations](http://software.dzhuvinov.com/cors-filter.html)
