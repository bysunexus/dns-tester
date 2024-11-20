package in.fireye.dnstester.service;

import in.fireye.dnstester.resolver.IpInfoData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TestDNSResult {
  private Long dnsDelay;
  private String dnsServer;
  private List<IpInfoData> result = new ArrayList<>();

  public void addResult(IpInfoData data) {
    result.add(data);
  }
}
