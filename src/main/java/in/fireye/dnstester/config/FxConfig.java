package in.fireye.dnstester.config;

import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.model.ToastParameter;
import org.pomo.toasterfx.util.FXMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FxConfig {

  @Bean
  public ToastBarToasterService toasterService() {
    ToastBarToasterService service = new ToastBarToasterService();
    service.initialize();
    return service;
  }
}
