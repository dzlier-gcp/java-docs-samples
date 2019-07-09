package com.google.cloud.gameservices.samples.deployments;

// [START cloud_game_servers_deployment_delete]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DeleteDeployment {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void deleteGameServerDeployment(String projectId, String deploymentId)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String deploymentId = "your-game-server-deployment-id";
    GameServerDeploymentsServiceClient client = GameServerDeploymentsServiceClient.create(
        GameServerDeploymentsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/global", projectId);
    String deploymentName = String.format("%s/gameServerDeployments/%s", parent, deploymentId);

    RetryingFuture<OperationSnapshot> poll = client
        .deleteGameServerDeploymentAsync(deploymentName)
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Game Server Deployment deleted: " + deploymentName);
    } else {
      throw new RuntimeException("Game Server Deployment delete request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_deployment_delete]
