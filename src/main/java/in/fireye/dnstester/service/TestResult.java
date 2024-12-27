package in.fireye.dnstester.service;

import in.fireye.dnstester.resolver.IpInfoData;
import lombok.Data;

import java.util.Comparator;
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
    System.out.printf("Local Info:%s%n", localIpInfo.getCity());
    System.out.println("Test Results:");
    System.out.println("ip\tcity-isp\tdistance(km)");
    dnsResults.sort(Comparator.comparing(TestDNSResult::sortingWeight));
    for (TestDNSResult dnsResult : dnsResults) {
      System.out.printf("DNS Server: %s\tDelay:%dms%n", dnsResult.getDnsServer(), dnsResult.getDnsDelay());
      for (IpInfoData ipInfoData : dnsResult.getResult()) {
        System.out.printf("%s\t%s\t%,.2fkm%n", ipInfoData.getQueryIp(), ipInfoData.getCity(), ipInfoData.getDistance());
      }
      System.out.println("-------------------");
    }
  }
}
