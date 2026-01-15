package be.solid_kiss.peppyrus_api.client;

import be.solid_kiss.peppyrus_api.client.services.PeppyrusMessageClient;
import be.solid_kiss.peppyrus_api.client.services.PeppyrusOrganizationClient;
import be.solid_kiss.peppyrus_api.client.services.PeppyrusPeppolClient;

/**
 * Entrypoint for all peppyrus services
 */
public class PeppyrusClient {

  private final PeppyrusMessageClient messageClient;
  private final PeppyrusOrganizationClient organizationClient;
  private final PeppyrusPeppolClient peppolClient;

  private PeppyrusClient(PeppyrusClientConfig config) {
    this.messageClient = new PeppyrusMessageClient(config);
    this.organizationClient = new PeppyrusOrganizationClient(config);
    this.peppolClient = new PeppyrusPeppolClient(config);
  }

  private static PeppyrusClient create(PeppyrusClientConfig config) {
    return new PeppyrusClient(config);
  }

  public static PeppyrusClient create(String apiKey, PeppyrusEnv env) {
    PeppyrusClientConfig config = PeppyrusClientConfig.builder()
            .apiKey(apiKey)
            .withEnv(env)
            .build();
    return new PeppyrusClient(config);
  }

  public PeppyrusMessageClient messages() {
    return messageClient;
  }

  public PeppyrusOrganizationClient organization() {
    return organizationClient;
  }

  public PeppyrusPeppolClient peppol() {
    return peppolClient;
  }
}
