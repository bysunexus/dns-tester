package in.fireye.dnstester.config;

import inet.ipaddr.IPAddressString;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.xbill.DNS.ClientSubnetOption;
import org.xbill.DNS.EDNSOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ConfigurationProperties(prefix = "fireye.dns.tester")
@Data
public class TesterConfig {

  private List<String> servers;

  private boolean supportIpv6;

  private boolean useTcp;

  private boolean useEdns;

  private String subnetCidr4;
  private String subnetCidr6;

  private List<EDNSOption> subnetOptions;


  @PostConstruct
  public void updateClientSubnetOption() {
    if (!useEdns) {
      subnetOptions = Collections.emptyList();
      return;
    }

    subnetOptions = new ArrayList<>();
    addSubnetOpt(subnetCidr4);
    addSubnetOpt(subnetCidr6);
  }

  private void addSubnetOpt(String subnetCidr) {
    if (StringUtils.isNoneBlank(subnetCidr)) {
      IPAddressString ipAddressString = new IPAddressString(subnetCidr);
      if (!ipAddressString.isValid() || !ipAddressString.isPrefixed()) {
        throw new IllegalArgumentException("Invalid subnet CIDR format, should be like 192.168.0.0/24");
      }
      subnetOptions.add(new ClientSubnetOption(
        ipAddressString.getNetworkPrefixLength(),
        ipAddressString.getAddress().toPrefixBlock().toZeroHost().toInetAddress()
      ));
    }
  }
}
