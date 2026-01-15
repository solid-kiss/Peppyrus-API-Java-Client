package be.solid_kiss.peppyrus_api.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeppyrusApiExceptionTest {

  @Test
  void testConstructor_WithMessage() {
    String message = "Test error message";

    PeppyrusApiException exception = new PeppyrusApiException(message);

    assertEquals(message, exception.getMessage());
    assertNull(exception.getStatusCode());
    assertFalse(exception.isAuthenticationError());
    assertFalse(exception.isNotFoundError());
    assertFalse(exception.isValidationError());
  }

  @Test
  void testConstructor_WithMessageAndCause() {
    String message = "Test error message";
    Throwable cause = new RuntimeException("Root cause");

    PeppyrusApiException exception = new PeppyrusApiException(message, cause);

    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertNull(exception.getStatusCode());
  }

  @Test
  void testConstructor_WithMessageAndStatusCode() {
    String message = "API error";
    int statusCode = 500;

    PeppyrusApiException exception = new PeppyrusApiException(message, statusCode);

    assertEquals(message, exception.getMessage());
    assertEquals(statusCode, exception.getStatusCode());
  }

  @Test
  void testIsAuthenticationError_Returns401() {
    PeppyrusApiException exception = new PeppyrusApiException("Auth failed", 401);

    assertTrue(exception.isAuthenticationError());
    assertFalse(exception.isNotFoundError());
    assertFalse(exception.isValidationError());
    assertEquals(401, exception.getStatusCode());
  }

  @Test
  void testIsNotFoundError_Returns404() {
    PeppyrusApiException exception = new PeppyrusApiException("Not found", 404);

    assertTrue(exception.isNotFoundError());
    assertFalse(exception.isAuthenticationError());
    assertFalse(exception.isValidationError());
    assertEquals(404, exception.getStatusCode());
  }

  @Test
  void testIsValidationError_Returns422() {
    PeppyrusApiException exception = new PeppyrusApiException("Validation failed", 422);

    assertTrue(exception.isValidationError());
    assertFalse(exception.isAuthenticationError());
    assertFalse(exception.isNotFoundError());
    assertEquals(422, exception.getStatusCode());
  }

  @Test
  void testIsAuthenticationError_WithNoStatusCode() {
    PeppyrusApiException exception = new PeppyrusApiException("Error");

    assertFalse(exception.isAuthenticationError());
    assertNull(exception.getStatusCode());
  }

  @Test
  void testIsNotFoundError_WithDifferentStatusCode() {
    PeppyrusApiException exception = new PeppyrusApiException("Error", 500);

    assertFalse(exception.isNotFoundError());
    assertEquals(500, exception.getStatusCode());
  }

  @Test
  void testIsValidationError_WithDifferentStatusCode() {
    PeppyrusApiException exception = new PeppyrusApiException("Error", 400);

    assertFalse(exception.isValidationError());
    assertEquals(400, exception.getStatusCode());
  }

  @Test
  void testGetStatusCode_ReturnsCorrectValue() {
    PeppyrusApiException exception1 = new PeppyrusApiException("Error", 400);
    PeppyrusApiException exception2 = new PeppyrusApiException("Error", 500);
    PeppyrusApiException exception3 = new PeppyrusApiException("Error");

    assertEquals(400, exception1.getStatusCode());
    assertEquals(500, exception2.getStatusCode());
    assertNull(exception3.getStatusCode());
  }

  @Test
  void testExceptionIsThrowable() {
    PeppyrusApiException exception = new PeppyrusApiException("Test");

    assertInstanceOf(Exception.class, exception);
    assertInstanceOf(Throwable.class, exception);
  }

  @Test
  void testMultipleStatusCodes() {
    assertEquals(200, new PeppyrusApiException("OK", 200).getStatusCode());
    assertEquals(301, new PeppyrusApiException("Moved", 301).getStatusCode());
    assertEquals(400, new PeppyrusApiException("Bad Request", 400).getStatusCode());
    assertEquals(403, new PeppyrusApiException("Forbidden", 403).getStatusCode());
    assertEquals(500, new PeppyrusApiException("Internal Error", 500).getStatusCode());
    assertEquals(503, new PeppyrusApiException("Service Unavailable", 503).getStatusCode());
  }
}
