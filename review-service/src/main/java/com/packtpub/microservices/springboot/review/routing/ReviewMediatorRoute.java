package com.packtpub.microservices.springboot.review.routing;

import com.packtpub.microservices.springboot.review.beans.ReviewBean;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Slf4j
@Component
@NoArgsConstructor
public class ReviewMediatorRoute extends RouteBuilder {

  @Override
  public void configure() {

    from("{{direct.getReview.mediator.endpoint}}")
            .transform(method(ReviewBean.class, "retrieveReviews"))
            .end();

    from("{{direct.createReview.mediator.endpoint}}")
            .transform(method(ReviewBean.class, "createReview"))
            .end();

    from("{{direct.deleteReview.mediator.endpoint}}")
            .transform(method(ReviewBean.class, "deleteReview"))
            .end();

  }
}
