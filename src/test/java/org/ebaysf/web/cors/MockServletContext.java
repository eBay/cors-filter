/**
 * Copyright 2012-2013 eBay Software Foundation, All Rights Reserved.
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
package org.ebaysf.web.cors;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MockServletContext implements ServletContext {

    public String getContextPath() {
        throw new RuntimeException("Not implemented");
    }

    public ServletContext getContext(String uripath) {
        throw new RuntimeException("Not implemented");
    }

    public int getMajorVersion() {
        throw new RuntimeException("Not implemented");
    }

    public int getMinorVersion() {
        throw new RuntimeException("Not implemented");
    }

    public String getMimeType(String file) {
        throw new RuntimeException("Not implemented");
    }

    public Set getResourcePaths(String path) {
        throw new RuntimeException("Not implemented");
    }

    public URL getResource(String path) throws MalformedURLException {
        throw new RuntimeException("Not implemented");
    }

    public InputStream getResourceAsStream(String path) {
        throw new RuntimeException("Not implemented");
    }

    public RequestDispatcher getRequestDispatcher(String path) {

        throw new RuntimeException("Not implemented");
    }

    public RequestDispatcher getNamedDispatcher(String name) {

        throw new RuntimeException("Not implemented");
    }

    public Servlet getServlet(String name) throws ServletException {

        throw new RuntimeException("Not implemented");
    }

    public Enumeration getServlets() {

        throw new RuntimeException("Not implemented");
    }

    public Enumeration getServletNames() {

        throw new RuntimeException("Not implemented");
    }

    public void log(String msg) {
        // NOOP
    }

    public void log(Exception exception, String msg) {
        // NOOP
    }

    public void log(String message, Throwable throwable) {
        // NOOP
    }

    public String getRealPath(String path) {

        throw new RuntimeException("Not implemented");
    }

    public String getServerInfo() {

        throw new RuntimeException("Not implemented");
    }

    public String getInitParameter(String name) {

        throw new RuntimeException("Not implemented");
    }

    public Enumeration getInitParameterNames() {

        throw new RuntimeException("Not implemented");
    }

    public Object getAttribute(String name) {

        throw new RuntimeException("Not implemented");
    }

    public Enumeration getAttributeNames() {

        throw new RuntimeException("Not implemented");
    }

    public void setAttribute(String name, Object object) {
        throw new RuntimeException("Not implemented");
    }

    public void removeAttribute(String name) {
        throw new RuntimeException("Not implemented");
    }

    public String getServletContextName() {

        throw new RuntimeException("Not implemented");
    }

}
