package com.packtpub.microservices.springboot.recommendation;

import com.packtpub.microservices.springboot.recommendation.persistence.RecommendationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;

/**
 * @author dougdb
 */
@SpringBootApplication
// apis/utils packages explicit scan
@ComponentScan("com.packtpub")
public class RecommendationServiceApp {

  public static void main(String... args) {
    SpringApplication.run(RecommendationServiceApp.class, args);
  }

  @Autowired
  ReactiveMongoOperations mongoOps;

  @EventListener(ContextRefreshedEvent.class)
  public void setupIndicesAfterStartup() {
    var mapCtx = this.mongoOps
            .getConverter().getMappingContext();
    var resolver = new MongoPersistentEntityIndexResolver(mapCtx);
    var ops = mongoOps.indexOps(RecommendationEntity.class);
    //
    resolver.resolveIndexFor(RecommendationEntity.class).forEach(e -> ops.ensureIndex(e).block());
  }
}
