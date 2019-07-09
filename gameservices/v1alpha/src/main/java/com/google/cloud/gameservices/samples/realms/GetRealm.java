package com.google.cloud.gameservices.samples.realms;

// [START cloud_game_servers_realm_get]

import com.google.cloud.gaming.v1alpha.RealmsServiceClient;
import com.google.cloud.gaming.v1alpha.Realm;
import com.google.cloud.gaming.v1alpha.RealmsServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class GetRealm {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void getRealm(String projectId, String regionId, String realmId)
      throws IOException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String realmId = "your-realm-id";
    RealmsServiceClient client = RealmsServiceClient.create(
        RealmsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String realmName = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);

    Realm allocationPolicy = client.getRealm(realmName);

    System.out.println("Realm found: " + allocationPolicy.getName());
  }
}
// [END cloud_game_servers_realm_get]
