package be.solid_kiss.peppyrus_api.client;


import be.solid_kiss.peppyrus_api.client.services.PeppyrusOrganizationClient;
import be.solid_kiss.peppyrus_api.model.OrganizationInfo;
import be.solid_kiss.peppyrus_api.model.OrganizationPeppolInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PeppyrusOrganizationClientTest {

  private PeppyrusOrganizationClient organizationClient;
  private HttpClient mockHttpClient;
  private HttpResponse<String> mockResponse;
  private ObjectMapper objectMapper;

  private static final String TEST_API_KEY = "test-api-key";

  @BeforeEach
  void setUp() {
    mockHttpClient = mock(HttpClient.class);
    mockResponse = mock(HttpResponse.class);
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(TEST_API_KEY)
            .baseUri(PeppyrusClientConfig.TEST_URI)
            .httpClient(mockHttpClient)
            .build();

    organizationClient = new PeppyrusOrganizationClient(config);
  }

  @Test
  void testGetInfo_Success() throws Exception {
    OrganizationInfo expectedInfo = new OrganizationInfo();
    expectedInfo.setName("Test Company");
    expectedInfo.setVAT("BE0123456789");
    expectedInfo.setStreet("Test Street");
    expectedInfo.setHouseNumber("123");
    expectedInfo.setZipCode("1000");
    expectedInfo.setCity("Brussels");
    expectedInfo.setCountry("Belgium");

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedInfo));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    OrganizationInfo result = organizationClient.getInfo();

    assertNotNull(result);
    assertEquals("Test Company", result.getName());
    assertEquals("BE0123456789", result.getVAT());
    assertEquals("Test Street", result.getStreet());
    assertEquals("123", result.getHouseNumber());
    assertEquals("1000", result.getZipCode());
    assertEquals("Brussels", result.getCity());
    assertEquals("Belgium", result.getCountry());

    // Verify correct endpoint was called
    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    HttpRequest capturedRequest = requestCaptor.getValue();
    assertTrue(capturedRequest.uri().toString().endsWith("/organization/info"));
    assertEquals("GET", capturedRequest.method());
  }

  @Test
  void testGetPeppol_Success() throws Exception {
    OrganizationPeppolInfo.Participant participant =
            new OrganizationPeppolInfo.Participant("9925:be0123456789", true, true);

    OrganizationPeppolInfo expectedInfo = new OrganizationPeppolInfo();
    expectedInfo.setParticipants(participant);

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedInfo));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    OrganizationPeppolInfo result = organizationClient.getPeppol();

    assertNotNull(result);
    assertNotNull(result.getParticipants());
    assertEquals("9925:be0123456789", result.getParticipants().participantId());
    assertTrue(result.getParticipants().canReceive());
    assertTrue(result.getParticipants().isSender());

    // Verify correct endpoint was called
    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    HttpRequest capturedRequest = requestCaptor.getValue();
    assertTrue(capturedRequest.uri().toString().endsWith("/organization/peppol"));
    assertEquals("GET", capturedRequest.method());
  }

  @Test
  void testGetInfo_AuthenticationError() throws Exception {
    when(mockResponse.statusCode()).thenReturn(401);
    when(mockResponse.body()).thenReturn("Authentication failed");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    // Act & Assert
    PeppyrusApiException exception = assertThrows(PeppyrusApiException.class,
            () -> organizationClient.getInfo());

    assertTrue(exception.isAuthenticationError());
    assertEquals(401, exception.getStatusCode());
  }

  @Test
  void testGetPeppol_NotFound() throws Exception {
    when(mockResponse.statusCode()).thenReturn(404);
    when(mockResponse.body()).thenReturn("Organization not found");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    // Act & Assert
    PeppyrusApiException exception = assertThrows(PeppyrusApiException.class,
            () -> organizationClient.getPeppol());

    assertTrue(exception.isNotFoundError());
    assertEquals(404, exception.getStatusCode());
  }

  @Test
  void testGetInfo_WithApiKeyHeader() throws Exception {
    OrganizationInfo expectedInfo = new OrganizationInfo();
    expectedInfo.setName("Test Company");

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedInfo));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    organizationClient.getInfo();

    // Assert - verify API key is included in header
    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    HttpRequest capturedRequest = requestCaptor.getValue();
    assertTrue(capturedRequest.headers().firstValue("X-Api-Key").isPresent());
    assertEquals(TEST_API_KEY, capturedRequest.headers().firstValue("X-Api-Key").get());
    assertTrue(capturedRequest.headers().firstValue("Content-Type").isPresent());
    assertEquals("application/json", capturedRequest.headers().firstValue("Content-Type").get());
  }

  @Test
  void testGetPeppol_CanReceiveFalse() throws Exception {
    OrganizationPeppolInfo.Participant participant =
            new OrganizationPeppolInfo.Participant("9925:be0123456789", false, true);

    OrganizationPeppolInfo expectedInfo = new OrganizationPeppolInfo();
    expectedInfo.setParticipants(participant);

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedInfo));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    OrganizationPeppolInfo result = organizationClient.getPeppol();

    assertNotNull(result);
    assertFalse(result.getParticipants().canReceive());
    assertTrue(result.getParticipants().isSender());
  }
}
