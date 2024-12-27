package in.fireye.dnstester.resolver;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Slf4j
public class IpResolverMmdb implements IpResolver {

  private static final String LANGUAGE = "zh-CN";

  private final DatabaseReader databaseReader;

  public IpResolverMmdb() {
    try {
      File dbFile = ResourceUtils.getFile("classpath:IPDATA.mmdb");
      this.databaseReader = new DatabaseReader.Builder(dbFile).withCache(new CHMCache()).build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Optional<IpInfoData> resolve(String ip) {
    InetAddress ipAddress = null;
    try {
      ipAddress = InetAddress.getByName(ip);
    } catch (UnknownHostException e) {
      log.warn("ip[{}]地址错误:", ip, e);
      return Optional.empty();
    }
    CityResponse response = null;
    try {
      response = databaseReader.city(ipAddress);
    } catch (IOException | GeoIp2Exception e) {
      log.warn("ip[{}]地址错误:", ip, e);
      return Optional.empty();
    }
    IpInfoData ipInfoData = new IpInfoData();
    ipInfoData.setCity(response.getCity().getNames().get(LANGUAGE));
    ipInfoData.setLatitude(response.getLocation().getLatitude());
    ipInfoData.setLongitude(response.getLocation().getLongitude());
    ipInfoData.setQueryIp(ip);
    return Optional.of(ipInfoData);
  }
}
