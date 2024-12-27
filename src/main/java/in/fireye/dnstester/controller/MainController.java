package in.fireye.dnstester.controller;

import in.fireye.dnstester.common.IpUtils;
import in.fireye.dnstester.resolver.IpInfoData;
import in.fireye.dnstester.service.TestDNSResult;
import in.fireye.dnstester.service.TestResult;
import in.fireye.dnstester.service.TestService;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.model.ToastParameter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class MainController implements ApplicationContextAware {
  @Resource
  private TestService testService;
  @Resource
  private ToastBarToasterService toasterService;

  private ApplicationContext springApplicationContext;

  @FXML
  private TextField domainField;
  @FXML
  private TextArea resultArea;
  @FXML
  private Pane resultPane;
  @FXML
  private TextField bestFirst;
  @FXML
  private TextField bsetSecond;
  @FXML
  private Button startBtn;

  @FXML
  public void openSettings(ActionEvent actionEvent) {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/settings.fxml"));
      loader.setControllerFactory(springApplicationContext::getBean);
      Parent root = loader.load();
      Scene scene = new Scene(root, 600, 400);
      Stage stage = new Stage();
      stage.setTitle("设置");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setResizable(false);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      log.error("无法打开设置页面", e);
    }
  }

  @FXML
  public void openAbout(ActionEvent actionEvent) {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/about.fxml"));
      loader.setControllerFactory(springApplicationContext::getBean);
      Parent root = loader.load();
      Scene scene = new Scene(root, 340, 180);
      Stage stage = new Stage();
      stage.setTitle("关于");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setResizable(false);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      log.error("无法打开设置页面", e);
    }
  }

  @FXML
  public void doTest(MouseEvent mouseEvent) {
    if (startBtn.isDisable()) {
      log.info("正在测试dns，请稍后");
      return;
    }
    startBtn.setDisable(true);
    resultArea.clear();
    resultPane.setVisible(false);

    String domain = domainField.getText();
    if (!IpUtils.isDomain(domain)) {
      toasterService.fail(
        "域名解析错误",
        "输入的内容不是域名",
        ToastParameter.builder().timeout(Duration.seconds(2)).build()
      );
      return;
    }

    // 异步执行防止ui被阻塞
    new Thread(() -> {

      TestResult result = testService.test(domain);
      IpInfoData localIpInfo = result.getLocalIpInfo();
      List<TestDNSResult> dnsResults = result.getDnsResults()
        .stream()
        .filter(dnsResult -> !CollectionUtils.isEmpty(dnsResult.getResult()))
        .sorted(Comparator.comparing(TestDNSResult::sortingWeight))
        .toList();
      Platform.runLater(() -> {
        resultArea.appendText(String.format("EDNS设置的本地信息为：%s %s%n", localIpInfo.getQueryIp(), localIpInfo.getCity()));
        int count = 0;
        for (TestDNSResult dnsResult : dnsResults) {
          if (count == 0) {
            bestFirst.setText(dnsResult.getDnsServer());
          }
          if (count == 1) {
            bsetSecond.setText(dnsResult.getDnsServer());
          }
          resultArea.appendText(String.format("DNS服务器: %s\t延迟: %dms 解析结果:%n", dnsResult.getDnsServer(), dnsResult.getDnsDelay()));
          for (IpInfoData ipInfoData : dnsResult.getResult()) {
            resultArea.appendText(String.format("%s\t%s\t%,.2fkm%n", ipInfoData.getQueryIp(), ipInfoData.getCity(), ipInfoData.getDistance()));
          }
          resultArea.appendText("\n");
          count++;
        }
        resultPane.setVisible(true);
        startBtn.setDisable(false);
      });
    }).start();
  }

  @FXML
  public void copyFirst(MouseEvent mouseEvent) {
    copyDnsAddress(bestFirst);
  }


  @FXML
  public void copySecond(MouseEvent mouseEvent) {
    copyDnsAddress(bsetSecond);
  }

  private void copyDnsAddress(TextField textField) {
    String dnsServer = textField.getText();
    ClipboardContent content = new ClipboardContent();
    content.putString(dnsServer);
    Clipboard clipboard = Clipboard.getSystemClipboard();
    clipboard.setContent(content);
    toasterService.info("复制成功", dnsServer + "已复制到剪切板", ToastParameter.builder().timeout(Duration.seconds(2)).build());
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    springApplicationContext = applicationContext;
  }
}

