package in.fireye.dnstester.resolver;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DnsInfo {

  private Long dnsDelay;
  private String dnsServer;
  private List<String> result = new ArrayList<>();

}
