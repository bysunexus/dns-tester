package in.fireye.dnstester;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class GuiApplication extends Application {

  private static ConfigurableApplicationContext springApplicationContext;

  @Override
  public void init() {
    springApplicationContext = SpringApplication.run(DnsTesterApplication.class, getParameters().getRaw().toArray(new String[0]));

  }

  @Override
  public void start(Stage stage) {
    springApplicationContext.publishEvent(new GuiApplicationStartEvent(stage));
  }

  @Override
  public void stop() {
    springApplicationContext.close();
    Platform.exit();
    System.exit(0);
  }

}
