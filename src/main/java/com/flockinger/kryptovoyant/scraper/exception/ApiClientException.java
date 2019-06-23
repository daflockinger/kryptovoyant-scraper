package com.flockinger.kryptovoyant.scraper.exception;

public class ApiClientException extends ScraperException {

  public ApiClientException(String message) {
    super(message);
  }

  public ApiClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
