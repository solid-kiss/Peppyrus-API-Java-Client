package io.github.solid_kiss.peppyrus_api.client;

import io.github.solid_kiss.peppyrus_api.client.services.PeppyrusMessageClient;
import io.github.solid_kiss.peppyrus_api.model.Message;
import io.github.solid_kiss.peppyrus_api.model.MessageBody;
import io.github.solid_kiss.peppyrus_api.model.MessageList;
import io.github.solid_kiss.peppyrus_api.model.MessageReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PeppyrusMessageClientTest {

  private PeppyrusMessageClient messageClient;
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

    messageClient = new PeppyrusMessageClient(config);
  }

  @Test
  void testPostMessage_Success() throws Exception {

    MessageBody messageBody = new MessageBody();
    messageBody.setSender("9925:be0123456789");
    messageBody.setRecipient("9925:be9876543210");
    messageBody.setProcessType("cenbii-procid-ubl::urn:fdc:peppol.eu:2017:poacc:billing:01:1.0");
    messageBody.setDocumentType("busdox-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
    messageBody.setFileContent("base64content");

    Message expectedMessage = new Message();
    expectedMessage.setId("message-123");
    expectedMessage.setSender("9925:be0123456789");
    expectedMessage.setRecipient("9925:be9876543210");
    expectedMessage.setConfirmed(false);

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedMessage));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    Message result = messageClient.postMessage(messageBody);

    assertNotNull(result);
    assertEquals("message-123", result.getId());
    assertEquals("9925:be0123456789", result.getSender());
    assertEquals("9925:be9876543210", result.getRecipient());
    assertFalse(result.getConfirmed());

    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    HttpRequest capturedRequest = requestCaptor.getValue();
    assertTrue(capturedRequest.headers().firstValue("X-Api-Key").isPresent());
    assertEquals(TEST_API_KEY, capturedRequest.headers().firstValue("X-Api-Key").get());
    assertEquals("POST", capturedRequest.method());
    assertTrue(capturedRequest.uri().toString().endsWith("/message"));
  }

  @Test
  void testListMessages_WithoutParams() throws Exception {
    MessageList expectedList = new MessageList();
    expectedList.setMeta(new MessageList.Meta(1, 1, 2));

    Message msg1 = new Message();
    msg1.setId("msg-1");
    Message msg2 = new Message();
    msg2.setId("msg-2");
    expectedList.setItems(Set.of(msg1, msg2));

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedList));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    MessageList result = messageClient.listMessages();

    assertNotNull(result);
    assertEquals(2, result.getItems().size());
    assertEquals(1, result.getMeta().pages());
    assertEquals(2, result.getMeta().itemCount());
  }

  @Test
  void testListMessages_WithParams() throws Exception {
    MessageList expectedList = new MessageList();
    expectedList.setMeta(new MessageList.Meta(1, 1, 1));
    expectedList.setItems(Set.of(new Message()));

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedList));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    PeppyrusMessageClient.MessageListParams params = new PeppyrusMessageClient.MessageListParams()
            .folder("INBOX")
            .confirmed(false)
            .page(1)
            .perPage(10);

    MessageList result = messageClient.listMessages(params);

    assertNotNull(result);

    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    String uri = requestCaptor.getValue().uri().toString();
    assertTrue(uri.contains("folder=INBOX"));
    assertTrue(uri.contains("confirmed=false"));
    assertTrue(uri.contains("page=1"));
    assertTrue(uri.contains("perPage=10"));
  }

  @Test
  void testGetMessage_Success() throws Exception {
    String messageId = "message-123";
    Message expectedMessage = new Message();
    expectedMessage.setId(messageId);
    expectedMessage.setSender("9925:be0123456789");

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedMessage));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    Message result = messageClient.getMessage(messageId);

    assertNotNull(result);
    assertEquals(messageId, result.getId());
    assertEquals("9925:be0123456789", result.getSender());
  }

  @Test
  void testGetMessage_NotFound() throws Exception {
    when(mockResponse.statusCode()).thenReturn(404);
    when(mockResponse.body()).thenReturn("Message not found");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    PeppyrusApiException exception = assertThrows(PeppyrusApiException.class,
            () -> messageClient.getMessage("non-existent-id"));

    assertTrue(exception.isNotFoundError());
    assertEquals(404, exception.getStatusCode());
  }

  @Test
  void testConfirmMessage_Success() throws Exception {
    String messageId = "message-123";
    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn("true");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    Boolean result = messageClient.confirmMessage(messageId);

    assertNotNull(result);
    assertTrue(result);

    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
    assertEquals("PATCH", requestCaptor.getValue().method());
    assertTrue(requestCaptor.getValue().uri().toString().contains("/confirm"));
  }

  @Test
  void testDeleteMessage_Success() throws Exception {
    String messageId = "message-123";
    when(mockResponse.statusCode()).thenReturn(204);
    when(mockResponse.body()).thenReturn("");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    assertDoesNotThrow(() -> messageClient.deleteMessage(messageId));

    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
    assertEquals("DELETE", requestCaptor.getValue().method());
  }

  @Test
  void testGetMessageReport_Success() throws Exception {
    String messageId = "message-123";
    MessageReport expectedReport = new MessageReport();
    expectedReport.setTransmissionRules("rules");

    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(objectMapper.writeValueAsString(expectedReport));
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    MessageReport result = messageClient.getMessageReport(messageId);

    assertNotNull(result);
    assertEquals("rules", result.getTransmissionRules());
  }

  @Test
  void testAuthenticationError() throws Exception {
    when(mockResponse.statusCode()).thenReturn(401);
    when(mockResponse.body()).thenReturn("Authentication failed");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    PeppyrusApiException exception = assertThrows(PeppyrusApiException.class,
            () -> messageClient.listMessages());

    assertTrue(exception.isAuthenticationError());
    assertEquals(401, exception.getStatusCode());
    assertTrue(exception.getMessage().contains("Authentication failed"));
  }

  @Test
  void testValidationError() throws Exception {
    MessageBody invalidBody = new MessageBody();
    when(mockResponse.statusCode()).thenReturn(422);
    when(mockResponse.body()).thenReturn("Invalid message body");
    when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);

    PeppyrusApiException exception = assertThrows(PeppyrusApiException.class,
            () -> messageClient.postMessage(invalidBody));

    assertTrue(exception.isValidationError());
    assertEquals(422, exception.getStatusCode());
  }

  @Test
  void testMessageListParams_Builder() {
    PeppyrusMessageClient.MessageListParams params = new PeppyrusMessageClient.MessageListParams()
            .folder("INBOX")
            .sender("9925:be0123456789")
            .receiver("9925:be9876543210")
            .confirmed(true)
            .page(2)
            .perPage(25);

    assertEquals("INBOX", params.getFolder());
    assertEquals("9925:be0123456789", params.getSender());
    assertEquals("9925:be9876543210", params.getReceiver());
    assertTrue(params.getConfirmed());
    assertEquals(2, params.getPage());
    assertEquals(25, params.getPerPage());
  }
}
