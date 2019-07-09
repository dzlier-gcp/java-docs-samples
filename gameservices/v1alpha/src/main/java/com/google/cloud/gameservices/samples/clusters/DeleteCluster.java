package com.google.cloud.gameservices.samples.clusters;

// [START cloud_game_servers_cluster_delete]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DeleteCluster {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void deleteGameServerCluster(
      String projectId, String regionId, String realmId, String clusterId)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String clusterId = "your-game-server-cluster-id";
    GameServerClustersServiceClient client = GameServerClustersServiceClient.create(
        GameServerClustersServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);
    String clusterName = String.format("%s/gameServerClusters/%s", parent, clusterId);

    RetryingFuture<OperationSnapshot> poll = client
        .deleteGameServerClusterAsync(clusterName)
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Game Server Cluster deleted: " + clusterName);
    } else {
      throw new RuntimeException("Game Server Cluster delete request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_cluster_delete]
