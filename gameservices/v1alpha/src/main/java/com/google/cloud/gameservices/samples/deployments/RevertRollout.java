package com.google.cloud.gameservices.samples.deployments;

// [START cloud_game_servers_deployment_start_rollout]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceClient;
import com.google.cloud.gaming.v1alpha.GameServerDeploymentsServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RevertRollout {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void revertRollout(String deploymentName)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    // String deploymentName =
    //     "projects/{project_id}/locations/{location}/gameServerDeployments/{deployment_id}";
    GameServerDeploymentsServiceClient client = GameServerDeploymentsServiceClient.create(
        GameServerDeploymentsServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    RetryingFuture<OperationSnapshot> poll =
        client.revertRolloutAsync(deploymentName).getPollingFuture();

    OperationSnapshot response = poll.get(1, TimeUnit.MINUTES);
    if (response.isDone()) {
      System.out.println("Rollout reverted: " + response.getResponse());
    } else {
      throw new RuntimeException("Revert Rollout request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_deployment_start_rollout]
