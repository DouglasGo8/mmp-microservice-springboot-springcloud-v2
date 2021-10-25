package com.packtpub.microservices.springboot.product.composite.mediation.dto;

import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.core.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dougdb
 */
@Getter
@Setter
@Component
@NoArgsConstructor
public class ProductAggregateDto {

    private Product product;
    private List<Review> reviews;
    private List<Recommendation> recommendations;

    public ProductAggregateDto(final Product product) {
        this.product = product;
    }

    public ProductAggregateDto(final List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }


}
