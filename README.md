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
|cors.allowed.origins    | A list of origins that are allowed access to the resource. If any origin is allowed to access the resource, then a '*' can be specified. Otherwise, a whitelist of origins (comma separated) should be specified. Ex: http://www.w3.org, https://www.mysite.com. By default, any origin ('*') is allowed to access the resource.|

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
