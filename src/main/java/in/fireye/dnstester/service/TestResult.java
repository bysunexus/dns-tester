package in.fireye.dnstester.service;

import in.fireye.dnstester.resolver.IpInfoData;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class TestResult {
  private IpInfoData localIpInfo;
  List<TestDNSResult> dnsResults = new LinkedList<>();

  public void addDnsResult(TestDNSResult dnsResult) {
    dnsResults.add(dnsResult);
  }

  public void print() {
    System.out.printf("Local Info:%s %s%n", localIpInfo.getCity(), localIpInfo.getIsp());
    System.out.println("Test Results:");
    System.out.println("ip\tcity-isp\tdistance(km)");
    for (TestDNSResult dnsResult : dnsResults) {
      System.out.printf("DNS Server: %s\tDelay:%dms%n", dnsResult.getDnsServer(), dnsResult.getDnsDelay());
      for (IpInfoData ipInfoData : dnsResult.getResult()) {

        System.out.printf("%s\t%s-%s\t%,.2fkm%n", ipInfoData.getIp(), ipInfoData.getCity(), ipInfoData.getIsp(), ipInfoData.getDistance() / 1000.0);
      }
      System.out.println("-------------------");
    }
  }
}
