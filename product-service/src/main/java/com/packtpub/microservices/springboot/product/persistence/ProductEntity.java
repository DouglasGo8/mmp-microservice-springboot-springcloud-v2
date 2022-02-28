package com.packtpub.microservices.springboot.product.persistence;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author dougdb
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class ProductEntity {

  @Id
  private String id;

  @Version
  private Integer version;

  @Indexed(unique = true)
  private int productId;

  private int productWeight;

  private String productName;

  public ProductEntity(int productId, String productName, int productWeight) {
    this.productId = productId;
    this.productName = productName;
    this.productWeight = productWeight;
  }


}
