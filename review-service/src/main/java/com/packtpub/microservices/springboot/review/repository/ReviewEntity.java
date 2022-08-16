package com.packtpub.microservices.springboot.review.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author dougdb
 */
@Getter
@Setter
@NoArgsConstructor
@Table(value = "reviews")
//@Table(name = "reviews", indexes = {@Index(name = "reviews_unique_idx", unique = true, columnList = "productId,reviewId")})
public class ReviewEntity {

  @Id
  private int id;

  //@Version
  //private int version;

  @Column(value = "productId")
  private int productId;
  @Column(value = "reviewId")
  private int reviewId;

    private String author;
    private String subject;
  private String content;


  public ReviewEntity(int productId, int reviewId, String author, String subject, String content) {
    this.productId = productId;
    this.reviewId = reviewId;
    this.author = author;
    this.subject = subject;
    this.content = content;
  }

}
