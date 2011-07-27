package joodo.tsukuri;

import org.mortbay.io.Buffer;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.resource.Resource;
import org.mortbay.util.URIUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class StaticFileFilter implements Filter
{
  public Resource root;
  private MimeTypes mimeTypes;

  public void init(FilterConfig filterConfig) throws ServletException
  {
    try
    {
      mimeTypes = new MimeTypes();
      root = Resource.newResource("public");
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String servletPath = request.getServletPath();
    String pathInfo = request.getPathInfo();
    String relativePath = URIUtil.addPaths(servletPath, pathInfo);

    final Resource resource = root.addPath(relativePath);
    try
    {
      if(resource.exists() && !resource.isDirectory())
        sendResource(response, resource);
      else
        filterChain.doFilter(servletRequest, servletResponse);
    }
    finally
    {
      resource.release();
    }
  }

  private void sendResource(HttpServletResponse response, Resource resource) throws IOException
  {
    long contentLength = resource.length();
    writeHeaders(response, resource, contentLength);
    OutputStream out = response.getOutputStream();
    resource.writeTo(out, 0L, contentLength);
  }

  private void writeHeaders(HttpServletResponse response, Resource resource, long count)
  {
    final Buffer mimeType = mimeTypes.getMimeByExtension(resource.getName());
    if(mimeType != null)
      response.setContentType(mimeType.toString());
    if(count != -1L)
    {
      if(count < 2147483647L)
        response.setContentLength((int) count);
      else
        response.setHeader("Content-Length", String.valueOf(count));
    }
    response.setDateHeader("Last-Modified", resource.lastModified());
    response.setHeader("Cache-Control", "private");
  }

  public void destroy()
  {
  }
}
