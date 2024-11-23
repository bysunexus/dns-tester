package in.fireye.dnstester.controller;

import in.fireye.dnstester.common.IpUtils;
import in.fireye.dnstester.config.TesterConfig;
import jakarta.annotation.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.model.ToastParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;

@Component
@Slf4j
public class SettingsController {

  @Resource
  private TesterConfig testerConfig;
  @Resource
  private ToastBarToasterService toasterService;

  @FXML
  private ListView<String> dnsServersView;
  @FXML
  private TextField dnsServerTextField;
  @FXML
  private CheckBox ipv6Checkbox;
  @FXML
  private CheckBox tcpCheckbox;
  @FXML
  private CheckBox ednsCheckbox;
  @FXML
  private TextField cidr1TextField;
  @FXML
  private TextField cidr2TextField;
  @FXML
  private VBox rootVBox;


  @FXML
  public void initialize() {
    log.info("init config view");
    dnsServersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    dnsServersView.setItems(FXCollections.observableList(new ArrayList<>(testerConfig.getServers())));
    ipv6Checkbox.setSelected(testerConfig.isSupportIpv6());
    tcpCheckbox.setSelected(testerConfig.isUseTcp());
    ednsCheckbox.setSelected(testerConfig.isUseEdns());
    cidr1TextField.setText(testerConfig.getSubnetCidr4());
    cidr2TextField.setText(testerConfig.getSubnetCidr6());
  }

  public void saveSettings(MouseEvent mouseEvent) {
    ObservableList<String> dnsServersList = dnsServersView.getItems();
    if(CollectionUtils.isEmpty(dnsServersList)){
      toasterService.fail(
        "DNS服务器列表为空",
        "请至少添加一个DNS服务器",
        ToastParameter.builder().timeout(Duration.seconds(2)).build()
      );
      return;
    }
    testerConfig.setServers(new ArrayList<>(dnsServersList));
    testerConfig.setSupportIpv6(ipv6Checkbox.isSelected());
    testerConfig.setUseTcp(tcpCheckbox.isSelected());
    testerConfig.setUseEdns(ednsCheckbox.isSelected());
    testerConfig.setSubnetCidr4(cidr1TextField.getText());
    testerConfig.setSubnetCidr6(cidr2TextField.getText());
    testerConfig.updateClientSubnetOption();

    closeSettings();
  }

  public void cancel(MouseEvent mouseEvent) {
    closeSettings();
  }

  private void closeSettings() {
    Stage stage = (Stage) rootVBox.getScene().getWindow();
    stage.close();
  }

  public void addDnsServer(MouseEvent mouseEvent) {
    String dnsServer = dnsServerTextField.getText();
    if (!IpUtils.isValidIP(dnsServer)) {
      toasterService.fail(
        "ip地址错误",
        "所添加的ip地址格式错误",
        ToastParameter.builder().timeout(Duration.seconds(2)).build()
      );
      return;
    }
    dnsServersView.getItems().add(dnsServer);
  }

  public void removeDnsServer(MouseEvent mouseEvent) {
    ObservableList<String> selectedItems = dnsServersView.getSelectionModel().getSelectedItems();
    dnsServersView.getItems().removeAll(selectedItems);
  }
}
