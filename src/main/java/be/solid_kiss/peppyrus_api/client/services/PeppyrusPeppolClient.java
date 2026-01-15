package be.solid_kiss.peppyrus_api.client.services;

import be.solid_kiss.peppyrus_api.client.PeppyrusApiException;
import be.solid_kiss.peppyrus_api.client.PeppyrusBaseClient;
import be.solid_kiss.peppyrus_api.client.PeppyrusClientConfig;
import be.solid_kiss.peppyrus_api.model.BusinessCard;
import be.solid_kiss.peppyrus_api.model.Participant;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeppyrusPeppolClient extends PeppyrusBaseClient {

  public PeppyrusPeppolClient(PeppyrusClientConfig config) {
    super(config);
  }

  /**
   * Recherche le meilleur participant pour un numéro de TVA et un code pays
   */
  public Participant getBestMatch(String vatNumber, String countryCode) throws PeppyrusApiException {
    Map<String, String> params = new HashMap<>();
    params.put("vatNumber", vatNumber);
    params.put("countryCode", countryCode);

    String path = "/peppol/bestMatch" + buildQueryString(params);
    return sendGet(path, Participant.class);
  }

  /**
   * Recherche les capacités d'un participant dans le SMP
   */
  public Participant lookup(String participantId) throws PeppyrusApiException {
    Map<String, String> params = new HashMap<>();
    params.put("participantId", participantId);

    String path = "/peppol/lookup" + buildQueryString(params);
    return sendGet(path, Participant.class);
  }

  public List<BusinessCard> search(PeppolSearchParams params) throws PeppyrusApiException {
    String path = "/peppol/search" + buildQueryString(params.toMap());

    try {
      String response = sendGetRaw(path);
      return objectMapper.readValue(response, new TypeReference<List<BusinessCard>>() {});
    } catch (IOException e) {
      throw new PeppyrusApiException("Failed to parse search results", e);
    }
  }

  public List<BusinessCard> search(String query) throws PeppyrusApiException {
    return search(new PeppolSearchParams().query(query));
  }

  private String sendGetRaw(String path) throws PeppyrusApiException {
    var request = createRequestBuilder(path).GET().build();

    try {
      var response = config.getHttpClient()
              .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        return response.body();
      } else {
        throw new PeppyrusApiException("API error: " + response.body(), response.statusCode());
      }
    } catch (IOException e) {
      throw new PeppyrusApiException("Network error during API call", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new PeppyrusApiException("Request was interrupted", e);
    }
  }

  /**
   * Construit la query string à partir des paramètres
   */
  private String buildQueryString(Map<String, String> params) {
    if (params.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder("?");
    params.forEach((key, value) -> {
      if (value != null && !value.isBlank()) {
        if (sb.length() > 1) {
          sb.append("&");
        }
        sb.append(key).append("=").append(encode(value));
      }
    });

    return sb.length() > 1 ? sb.toString() : "";
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  /**
   * Classe pour les paramètres de recherche PEPPOL
   */
  public static class PeppolSearchParams {
    private String query;
    private String participantId;
    private String name;
    private String country;
    private String geoInfo;
    private String contact;
    private String identifierScheme;
    private String identifierValue;

    public PeppolSearchParams query(String query) {
      this.query = query;
      return this;
    }

    public PeppolSearchParams participantId(String participantId) {
      this.participantId = participantId;
      return this;
    }

    public PeppolSearchParams name(String name) {
      this.name = name;
      return this;
    }

    public PeppolSearchParams country(String country) {
      this.country = country;
      return this;
    }

    public PeppolSearchParams geoInfo(String geoInfo) {
      this.geoInfo = geoInfo;
      return this;
    }

    public PeppolSearchParams contact(String contact) {
      this.contact = contact;
      return this;
    }

    public PeppolSearchParams identifierScheme(String identifierScheme) {
      this.identifierScheme = identifierScheme;
      return this;
    }

    public PeppolSearchParams identifierValue(String identifierValue) {
      this.identifierValue = identifierValue;
      return this;
    }

    Map<String, String> toMap() {
      Map<String, String> map = new HashMap<>();
      if (query != null) map.put("query", query);
      if (participantId != null) map.put("participantId", participantId);
      if (name != null) map.put("name", name);
      if (country != null) map.put("country", country);
      if (geoInfo != null) map.put("geoInfo", geoInfo);
      if (contact != null) map.put("contact", contact);
      if (identifierScheme != null) map.put("identifierScheme", identifierScheme);
      if (identifierValue != null) map.put("identifierValue", identifierValue);
      return map;
    }
  }
}
