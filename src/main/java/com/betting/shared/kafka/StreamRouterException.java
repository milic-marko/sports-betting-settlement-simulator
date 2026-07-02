package com.betting.shared.kafka;

public class StreamRouterException extends RuntimeException {
  public StreamRouterException(String message) {
    super(message);
  }

  public StreamRouterException(Throwable cause) {
    super(cause);
  }
}
