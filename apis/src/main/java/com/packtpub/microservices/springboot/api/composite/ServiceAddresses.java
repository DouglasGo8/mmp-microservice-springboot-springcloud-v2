package com.packtpub.microservices.springboot.api.composite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceAddresses {

  private final String cmp;
  private final String pro;
  private final String rev;
  private final String rec;

  public ServiceAddresses() {
    cmp = null;
    pro = null;
    rev = null;
    rec = null;
  }
}
