package com.google.cloud.gameservices.samples.realms;

// [START cloud_game_servers_realm_list]

import com.google.cloud.gaming.v1alpha.RealmsServiceClient;
import com.google.cloud.gaming.v1alpha.Realm;
import com.google.cloud.gaming.v1alpha.RealmsServiceClient.ListRealmsPagedResponse;
import com.google.cloud.gaming.v1alpha.RealmsServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class ListRealms {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void listRealms(String projectId, String regionId)
      throws IOException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    RealmsServiceClient client = RealmsServiceClient.create(
        RealmsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/%s", projectId, regionId);

    ListRealmsPagedResponse response = client.listRealms(parent);

    for (Realm realm : response.iterateAll()) {
      System.out.println("Realm found: " + realm.getName());
    }

  }
}
// [END cloud_game_servers_realm_list]
