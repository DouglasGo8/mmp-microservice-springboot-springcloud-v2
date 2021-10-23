package com.packtpub.microservices.springboot.product.mediation.beans;

import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.product.mediation.dto.ProductDto;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
@AllArgsConstructor
public class ProductBean {

  private final ServiceUtil serviceUtil;

  @Handler
  public ProductDto myProduct(@Body int productId) {
    var product = new Product(productId, 123,
            "name-" + productId,
            serviceUtil.getServiceAddress());

    return new ProductDto(product);
  }
}
