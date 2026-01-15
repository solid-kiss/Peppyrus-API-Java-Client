package be.solid_kiss.peppyrus_api.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

/**
 *  Peppyrus API client configuration
 */
public class PeppyrusClientConfig {

  private final String apiKey;
  private final URI baseUri;
  private final HttpClient httpClient;

  private final static URI TEST_URI = URI.create("https://api.test.peppyrus.be/v1");
  private final static URI PROD_URI = URI.create("https://api.peppyrus.be/v1");

  private PeppyrusClientConfig(Builder builder) {
    this.apiKey = builder.apiKey;
    this.baseUri = builder.baseUri;
    this.httpClient = builder.httpClient != null ? builder.httpClient : createDefaultHttpClient();
  }

  private HttpClient createDefaultHttpClient() {
    return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(30))
            .build();
  }

  public String getApiKey() {
    return apiKey;
  }

  public URI getBaseUri() {
    return baseUri;
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String apiKey;
    private URI baseUri;
    private HttpClient httpClient;

    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder baseUri(String baseUri) {
      this.baseUri = URI.create(baseUri);
      return this;
    }

    public Builder baseUri(URI baseUri) {
      this.baseUri = baseUri;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder withEnv(PeppyrusEnv peppyrusEnv) {
      this.baseUri = peppyrusEnv.equals(PeppyrusEnv.TEST) ? TEST_URI : PROD_URI;
      return this;
    }

    public PeppyrusClientConfig build() {
      if (apiKey == null || apiKey.isBlank()) {
        throw new IllegalStateException("API key is required");
      }
      if (baseUri == null) {
        throw new IllegalStateException("Base URI is required");
      }
      return new PeppyrusClientConfig(this);
    }
  }

  public enum PeppyrusEnv {
    TEST, PROD
  }
}
