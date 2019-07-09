package com.google.cloud.gameservices.samples.clusters;

// [START cloud_game_servers_cluster_list]

import com.google.cloud.gaming.v1alpha.GameServerCluster;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceClient.ListGameServerClustersPagedResponse;
import com.google.cloud.gaming.v1alpha.GameServerClustersServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class ListClusters {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void listGameServerClusters(String projectId, String regionId, String realmId)
      throws IOException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    GameServerClustersServiceClient client = GameServerClustersServiceClient.create(
        GameServerClustersServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format(
        "projects/%s/locations/%s/realms/%s", projectId, regionId, realmId);

    ListGameServerClustersPagedResponse response = client.listGameServerClusters(parent);

    for (GameServerCluster game_server_cluster : response.iterateAll()) {
      System.out.println("Game Server Cluster found: " + game_server_cluster.getName());
    }

  }
}
// [END cloud_game_servers_cluster_list]
