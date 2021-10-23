package com.packtpub.microservices.springboot.review.mediation.beans;


import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.review.mediation.dto.ReviewDto;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ReviewBean {
  private final ServiceUtil serviceUtil;


  @Handler
  public ReviewDto myReviews(@Body int productId) {
    var list = new ArrayList<Review>();
    list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

    return new ReviewDto(list);
  }
}
