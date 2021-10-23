package com.packtpub.microservices.springboot.utils.http;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author dougdb
 */
@Component
public class ServiceUtil {

  @Getter
  private final String port;
  private String serviceAddress = null;

  @Autowired
  public ServiceUtil(@Value("${server.port}") String port) {
    this.port = port;
  }

  public String getServiceAddress() {
    if (serviceAddress == null) {
      serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
    }
    return serviceAddress;
  }

  private String findMyHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "Unknown host name";
    }
  }

  private String findMyIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return "unknown IP address";
    }
  }


}
