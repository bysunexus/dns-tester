package in.fireye.dnstester.resolver;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.Optional;

@Component
public class IpResolver {
  private static final String MEITU_URI = "https://webapi-pc.meitu.com/common/ip_location?ip={0}";
  @Resource
  private HttpClient httpClient;

  public Optional<IpInfoData> resolve(String ip){

    HttpRequest request = HttpRequest.newBuilder()
      .GET()
      .uri(URI.create(MessageFormat.format(MEITU_URI, ip)))
      .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36") // add request header
      .build();

    HttpResponse<IpInfo> response = null;
    try {
      response = httpClient.send(request, JsonBodyHandler.of(IpInfo.class));
    } catch (IOException | InterruptedException e) {
      return Optional.empty();
    }
    if (response.statusCode() != 200) {
      return Optional.empty();
    }

    IpInfo ipInfo = response.body();
    if (ipInfo == null || ipInfo.getData() == null) {
      return Optional.empty();
    }
    Optional<IpInfoData> result = Optional.ofNullable(ipInfo.getData().get(ip));
    result.ifPresent(ipInfoData -> ipInfoData.setIp(ip));
    return result;
  }
}
