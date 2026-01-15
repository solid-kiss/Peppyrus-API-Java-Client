package be.solid_kiss.peppyrus_api.client;

import be.solid_kiss.peppyrus_api.client.services.PeppyrusMessageClient;
import be.solid_kiss.peppyrus_api.client.services.PeppyrusOrganizationClient;
import be.solid_kiss.peppyrus_api.client.services.PeppyrusPeppolClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeppyrusClientTest {

  private final static String TEST_API_KEY = "test-api-key";


  @Test
  void testCreate_WithConfig() {

    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    assertNotNull(client);
    assertNotNull(client.messages());
    assertNotNull(client.organization());
    assertNotNull(client.peppol());
  }

  @Test
  void testCreate_WithApiKeyAndTestEnv() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY,
            PeppyrusEnv.TEST);

    assertNotNull(client);
    assertNotNull(client.messages());
    assertNotNull(client.organization());
    assertNotNull(client.peppol());
  }

  @Test
  void testCreate_WithApiKeyAndProdEnv() {
    PeppyrusClient client = PeppyrusClient.create("prod-api-key",
            PeppyrusEnv.PROD);

    assertNotNull(client);
    assertNotNull(client.messages());
    assertNotNull(client.organization());
    assertNotNull(client.peppol());
  }

  @Test
  void testMessages_ReturnsSameInstance() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    PeppyrusMessageClient client1 = client.messages();
    PeppyrusMessageClient client2 = client.messages();

    assertSame(client1, client2, "Should return the same instance");
  }

  @Test
  void testOrganization_ReturnsSameInstance() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    PeppyrusOrganizationClient client1 = client.organization();
    PeppyrusOrganizationClient client2 = client.organization();

    assertSame(client1, client2, "Should return the same instance");
  }

  @Test
  void testPeppol_ReturnsSameInstance() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    PeppyrusPeppolClient client1 = client.peppol();
    PeppyrusPeppolClient client2 = client.peppol();

    assertSame(client1, client2, "Should return the same instance");
  }

  @Test
  void testMessages_ReturnsCorrectType() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    assertInstanceOf(PeppyrusMessageClient.class, client.messages());
  }

  @Test
  void testOrganization_ReturnsCorrectType() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    assertInstanceOf(PeppyrusOrganizationClient.class, client.organization());
  }

  @Test
  void testPeppol_ReturnsCorrectType() {
    PeppyrusClient client = PeppyrusClient.create(TEST_API_KEY, PeppyrusEnv.TEST);

    assertInstanceOf(PeppyrusPeppolClient.class, client.peppol());
  }

  @Test
  void testCreate_WithInvalidConfig_ThrowsException() {
    assertThrows(IllegalStateException.class, () -> {
      PeppyrusClient.create("", PeppyrusEnv.TEST);
    });
  }
}
