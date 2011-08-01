package joodo.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FakeHttpResponse implements HttpServletResponse
{
  public List<Cookie> cookies = new LinkedList<Cookie>();
  public List<Pair> headers = new LinkedList<Pair>();
  public int status;
  public String statusMessage;
  public boolean committed;
  public String characterEncoding = "UTF-8";
  public String contentType;
  public ServletOutputStream outputStream;
  public PrintWriter writer;
  public int bufferSize;
  public Locale locale;

  public void addCookie(Cookie cookie)
  {
    cookies.add(cookie);
  }

  public boolean containsHeader(String s)
  {
    for(Pair header : headers)
    {
      if(header.first != null && header.first.equals(s))
        return true;
    }
    return false;
  }

  public String encodeURL(String s)
  {
    try
    {
      return URLEncoder.encode(s, characterEncoding);
    }
    catch(UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public String encodeRedirectURL(String s)
  {
    return encodeUrl(s);
  }

  public String encodeUrl(String s)
  {
    return encodeURL(s);
  }

  public String encodeRedirectUrl(String s)
  {
    return encodeUrl(s);
  }

  public void sendError(int i, String s) throws IOException
  {
    status = i;
    statusMessage = s;
    committed = true;
  }

  public void sendError(int i) throws IOException
  {
    status = i;
    committed = true;
  }

  public void sendRedirect(String s) throws IOException
  {
    status = 302;
    setHeader("Location", s);
    committed = true;
  }

  public void setDateHeader(String s, long l)
  {
    removeHeader(s);
    addDateHeader(s, l);
  }

  public void removeHeader(String s)
  {
    List<Pair> badEggs = new LinkedList<Pair>();
    for(Pair header : headers)
    {
      if(header.first != null && header.first.equals(s))
        badEggs.add(header);
    }
    for(Pair badEgg : badEggs)
    {
      headers.remove(badEgg);
    }
  }

  public void addDateHeader(String s, long l)
  {
    Date date = new Date();
    date.setTime(l);
    addHeader(s, Http.dateFormat.format(date));
  }

  public void setHeader(String s, String s1)
  {
    removeHeader(s);
    addHeader(s, s1);
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

  public void addHeader(String s, String s1)
  {
    headers.add(new Pair(s, s1));
  }

  public void setIntHeader(String s, int i)
  {
    removeHeader(s);
    addIntHeader(s, i);
  }

  public void addIntHeader(String s, int i)
  {
    addHeader(s, "" + i);
  }

  public void setStatus(int i)
  {
    status = i;
  }

  public void setStatus(int i, String s)
  {
    status = i;
    statusMessage = s;
  }

  public String getCharacterEncoding()
  {
    return characterEncoding;
  }

  public String getContentType()
  {
    return getHeader("Content-Type");
  }

  public ServletOutputStream getOutputStream() throws IOException
  {
    return outputStream;
  }

  public PrintWriter getWriter() throws IOException
  {
    return writer;
  }

  public void setCharacterEncoding(String s)
  {
    characterEncoding = s;
  }

  public void setContentLength(int i)
  {
    setIntHeader("Content-Length", i);
  }

  public void setContentType(String s)
  {
    setHeader("Content-Type", s);
  }

  public void setBufferSize(int i)
  {
    bufferSize = i;
  }

  public int getBufferSize()
  {
    return bufferSize;
  }

  public void flushBuffer() throws IOException
  {
  }

  public void resetBuffer()
  {
  }

  public boolean isCommitted()
  {
    return committed;
  }

  public void reset()
  {
    if(committed)
      throw new IllegalStateException();
    headers.clear();
    status = 0;
    statusMessage = null;
  }

  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  public Locale getLocale()
  {
    return locale;
  }
}
