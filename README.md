## CORS Filter

CORS (Cross Origin Resource Sharing) is a mechanism supported by W3C to enable cross domain requests, in web-browsers. CORS requires support from both browser and server, to work.

### How to use CORS ?
Include cors-filter jar in your classpath. And, add filter configuration to your web.xml. Example:
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

### Filter configuration parameters
|param-name              |description                                                                                                  |
|------------------------|-------------------------------------------------------------------------------------------------------------|
|cors.allowed.origins    | A list of origins that are allowed to access the resource. If any origin is to be allowed to access the resource, then a '*' can be specified. Otherwise, a whitelist of comma separated origins should be specified. Ex: http://www.w3.org, https://www.apache.org. **Defaults:** Any origin ('*') is allowed to access the resource.|
|cors.allowed.methods    | A comma separated list of HTTP methods that can be used to access the resource, using cross-origin requests. Ex: GET,POST. **Defaults:** GET,POST,HEAD,OPTIONS|
|cors.allowed.headers    | A comma separated list of request headers that are supported for cross-origin requests. Ex: Origin,Accept. **Defaults:** Origin,Accept,X-Requested-With,Content-Type|
|cors.exposed.headers    | A comma separated list of header field names other than the simple response headers that the resource can expose. If included in pre-flight response, the browser will allow client code to read that. Ex: X-CUSTOM-HEADER-PING,X-CUSTOM-HEADER-PONG. **Default:** None |
|cors.preflight.maxage   | The amount of seconds, browser is allowed to cache the result of the pre-flight request. **Defaults:** 1800 |
|cors.support.credentials| A flag that indicates whether the resource supports user credentials. **Defaults:** false |
|cors.logging.enabled    | A flag to control logging to container logs. **Defaults:** false|

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