package com.packtpub.microservices.springboot.product.beans;

import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.product.dto.ProductDto;
import com.packtpub.microservices.springboot.product.persistence.ProductRepository;
import com.packtpub.microservices.springboot.product.services.ProductMapper;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Slf4j
@Component
@AllArgsConstructor
public class ProductBean {

  private final ProductMapper mapper;
  private final ServiceUtil serviceUtil;
  private final ProductRepository repository;

  public ProductDto getProductById(@Body int productId) {

    // var product = new Product(productId, 123, "name-" + productId, serviceUtil.getServiceAddress());

    log.info("getProductById fired with {} param", productId);

    var entity = this.repository.findByProductId(productId)
            .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

    var response = mapper.entityToApi(entity);

    response.setServiceAddress(serviceUtil.getServiceAddress());

    return new ProductDto(response);
  }

  public void createProduct() {
    log.info("under construction....");
  }
}
