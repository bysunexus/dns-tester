package in.fireye.dnstester.service;

import in.fireye.dnstester.common.GeoUtils;
import in.fireye.dnstester.config.TesterConfig;
import in.fireye.dnstester.resolver.DnsInfo;
import in.fireye.dnstester.resolver.DnsResolver;
import in.fireye.dnstester.resolver.IpInfoData;
import in.fireye.dnstester.resolver.IpResolver;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class TestService {
  @Resource
  private IpResolver ipResolver;
  @Resource
  private DnsResolver dnsResolver;
  @Resource
  private TesterConfig testerConfig;

  public TestResult test(String hostname) {
    TestResult result = new TestResult();

    // 设置本地IP信息
    if (StringUtils.isNoneBlank(testerConfig.getSubnetCidr4())) {
      ipResolver.resolve(testerConfig.getSubnetCidr4().split("/")[0]).ifPresent(result::setLocalIpInfo);
    } else {
      ipResolver.resolve("").ifPresent(result::setLocalIpInfo);
    }
    if (result.getLocalIpInfo() == null) {
      throw new IllegalArgumentException("Local IP address cannot be resolved.");
    }

    List<Callable<TestDNSResult>> tasks = new ArrayList<>(testerConfig.getServers().size());
    for (String dnsServer : testerConfig.getServers()) {
      tasks.add(() -> {
        TestDNSResult testDNSResult = new TestDNSResult();
        testDNSResult.setDnsServer(dnsServer);
        DnsInfo resolveResult = dnsResolver.resolve(hostname, dnsServer);
        testDNSResult.setDnsDelay(resolveResult.getDnsDelay());
        resolveResult.getResult().forEach(ip -> {
          Optional<IpInfoData> ipInfo = ipResolver.resolve(ip);
          ipInfo.ifPresent(ipInfoData -> {
            ipInfoData.setDistance(GeoUtils.getDistance(
              ipInfoData.getLongitude(), ipInfoData.getLatitude(),
              result.getLocalIpInfo().getLongitude(), result.getLocalIpInfo().getLatitude()
            ));
            testDNSResult.addResult(ipInfoData);
          });
        });
        return testDNSResult;
      });
    }
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<TestDNSResult>> results = executor.invokeAll(tasks);
      for (Future<TestDNSResult> testDNSResultFuture : results) {
        result.addDnsResult(testDNSResultFuture.get());
      }
    } catch (Exception e) {
      log.error("解析dns时发生错误：", e);
      throw new RuntimeException(e);
    }
    return result;
  }
}
