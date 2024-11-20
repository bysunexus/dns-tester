package in.fireye.dnstester.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.xbill.DNS.ClientSubnetOption;
import org.xbill.DNS.EDNSOption;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
  public void updateClientSubnetOption() throws UnknownHostException {
    if(!useEdns){
      subnetOptions = Collections.emptyList();
      return ;
    }
    if(supportIpv6 && StringUtils.isBlank(subnetCidr6)){
      throw new IllegalArgumentException("supportIpv6 set to true, subnetCidr6 can not be empty");
    }
    subnetOptions = new ArrayList<>();
    if (StringUtils.isNoneBlank(subnetCidr4)) {
      String[] subnets = subnetCidr4.split("/");
      if (subnets.length != 2) {
        throw new IllegalArgumentException("Invalid subnet CIDR format, should be like 192.168.0.0/24");
      }
      subnetOptions.add(new ClientSubnetOption(Integer.parseInt(subnets[1]), InetAddress.getByName(subnets[0])));
    }

    if(supportIpv6){
      String[] subnets = subnetCidr6.split("/");
      if (subnets.length != 2) {
        throw new IllegalArgumentException("Invalid subnet CIDR format, should be like f80::/64");
      }
      subnetOptions.add(new ClientSubnetOption(Integer.parseInt(subnets[1]), InetAddress.getByName(subnets[0])));
    }
  }
}
