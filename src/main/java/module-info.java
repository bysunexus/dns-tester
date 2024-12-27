open module dns.tester {
  requires com.alibaba.fastjson2;
  requires jakarta.annotation;
  requires org.apache.commons.lang3;
  requires org.dnsjava;
  requires spring.boot;
  requires spring.boot.autoconfigure;
  requires spring.context;
  requires spring.core;
  requires spring.beans;
  requires lombok;
  requires javafx.swing;
  requires org.pomo.toasterfx;
  requires javafx.controls;
  requires javafx.fxml;
  requires org.slf4j;
  requires inet.ipaddr;
  requires ip2location;
  requires com.maxmind.geoip2;
  requires java.net.http;
  exports in.fireye.dnstester;
}
