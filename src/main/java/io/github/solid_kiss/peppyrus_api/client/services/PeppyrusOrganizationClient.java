package io.github.solid_kiss.peppyrus_api.client.services;


import io.github.solid_kiss.peppyrus_api.client.PeppyrusApiException;
import io.github.solid_kiss.peppyrus_api.client.PeppyrusBaseClient;
import io.github.solid_kiss.peppyrus_api.client.PeppyrusClientConfig;
import io.github.solid_kiss.peppyrus_api.model.OrganizationInfo;
import io.github.solid_kiss.peppyrus_api.model.OrganizationPeppolInfo;

public class PeppyrusOrganizationClient extends PeppyrusBaseClient {

  public PeppyrusOrganizationClient(PeppyrusClientConfig config) {
    super(config);
  }

  public OrganizationInfo getInfo() throws PeppyrusApiException {
    return sendGet("/organization/info", OrganizationInfo.class);
  }

  public OrganizationPeppolInfo getPeppol() throws PeppyrusApiException {
    return sendGet("/organization/peppol", OrganizationPeppolInfo.class);
  }
}
