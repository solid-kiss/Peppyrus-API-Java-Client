package be.solid_kiss.peppyrus_api.client;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public abstract class PeppyrusBaseClient {

  protected final PeppyrusClientConfig config;
  protected final ObjectMapper objectMapper;
  private final static String API_KEY_HEADER_NAME = "X-Api-Key";

  protected PeppyrusBaseClient(PeppyrusClientConfig config) {
    this.config = config;
    this.objectMapper = createObjectMapper();
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  protected HttpRequest.Builder createRequestBuilder(String path) {
    URI uri = config.getBaseUri().resolve(path);
    return HttpRequest.newBuilder()
            .uri(uri)
            .timeout(Duration.ofSeconds(30))
            .header(API_KEY_HEADER_NAME, config.getApiKey())
            .header("Content-Type", "application/json")
            .header("Accept", "application/json");
  }


  protected <T> T sendGet(String path, Class<T> responseType) throws PeppyrusApiException {
    HttpRequest request = createRequestBuilder(path)
            .GET()
            .build();

    return sendRequest(request, responseType);
  }

  protected <T> T sendPost(String path, Object body, Class<T> responseType) throws PeppyrusApiException {
    try {
      String jsonBody = objectMapper.writeValueAsString(body);
      HttpRequest request = createRequestBuilder(path)
              .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
              .build();

      return sendRequest(request, responseType);
    } catch (IOException e) {
      throw new PeppyrusApiException("Failed to serialize request body", e);
    }
  }

  protected <T> T sendPatch(String path, Class<T> responseType) throws PeppyrusApiException {
    HttpRequest request = createRequestBuilder(path)
            .method("PATCH", HttpRequest.BodyPublishers.noBody())
            .build();

    return sendRequest(request, responseType);
  }

  protected void sendDelete(String path) throws PeppyrusApiException {
    HttpRequest request = createRequestBuilder(path)
            .DELETE()
            .build();

    sendRequest(request, Void.class);
  }

  private <T> T sendRequest(HttpRequest request, Class<T> responseType) throws PeppyrusApiException {
    try {
      HttpResponse<String> response = config.getHttpClient()
              .send(request, HttpResponse.BodyHandlers.ofString());

      return handleResponse(response, responseType);
    } catch (IOException e) {
      throw new PeppyrusApiException("Network error during API call", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new PeppyrusApiException("Request was interrupted", e);
    }
  }

  private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType)
          throws PeppyrusApiException {
    int statusCode = response.statusCode();
    String body = response.body();

    if (statusCode >= 200 && statusCode < 300) {
      if (responseType == Void.class || body == null || body.isBlank()) {
        return null;
      }
      try {
        return objectMapper.readValue(body, responseType);
      } catch (IOException e) {
        throw new PeppyrusApiException("Failed to parse response body", e);
      }
    } else {
      handleErrorResponse(statusCode, body);
      return null; // Never reached
    }
  }

  private void handleErrorResponse(int statusCode, String body) throws PeppyrusApiException {
    String message = body != null && !body.isBlank() ? body : "Unknown error";

    switch (statusCode) {
      case 401:
        throw new PeppyrusApiException("Authentication failed. Check your API key.", statusCode);
      case 404:
        throw new PeppyrusApiException("Resource not found: " + message, statusCode);
      case 422:
        throw new PeppyrusApiException("Unprocessable entity: " + message, statusCode);
      default:
        throw new PeppyrusApiException("API error (" + statusCode + "): " + message, statusCode);
    }
  }
}
