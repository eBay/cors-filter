## CORS Filter

CORS (Cross Origin Resource Sharing) is a mechanism supported by W3C to enable cross domain requests, in web-browsers. CORS requires support from both browser and server, to work.

### How to use CORS ?
Include cors-filter.jar in your classpath. And, add filter configuration to your web.xml. Example:
```xml
  <filter>
    <filter-name>CORS Filter</filter-name>
    <filter-class>com.ebay.web.cors.CORSFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>CORS Filter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

### Using CORS with Maven
Coming soon.

### Filter configuration parameters
|param-name              |description                                                                                                  |
|------------------------|-------------------------------------------------------------------------------------------------------------|
|cors.allowed.origins    | A list of origins that are allowed to access the resource. A '*' can be specified to enable access to resource from any origin. Otherwise, a whitelist of comma separated origins should be specified. Ex: http://www.w3.org, https://www.apache.org. **Defaults:** Any origin ('*') is allowed to access the resource.|
|cors.allowed.methods    | A comma separated list of HTTP methods that can be used to access the resource, using cross-origin requests. These are the methods which will be included as part of 'Access-Control-Allow-Methods' header in a pre-flight response. Ex: GET,POST. **Defaults:** GET,POST,HEAD,OPTIONS|
|cors.allowed.headers    | A comma separated list of request headers that can be used when making an actual request. These are returned as part of 'Access-Control-Allow-Headers' header in a pre-flight response. Ex: Origin,Accept. **Defaults:** Origin,Accept,X-Requested-With,Content-Type|
|cors.exposed.headers    | A comma separated list of headers other than the simple response headers that browsers are allowed to access. These are the headers which will be included as part of 'Access-Control-Expose-Headers' header in a pre-flight response. Ex: X-CUSTOM-HEADER-PING,X-CUSTOM-HEADER-PONG. **Default:** None |
|cors.preflight.maxage   | The amount of seconds, browser is allowed to cache the result of the pre-flight request. **Defaults:** 1800 |
|cors.support.credentials| A flag that indicates whether the resource supports user credentials. This flag is exposed as part of 'Access-Control-Allow-Credentials' header in a pre-flight response. It helps browser determine whether or not an actual request can be made using credentials. **Defaults:** true |
|cors.logging.enabled    | A flag to control logging to container logs. **Defaults:** false|

To override filter configuration defaults, one can specify init-params while configuring filter in web.xml. Example:
```xml
<filter>
    <filter-name>CORS Filter</filter-name>
    <filter-class>com.ebay.web.cors.CORSFilter</filter-class>
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
      <param-value>Content-Type,X-Requested-With,accept,Origin</param-value>
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
      <description>A flag control logging</description>
      <param-name>cors.logging.enabled</param-name>
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

### Information added by CORS filter about request in HttpServletRequest object
CORS filter adds information about a CORS request, in the HttpServletRequest object, for consumption downstream. Following attributes are set:

* **cors.isCorsRequest**: Flag to determine if a request is a CORS request
* **cors.origin**: Origin URL
* **cors.requestType**: simple or preflight or not_cors or invalid_cors
* **cors.request.headers**: Request headers sent as 'Access-Control-Request-Headers' header, for pre-flight request.

## References
Here's a list of good resources to start with CORS:

* [W3C's CORS Spec](http://www.w3.org/TR/cors/)
* [HTML5 Rocks CORS Tutorial](http://www.html5rocks.com/en/tutorials/cors/)
* [Mozilla's HTTP access control (CORS)](https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS)
* [Mozilla's Server-Side Access Control](https://developer.mozilla.org/en-US/docs/Server-Side_Access_Control)
* [Enable CORS](http://enable-cors.org)
* [Other implementations](http://software.dzhuvinov.com/cors-filter.html)

A list of good security resources around CORS:
* [OWASP HTML 5 Security Cheatsheet and CORS](https://www.owasp.org/index.php/HTML5_Security_Cheat_Sheet#Cross_Origin_Resource_Sharing)
* [OWASP CORS Preflight Scrutiny](https://www.owasp.org/index.php/CORS_RequestPreflighScrutiny)
* [OWASP CORS Origin Scrutiny](https://www.owasp.org/index.php/CORS_OriginHeaderScrutiny)
* [HTML 5 Security Wiki and CORS](https://code.google.com/p/html5security/wiki/CrossOriginRequestSecurity)