## CORS Filter
[![Build Status](https://travis-ci.org/eBay/cors-filter.png?branch=master)](https://travis-ci.org/eBay/cors-filter)

* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Using CORS Filter in Your Maven Project](#using-cors-filter-in-your-maven-project)
* [Configuring CORS Filter](#configuring-cors-filter)
* [Information added by CORS Filter about request in HttpServletRequest object](#information-added-by-cors-filter-about-request-in-httpservletrequest-object)
* [Background on CORS](#background-on-cors)
* [How it works](#how-it-works)
* [References](#references)

### Introduction
[CORS](http://www.w3.org/TR/cors/) (Cross Origin Resource Sharing) is a mechanism to enable cross origin requests. W3C defines the standards that enable this mechanism. CORS requires support from both the server and the browser in order to work. This is a Java Servlet Filter implementation of server-side CORS for Java web containers such as Apache Tomcat. Salient features:
* Easy to integrate and use - enable CORS capability with a few lines in your web.xml!
* Protects against CRLF injection / response splitting attacks.

Here's a link to a demo application running on google app engine: http://corsdemo.appspot.com/

### Quick Start
Include cors-filter-x.x.x.jar in your web-application's classpath. Then, add the filter configuration to your web.xml. Example:
```xml
  <filter>
    <filter-name>CORS Filter</filter-name>
    <filter-class>org.ebaysf.web.cors.CORSFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>CORS Filter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

### Using CORS Filter in Your Maven Project
Add the cors-filter as a dependency:
```xml
  <dependency>
    <groupId>org.ebaysf.web</groupId>
    <artifactId>cors-filter</artifactId>
    <version>1.0.0</version>
  </dependency>
```

And, add the filter configuration in web.xml as demonstrated in the Quick Start section

### Configuring CORS Filter
The minimal configuration required to use CORS Filter is:
```xml
  <filter>
    <filter-name>CORS Filter</filter-name>
    <filter-class>org.ebaysf.web.cors.CORSFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>CORS Filter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

The table below lists various parameters that can be used to configure CORS Filter. If a parameter is not provided a default (documented below) is used.

|param-name              |description                                                                                                  |
|------------------------|-------------------------------------------------------------------------------------------------------------|
|cors.allowed.origins    | A list of origins that are allowed to access the resource. A '*' can be specified to enable access to resource from any origin. Otherwise, a whitelist of comma separated origins can be provided. Ex: http://www.w3.org, https://www.apache.org. **Defaults:** * (Any origin is allowed to access the resource).|
|cors.allowed.methods    | A comma separated list of HTTP methods that can be used to access the resource, using cross-origin requests. These are the methods which will also be included as part of 'Access-Control-Allow-Methods' header in a pre-flight response. Ex: GET,POST. **Defaults:** GET,POST,HEAD,OPTIONS|
|cors.allowed.headers    | A comma separated list of request headers that can be used when making an actual request. These header will also be returned as part of 'Access-Control-Allow-Headers' header in a pre-flight response. Ex: Origin,Accept. **Defaults:** Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers|
|cors.exposed.headers    | A comma separated list of headers other than the simple response headers that browsers are allowed to access. These are the headers which will also be included as part of 'Access-Control-Expose-Headers' header in the pre-flight response. Ex: X-CUSTOM-HEADER-PING,X-CUSTOM-HEADER-PONG. **Default:** None |
|cors.preflight.maxage   | The amount of seconds, browser is allowed to cache the result of the pre-flight request. This will be included as part of 'Access-Control-Max-Age' header in the pre-flight response. A negative value will prevent CORS Filter from adding this response header from pre-flight response. **Defaults:** 1800 |
|cors.support.credentials| A flag that indicates whether the resource supports user credentials. This flag is exposed as part of 'Access-Control-Allow-Credentials' header in a pre-flight response. It helps browser determine whether or not an actual request can be made using credentials. **Defaults:** true |
|cors.logging.enabled    | A flag to control logging to container logs. **Defaults:** false|
|cors.request.decorate   | A flag to control if the request should be decorated or not. **Defaults:** true|

To override filter configuration defaults, specify them in the init-params while configuring the filter in web.xml. Example:
```xml
  <filter>
    <filter-name>CORS Filter</filter-name>
    <filter-class>org.ebaysf.web.cors.CORSFilter</filter-class>
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
      <param-value>Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers</param-value>
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
      <description>A flag to control logging</description>
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

### Information added by CORS Filter about request in HttpServletRequest object
CORS Filter adds information about a CORS request, in the HttpServletRequest object, for consumption downstream. Following attributes are set:

* **cors.isCorsRequest**: Flag to determine if a request is a CORS request.
* **cors.request.origin**: Origin URL.
* **cors.request.type**: Type of CORS request. Possible values: 
  * SIMPLE: A request which is not preceded by a pre-flight request.
  * ACTUAL: A request which is preceded by a pre-flight request.
  * PRE_FLIGHT: A pre-flight request.
  * NOT_CORS: A normal same-origin request.
  * INVALID_CORS: A cross-origin request, which is invalid.
* **cors.request.headers**: Request headers sent as 'Access-Control-Request-Headers' header, for a pre-flight request.

To prevent CORS Filter from setting above attributes, set 'cors.request.decorate' init-param to false.

### Background on CORS
A cross origin request, is a HTTP request for a resource that serves from a different origin than the origin of the application that is requesting the resource. For example, a request originating from a page served from http://www.ebay.com, to a resource on http://www.google.com.

[Same origin policy](http://en.wikipedia.org/wiki/Same_origin_policy) in browsers prevents XMLHttpRequest to make a request to a resource, on a origin that's different from the source origin.

By enabling CORS support on server side, a resource can support cross-origin requests in a way that's supported by W3C standards. For more details, please refer: [W3C CORS](http://www.w3.org/TR/cors/)

### How it works
![CORS Flowchart](https://github.com/ebay/cors-filter/raw/master/cors-flowchart.png)

## References
Here's a list of good resources to start with CORS:

* [W3C's CORS Spec](http://www.w3.org/TR/cors/)
* [HTML5 Rocks CORS Tutorial](http://www.html5rocks.com/en/tutorials/cors/)
* [Mozilla's HTTP access control (CORS)](https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS)
* [Mozilla's Server-Side Access Control](https://developer.mozilla.org/en-US/docs/Server-Side_Access_Control)
* [Enable CORS](http://enable-cors.org)
* [Other](http://dev.eclipse.org/svnroot/rt/org.eclipse.jetty/jetty/trunk/jetty-servlets/src/main/java/org/eclipse/jetty/servlets/CrossOriginFilter.java) [implementations](http://software.dzhuvinov.com/cors-filter.html)
* [Same origin policy](http://en.wikipedia.org/wiki/Same_origin_policy)

A list of good security resources around CORS:
* [OWASP HTML 5 Security Cheatsheet and CORS](https://www.owasp.org/index.php/HTML5_Security_Cheat_Sheet#Cross_Origin_Resource_Sharing)
* [OWASP CORS Preflight Scrutiny](https://www.owasp.org/index.php/CORS_RequestPreflighScrutiny)
* [OWASP CORS Origin Scrutiny](https://www.owasp.org/index.php/CORS_OriginHeaderScrutiny)
* [HTML 5 Security Wiki and CORS](https://code.google.com/p/html5security/wiki/CrossOriginRequestSecurity)
