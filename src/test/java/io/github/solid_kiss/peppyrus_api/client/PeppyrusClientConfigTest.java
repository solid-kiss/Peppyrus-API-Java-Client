package io.github.solid_kiss.peppyrus_api.client;


import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

class PeppyrusClientConfigTest {

  private final static String TEST_API_KEY = "test-api-key";
  private final static String PROD_API_KEY = "prod-api-key";

  @Test
  void testBuilder_WithRequiredFields() {
    // Arrange & Act
    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(TEST_API_KEY)
            .baseUri(PeppyrusClientConfig.TEST_URI)
            .build();

    // Assert
    assertNotNull(config);
    assertEquals(TEST_API_KEY, config.getApiKey());
    assertEquals("https://api.test.peppyrus.be/v1", config.getBaseUri().toString());
    assertNotNull(config.getHttpClient());
  }

  @Test
  void testBuilder_WithTestEnvironment() {
    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(TEST_API_KEY)
            .withEnv(PeppyrusEnv.TEST)
            .build();

    assertNotNull(config);
    assertEquals(PeppyrusClientConfig.TEST_URI, config.getBaseUri());
  }

  @Test
  void testBuilder_WithProductionEnvironment() {
    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(PROD_API_KEY)
            .withEnv(PeppyrusEnv.PROD)
            .build();

    assertNotNull(config);
    assertEquals(PeppyrusClientConfig.PROD_URI, config.getBaseUri());
  }

  @Test
  void testBuilder_WithCustomHttpClient() {
    HttpClient customClient = HttpClient.newBuilder().build();

    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(TEST_API_KEY)
            .baseUri(PeppyrusClientConfig.TEST_URI)
            .httpClient(customClient)
            .build();

    assertNotNull(config);
    assertSame(customClient, config.getHttpClient());
  }

  @Test
  void testBuilder_MissingApiKey_ThrowsException() {
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      PeppyrusClientConfig.builder()
              .baseUri(PeppyrusClientConfig.TEST_URI)
              .build();
    });

    assertEquals("API key is required", exception.getMessage());
  }

  @Test
  void testBuilder_BlankApiKey_ThrowsException() {
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      PeppyrusClientConfig.builder()
              .apiKey("   ")
              .baseUri(PeppyrusClientConfig.TEST_URI)
              .build();
    });

    assertEquals("API key is required", exception.getMessage());
  }

  @Test
  void testBuilder_MissingBaseUri_ThrowsException() {
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      PeppyrusClientConfig.builder()
              .apiKey(TEST_API_KEY)
              .build();
    });

    assertEquals("Base URI is required", exception.getMessage());
  }

  @Test
  void testBuilder_CreatesDefaultHttpClient() {
    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(TEST_API_KEY)
            .baseUri(PeppyrusClientConfig.TEST_URI)
            .build();

    HttpClient httpClient = config.getHttpClient();
    assertNotNull(httpClient);
    assertEquals(HttpClient.Version.HTTP_2, httpClient.version());
  }

}
