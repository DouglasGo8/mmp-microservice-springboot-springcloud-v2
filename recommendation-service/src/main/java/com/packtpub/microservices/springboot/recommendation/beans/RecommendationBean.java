package com.packtpub.microservices.springboot.recommendation.beans;

import com.mongodb.DuplicateKeyException;
import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.recommendation.persistence.RecommendationRepository;
import com.packtpub.microservices.springboot.recommendation.services.RecommendationMapper;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;


/**
 * @author dougdb
 */
@Slf4j
@Component
@AllArgsConstructor
public class RecommendationBean {

  private final ServiceUtil serviceUtil;
  private final RecommendationMapper mapper;
  private final RecommendationRepository repository;

  public Flux<Recommendation> getRecommendations(final /*@Body*/ int productId) {
    //
    /* var list = new ArrayList<Recommendation>();
    //
    list.add(new Recommendation(1, productId, 1, "Author 1", "Content 1",
            serviceUtil.getServiceAddress()));
    list.add(new Recommendation(2, productId, 2, "Author 2", "Content 2",
            serviceUtil.getServiceAddress()));
    list.add(new Recommendation(3, productId, 3, "Author 3", "Content 3",
            serviceUtil.getServiceAddress()));
    //
    return new RecommendationDto(list);*/

    // if (productId < 1) {
    //  throw new InvalidInputException("Invalid productId: " + productId);
    // }

    // var entityList = repository.findByProductId(productId);
    // var list = mapper.entityListToApiList(entityList);

    // list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    // return new RecommendationDto(list);

    log.info("Will get recommendations for product with id _ 2={}", productId);
    //
    return this.repository.findByProductId(productId)
            .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
            .log(log.getName(), Level.FINE)
            .map(mapper::entityToApi)
            //.doOnNext(e -> log.info("Fired {}", e.getProductId()))
            .map(this::setServiceAddress);

    //

    //return Flux.empty();
  }

  public /*void*/ Mono<Void> deleteRecommendation(/*@Body*/ int productId) {
    log.info("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
    //this.repository.deleteAll(repository.findByProductId(productId));
    return this.repository.deleteAll(this.repository.findByProductId(productId));
  }

  public /*RecommendationDto*/ Mono<Recommendation> createRecommendation(final /*@Body*/ Recommendation body) {

    log.info("createRecommendation: created a recommendation entity:{}-{}",
            body.getProductId(), body.getRecommendationId());

    /*try {
      var entity = mapper.apiToEntity(body);
      var newEntity = this.repository.save(entity);

      return new RecommendationDto(List.of(mapper.entityToApi(newEntity)));

    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId());
    }*/

    var entity = mapper.apiToEntity(body);
    //
    return this.repository.save(entity)
            .log(log.getName(), Level.FINE)
            .onErrorMap(DuplicateKeyException.class,
                    ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", " +
                            "Recommendation Id:" + body.getRecommendationId()))
            .map(mapper::entityToApi);
  }


  private Recommendation setServiceAddress(Recommendation e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}
