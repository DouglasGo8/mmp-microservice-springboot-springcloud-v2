package com.packtpub.microservices.springboot.review.services;

import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.review.repository.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

  @Mappings({@Mapping(target = "serviceAddress", ignore = true)})
  Review entityToApi(ReviewEntity entity);

  @Mappings({@Mapping(target = "id", ignore = true)})
  ReviewEntity apiToEntity(Review api);

  List<Review> entityListToApiList(List<ReviewEntity> entity);

  List<ReviewEntity> apiListToEntityList(List<Review> api);

}
