package com.google.cloud.gameservices.samples.clusters;

// [START cloud_game_servers_cluster_get]

import com.google.cloud.gaming.v1alpha.GameServerCluster;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class GetCluster {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void getGameServerCluster(
      String projectId, String regionId, String realmId, String clusterId) throws IOException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String clusterId = "your-game-server-cluster-id";
    GameServerClustersServiceClient client = GameServerClustersServiceClient.create(
        GameServerClustersServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);
    String clusterName = String.format("%s/gameServerClusters/%s", parent, clusterId);

    GameServerCluster allocationPolicy = client.getGameServerCluster(clusterName);

    System.out.println("Game Server Cluster found: " + allocationPolicy.getName());
  }
}
// [END cloud_game_servers_cluster_get]
