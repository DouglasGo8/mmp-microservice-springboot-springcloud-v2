package com.packtpub.microservices.springboot.product.composite.mediation.beans;

import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductCompositeBean {
  private final ServiceUtil serviceUtil;
}
