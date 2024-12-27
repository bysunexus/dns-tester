package in.fireye.dnstester.resolver;

import lombok.Data;

@Data
public class IpInfoData {
  private String city;
  private Double latitude;
  private Double longitude;
  private String queryIp;
  private Double distance;
}
