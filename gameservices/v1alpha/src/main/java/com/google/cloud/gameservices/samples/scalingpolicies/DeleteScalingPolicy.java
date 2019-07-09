package com.google.cloud.gameservices.samples.scalingpolicies;

// [START cloud_game_servers_scaling_policy_delete]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DeleteScalingPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void deleteScalingPolicy(String projectId, String regionId, String policyId)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String policyId = "your-policy-id";
    ScalingPoliciesServiceClient client = ScalingPoliciesServiceClient.create(
        ScalingPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/%s", projectId, regionId);
    String policyName = String.format("%s/scalingPolicies/%s", parent, policyId);

    RetryingFuture<OperationSnapshot> poll = client
        .deleteScalingPolicyAsync(policyName)
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Scaling Policy deleted: " + policyName);
    } else {
      throw new RuntimeException("Scaling Policy delete request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_scaling_policy_delete]
