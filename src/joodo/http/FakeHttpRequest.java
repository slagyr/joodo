package joodo.http;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;

public class FakeHttpRequest implements HttpServletRequest
{
  public String authType;
  public List<Cookie> cookies = new LinkedList<Cookie>();
  public List<Pair> headers = new LinkedList<Pair>();
  public Hashtable<String, Object> attributes = new Hashtable<String, Object>();
  public Hashtable<String, String> parameters = new Hashtable<String, String>();
  public Hashtable<String, RequestDispatcher> dispatchers = new Hashtable<String, RequestDispatcher>();
  public String method;
  public String pathInfo;
  public String pathTranslated;
  public String contextPath;
  public String queryString;
  public String remoteUser;
  public String sessionId;
  public String uri;
  public StringBuffer url;
  public String path;
  public HttpSession session;
  public boolean requestedSessionIdValid;
  public boolean requestedSessionIdFromCookie;
  public boolean requestedSessionIdFromUrl;
  public String characterEncoding = "UTF-8";
  public int contentLength;
  public String contentType;
  public ServletInputStream inputStream;
  public String protocol;
  public String scheme;
  public String serverName;
  public int port;
  public BufferedReader reader;
  public String remoteAddr;
  public String remoteHost;
  public Locale locale;
  public Enumeration locales;
  public boolean secure;
  public String realPath;
  public int remotePort;
  public String localName;
  public String localAddr;
  public int localPort;


  public String getAuthType()
  {
    authType = null;
    return authType;
  }

  public Cookie[] getCookies()
  {
    return cookies.toArray(new Cookie[cookies.size()]);
  }

  public long getDateHeader(String s)
  {
    final String header = getHeader(s);
    try
    {
      if(header != null)
        return Http.dateFormat.parse(header).getTime();
      else
        return 0;
    }
    catch(ParseException e)
    {
      return 0;
    }
  }

  public String getHeader(String s)
  {
    for(Pair header : headers)
    {
      if(header.first != null && header.first.equals(s))
        return header.second;
    }
    return null;
  }

  public Enumeration getHeaders(String s)
  {
    Vector<String> result = new Vector<String>();
    for(Pair header : headers)
    {
      if(header.first != null && header.first.equals(s))
        result.add(header.second);
    }
    return result.elements();
  }

  public Enumeration getHeaderNames()
  {
    Vector<String> result = new Vector<String>();
    for(Pair header : headers)
      result.add(header.first);
    return result.elements();
  }

  public int getIntHeader(String s)
  {
    final String header = getHeader(s);
    try
    {
      if(header != null)
        return Integer.parseInt(header);
      else
        return -1;
    }
    catch(Exception e)
    {
      return -1;
    }
  }

  public String getMethod()
  {
    return method;
  }

  public String getPathInfo()
  {
    return pathInfo;
  }

  public String getPathTranslated()
  {
    return pathTranslated;
  }

  public String getContextPath()
  {
    return contextPath;
  }

  public String getQueryString()
  {
    return queryString;
  }

  public String getRemoteUser()
  {
    return remoteUser;
  }

  public boolean isUserInRole(String s)
  {
    return false;
  }

  public Principal getUserPrincipal()
  {
    return null;
  }

  public String getRequestedSessionId()
  {
    return sessionId;
  }

  public String getRequestURI()
  {
    return uri;
  }

  public StringBuffer getRequestURL()
  {
    return url;
  }

  public String getServletPath()
  {
    return path;
  }

  public HttpSession getSession(boolean b)
  {
    if(b)
      return getSession();
    else
      return session;
  }

  public HttpSession getSession()
  {
    if(session == null)
      session = new FakeHttpSession();
    return session;
  }

  public boolean isRequestedSessionIdValid()
  {
    return requestedSessionIdValid;
  }

  public boolean isRequestedSessionIdFromCookie()
  {
    return requestedSessionIdFromCookie;
  }

  public boolean isRequestedSessionIdFromURL()
  {
    return requestedSessionIdFromUrl;
  }

  public boolean isRequestedSessionIdFromUrl()
  {
    return requestedSessionIdFromUrl;
  }

  public Object getAttribute(String s)
  {
    return attributes.get(s);
  }

  public Enumeration getAttributeNames()
  {
    return attributes.keys();
  }

  public String getCharacterEncoding()
  {
    return characterEncoding;
  }

  public void setCharacterEncoding(String s) throws UnsupportedEncodingException
  {
    characterEncoding = s;
  }

  public int getContentLength()
  {
    return contentLength;
  }

  public String getContentType()
  {
    return contentType;
  }

  public ServletInputStream getInputStream() throws IOException
  {
    return inputStream;
  }

  public String getParameter(String s)
  {
    return parameters.get(s);
  }

  public Enumeration getParameterNames()
  {
    return parameters.keys();
  }

  public String[] getParameterValues(String s)
  {
    return parameters.values().toArray(new String[parameters.size()]);
  }

  public Map getParameterMap()
  {
    return parameters;
  }

  public String getProtocol()
  {
    return protocol;
  }

  public String getScheme()
  {
    return scheme;
  }

  public String getServerName()
  {
    return serverName;
  }

  public int getServerPort()
  {
    return port;
  }

  public BufferedReader getReader() throws IOException
  {
    return reader;
  }

  public String getRemoteAddr()
  {
    return remoteAddr;
  }

  public String getRemoteHost()
  {
    return remoteHost;
  }

  public void setAttribute(String s, Object o)
  {
    attributes.put(s, o);
  }

  public void removeAttribute(String s)
  {
    attributes.remove(s);
  }

  public Locale getLocale()
  {
    return locale;
  }

  public Enumeration getLocales()
  {
    return locales;
  }

  public boolean isSecure()
  {
    return secure;
  }

  public RequestDispatcher getRequestDispatcher(String s)
  {
    return dispatchers.get(s);
  }

  public String getRealPath(String s)
  {
    return realPath;
  }

  public int getRemotePort()
  {
    return remotePort;
  }

  public String getLocalName()
  {
    return localName;
  }

  public String getLocalAddr()
  {
    return localAddr;
  }

  public int getLocalPort()
  {
    return localPort;
  }

}
