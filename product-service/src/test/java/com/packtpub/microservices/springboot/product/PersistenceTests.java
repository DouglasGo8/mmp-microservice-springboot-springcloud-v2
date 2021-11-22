package com.packtpub.microservices.springboot.product;


import com.packtpub.microservices.springboot.product.persistence.ProductEntity;
import com.packtpub.microservices.springboot.product.persistence.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author dougdb
 */
@Slf4j
@SpringBootTest
@CamelSpringBootTest
public class PersistenceTests {


  @Autowired
  private CamelContext camelContext;

  @Autowired
  private ProducerTemplate producerTemplate;

  @Autowired
  private ProductRepository repo;

  private ProductEntity savedEntity;


  @BeforeEach
  void setupDb() {

    log.info("********************** setupDb Fired **********************");

    this.repo.deleteAll();
    var entity = new ProductEntity(1, "n", 1);
    savedEntity = this.repo.save(entity);
    assertEqualsProduct(entity, savedEntity);
  }

  @AfterEach
  public void cleanDb() {
    log.info("********************** cleanDb Fired **********************");

    this.repo.deleteAll();
  }

  @Test
  void create() {

    var newEntity = new ProductEntity(2, "n", 2);
    this.repo.save(newEntity);
    //
    var foundEntity = this.repo.findById(newEntity.getId()).get();
    assertEqualsProduct(newEntity, foundEntity);

    assertEquals(2, this.repo.count());
  }

  @Test
  void update() {
    savedEntity.setProductName("n2");
    this.repo.save(savedEntity);

    var foundEntity = this.repo.findById(savedEntity.getId()).get();
    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("n2", foundEntity.getProductName());
  }

  @Test
  void delete() {
    this.repo.delete(savedEntity);
    assertFalse(this.repo.existsById(savedEntity.getId()));
  }

  @Test
  void getByProductId() {
    Optional<ProductEntity> entity = this.repo.findByProductId(savedEntity.getProductId());

    assertTrue(entity.isPresent());
    assertEqualsProduct(savedEntity, entity.get());
  }


  @Test
  @Disabled
  void duplicateError() {
    assertThrows(DuplicateKeyException.class, () -> {
      ProductEntity entity = new ProductEntity(savedEntity.getProductId(), "n", 1);
      this.repo.save(entity);
    });
  }

  @Test
  void optimisticLockError() {

    // Store the saved entity in two separate entity objects
    ProductEntity entity1 = this.repo.findById(savedEntity.getId()).get();
    ProductEntity entity2 = this.repo.findById(savedEntity.getId()).get();

    // Update the entity using the first entity object
    entity1.setProductName("n1");
    this.repo.save(entity1);

    // Update the entity using the second entity object.
    // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
    assertThrows(OptimisticLockingFailureException.class, () -> {
      entity2.setProductName("n2");
      this.repo.save(entity2);
    });

    // Get the updated entity from the database and verify its new sate
    ProductEntity updatedEntity = this.repo.findById(savedEntity.getId()).get();
    assertEquals(1, (int) updatedEntity.getVersion());
    assertEquals("n1", updatedEntity.getProductName());
  }

  @Test
  void paging() {

    this.repo.deleteAll();

    var newProducts = rangeClosed(1001, 1010)
            .mapToObj(i -> new ProductEntity(i, "name " + i, i))
            .collect(Collectors.toList());
    this.repo.saveAll(newProducts);

    Pageable nextPage = PageRequest.of(0, 4, ASC, "productId");

    nextPage = testNextPage(nextPage, "[1001, 1002, 1003, 1004]", true);
    nextPage = testNextPage(nextPage, "[1005, 1006, 1007, 1008]", true);
    nextPage = testNextPage(nextPage, "[1009, 1010]", false);
  }


  private Pageable testNextPage(Pageable nextPage, String expectedProductIds, boolean expectsNextPage) {
    Page<ProductEntity> productPage = this.repo.findAll(nextPage);
    assertEquals(expectedProductIds, productPage.getContent().stream().map(p -> p.getProductId()).collect(Collectors.toList()).toString());
    assertEquals(expectsNextPage, productPage.hasNext());
    return productPage.nextPageable();
  }

  private void assertEqualsProduct(ProductEntity expectedEntity, ProductEntity actualEntity) {
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getProductName(), actualEntity.getProductName());
    assertEquals(expectedEntity.getProductWeight(), actualEntity.getProductWeight());
  }

}
