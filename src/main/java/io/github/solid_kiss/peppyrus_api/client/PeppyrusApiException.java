package io.github.solid_kiss.peppyrus_api.client;


public class PeppyrusApiException extends Exception {

  private final Integer statusCode;

  public PeppyrusApiException(String message) {
    super(message);
    this.statusCode = null;
  }

  public PeppyrusApiException(String message, Throwable cause) {
    super(message, cause);
    this.statusCode = null;
  }

  public PeppyrusApiException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public boolean isAuthenticationError() {
    return statusCode != null && statusCode == 401;
  }

  public boolean isNotFoundError() {
    return statusCode != null && statusCode == 404;
  }

  public boolean isValidationError() {
    return statusCode != null && statusCode == 422;
  }
}
