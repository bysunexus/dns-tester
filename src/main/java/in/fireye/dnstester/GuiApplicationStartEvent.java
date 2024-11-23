package in.fireye.dnstester;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class GuiApplicationStartEvent extends ApplicationEvent {
  public GuiApplicationStartEvent(Stage source) {
    super(source);
  }

  public Stage getStage() {
    return (Stage) getSource();
  }
}
