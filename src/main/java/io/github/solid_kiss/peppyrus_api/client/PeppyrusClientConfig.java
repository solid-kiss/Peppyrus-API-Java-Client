package io.github.solid_kiss.peppyrus_api.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

/**
 *  Peppyrus API client configuration
 *
 * @author jona
 * @version $Id: $Id
 */
public class PeppyrusClientConfig {

  private final String apiKey;
  private final URI baseUri;
  private final HttpClient httpClient;

  /** Constant <code>TEST_URI</code> */
  public final static URI TEST_URI = URI.create("https://api.test.peppyrus.be/v1");
  /** Constant <code>PROD_URI</code> */
  public final static URI PROD_URI = URI.create("https://api.peppyrus.be/v1");

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

  /**
   * <p>Getter for the field <code>apiKey</code>.</p>
   *
   * @return a {@link java.lang.String} object
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * <p>Getter for the field <code>baseUri</code>.</p>
   *
   * @return a {@link java.net.URI} object
   */
  public URI getBaseUri() {
    return baseUri;
  }

  /**
   * <p>Getter for the field <code>httpClient</code>.</p>
   *
   * @return a HttpClient object
   */
  public HttpClient getHttpClient() {
    return httpClient;
  }

  /**
   * <p>builder.</p>
   *
   * @return a {@link io.github.solid_kiss.peppyrus_api.client.PeppyrusClientConfig.Builder} object
   */
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

}
