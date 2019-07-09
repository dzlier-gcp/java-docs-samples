package com.google.cloud.gameservices.samples.realms;

// [START cloud_game_servers_realm_create]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.CreateRealmRequest;
import com.google.cloud.gaming.v1alpha.Realm;
import com.google.cloud.gaming.v1alpha.RealmsServiceClient;
import com.google.cloud.gaming.v1alpha.RealmsServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateRealm {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void createRealm(String projectId, String regionId, String realmId)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String realmId = "your-realm-id";
    RealmsServiceClient client = RealmsServiceClient.create(
        RealmsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/%s", projectId, regionId);
    String realmName = String.format("%s/realms/%s", parent, realmId);

    Realm realm = Realm
        .newBuilder()
        .setName(realmName)
        .setTimeZone("America/Los_Angeles")
        .build();

    RetryingFuture<OperationSnapshot> poll = client.createRealmAsync(
        CreateRealmRequest
            .newBuilder()
            .setParent(parent)
            .setRealmId(realmId)
            .setRealm(realm)
            .build()).getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Realm created: " + realm.getName());
    } else {
      throw new RuntimeException("Realm create request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_realm_create]
