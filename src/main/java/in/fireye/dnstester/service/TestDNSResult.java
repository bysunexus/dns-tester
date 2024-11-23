package in.fireye.dnstester.service;

import in.fireye.dnstester.resolver.IpInfoData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestDNSResult {

  /**
   * 国内最远距离 5500km*10
   */
  private static final double MAX_DISTANCE = 55000.0D;
  /**
   * （最大延迟（2000ms） - 最小延迟（10ms））*10
   */
  private static final double MAX_DELAY = 19900.0D;
  /**
   * 最小延迟（10ms）
   */
  private static final double MINI_DELAY = 10.0D;
  private Long dnsDelay;
  private String dnsServer;
  private List<IpInfoData> result = new ArrayList<>();

  public void addResult(IpInfoData data) {
    result.add(data);
  }

  public double sortingWeight() {
    long distance = Double.valueOf(result.stream().mapToDouble(IpInfoData::getDistance).average().orElse(0.0)).longValue();
    // 获取重整化权重 （延迟-最小延迟）/（最大延迟-最小延迟）*80% +(距离-最小距离)/（最大距离-最小距离）*20%
    return ((dnsDelay-MINI_DELAY)*8)/MAX_DELAY +(distance*2)/MAX_DISTANCE;
  }
}
