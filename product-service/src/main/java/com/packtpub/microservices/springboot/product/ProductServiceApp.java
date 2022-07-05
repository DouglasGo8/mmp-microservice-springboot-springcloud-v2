package com.packtpub.microservices.springboot.product;

import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.product.persistence.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author dougdb
 */
@Slf4j
@SpringBootApplication
// apis/utils packages explicit scan
@ComponentScan("com.packtpub")
public class ProductServiceApp {

  public static void main(String... args) {

    var ctx = SpringApplication.run(ProductServiceApp.class, args);

    var mongoDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
    var mongoDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");

    log.info("Connected to MongoDB {}:{}", mongoDbHost, mongoDbPort);

  }

  @Autowired
  ReactiveMongoOperations mongoOps;

  @EventListener(ContextRefreshedEvent.class)
  public void setupIndicesAfterStartup() {
    var mapCtx = this.mongoOps
            .getConverter().getMappingContext();
    var resolver = new MongoPersistentEntityIndexResolver(mapCtx);
    var ops = mongoOps.indexOps(ProductEntity.class);
    //
    resolver.resolveIndexFor(ProductEntity.class).forEach(e -> ops.ensureIndex(e).block());
  }
}
