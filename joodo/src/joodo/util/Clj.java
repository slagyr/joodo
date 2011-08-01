package joodo.util;

import clojure.lang.Namespace;
import clojure.lang.Symbol;

public class Clj
{
  public static String nsToFilename(Symbol symbol)
  {
    return nsToFilename(symbol.getName());
  }

  public static String nsToFilename(String name)
  {
    return name.replace('.', '/').replace('-', '_') + ".clj";
  }

  public static String nsToFilename(Namespace ns)
  {
    return nsToFilename(ns.getName());
  }
}
