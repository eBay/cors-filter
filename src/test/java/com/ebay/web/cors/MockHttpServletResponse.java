package com.ebay.web.cors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MockHttpServletResponse implements HttpServletResponse {
    List headerNames = new ArrayList();
    List headerValues = new ArrayList();
    PrintWriter pw;
    int status;

    public String getCharacterEncoding() {

        return null;
    }

    public String getContentType() {

        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {

        return null;
    }

    public PrintWriter getWriter() throws IOException {
        if (pw == null) {
            pw = new PrintWriter(new StringWriter());
        }
        return pw;
    }

    public void setCharacterEncoding(String charset) {

    }

    public void setContentLength(int len) {

    }

    public void setContentType(String type) {

    }

    public void setBufferSize(int size) {

    }

    public int getBufferSize() {

        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {

        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale loc) {

    }

    public Locale getLocale() {

        return null;
    }

    public void addCookie(Cookie cookie) {

    }

    public boolean containsHeader(String name) {

        return false;
    }

    public String encodeURL(String url) {

        return null;
    }

    public String encodeRedirectURL(String url) {

        return null;
    }

    public String encodeUrl(String url) {

        return null;
    }

    public String encodeRedirectUrl(String url) {

        return null;
    }

    public void sendError(int sc, String msg) throws IOException {

    }

    public void sendError(int sc) throws IOException {

    }

    public void sendRedirect(String location) throws IOException {

    }

    public void setDateHeader(String name, long date) {

    }

    public void addDateHeader(String name, long date) {

    }

    public String getHeader(String name) {
        int index = headerNames.indexOf(name);
        if (index != -1) {
            return (String) headerValues.get(index);
        }
        return null;
    }

    public void setHeader(String name, String value) {
        int index = headerNames.indexOf(name);
        if (index != -1) {
            headerValues.set(index, value);
        } else {
            headerNames.add(name);
            headerValues.add(value);
        }
    }

    public void addHeader(String name, String value) {
        headerNames.add(name);
        headerValues.add(value);
    }

    public void setIntHeader(String name, int value) {

    }

    public void addIntHeader(String name, int value) {

    }

    public void setStatus(int sc) {
        this.status = sc;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int sc, String sm) {

    }

}
