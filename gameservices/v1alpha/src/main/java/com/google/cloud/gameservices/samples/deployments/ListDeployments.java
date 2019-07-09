package com.google.cloud.gameservices.samples.deployments;

// [START cloud_game_servers_deployment_list]

import com.google.cloud.gaming.v1alpha.GameServerDeployment;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient.ListGameServerDeploymentsPagedResponse;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class ListDeployments {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void listGameServerDeployments(String projectId) throws IOException {
    // String projectId = "your-project-id";
    GameServerDeploymentsServiceClient client = GameServerDeploymentsServiceClient.create(
        GameServerDeploymentsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/global", projectId);

    ListGameServerDeploymentsPagedResponse response = client.listGameServerDeployments(parent);

    for (GameServerDeployment game_server_deployment : response.iterateAll()) {
      System.out.println("Game Server Deployment found: " + game_server_deployment.getName());
    }
  }
}
// [END cloud_game_servers_deployment_list]
