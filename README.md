# dns-tester
dns检测

ip地址解析改为使用本地库
数据库下载
https://lite.ip2location.com/database-download （DB5.LITE ipv6 数据库文件修改为IPDATA.BIN）
或者
https://github.com/P3TERX/GeoLite.mmdb （GeoLite2-City.mmdb 数据库文件修改为IPDATA.mmdb）

修改 [spring.factories](src/main/resources/META-INF/spring.factories) 文件使用不同的数据库
in.fireye.dnstester.resolver.IpResolverIp2loc 使用IP2LOCATION数据库（IPDATA.BIN）
in.fireye.dnstester.resolver.IpResolverMmdb 使用GeoLite2-City.mmdb数据库（IPDATA.mmdb）

