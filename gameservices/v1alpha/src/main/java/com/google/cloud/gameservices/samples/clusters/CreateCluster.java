package com.google.cloud.gameservices.samples.clusters;

// [START cloud_game_servers_cluster_create]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.CreateGameServerClusterRequest;
import com.google.cloud.gaming.v1alpha.GameServerCluster;
import com.google.cloud.gaming.v1alpha.GameServerClusterConnectionInfo;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceSettings;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateCluster {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void createGameServerCluster(
      String projectId, String regionId, String realmId, String clusterId, String gkeName)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String clusterId = "your-game-server-cluster-id";
    // String gkeName = "projects/your-project-id/locations/us-central1/clusters/test";
    GameServerClustersServiceClient client = GameServerClustersServiceClient.create(
        GameServerClustersServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);
    String game_server_clusterName = String.format("%s/gameServerClusters/%s", parent, clusterId);

    GameServerCluster gameServerCluster = GameServerCluster
        .newBuilder()
        .setName(game_server_clusterName)
        .setConnectionInfo(GameServerClusterConnectionInfo
            .newBuilder()
            .setGkeName(gkeName)
            .setNamespace("default"))
        .build();

    RetryingFuture<OperationSnapshot> poll = client.createGameServerClusterAsync(
        CreateGameServerClusterRequest
            .newBuilder()
            .setParent(parent)
            .setGameServerClusterId(clusterId)
            .setGameServerCluster(gameServerCluster)
            .build()).getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Game Server Cluster created: " + gameServerCluster.getName());
    } else {
      throw new RuntimeException("Game Server Cluster create request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_cluster_create]
