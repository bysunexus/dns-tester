package in.fireye.dnstester.resolver;


import com.alibaba.fastjson2.JSON;

import java.io.InputStream;
import java.net.http.HttpResponse;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
  private final Class<T> t;


  public JsonBodyHandler(Class<T> t) {
    this.t = t;
  }

  public static <T> JsonBodyHandler<T> of(Class<T> t) {
    return new JsonBodyHandler<>(t);
  }

  @Override
  public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
    HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

    return HttpResponse.BodySubscribers.mapping(
      upstream,
      inputStream -> toSupplierOfType(inputStream, t));
  }

  private T toSupplierOfType(InputStream inputStream, Class<T> t) {
    return JSON.parseObject(inputStream, t);
  }
}
