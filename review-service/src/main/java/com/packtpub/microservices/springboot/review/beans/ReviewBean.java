package com.packtpub.microservices.springboot.review.beans;


import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.review.repository.ReviewRepository;
import com.packtpub.microservices.springboot.review.services.ReviewMapper;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
public class ReviewBean {

  private final ReviewMapper mapper;
  private final ServiceUtil serviceUtil;
  private final ReviewRepository repository;

  public /*ReviewDto*/ Flux<Review> retrieveReviews(/*@Body*/ int productId) {

    /*var list = new ArrayList<Review>();
    list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

    return new ReviewDto(list);*/

    //if (productId < 1) {
    //  throw new InvalidInputException("Invalid productId: " + productId);
    //}

    /*var entityList = repository.findByProductId(productId);
    var list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.info("getReviews: response size: {}", list.size());

    return new ReviewDto(list);*/

    log.info("Will get reviews for product with id= _2 {}", productId);

    return this.repository.findByProductId(productId)
            .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
            .log(log.getName(), Level.FINE)
            .map(mapper::entityToApi)
            .map(this::setServiceAddress);
  }

  public /*Review*/ Mono<Review> createReview(/*@Body*/ Review body) {
    /*try {
      var entity = mapper.apiToEntity(body);
      var newEntity = repository.save(entity);
      log.info("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " +
              body.getProductId() + ", Review Id:" + body.getReviewId());
    }*/
    //
    log.info("createReview: created a review entity _ 2: {}/{}", body.getProductId(), body.getReviewId());
    //
    var entity = mapper.apiToEntity(body);
    //
    return this.repository.save(entity)
            .log(log.getName(), Level.FINE)
            .onErrorMap(DataIntegrityViolationException.class,
                    ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", " +
                            "Recommendation Id:" + body.getReviewId()))
            .map(mapper::entityToApi);

  }

  public /*void*/ Mono<Void> deleteReview(/*@Body*/ int productId) {
    log.info("deleteReviews: tries to delete reviews for the product with productId: _ 2 {}", productId);
    //repository.deleteAll(repository.findByProductId(productId));
    return this.repository.deleteAll(this.repository.findByProductId(productId));
  }


  private Review setServiceAddress(Review e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}
