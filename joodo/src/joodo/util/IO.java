package joodo.util;

import java.io.IOException;
import java.io.InputStream;

public class IO
{
  public static byte[] readStreamToEnd(InputStream stream) throws IOException
  {
    byte[] bytes = new byte[1028];
    int b = 0;
    int count = 0;
    while(-1 != (b = stream.read()))
    {
      bytes[count++] = (byte)b;
      if(count >= bytes.length)
        bytes = enlarge(bytes);
    }

    return condense(bytes, count);
  }

  private static byte[] condense(byte[] bytes, int count)
  {
    byte[] fitted = new byte[count];
    System.arraycopy(bytes, 0, fitted, 0, count);
    return fitted;
  }

  private static byte[] enlarge(byte[] bytes)
  {
    byte[] enlarged = new byte[bytes.length * 2];
    System.arraycopy(bytes, 0, enlarged, 0, bytes.length);
    return enlarged;
  }
}
