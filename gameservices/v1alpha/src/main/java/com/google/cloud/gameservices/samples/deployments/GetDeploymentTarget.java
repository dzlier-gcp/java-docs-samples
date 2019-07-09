package com.google.cloud.gameservices.samples.deployments;

// [START cloud_game_servers_deployment_start_rollout]

import com.google.cloud.gaming.v1alpha.DeploymentTarget;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;

import java.io.IOException;
import java.util.Optional;

public class GetDeploymentTarget {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void getDeploymentTarget(String deploymentName) throws IOException {
    // String deploymentName =
    //     "projects/{project_id}/locations/{location}/gameServerDeployments/{deployment_id}";
    GameServerDeploymentsServiceClient client = GameServerDeploymentsServiceClient.create(
        GameServerDeploymentsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    DeploymentTarget target = client.getDeploymentTarget(deploymentName);

    System.out.printf("Found target with %d clusters.", target.getClustersCount());
  }
}
// [END cloud_game_servers_deployment_start_rollout]
