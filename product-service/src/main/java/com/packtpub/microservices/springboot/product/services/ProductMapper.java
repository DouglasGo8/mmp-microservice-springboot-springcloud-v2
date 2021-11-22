package com.packtpub.microservices.springboot.product.services;

import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.product.persistence.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author dougdb
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
  /**
   * @param entity Entity to Be persisted
   * @return Product domain
   */
  @Mappings({@Mapping(target = "serviceAddress", ignore = true)})
  Product entityToApi(ProductEntity entity);

  /**
   * @param api Product Domain
   * @return Entity to be persisted
   */
  @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true)})
  ProductEntity apiToEntity(Product api);
}
