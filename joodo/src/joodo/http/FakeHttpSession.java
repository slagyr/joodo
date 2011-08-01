package joodo.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

public class FakeHttpSession implements HttpSession
{
  public int creationTime;
  public String id;
  public int lastAccessedTimes;
  public ServletContext servletContext;
  public int maxInactiveInterval;
  public HttpSessionContext sessionContext;
  public Hashtable<String, Object> attributes = new Hashtable<String, Object>();
  public Hashtable<String, Object> values = new Hashtable<String, Object>();
  public boolean valid;
  public boolean isNew = true;

  public long getCreationTime()
  {
    return creationTime;
  }

  public String getId()
  {
    return id;
  }

  public long getLastAccessedTime()
  {
    return lastAccessedTimes;
  }

  public ServletContext getServletContext()
  {
    return servletContext;
  }

  public void setMaxInactiveInterval(int i)
  {
    maxInactiveInterval = i;
  }

  public int getMaxInactiveInterval()
  {
    return maxInactiveInterval;
  }

  public HttpSessionContext getSessionContext()
  {
    return sessionContext;
  }

  public Object getAttribute(String s)
  {
    return attributes.get(s);
  }

  public Object getValue(String s)
  {
    return values.get(s);
  }

  public Enumeration getAttributeNames()
  {
    return attributes.keys();
  }

  public String[] getValueNames()
  {
    final Set<String> keys = values.keySet();
    return keys.toArray(new String[keys.size()]);
  }

  public void setAttribute(String s, Object o)
  {
    attributes.put(s, o);
  }

  public void putValue(String s, Object o)
  {
    values.put(s, o);
  }

  public void removeAttribute(String s)
  {
    attributes.remove(s);
  }

  public void removeValue(String s)
  {
    values.remove(s);
  }

  public void invalidate()
  {
    valid = false;
  }

  public boolean isNew()
  {
    return isNew;
  }
}
