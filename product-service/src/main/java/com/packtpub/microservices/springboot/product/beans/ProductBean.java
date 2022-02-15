package com.packtpub.microservices.springboot.product.beans;

import com.mongodb.DuplicateKeyException;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
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

  /**
   * @param productId id of Product
   * @return mapped ProductDto
   */
  public ProductDto getProductById(@Body int productId) {

    // var product = new Product(productId, 123, "name-" + productId, serviceUtil.getServiceAddress());

    log.info("getProductById fired with {} param", productId);

    var entity = this.repository.findByProductId(productId)
            .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

    var response = mapper.entityToApi(entity);

    response.setServiceAddress(serviceUtil.getServiceAddress());

    return new ProductDto(response);
  }

  /**
   * @param product to be Saved
   * @return mapped ProductDto
   */
  public ProductDto createProduct(@Body Product product) {

    log.info("createProduct fired={}", product);

    try {
      var entity = mapper.apiToEntity(product);
      var newEntity = this.repository.save(entity);

      return new ProductDto(mapper.entityToApi(newEntity));
    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Product Id" + product.getProductId());
    }
  }

  /**
   *
   * @param productId to be deleted
   */
  public void deleteProduct(@Body int productId) {
    this.repository.findByProductId(productId).ifPresent(repository::delete);
  }
}
