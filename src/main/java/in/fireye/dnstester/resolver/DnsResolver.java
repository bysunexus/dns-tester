package in.fireye.dnstester.resolver;

import in.fireye.dnstester.config.TesterConfig;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xbill.DNS.Record;
import org.xbill.DNS.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class DnsResolver {

  @Resource
  private TesterConfig testerConfig;


  public DnsInfo resolve(String domain, String dnsServer) throws IOException {
    DnsInfo result = new DnsInfo();
    result.setDnsServer(dnsServer);
    Resolver resolver = createResolver(dnsServer);
    int times = 1;
    Lookup lookupA = new Lookup(domain, Type.A);
    lookupA.setCache(null);
    lookupA.setResolver(resolver);
    StopWatch stopWatch = StopWatch.createStarted();
    Record[] recordsA = lookupA.run();
    stopWatch.suspend();
    if (ArrayUtils.isNotEmpty(recordsA)) {
      ARecord record = (ARecord) recordsA[0];
      result.getResult().add(record.getAddress().getHostAddress());
    }
    if (testerConfig.isSupportIpv6()) {
      times = 2;
      Lookup lookupAAAA = new Lookup(domain, Type.AAAA);
      lookupAAAA.setCache(null);
      lookupAAAA.setResolver(resolver);
      stopWatch.resume();
      Record[] recordsAAAA = lookupAAAA.run();
      stopWatch.stop();
      if (ArrayUtils.isNotEmpty(recordsAAAA)) {
        AAAARecord record = (AAAARecord) recordsAAAA[0];
        result.getResult().add(record.getAddress().getHostAddress());
      }
    }
    result.setDnsDelay(stopWatch.getTime(TimeUnit.MILLISECONDS) / times);
    return result;
  }

  public Resolver createResolver(String dnsServer) throws UnknownHostException {
    Resolver resolver = new SimpleResolver(dnsServer);
    resolver.setTCP(testerConfig.isUseTcp());
    resolver.setTimeout(Duration.ofSeconds(2));
    if (!testerConfig.isUseEdns()) {
      resolver.setEDNS(-1);
      return resolver;
    }
    if (CollectionUtils.isEmpty(testerConfig.getSubnetOptions())) {
      resolver.setEDNS(0, 0, 0);
    } else {
      resolver.setEDNS(0, 0, 0, testerConfig.getSubnetOptions());
    }
    return resolver;
  }
}
