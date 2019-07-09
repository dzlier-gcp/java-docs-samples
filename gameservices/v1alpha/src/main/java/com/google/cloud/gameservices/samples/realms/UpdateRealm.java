package com.google.cloud.gameservices.samples.realms;

// [START cloud_game_servers_realm_update]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.RealmsServiceClient;
import com.google.cloud.gaming.v1alpha.Realm;
import com.google.cloud.gaming.v1alpha.RealmsServiceSettings;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.UpdateRealmRequest;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Int32Value;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UpdateRealm {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void updateRealm(String projectId, String regionId, String realmId)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String realmId = "your-realm-id";
    RealmsServiceClient client = RealmsServiceClient.create(
        RealmsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String realmName = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);

    Realm realm = Realm
        .newBuilder()
        .setTimeZone("America/New_York")
        .build();

    RetryingFuture<OperationSnapshot> poll = client.updateRealmAsync(UpdateRealmRequest
        .newBuilder()
        .setRealm(realm)
        .setUpdateMask(FieldMask.newBuilder().addPaths("time_zone"))
        .build())
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      Realm updatedPolicy = client.getRealm(realmName);
      System.out.println("Realm updated: " + updatedPolicy.getName());
    } else {
      throw new RuntimeException("Realm update request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_realm_update]
