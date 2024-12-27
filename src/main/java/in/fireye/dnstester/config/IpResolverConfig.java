package in.fireye.dnstester.config;

import in.fireye.dnstester.resolver.IpResolver;
import in.fireye.dnstester.resolver.IpResolverMmdb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

@Configuration
public class IpResolverConfig {
  @Bean
  public IpResolver ipResolver() {

    List<IpResolver> resolvers = SpringFactoriesLoader.loadFactories(IpResolver.class, getClass().getClassLoader());
    for (IpResolver resolver : resolvers) {
      return resolver;
    }
    return new IpResolverMmdb();
  }
}
