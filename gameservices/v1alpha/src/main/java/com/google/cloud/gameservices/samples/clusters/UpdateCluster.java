package com.google.cloud.gameservices.samples.clusters;

// [START cloud_game_servers_cluster_update]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.GameServerCluster;
import com.google.cloud.gaming.v1alpha.GameServerClusterConnectionInfo;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceSettings;
import com.google.cloud.gaming.v1alpha.GameServerTemplate;
import com.google.cloud.gaming.v1alpha.UpdateGameServerClusterRequest;
import com.google.protobuf.FieldMask;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UpdateCluster {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void updateGameServerCluster(
      String projectId, String regionId, String realmId, String clusterId)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String clusterId = "your-game-server-cluster-id";
    GameServerClustersServiceClient client = GameServerClustersServiceClient.create(
        GameServerClustersServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);
    String clusterName = String.format(
        "%s/gameServerClusters/%s", parent, clusterId);

    GameServerCluster cluster = GameServerCluster
        .newBuilder()
        .setConnectionInfo(
            GameServerClusterConnectionInfo.newBuilder().setNamespace("new_namespace"))
        .build();

    RetryingFuture<OperationSnapshot> poll = client.updateGameServerClusterAsync(
        UpdateGameServerClusterRequest
            .newBuilder()
            .setGameServerCluster(cluster)
            .setUpdateMask(FieldMask.newBuilder().addPaths("connection_info/namespace"))
            .build())
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      GameServerCluster updatedPolicy = client.getGameServerCluster(clusterName);
      System.out.println("Game Server Cluster updated: " + updatedPolicy.getName());
    } else {
      throw new RuntimeException("Game Server Cluster update request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_cluster_update]
