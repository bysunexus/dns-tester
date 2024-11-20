package in.fireye.dnstester;

import in.fireye.dnstester.config.TesterConfig;
import in.fireye.dnstester.service.TestResult;
import in.fireye.dnstester.service.TestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableConfigurationProperties({TesterConfig.class})
public class DnsTesterApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext app = SpringApplication.run(DnsTesterApplication.class, args);
    TestService testService = app.getBean(TestService.class);
    try {
      TestResult result = testService.test("www.bilibili.com");

      result.print();
      System.exit(0);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

}
