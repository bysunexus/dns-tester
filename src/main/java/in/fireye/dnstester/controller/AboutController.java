package in.fireye.dnstester.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AboutController {

  @FXML
  public void openLink(MouseEvent mouseEvent) {
    Hyperlink link = (Hyperlink) mouseEvent.getSource();
    String url = link.getText();
    try {
      String os = System.getProperty("os.name").toLowerCase();
      ProcessBuilder processBuilder;
      if (os.contains("win")) {
        // Windows系统
        processBuilder = new ProcessBuilder("cmd", "/c", "start", url);
      } else if (os.contains("mac")) {
        // macOS系统
        processBuilder = new ProcessBuilder("open", url);
      } else {
        // 其他系统
        processBuilder = new ProcessBuilder("xdg-open", url);
      }
      processBuilder.start();
    } catch (IOException ignored) {
      log.debug("无法打开链接：" + link.getText());
    }
    Stage stage = (Stage) link.getScene().getWindow();
    stage.close();
  }

  public void close(MouseEvent mouseEvent) {
    AnchorPane anchorPane = (AnchorPane) mouseEvent.getSource();
    Stage stage = (Stage) anchorPane.getScene().getWindow();
    stage.close();
  }
}
