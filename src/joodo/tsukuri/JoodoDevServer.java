package joodo.tsukuri;

import joodo.kake.JoodoServlet;
import mmargs.Arguments;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.log.Log;
import org.mortbay.log.StdErrLog;

import java.util.Map;
import java.util.logging.Logger;

public class JoodoDevServer
{
  public String address = "127.0.0.1";
  public int port = 8080;
  public String env = "development";
  public String dir = ".";
  private static Arguments argSpec = new Arguments();

  static
  {
    argSpec.addValueOption("p", "port", "PORT", "Change the port (default: 8080)");
    argSpec.addValueOption("a", "address", "ADDRESS", "Change the address (default: 127.0.0.1)");
    argSpec.addValueOption("e", "environment", "ENVIRONMENT", "Change the environment (default: development)");
    argSpec.addValueOption("d", "directory", "DIRECTORY", "Change the directory (default: .)");
  }

  private static Logger log = Logger.getLogger(JoodoDevServer.class.getName());

  public static void main(String[] args) throws Exception
  {
    enableConsoleLogging();
    JoodoDevServer joodoDevServer = new JoodoDevServer();
    joodoDevServer.parseArgs(args);
    joodoDevServer.start();
  }

  public JoodoDevServer()
  {
  }

  public void parseArgs(String[] args)
  {
    final Map<String, Object> options = argSpec.parse(args);
    final Object errors = options.get("*errors");
    if(errors != null)
    {
      System.out.println("Usage: lein server " + argSpec.argString());
      System.out.println(argSpec.optionsString());
      System.exit(-1);
    }
    else
    {
      if(options.containsKey("port"))
        port = Integer.parseInt((String) options.get("port"));
      if(options.containsKey("address"))
        address = (String) options.get("address");
      if(options.containsKey("environment"))
        env = (String) options.get("environment");
      if(options.containsKey("directory"))
        dir = (String) options.get("directory");
    }
  }

  private void start() throws Exception
  {
    Server server = new Server(port);

    final ServletHandler servletHandler = new ServletHandler();
    servletHandler.addFilterWithMapping(StaticFileFilter.class, "/*", Handler.REQUEST);
    servletHandler.addServletWithMapping(JoodoServlet.class, "/");
    server.addHandler(servletHandler);

    log.info(toString() + " starting up ...");
    server.start();
    log.info(toString() + " is up and running.");

    server.join();
  }

  @Override
  public String toString()
  {
    return "JoodoDevServer " + env + ":" + dir + " " + address + ":" + port;
  }

  private static void enableConsoleLogging()
  {
    Log.setLog(new StdErrLog());
    Log.getLog().setDebugEnabled(true);
  }
}