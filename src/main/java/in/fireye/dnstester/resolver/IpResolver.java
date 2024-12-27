package in.fireye.dnstester.resolver;

import java.util.Optional;

public interface IpResolver {
  Optional<IpInfoData> resolve(String ip);
}
