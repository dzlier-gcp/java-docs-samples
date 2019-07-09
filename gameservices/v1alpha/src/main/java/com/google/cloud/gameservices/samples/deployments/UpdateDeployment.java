package com.google.cloud.gameservices.samples.deployments;

// [START cloud_game_servers_deployment_update]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.GameServerDeployment;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;
import com.google.cloud.gaming.v1alpha.GameServerTemplate;
import com.google.cloud.gaming.v1alpha.UpdateGameServerDeploymentRequest;
import com.google.protobuf.FieldMask;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UpdateDeployment {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void updateGameServerDeployment(
      String projectId, String deploymentId)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    // String projectId = "your-project-id";
    // String deploymentId = "your-game-server-deployment-id";
    GameServerDeploymentsServiceClient client = GameServerDeploymentsServiceClient.create(
        GameServerDeploymentsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String deploymentName = String.format(
        "projects/%s/locations/global/gameServerDeployments/%s",
        projectId,
        deploymentId);

    GameServerDeployment game_server_deployment = GameServerDeployment
        .newBuilder()
        .setNewGameServerTemplate(GameServerTemplate
            .newBuilder()
            .setDescription("Updated deployment template.")
            .build())
        .build();

    RetryingFuture<OperationSnapshot> poll = client.updateGameServerDeploymentAsync(
        UpdateGameServerDeploymentRequest
            .newBuilder()
            .setGameServerDeployment(game_server_deployment)
            .setUpdateMask(FieldMask.newBuilder().addPaths("new_game_server_template/description"))
            .build())
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      GameServerDeployment updatedPolicy = client.getGameServerDeployment(deploymentName);
      System.out.println("Game Server Deployment updated: " + updatedPolicy.getName());
    } else {
      throw new RuntimeException("Game Server Deployment update request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_deployment_update]
