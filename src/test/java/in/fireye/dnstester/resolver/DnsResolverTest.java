package in.fireye.dnstester.resolver;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DnsResolverTest {
  @Resource
  DnsResolver dnsResolver;

  @Test
  void resolve() throws IOException {

    DnsInfo result = dnsResolver.resolve("www.bilibili.com", "223.5.5.5");
    System.out.println(result);
  }
}
