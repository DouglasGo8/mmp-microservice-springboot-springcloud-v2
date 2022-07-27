package com.packtpub.microservices.springboot.apis.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.Getter;

import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.now;

/**
 * @param <K>
 * @param <T>
 * @author dougdb
 */
public class Event<K, T> {

  public enum Type {CREATE, DELETE}

  @Getter
  private final K key;
  @Getter
  private final T data;
  //
  @Getter
  private final Type eventType;
  private final ZonedDateTime eventCreatedAt;

  public Event() {
    this.key = null;
    this.data = null;
    this.eventType = null;
    this.eventCreatedAt = null;
  }

  public Event(Type eventType, K key, T data) {
    this.eventType = eventType;
    this.key = key;
    this.data = data;
    this.eventCreatedAt = now();
  }

  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  public ZonedDateTime getEventCreatedAt() {
    return eventCreatedAt;
  }
}
