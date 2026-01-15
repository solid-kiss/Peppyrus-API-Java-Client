package be.solid_kiss.peppyrus_api.client.services;


import be.solid_kiss.peppyrus_api.client.PeppyrusApiException;
import be.solid_kiss.peppyrus_api.client.PeppyrusBaseClient;
import be.solid_kiss.peppyrus_api.client.PeppyrusClientConfig;
import be.solid_kiss.peppyrus_api.model.OrganizationInfo;
import be.solid_kiss.peppyrus_api.model.OrganizationPeppolInfo;

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
