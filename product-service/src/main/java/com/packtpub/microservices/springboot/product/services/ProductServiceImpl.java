package com.packtpub.microservices.springboot.product.services;

import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.core.product.ProductService;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.product.dto.ProductDto;
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
public class ProductServiceImpl implements ProductService {

  private final ProducerTemplate producerTemplate;

  @Override
  public Product createProduct(Product body) {
    var dto = this.producerTemplate
            .requestBody("{{direct.product.routing.createProduct.endpoint}}", body, ProductDto.class);
    return dto.getProduct();
  }

  @Override
  public void deleteProduct(int productId) {
    this.producerTemplate.asyncRequestBody("{{direct.product.routing.deleteProduct.endpoint}}", productId);
  }

  @Override
  public Product getProduct(int productId) {
    log.debug("/product return the found product for productId={}", productId);
    if (productId < 1)
      throw new InvalidInputException("Invalid productId: " + productId);
    if (productId == 13)
      throw new NotFoundException("No product found for productId: " + productId);

    var dto = this.producerTemplate
            .requestBody("{{direct.product.routing.getProductById.endpoint}}", productId, ProductDto.class);

    return dto.getProduct();
  }


}
