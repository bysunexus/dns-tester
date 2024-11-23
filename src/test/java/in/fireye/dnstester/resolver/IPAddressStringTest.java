package in.fireye.dnstester.resolver;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.junit.jupiter.api.Test;

public class IPAddressStringTest {

  @Test
  void resolve() {
    IPAddressString aaa = new IPAddressString("2001:0db8:85a3:0000::/64");
    System.out.println(aaa.getNetworkPrefixLength());
    System.out.println(aaa.getHostAddress());
    System.out.println(aaa.isPrefixed());


    IPAddressString ipAddressString = new IPAddressString("2001:0db8:85a3:0000::/64");
    IPAddress address = ipAddressString.getAddress();
    System.out.println("count: " + address.getCount());
    IPAddress hostAddress = ipAddressString.getHostAddress();
    IPAddress prefixBlock = address.toPrefixBlock().toZeroHost();
    Integer prefixLength = ipAddressString.getNetworkPrefixLength();
    System.out.println(address);
    System.out.println(address.toCanonicalWildcardString());
    System.out.println(hostAddress);
    System.out.println(prefixLength);
    System.out.println(prefixBlock.withoutPrefixLength());
  }
}
