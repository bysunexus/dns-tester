package in.fireye.dnstester.resolver;

import lombok.Data;

import java.util.Map;

@Data
public class IpInfo {
  private int code;
  private Map<String, IpInfoData> data;
  private String reqid;
}
