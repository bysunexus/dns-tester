package in.fireye.dnstester.resolver;

import jakarta.annotation.Resource;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
class IpResolverTest {

  @BeforeAll
  static void initJfxRuntime() {
    Platform.startup(() -> {
    });
  }

  @Resource
  private IpResolver ipResolver;

  @Test
  void resolve() throws IOException, InterruptedException {
    Optional<IpInfoData> data = ipResolver.resolve("39.156.66.14");
    data.ifPresent(System.out::println);
  }
}
