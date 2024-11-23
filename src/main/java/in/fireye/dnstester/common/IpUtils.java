package in.fireye.dnstester.common;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  /**
   * 判断是否为域名
   *
   * @param input 输入字符串
   * @return 是否为域名
   */
  public static boolean isDomain(String input) {
    if (StringUtils.isAnyBlank(input)) {
      return false;
    }
    String regex = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);
    return matcher.matches();
  }

  public static boolean isValidIP(String ip) {
    if (StringUtils.isAnyBlank(ip)) {
      return false;
    }
    try {
      InetAddress inet = InetAddress.getByName(ip);
      return inet != null;
    } catch (UnknownHostException e) {
      return false;
    }
  }
}
