package in.fireye.dnstester.resolver;


import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;

@Slf4j
public class TextBodyHandler implements HttpResponse.BodyHandler<String> {
  @Override
  public HttpResponse.BodySubscriber<String> apply(HttpResponse.ResponseInfo responseInfo) {
    HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

    return HttpResponse.BodySubscribers.mapping(
      upstream,
      this::toSupplierOfType
    );
  }

  private String toSupplierOfType(InputStream inputStream) {
    try {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      for (int length; (length = inputStream.read(buffer)) != -1; ) {
        result.write(buffer, 0, length);
      }
      return result.toString();
    } catch (IOException e) {
      log.warn("解析http请求响应时发生错误：", e);
      return null;
    }
  }
}
