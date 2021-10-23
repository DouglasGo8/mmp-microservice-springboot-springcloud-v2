package com.packtpub.microservices.springboot.recommendation.mediation.beans;

import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.recommendation.mediation.dto.RecommendationDto;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class RecommendationBean {

  private final ServiceUtil serviceUtil;

  @Handler
  public RecommendationDto getRecommendations(final @Body int productId) {
    //
    var list = new ArrayList<Recommendation>();
    //
    list.add(new Recommendation(1, productId, 1, "Author 1", "Content 1",
            serviceUtil.getServiceAddress()));
    list.add(new Recommendation(2, productId, 2, "Author 2", "Content 2",
            serviceUtil.getServiceAddress()));
    list.add(new Recommendation(3, productId, 3, "Author 3", "Content 3",
            serviceUtil.getServiceAddress()));
    //
    return new RecommendationDto(list);

  }
}
