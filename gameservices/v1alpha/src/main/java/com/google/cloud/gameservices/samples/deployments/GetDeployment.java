package com.google.cloud.gameservices.samples.deployments;

// [START cloud_game_servers_deployment_get]

import com.google.cloud.gaming.v1alpha.GameServerDeployment;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class GetDeployment {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void getGameServerDeployment(String projectId, String deploymentId)
      throws IOException {
    // String projectId = "your-project-id";
    // String deploymentId = "your-game-server-deployment-id";
    GameServerDeploymentsServiceClient client = GameServerDeploymentsServiceClient.create(
        GameServerDeploymentsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String deploymentName = String.format(
        "projects/%s/locations/global/gameServerDeployments/%s",
        projectId,
        deploymentId);

    GameServerDeployment deployment = client.getGameServerDeployment(deploymentName);

    System.out.println("Game Server Deployment found: " + deployment.getName());
  }
}
// [END cloud_game_servers_deployment_get]
