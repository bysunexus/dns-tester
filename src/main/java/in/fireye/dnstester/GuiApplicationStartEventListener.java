package in.fireye.dnstester;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GuiApplicationStartEventListener implements ApplicationListener<GuiApplicationStartEvent> {

  private final ApplicationContext springApplicationContext;

  public GuiApplicationStartEventListener(ApplicationContext springApplicationContext) {
    this.springApplicationContext = springApplicationContext;
  }

  @Override
  public void onApplicationEvent(GuiApplicationStartEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/main.fxml"));
      fxmlLoader.setControllerFactory(springApplicationContext::getBean);
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root, 640, 480);
      Stage stage = event.getStage();
      stage.setResizable(false);
      stage.setScene(scene);
      stage.setTitle("DNS测试工具");
      stage.show();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
