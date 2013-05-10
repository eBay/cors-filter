package com.ebay.web.cors;

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
        // TODO Auto-generated method stub
        return null;
    }

    public ServletContext getContext(String uripath) {
        // TODO Auto-generated method stub
        return null;
    }

    public int getMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getMimeType(String file) {
        // TODO Auto-generated method stub
        return null;
    }

    public Set getResourcePaths(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getResource(String path) throws MalformedURLException {
        // TODO Auto-generated method stub
        return null;
    }

    public InputStream getResourceAsStream(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Servlet getServlet(String name) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServlets() {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServletNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public void log(String msg) {
        // TODO Auto-generated method stub
        
    }

    public void log(Exception exception, String msg) {
        // TODO Auto-generated method stub
        
    }

    public void log(String message, Throwable throwable) {
        // TODO Auto-generated method stub
        
    }

    public String getRealPath(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServerInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getInitParameter(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getInitParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAttribute(String name, Object object) {
        // TODO Auto-generated method stub
        
    }

    public void removeAttribute(String name) {
        // TODO Auto-generated method stub
        
    }

    public String getServletContextName() {
        // TODO Auto-generated method stub
        return null;
    }

}
