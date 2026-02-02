package io.github.solid_kiss.peppyrus_api.client.services;

import io.github.solid_kiss.peppyrus_api.client.PeppyrusApiException;
import io.github.solid_kiss.peppyrus_api.client.PeppyrusBaseClient;
import io.github.solid_kiss.peppyrus_api.client.PeppyrusClientConfig;
import io.github.solid_kiss.peppyrus_api.model.Message;
import io.github.solid_kiss.peppyrus_api.model.MessageBody;
import io.github.solid_kiss.peppyrus_api.model.MessageList;
import io.github.solid_kiss.peppyrus_api.model.MessageReport;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PeppyrusMessageClient extends PeppyrusBaseClient {

  public PeppyrusMessageClient(PeppyrusClientConfig config) {
    super(config);
  }

  public Message postMessage(MessageBody messageBody) throws PeppyrusApiException {
    return sendPost("message", messageBody, Message.class);
  }

  public MessageList listMessages(MessageListParams params) throws PeppyrusApiException {
    String path = "message/list" + buildQueryString(params);
    return sendGet(path, MessageList.class);
  }

  public MessageList listMessages() throws PeppyrusApiException {
    return listMessages(new MessageListParams());
  }

  public Message getMessage(String messageId) throws PeppyrusApiException {
    return sendGet("message/" + messageId, Message.class);
  }

  public void deleteMessage(String messageId) throws PeppyrusApiException {
    sendDelete("message/" + messageId);
  }

  public Boolean confirmMessage(String messageId) throws PeppyrusApiException {
    return sendPatch("message/" + messageId + "/confirm", Boolean.class);
  }

  public MessageReport getMessageReport(String messageId) throws PeppyrusApiException {
    return sendGet("message/" + messageId + "/report", MessageReport.class);
  }

  private String buildQueryString(MessageListParams params) {
    Map<String, String> queryParams = new HashMap<>();

    if (params.getFolder() != null) {
      queryParams.put("folder", params.getFolder());
    }
    if (params.getSender() != null) {
      queryParams.put("sender", params.getSender());
    }
    if (params.getReceiver() != null) {
      queryParams.put("receiver", params.getReceiver());
    }
    if (params.getConfirmed() != null) {
      queryParams.put("confirmed", params.getConfirmed().toString());
    }
    if (params.getPage() != null) {
      queryParams.put("page", params.getPage().toString());
    }
    if (params.getPerPage() != null) {
      queryParams.put("perPage", params.getPerPage().toString());
    }

    if (queryParams.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder("?");
    queryParams.forEach((key, value) -> {
      if (sb.length() > 1) {
        sb.append("&");
      }
      sb.append(key).append("=").append(encode(value));
    });

    return sb.toString();
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

   public static class MessageListParams {
    private String folder;
    private String sender;
    private String receiver;
    private Boolean confirmed;
    private Integer page;
    private Integer perPage;

    public MessageListParams folder(String folder) {
      this.folder = folder;
      return this;
    }

    public MessageListParams sender(String sender) {
      this.sender = sender;
      return this;
    }

    public MessageListParams receiver(String receiver) {
      this.receiver = receiver;
      return this;
    }

    public MessageListParams confirmed(Boolean confirmed) {
      this.confirmed = confirmed;
      return this;
    }

    public MessageListParams page(Integer page) {
      this.page = page;
      return this;
    }

    public MessageListParams perPage(Integer perPage) {
      this.perPage = perPage;
      return this;
    }

    // Getters
    public String getFolder() { return folder; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public Boolean getConfirmed() { return confirmed; }
    public Integer getPage() { return page; }
    public Integer getPerPage() { return perPage; }
  }
}
