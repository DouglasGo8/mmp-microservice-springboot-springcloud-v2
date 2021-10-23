package com.packtpub.microservices.springboot.product.composite.mediation.dto;

import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dougdb
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregateDto {
  private ProductAggregate productAggregate;
}
