package in.fireye.dnstester;

import in.fireye.dnstester.config.TesterConfig;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.xbill.DNS.Lookup;

@SpringBootApplication
@ConfigurationPropertiesScan
@Configuration
@EnableConfigurationProperties(TesterConfig.class)
@PropertySource("classpath:dnstester.properties")
public class DnsTesterApplication {

  public static void main(String[] args) {
    Lookup.setDefaultHostsFileParser(null);
    Application.launch(GuiApplication.class, args);

  }
}
