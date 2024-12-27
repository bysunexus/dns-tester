package in.fireye.dnstester.resolver;

import in.fireye.dnstester.common.IpUtils;
import lombok.extern.slf4j.Slf4j;
import net.renfei.ip2location.IP2Location;
import net.renfei.ip2location.IPResult;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class IpResolverIp2loc implements IpResolver {
  IP2Location ipDatabase;

  public IpResolverIp2loc() throws IOException {
    File dbFile = ResourceUtils.getFile("classpath:IPDATA.BIN");
    ipDatabase = new IP2Location();
    ipDatabase.Open(dbFile.getAbsolutePath(), true);
  }

  public Optional<IpInfoData> resolve(String ip) {
    if (!IpUtils.isValidIP(ip)) {
      log.error("ip[{}]地址错误:", ip);
      return Optional.empty();
    }
    IPResult ipResult = null;
    try {
      ipResult = ipDatabase.IPQuery(ip);
    } catch (IOException e) {
      log.error("resolve ip error", e);
      return Optional.empty();
    }
    IpInfoData ipInfoData = new IpInfoData();
    ipInfoData.setCity(ipResult.getCity());
    ipInfoData.setLatitude((double) ipResult.getLatitude());
    ipInfoData.setLongitude((double) ipResult.getLongitude());
    ipInfoData.setQueryIp(ip);
    return Optional.of(ipInfoData);
  }
}
