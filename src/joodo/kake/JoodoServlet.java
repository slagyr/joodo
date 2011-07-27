package joodo.kake;

import clojure.lang.*;
import joodo.util.Clj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JoodoServlet extends HttpServlet
{
  protected IFn serviceMethod;

  public JoodoServlet()
  {
    try
    {
      final Var var = loadVar("joodo.kake.servlet", "initialize-joodo-servlet");
      var.invoke(this);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void setServiceMethod(IFn serviceMethod)
  {
    this.serviceMethod = serviceMethod;
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    try
    {
      serviceMethod.invoke(this, req, resp);
    }
    catch(Exception e)
    {
      throw new ServletException(e);
    }
  }

  protected static Var loadVar(String namespace, String varName)
  {
    try
    {
      Symbol namespaceSymbol = Symbol.intern(namespace);
      Namespace ns = Namespace.find(namespaceSymbol);
      if(ns != null)
        return (Var)ns.getMapping(Symbol.create(varName));

      RT.load(namespace, false);

      ns = Namespace.find(namespaceSymbol);
      if(ns != null)
        return (Var)ns.getMapping(Symbol.create(varName));

      final String coreFilename = Clj.nsToFilename(namespace);
      RT.loadResourceScript(coreFilename);
      ns = Namespace.find(namespaceSymbol);
      if(ns != null)
        return (Var)ns.getMapping(Symbol.create(varName));

      throw new RuntimeException("var still not found after load attempts: " + namespace + "/" + varName);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException("Failed to load var:" + namespace + "/" + varName, e);
    }
  }
}
