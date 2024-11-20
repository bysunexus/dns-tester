package in.fireye.dnstester.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class IpUtils {

  /**
   * 获取本机所有ip地址
   *
   * @return 本地所有ip地址
   * @throws SocketException 网络异常
   */
  public static List<InetAddress> getLocalAddresses() throws SocketException {
    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
    List<InetAddress> result = new LinkedList<>();
    while (e.hasMoreElements()) {
      NetworkInterface ni = e.nextElement();
      Enumeration<InetAddress> addresses = ni.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress address = addresses.nextElement();
        if (address.isLoopbackAddress() || address.isLinkLocalAddress()) {
          continue;
        }
        result.add(address);
      }
    }
    return new ArrayList<>(result);
  }
}
