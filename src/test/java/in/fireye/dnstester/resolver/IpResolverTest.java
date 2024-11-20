package in.fireye.dnstester.resolver;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
class IpResolverTest {

  @Resource
  private IpResolver ipResolver;

  @Test
  void resolve() throws IOException, InterruptedException {
    Optional<IpInfoData> data = ipResolver.resolve("118.113.4.53");
    data.ifPresent(System.out::println);
  }
}
