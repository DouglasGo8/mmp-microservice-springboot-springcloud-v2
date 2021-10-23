package com.packtpub.microservices.springboot.product.composite.services;

import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.composite.ProductCompositeService;
import com.packtpub.microservices.springboot.product.composite.mediation.dto.ProductAggregateDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ProductCompositeServiceImpl implements ProductCompositeService {

  private final ProducerTemplate producerTemplate;

  @Override
  public ProductAggregate getProduct(int productId) {
    var dto = producerTemplate.requestBody("{{direct.product.composite.mediator.endpoint}}", productId, ProductAggregateDto.class);
    return dto.getProductAggregate();
  }
}
